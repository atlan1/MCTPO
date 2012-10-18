package com.atlan1.mctpo;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.util.ArrayList;
import java.util.List;

import com.atlan1.mctpo.Inventory.Inventory;
import com.atlan1.mctpo.Inventory.Slot;
import com.atlan1.mctpo.Texture.ColorMultiFilter;
import com.atlan1.mctpo.Texture.TextureLoader;

public class Character extends DoubleRectangle implements LivingThing{
	private static BufferedImage animationTexture;
	private static BufferedImage damageAnimationTexture;
	private static int[]  character = {0, 0};
	static{
		animationTexture = TextureLoader.loadImage("res/animation.png");
	}
	
	private List<Thing> collisions = new ArrayList<Thing>();
	
	public double fallingSpeed = 4d;
	public double jumpingSpeed = 2d;
	public double movementSpeed = 1.5d;
	public double sprintSpeed = 0.5d;
	public boolean isMoving = false;
	public boolean wouldJump = false;
	public boolean isJumping = false;
	public boolean isSprinting = false;
	public int jumpHeight = 12, jumpCount = 0; 
	public double dir = 1;
	public int animation, animationFrame, animationTime = 15;
	public int sprintAnimationTime = 8;
	public double buildRange = 60; //in pixels
	public boolean isFalling = false;
	public double startFalling = 0;
	public Inventory inventory = new Inventory(this);
	public HealthBar healthBar = new HealthBar(this);
	public int maxHealth = 100;
	public int health = 100;
	public boolean damaged = false;
	public int damageTime=10, damageFrame=0;
	public int destroyTime=0;
	public Block currentBlock;
	public Block lastBlock;
	public final int bUP = 0, bDOWN = 1, bRIGHT = 2, bLEFT = 3;
	public Line2D.Double[] bounds = new Line2D.Double[4];
	
	public Character(double width, double height) {
		ImageFilter redfilter = new ColorMultiFilter(2, 0, 0);
		ImageProducer imageprod = new FilteredImageSource(animationTexture.getSource(), redfilter);
		damageAnimationTexture = MCTPO.toBufferedImage(MCTPO.mctpo.createImage(imageprod));
		setBounds(width, height, (MCTPO.pixel.width / 2) - (width / 2), (MCTPO.pixel.height / 2) - (height / 2));
		calcBounds();
	}
	
	public boolean isCollidingWithAnyBlock(Line2D line) {
		for(int x=(int)(this.x/MCTPO.tileSize);x<(int)(this.x/MCTPO.tileSize+3);x++)
			for(int y=(int)(this.y/MCTPO.tileSize);y<(int)(this.y/MCTPO.tileSize+3);y++)
				if(x >= 0 && y >= 0 && x < World.worldW && y < World.worldH){
					boolean collide = World.blocks[x][y].contains(line.getP1())|| World.blocks[x][y].contains(line.getP2());
					if(collide)
						collisions.add(World.blocks[x][y].addCollision(this));
					if(!World.blocks[x][y].material.nonSolid&&collide)
							return true;
				}
		return false;
	}
	
	public boolean isCollidingWithBlock(Shape t) {
		for(Line2D.Double l : bounds){
			if(t.contains(l.getP1())||t.contains(l.getP2())){
				return true;
			}
		}
		return false;
	}
	
	public void render(Graphics g){
		if(dir>=0)
			g.drawImage(!damaged?animationTexture:damageAnimationTexture, (int)x - (int) MCTPO.sX, (int)y - (int) MCTPO.sY, (int)(x + width) - (int) MCTPO.sX, (int)(y + height) - (int) MCTPO.sY, (character[0] * MCTPO.tileSize)+(MCTPO.tileSize * animation), (character[1] * MCTPO.tileSize), (character[0] * MCTPO.tileSize)+(animation * MCTPO.tileSize)+ (int) width, (character[1] * MCTPO.tileSize) + (int) height,null);
		else
			g.drawImage(!damaged?animationTexture:damageAnimationTexture, (int)(x + width) - (int) MCTPO.sX, (int)y - (int) MCTPO.sY, (int)x - (int) MCTPO.sX, (int)(y + height) - (int) MCTPO.sY, (character[0] * MCTPO.tileSize)+(MCTPO.tileSize * animation), (character[1] * MCTPO.tileSize), (character[0] * MCTPO.tileSize)+(animation * MCTPO.tileSize)+ (int) width, (character[1] * MCTPO.tileSize) + (int) height ,null);
	}
	
	public void tick(){
		calcBounds();
		clearCollisions();
		isSprinting = MCTPO.mctpo.controlDown;
		
		int firstHealth = health;
		boolean noGroundCollision = !isCollidingWithAnyBlock(bounds[bDOWN]);
		if(!isJumping && noGroundCollision){
			y+=fallingSpeed;
			MCTPO.sY+=fallingSpeed;
			if(!isFalling && noGroundCollision){
				startFalling=this.y;
				isFalling = true;
			}
		}else{
			if(wouldJump)
				isJumping = true;
		}
		
		if(!noGroundCollision && isFalling){
			int deltaFallBlocks = (int) ((this.y-startFalling)/MCTPO.tileSize);
			if(deltaFallBlocks>3)
				health-=deltaFallBlocks;
			startFalling=0;
			isFalling = false;
		}
		if(isJumping){
			boolean canJump = !isCollidingWithAnyBlock(bounds[bUP]);
			if(canJump){
				if(jumpHeight<=jumpCount){
					isJumping = false;
					jumpCount = 0;
				}else{
					y-=jumpingSpeed;
					MCTPO.sY-=jumpingSpeed;
					jumpCount++;
				}
			}else{
				isJumping = false;
				jumpCount = 0;
			}
		}		
		if(isMoving){
			boolean canMove = false;
			
			if(dir == movementSpeed){
				canMove = !isCollidingWithAnyBlock(bounds[bRIGHT]);
			}else if (dir == -movementSpeed){
				canMove = !isCollidingWithAnyBlock(bounds[bLEFT]);
			}
			
			if(animationFrame >= (isSprinting?sprintAnimationTime:animationTime)) {
				if(animation<3){
					animation++;
					animationFrame=0;
				}else{
					animation=0;
					animationFrame=0;
				}
			}else{
				animationFrame+=1;
			}
			
			if(canMove){
				x+=isSprinting?dir<0?dir-sprintSpeed:dir+sprintSpeed:dir;
				MCTPO.sX+=isSprinting?dir<0?dir-sprintSpeed:dir+sprintSpeed:dir;
			}
		}else{
			animation = 1;
		}
		if(firstHealth-health>0){
			damaged = true;
		}
		if(damaged){
			if(damageTime<=damageFrame){
				damageFrame=0;
				damaged= false;
			}else{
				damageFrame++;
			}
		}
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
		if(health<=0){
			inventory.clear();
			this.respawn();
			health = maxHealth;
		}
		if(this.y/MCTPO.tileSize>World.worldH){
			this.teleport((int) this.x, 0);
		}
	}
	
