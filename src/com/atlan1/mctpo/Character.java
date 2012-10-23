package com.atlan1.mctpo;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.util.HashSet;
import java.util.Set;

import com.atlan1.mctpo.API.Collideable;
import com.atlan1.mctpo.API.Mobile;
import com.atlan1.mctpo.API.Side;
import com.atlan1.mctpo.HUD.HUD;
import com.atlan1.mctpo.HUD.HealthBar;
import com.atlan1.mctpo.HUD.InventoryBar;
import com.atlan1.mctpo.Inventory.Inventory;
import com.atlan1.mctpo.Inventory.Slot;
import com.atlan1.mctpo.Texture.ColorMultiFilter;
import com.atlan1.mctpo.Texture.TextureLoader;

public class Character extends Rectangle2D.Double implements Mobile{
	private static final long serialVersionUID = 1L;
	private static Image animationTexture;
	private static Image damageAnimationTexture;
	private static int[]  character = {0, 0};

	static{
		animationTexture = TextureLoader.loadImage("/res/animation.png");
		ImageFilter redfilter = new ColorMultiFilter(2, 0, 0);
		ImageProducer imageprod = new FilteredImageSource(animationTexture.getSource(), redfilter);
		damageAnimationTexture = MCTPO.toBufferedImage(MCTPO.mctpo.createImage(imageprod));
	}
	
	public double fallingSpeed = 8d/100;
	public double jumpingSpeed = 6d/100;
	public double movementSpeed = 4d/100;
	public double sprintSpeed = 2d/100;
	public boolean isMoving = false;
	public boolean wouldJump = false;
	public boolean isJumping = false;
	public boolean stopJumping = false;
	public boolean isSprinting = false;
	public int jumpHeight = 400, jumpCount = 0; 
	public double dir = 1;
	public long startAnimation = System.currentTimeMillis();
	public int animation;
	public int animationTime = 150;
	public int sprintAnimationTime = 100;
	public double buildRange = 4; //in blocks
	public boolean isFalling = false;
	public double startFalling = 0;
	public HUD hud = new HUD(new HealthBar(this), new InventoryBar());
	public Inventory inv = new Inventory(this, hud.getWidget(InventoryBar.class));
	public int maxHealth = 100;
	public int health = 100;
	public boolean damaged = false;
	public int damageTime=120;
	public long damageStartTime=0;
	public int destroyTime=0;
	public Block currentBlock;
	public Block lastBlock;
	public final int bUP = 0, bDOWN = 1, bRIGHT = 2, bLEFT = 3;
//	public Line2D.Double[] bounds = new Line2D.Double[4];
	public Set<Side> collidesSides = new HashSet<Side>();
	public Set<Collideable> collisionWith = new HashSet<Collideable>();
	public Rectangle union = new Rectangle();
//	public Double lastPosition = new Rectangle2D.Double(x, y, width, height);
	
	public Character(double width, double height) {
		this.width=width;
		this.height=height;
		this.x = (MCTPO.pixel.width / 2) - (width / 2);
		this.y= ((MCTPO.pixel.height / 2) - (height / 2));
//		lastPosition = new Rectangle2D.Double(x, y, this.width, this.height);
//		calcBounds();
	}
	
//	public boolean isCollidingWithAnyBlock(Line2D line) {
//		for(int x=(int)(this.x/MCTPO.blockSize);x<(int)(this.x/MCTPO.blockSize+3);x++)
//			for(int y=(int)(this.y/MCTPO.blockSize);y<(int)(this.y/MCTPO.blockSize+3);y++)
//				if(x >= 0 && y >= 0 && x < World.worldW && y < World.worldH){
//					boolean collide = World.blocks[x][y].contains(line.getP1())|| World.blocks[x][y].contains(line.getP2());
//					if(!World.blocks[x][y].material.nonSolid&&collide){
//							return true;
//					}
//				}
//		return false;
//	}
//	
//	public boolean isCollidingWithBlock(Shape t) {
//		for(Line2D.Double l : bounds){
//			if(t.contains(l.getP1())||t.contains(l.getP2())){
//				return true;
//			}
//		}
//		return false;
//	}
	
