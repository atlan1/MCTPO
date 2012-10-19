package com.atlan1.mctpo;
import java.applet.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.atlan1.mctpo.Listener.KeyListening;
import com.atlan1.mctpo.Listener.MouseListener;
import com.atlan1.mctpo.Texture.TextureLoader;


public class MCTPO extends Applet implements Runnable{
	public static MCTPO mctpo;
	public static JFrame frame;
	
	private static final long serialVersionUID = 1L;
	private static boolean isRunning = false;
	
	public static int pixelSize = 2;
	private Image screen;

	public static int blockSize = 20;
	
	public static final Dimension minSize = new Dimension(700, 500);
	public static Dimension size = new Dimension(700, 500);
	public static Dimension pixel = new Dimension(size.width/pixelSize, size.height/pixelSize);
	public static String name = "Minecraft Two Point o.O";
	public static double sX = 0, sY = 0;
	public static Point mouse = new Point(0, 0);
	public static boolean mouseLeftDown = false;
	public static boolean mouseRightDown = false;
	public boolean controlDown = false;
	
	public static World world;
	public static Sky sky;
	public static Character character;
	
	public static Cursor destroyCursor = Toolkit.getDefaultToolkit().createCustomCursor(TextureLoader.loadImage("/res/DestroyCursor.png"), mouse, "DestroyCursor");
	public static Cursor buildCursor = Toolkit.getDefaultToolkit().createCustomCursor(TextureLoader.loadImage("/res/BuildCursor.png"), mouse, "BuildCursor");
	public static Cursor crossHair = Toolkit.getDefaultToolkit().createCustomCursor(TextureLoader.loadImage("/res/CrossHair.png"), mouse, "CrosshairCursor");
	
	public MCTPO() {
		MCTPO.mctpo = this;
		setPreferredSize(size);	
	}
	
	public void start() {
		setCursor(buildCursor);
		requestFocus();
		
		character = new Character(MCTPO.blockSize, MCTPO.blockSize * 2);
		world = new World(character);
		sky = new Sky();
		
		MouseListener ml = new MouseListener(character);
		this.addMouseListener(ml);
		this.addMouseMotionListener(ml);
		this.addMouseWheelListener(ml);
		this.addKeyListener(new KeyListening(character));
		
		isRunning = true;
		new Thread(this).start();
	}
	
	public void stop() {
		isRunning = false;
	}
	
	
	public static void main(String args[]) {
		MCTPO c = new MCTPO();
		frame = new JFrame(name);
		frame.add(c);
		frame.setMinimumSize(minSize);
		frame.setSize(size);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setIconImage(TextureLoader.loadImage("/res/icon.png"));
		frame.setAlwaysOnTop(true);
		c.start();
	}
	
	public void tick() {
		character.tick();
		sky.tick();
		world.tick((int)sX, (int)sY, (pixel.width / blockSize) + 8, (pixel.height / blockSize) + 8);
	}
	
	public void render() {
		calcSize();
		
		Graphics g = screen.getGraphics();
		sky.render(g);
		
		character.render(g);
		world.render(g, (int)sX, (int)sY, (pixel.width / blockSize) + 2, (pixel.height / blockSize) + 2);
		character.hud.render(g);
		character.inv.render(g);
		g = getGraphics();
		g.drawImage(screen, 0, 0, size.width, size.height, 0, 0, pixel.width, pixel.height, null);
		g.dispose();
	}
	
	@Override
	public void run() {
		
		screen = createVolatileImage(pixel.width, pixel.height);
		while(isRunning){
			tick();
			render();
			
			try{
				Thread.sleep(18);
			}catch(Throwable t){}
		}
	}
	
	public void calcSize() { 
		if(!this.getSize().equals(size)){
			size = new Dimension(this.getSize());
			pixel = new Dimension(size.width/pixelSize, size.height/pixelSize);
			screen = createVolatileImage(pixel.width, pixel.height);
			character.hud.calcPosition();
			character.inv.calcPosition();
			moveCamera((int)character.x, (int)character.y);
		}
	}
	
	public void moveCamera(int x, int y) {
		MCTPO.sY = y - (MCTPO.pixel.height / 2);
		MCTPO.sX = x - (MCTPO.pixel.width / 2);
	}

	public static BufferedImage toBufferedImage(Image image) {
	    if (image instanceof BufferedImage) {
	        return (BufferedImage)image;
	    }
	
	    // This code ensures that all the pixels in the image are loaded
	image = new ImageIcon(image).getImage();
	
	// Determine if the image has transparent pixels; for this method's
	// implementation, see Determining If an Image Has Transparent Pixels
	boolean hasAlpha = hasAlpha(image);
	
	// Create a buffered image with a format that's compatible with the screen
	BufferedImage bimage = null;
	GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	try {
	    // Determine the type of transparency of the new buffered image
	    int transparency = Transparency.OPAQUE;
	    if (hasAlpha) {
	        transparency = Transparency.BITMASK;
	    }
	
	    // Create the buffered image
	    GraphicsDevice gs = ge.getDefaultScreenDevice();
	    GraphicsConfiguration gc = gs.getDefaultConfiguration();
	    bimage = gc.createCompatibleImage(
	        image.getWidth(null), image.getHeight(null), transparency);
	} catch (HeadlessException e) {
	    // The system does not have a screen
	}
	
	if (bimage == null) {
	    // Create a buffered image using the default color model
	    int type = BufferedImage.TYPE_INT_RGB;
	    if (hasAlpha) {
	        type = BufferedImage.TYPE_INT_ARGB;
	    }
	    bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
	}
	
	// Copy image to buffered image
	Graphics g = bimage.createGraphics();
	
	// Paint the image onto the buffered image
	    g.drawImage(image, 0, 0, null);
	    g.dispose();
	
	    return bimage;
	}
	
	public static boolean hasAlpha(Image image) {
	    // If buffered image, the color model is readily available
	    if (image instanceof BufferedImage) {
	        BufferedImage bimage = (BufferedImage)image;
	        return bimage.getColorModel().hasAlpha();
	    }

	    // Use a pixel grabber to retrieve the image's color model;
	    // grabbing a single pixel is usually sufficient
	     PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
	    try {
	        pg.grabPixels();
	    } catch (InterruptedException e) {
	    }

	    // Get the image's color model
	    ColorModel cm = pg.getColorModel();
	    return cm.hasAlpha();
	}
	
//	public static boolean extractFile(String regex) {
//		boolean found = false;
//		try {
//			String path = Component.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//			String decodedPath = URLDecoder.decode(path, "UTF-8");
//			JarFile jar = new JarFile(new File(decodedPath));
//			for (Enumeration<JarEntry> entries = jar.entries(); entries.hasMoreElements();) {
//				JarEntry entry = (JarEntry) entries.nextElement();
//				String name = entry.getName();
//				if (name.matches(regex)) {
//					if (!new File(Component.comp.getCodeBase().getFile()).exists()) {
//						new File(Component.comp.getCodeBase().getFile()).mkdir();
//					}
//					try {
//						File file = new File(new File(Component.comp.getCodeBase().getFile()), name);
//						if (!file.exists()) {
//							InputStream is = jar.getInputStream(entry);
//							FileOutputStream fos = new FileOutputStream(file);
//							while (is.available() > 0) {
//								fos.write(is.read());
//							}
//							fos.close();
//							is.close();
//							found = true;
//						}
//					} catch (Exception e) {
//					}
//				}
//			}
//		} catch (Exception e) {
//		}
//		return found;
//	}
}
