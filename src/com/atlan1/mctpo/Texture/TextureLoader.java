package com.atlan1.mctpo.Texture;

import com.atlan1.mctpo.MCTPO;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TextureLoader {
	
	private static TextureLoader loader;

	static{
		loader = new TextureLoader();
	}
	
	private TextureLoader() {
	}
	
	public static SpriteImage loadSpriteImage(String path) {
		try {
			return new SpriteImage(MCTPO.toBufferedImage(getImage(path)), MCTPO.blockSize);
		} catch (Exception e) {e.printStackTrace();}
		return null;
	}
	
	public static Image loadImage(String path) {
		try{
			return getImage(path);
		}catch(Exception e){e.printStackTrace();}
		return null;
	}
	
      public static Image getImage(String name) {
         InputStream instream;
         byte[] bytedata;
         int bytelenght;
         Toolkit toolkit = Toolkit.getDefaultToolkit();
         try {
            instream = loader.getClass().getResourceAsStream(name); 
         
            ByteArrayOutputStream bytes;
         
            bytes = new ByteArrayOutputStream();
            bytelenght = 1024;
            bytedata = new byte[bytelenght];
            int rb;
            while ((rb = instream.read(bytedata, 0, bytelenght)) > -1) {
               bytes.write(bytedata, 0, rb);
            }
            bytes.close();
            bytedata = bytes.toByteArray();
            instream.close();
            Image image = toolkit.createImage(bytedata);
            return image;
        }catch (IOException e) {
           return null;
        }
      }
}