	public void render(Graphics g){
		if(dir>=0)
			g.drawImage(!damaged?animationTexture:damageAnimationTexture, (int)x - (int) MCTPO.camX, (int)y - (int) MCTPO.camY, (int)(x + width) - (int) MCTPO.camX, (int)(y + height) - (int) MCTPO.camY, (character[0] * MCTPO.blockSize)+(MCTPO.blockSize * animation), (character[1] * MCTPO.blockSize), (character[0] * MCTPO.blockSize)+(animation * MCTPO.blockSize)+ (int) width, (character[1] * MCTPO.blockSize) + (int) height,null);
		else
			g.drawImage(!damaged?animationTexture:damageAnimationTexture, (int)(x + width) - (int) MCTPO.camX, (int)y - (int) MCTPO.camY, (int)x - (int) MCTPO.camX, (int)(y + height) - (int) MCTPO.camY, (character[0] * MCTPO.blockSize)+(MCTPO.blockSize * animation), (character[1] * MCTPO.blockSize), (character[0] * MCTPO.blockSize)+(animation * MCTPO.blockSize)+ (int) width, (character[1] * MCTPO.blockSize) + (int) height ,null);
	}
	
	public void tick(long d){
		isSprinting = MCTPO.controlDown;
//		collidesSides = getCollisionSides(collisionWith);
		int firstHealth = health;
		
		if(!isJumping){
			this.move(0, d*fallingSpeed);
			if(!isFalling){
				startFalling=this.y;
				isFalling = true;
			}
		}
		if(wouldJump&&jumpHeight>jumpCount&&!stopJumping)
			isJumping = true;
		if(isFalling){
			int deltaFallBlocks = (int) ((this.y-startFalling)/MCTPO.blockSize);
			if(deltaFallBlocks>3)
				health-=deltaFallBlocks;
			startFalling=0;
			isFalling = false;
		}
		if(isJumping&&!stopJumping){
			if(jumpHeight<=jumpCount){
				isJumping = false;
				stopJumping = true;
			}else{
				this.move(0, -d*jumpingSpeed);
				jumpCount+=d;
			}
		}else{
			jumpCount-=d;
			stopJumping = false;
		}
		
		if(!inv.isOpen()){	
			if(isMoving){
				if(MCTPO.thisTime - startAnimation >= (isSprinting?sprintAnimationTime:animationTime)) {
					if(animation<3){
						animation++;
						startAnimation = MCTPO.thisTime;
					}else{
						animation=0;
						startAnimation = MCTPO.thisTime;
					}
				}
				this.move((isSprinting?dir<0?dir-sprintSpeed:dir+sprintSpeed:dir)*d, 0);
			}else{
				animation = 1;
			}
		}
		if(firstHealth-health>0){
			damaged = true;
		}
		if(damaged){
			if(damageTime<=MCTPO.thisTime - damageStartTime ){
				damageStartTime = 0;
				damaged= false;
			}
		}
		if(!inv.isOpen()){
			if(currentBlock!=null&&currentBlock.material.nonSolid&&isBlockInBuildRange(currentBlock))
				MCTPO.mctpo.setCursor(MCTPO.buildCursor);
			else if(currentBlock!=null&&!currentBlock.material.nonSolid&&isBlockInBuildRange(currentBlock)){
				MCTPO.mctpo.setCursor(MCTPO.destroyCursor);
			}else if(currentBlock!=null&&!isBlockInBuildRange(currentBlock)){
				MCTPO.mctpo.setCursor(MCTPO.crossHair);
			}
			if(currentBlock!=null&&lastBlock!=null&&MCTPO.mouseLeftDown&&lastBlock.equals(currentBlock)&&isBlockInBuildRange(currentBlock)){
				destroyTime++;
			}else{
				destroyTime=0;
			}
			lastBlock = currentBlock;
			currentBlock = getCurrentBlock();
			if(currentBlock!=null)
				build();
		}else{
			MCTPO.mctpo.setCursor(MCTPO.crossHair);
		}
		if(health<=0){
			inv.clear();
			this.respawn();
			health = maxHealth;
		}
		if(this.y/MCTPO.blockSize>World.worldH){
			this.teleport((int) this.x, 0);
		}
		
//		lastPosition.setRect(x, y, width, height);
		collisionWith = new HashSet<Collideable>();
		collidesSides = new HashSet<Side>();
		
		inv.tick();
		hud.tick();
	}
	
//	public void calcBounds(){
//		bounds[bUP] = new Line2D.Double(new Point((int)(x+2), (int) (y+1)), new Point((int)(x + width -2), (int)(y+1)));
//		bounds[bDOWN] = new Line2D.Double(new Point((int)(x+2), (int) (y+height)), new Point((int)(x+width-2), (int)(y+height)));
//		bounds[bRIGHT] = new Line2D.Double(new Point((int)(x + width -1), (int) y), new Point((int)(x + width), (int) (y + (height-2))));
//		bounds[bLEFT] = new Line2D.Double(new Point((int)x-1, (int) y), new Point((int)x-1, (int) (y + (height-2))));
//	}
	