	public void calcBounds(){
		bounds[bUP] = new Line2D.Double(new Point((int)(x+2), (int) (y+1)), new Point((int)(x + width -2), (int)(y+1)));
		bounds[bDOWN] = new Line2D.Double(new Point((int)(x+2), (int) (y+height)), new Point((int)(x+width-2), (int)(y+height)));
		bounds[bRIGHT] = new Line2D.Double(new Point((int)(x + width -1), (int) y), new Point((int)(x + width), (int) (y + (height-2))));
		bounds[bLEFT] = new Line2D.Double(new Point((int)x-1, (int) y), new Point((int)x-1, (int) (y + (height-2))));
	}
	
	public void clearCollisions() {
		for(Thing t: new ArrayList<Thing>(collisions)) {
			if(!isCollidingWithBlock((Shape)t)){
				this.removeCollision(t);
				t.removeCollision(this);
			}
		}
	}
	
	public Block getCurrentBlock(){
		if (MCTPO.mouseLeftDown || MCTPO.mouseRightDown) {
			//Block[][] blocks = World.blocks;
			/*int camX=(int)MCTPO.sX;
			int camY=(int)MCTPO.sY;
			int renW=(MCTPO.pixel.width / MCTPO.tileSize) + 2;
			int renH=(MCTPO.pixel.height / MCTPO.tileSize) + 2;*/
			/*for(int x=(camX/MCTPO.tileSize);x<(camX/MCTPO.tileSize) + renW;x++){
				for(int y=(camY/MCTPO.tileSize);y<(camY/MCTPO.tileSize) + renH;y++){
					if(x>=0 && y>=0 && x<World.worldW && y<World.worldH){
						if(blocks[x][y].contains(new Point(MCTPO.fingerBuildP.x + (int)MCTPO.sX, MCTPO.fingerBuildP.y + (int)MCTPO.sY))){
							return blocks[x][y];
						}
					}
				}
			}*/
			try {
				Block b = getBlockIncluding(MCTPO.mouse.x, MCTPO.mouse.y); //changed for better performance
				return b;
			} catch (Exception e) {
				
			}
		}
		return null;
	}
	
	public Block getBlockIncluding(double x, double y) {
		return World.blocks[(int) ((x + MCTPO.sX) / MCTPO.tileSize)][(int) ((y + MCTPO.sY) / MCTPO.tileSize)];
	}
	
	public boolean isBlockInBuildRange(Block block) {
		return 	(new Point((int)(this.x+width/2), (int)(this.y+height/2))).distance(new Point((MCTPO.mouse.x + (int)MCTPO.sX), (MCTPO.mouse.y + (int)MCTPO.sY)))<=buildRange;
	}
	
	public void build(){
		if(isBlockInBuildRange(currentBlock)){
			Material m = currentBlock.material;
			if(MCTPO.mouseLeftDown){
				if(destroyTime>=m.hardness&&!(m.hardness<0)){
					if(inventory.containsMaterial(m)){
						boolean check = false;
						Slot[] slots = inventory.getSlotsContaining(m);
						for(Slot s : slots){
							if(s.stackSize<inventory.maxStackSize){
								s.stackSize++;
								check = true;
								break;
							}
						}
						if(!check && inventory.containsMaterial(Material.AIR)){
							Slot s2 = inventory.getSlot(Material.AIR);
							s2.material = m;
							s2.stackSize++;
						}
					}else if(inventory.containsMaterial(Material.AIR)){
						Slot s3 = inventory.getSlot(Material.AIR);
						s3.material = m;
						s3.stackSize++;
					}
					currentBlock.material = Material.AIR;
					destroyTime = 0;
				}
				return;
			}else if(MCTPO.mouseRightDown && m.nonSolid){
				if(!(inventory.slots[inventory.selected].material == Material.AIR)){
					if(inventory.slots[inventory.selected].stackSize>0)
						inventory.slots[inventory.selected].stackSize--;
					else{
						inventory.slots[inventory.selected].stackSize = 0;
						inventory.slots[inventory.selected].material = Material.AIR;
					}
					currentBlock.material = inventory.slots[inventory.selected].material;
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
		MCTPO.sY = y - (MCTPO.pixel.height / 2) + (height / 2);
		MCTPO.sX = x - (MCTPO.pixel.width / 2) + (width / 2);
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
	public Thing addCollision(Thing t) {
		collisions.add(t);
		return this;
	}

	@Override
	public Thing removeCollision(Thing t) {
		collisions.remove(t);
		return this;
	}
}