	public Block getCurrentBlock(){
//		if (MCTPO.mouseLeftDown || MCTPO.mouseRightDown) { Removed due to a small bug with cursors
			try {
				Block b = getBlockIncluding(MCTPO.mouse.x, MCTPO.mouse.y);
				return b;
			} catch (Exception e) {
				
			}
//		}
		return null;
	}
	
	public Block getBlockIncluding(double x, double y) {
		return World.blocks[(int) ((x + MCTPO.camX) / MCTPO.blockSize)][(int) ((y + MCTPO.camY) / MCTPO.blockSize)];
	}
	
	public boolean isBlockInBuildRange(Block block) {
		return 	(new Point((int)(this.x+width/2), (int)(this.y+height/2))).distance(new Point((int)block.getCenterX(), (int)block.getCenterY()))<=buildRange*MCTPO.blockSize;
	}
	
	public void build(){
		if(isBlockInBuildRange(currentBlock)){
			Material m = currentBlock.material;
			if(MCTPO.mouseLeftDown){
				if(destroyTime>=m.hardness&&!(m.hardness<0)){
					if(inv.containsMaterial(m)){
						boolean check = false;
						Slot[] slots = inv.getSlotsContaining(m);
						for(Slot s : slots){
							if(s.itemstack.stacksize<Inventory.maxStackSize){
								s.itemstack.stacksize++;
								check = true;
								break;
							}
						}
						if(!check &&inv.containsMaterial(Material.AIR)){
							Slot s2 = inv.getSlot(Material.AIR);
							s2.itemstack.material = m;
							s2.itemstack.stacksize++;
						}
					}else if(inv.containsMaterial(Material.AIR)){
						Slot s3 = inv.getSlot(Material.AIR);
						s3.itemstack.material = m;
						s3.itemstack.stacksize++;
					}
					currentBlock.material = Material.AIR;
					destroyTime = 0;
				}
				return;
			}else if(MCTPO.mouseRightDown && m.nonSolid){
				if(!(hud.getWidget(InventoryBar.class).slots[hud.getWidget(InventoryBar.class).selected].itemstack.material == Material.AIR)){
					if(hud.getWidget(InventoryBar.class).slots[hud.getWidget(InventoryBar.class).selected].itemstack.stacksize>0)
						hud.getWidget(InventoryBar.class).slots[hud.getWidget(InventoryBar.class).selected].itemstack.stacksize--;
					else{
						hud.getWidget(InventoryBar.class).slots[hud.getWidget(InventoryBar.class).selected].itemstack.stacksize = 0;
						hud.getWidget(InventoryBar.class).slots[hud.getWidget(InventoryBar.class).selected].itemstack.material = Material.AIR;
					}
					currentBlock.material = hud.getWidget(InventoryBar.class).slots[hud.getWidget(InventoryBar.class).selected].itemstack.material;
					return;
				}
			}
		}
	}
	
	public void respawn(){
		teleport(MCTPO.world.spawnPoint.x, MCTPO.world.spawnPoint.y);
	}
	
	public void teleport(int x, int y){
		this.y = y;
		this.x = x;
		MCTPO.camY = y - (MCTPO.pixel.height / 2) + (height / 2);
		MCTPO.camX = x - (MCTPO.pixel.width / 2) + (width / 2);
	}

	@Override
	public int getHealth() {
		return health;
	}

	@Override
	public void setHealth(int i) {
		health = i;
	}

	@Override
	public boolean isAlive() {
		return health>=0;
	}

	@Override
	public void move(int dx, int dy) {
		if(CollisionDetector.isColliding(new BB(new Rectangle2D.Double(this.x+dx, this.y+dy, this.width, this.height)))){
			return;
		}
		this.y += dy;
		this.x += dx;
		MCTPO.camY = this.y - (MCTPO.pixel.height / 2) + (height / 2);
		MCTPO.camX = this.x - (MCTPO.pixel.width / 2) + (width / 2);
	}

	@Override
	public void move(double dx, double dy) {
		if(CollisionDetector.isColliding(new BB(new Rectangle2D.Double(this.x+dx, this.y+dy, this.width, this.height)))){
			return;
		}else{
			this.y += dy;
			this.x += dx;
			MCTPO.camY = this.y - (MCTPO.pixel.height / 2) + (height / 2);
			MCTPO.camX = this.x - (MCTPO.pixel.width / 2) + (width / 2);
		}
	}
	
	@Override
	public boolean isColliding(Collideable otherC) {
		if(otherC instanceof Block&&((Block)otherC).material.nonSolid)
			return false;
		return this.intersects(otherC.getBounds());
	}
	
	public Rectangle2D getCollisionIntersection(Collideable otherC) {
		if(otherC instanceof Block&&((Block)otherC).material.nonSolid)
			return null;
		return this.createIntersection(otherC.getBounds());
	}

	public Set<Side> getCollisionSides(Set<Collideable> otherCs) {
		Set<Side> sides = new HashSet<Side>();
		Rectangle union = new Rectangle(-1, -1);
		Rectangle myself = CollisionDetector.toRectangle(this.getBounds());
		Rectangle last = null;
		for(Collideable c:otherCs){
			if(last == null){
				last = (Rectangle) myself.createIntersection(CollisionDetector.toRectangle(c.getBounds()));
			}
			Rectangle.union(last, myself.createIntersection(CollisionDetector.toRectangle(c.getBounds())), union);
			last = union;
		}
		this.union = union;
		System.out.println(union);
		System.out.println(myself);
//		if(union.equals(myself)){
//			for(Side s:Side.values()){
//				if(!s.equals(Side.UNKNOWN))
//					sides.add(s);
//			}
//		}else{
		
		if(union.getMaxX()==myself.getMaxX()){
			sides.add(Side.RIGHT);
		}
		if(union.getMinX()==myself.getMinX()){
			sides.add(Side.LEFT);
		}
		if(union.getMaxY()==myself.getMaxY()){
			sides.add(Side.BOTTOM);
		}
		if(union.getMinY()==myself.getMinY()){
			sides.add(Side.TOP);
		}
//			if(union.getMaxX()==myself.getMaxX()&&union.width<2){
//				sides.add(Side.RIGHT);
//			}
//			if(union.getMinX()==myself.getMinX()&&union.width<2){
//				sides.add(Side.LEFT);
//			}
//			if(union.getMaxY()==myself.getMaxY()&&(!(sides.contains(Side.LEFT)||sides.contains(Side.RIGHT))&&union.height>2)){
//				sides.add(Side.BOTTOM);
//			}
//			if(union.getMinY()==myself.getMinY()&&((!sides.contains(Side.LEFT)||!sides.contains(Side.RIGHT))&&!(union.height>2))){
//				sides.add(Side.TOP);
//			}
//		}
		
		
//		for(Collideable c:otherCs){
//			
//			Rectangle inter = CollisionDetector.toRectangle(getCollisionIntersection(c));
//			if(inter.getMinY()==myself.getMinY()&&!(inter.getHeight()>2)){
//					sides.add(Side.TOP);
//			}
//			if(inter.getMaxY()==myself.getMaxY()&&!(inter.getHeight()>2)){
//				sides.add(Side.BOTTOM);
//			}
//			if(inter.getMaxX()==myself.getMaxX()&&!(inter.width==inter.height)&&!(inter.getWidth()>2)){
//				sides.add(Side.RIGHT);
//			}
//			if(inter.getMinX()==myself.getMinX()&&!(inter.getWidth()>2)){
//				sides.add(Side.LEFT);
//			}
//		}
//		if(other.contains((int)(this.x+2), (int)(this.y+this.height))&&other.contains(((int)this.x+this.width-2), (int)(this.y+this.height)))
//			sides.add(Side.BOTTOM);
//		if(other.contains(this.x, this.y)&&other.contains(this.x+this.width, this.y))
//			sides.add(Side.TOP);
//		if(other.contains(this.x+this.width, this.y)&&other.contains(this.x+this.width, this.y+this.height))
//			sides.add(Side.RIGHT);
//		if(other.contains(this.x, this.y)&&other.contains(this.x, this.y+this.height))
//			sides.add(Side.LEFT);
//		if(other.contains(bounds[Side.BOTTOM.getId()].getP1())&&other.contains(bounds[Side.BOTTOM.getId()].getP2()))
//			sides.add(Side.BOTTOM);
//		if(other.contains(bounds[Side.TOP.getId()].getP1())&&other.contains(bounds[Side.TOP.getId()].getP2()))
//			sides.add(Side.TOP);
//		if(other.contains(bounds[Side.RIGHT.getId()].getP1())&&other.contains(bounds[Side.RIGHT.getId()].getP2()))
//			sides.add(Side.RIGHT);
//		if(other.contains(bounds[Side.LEFT.getId()].getP1())&&other.contains(bounds[Side.LEFT.getId()].getP2()))
//			sides.add(Side.LEFT);
//		int ox=other.x, oy = other.y, owidth=other.width, oheight=other.height;
//		int x=(int) this.x-2, y=(int) this.y-2, width=(int) this.width+2, height=(int) this.height+2;
//		if(y+height>=other.y&&!(y>=other.y+other.height)&&other.x<=x+width&&other.x+other.width>=x)
//			sides.add(Side.BOTTOM);
//		if(y>=other.y+other.height&&!(y+height>=other.y)&&other.x<=x+width&&other.x+other.width>=x)
//			sides.add(Side.TOP);
//		if(x+width<=other.x&&!(x<=other.x+other.width)&&other.y<=y+height&&other.y+other.height>=y)
//			sides.add(Side.RIGHT);
//		if(x<=other.x+other.width&&!(x+width<=other.x)&&other.y<=y+height&&other.y+other.height>=y)
//			sides.add(Side.LEFT);
//		if(sides.isEmpty())
//			sides.add(Side.UNKNOWN);
		return sides;
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, (int)width, (int)height);
	}

	@Override
	public void onCollide(Collideable c) 
	{
		collisionWith.add(c);
	}
//
//	private void setBounds(Double l) {
//		this.width=l.width;
//		this.height=l.height;
//		this.x = l.x;
//		this.y= l.y;
//	}
}

