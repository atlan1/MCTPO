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
	public Set<Collideable> collisionWith = new HashSet<Collideable>();
	public Rectangle union = new Rectangle();
	
	public Character(double width, double height) {
		this.width=width;
		this.height=height;
		this.x = (MCTPO.pixel.width / 2) - (width / 2);
		this.y= ((MCTPO.pixel.height / 2) - (height / 2));
	}
	
	
	public void render(Graphics g){
		if(dir>=0)
			g.drawImage(!damaged?animationTexture:damageAnimationTexture, (int)x - (int) MCTPO.camX, (int)y - (int) MCTPO.camY, (int)(x + width) - (int) MCTPO.camX, (int)(y + height) - (int) MCTPO.camY, (character[0] * MCTPO.blockSize)+(MCTPO.blockSize * animation), (character[1] * MCTPO.blockSize), (character[0] * MCTPO.blockSize)+(animation * MCTPO.blockSize)+ (int) width, (character[1] * MCTPO.blockSize) + (int) height,null);
		else
			g.drawImage(!damaged?animationTexture:damageAnimationTexture, (int)(x + width) - (int) MCTPO.camX, (int)y - (int) MCTPO.camY, (int)x - (int) MCTPO.camX, (int)(y + height) - (int) MCTPO.camY, (character[0] * MCTPO.blockSize)+(MCTPO.blockSize * animation), (character[1] * MCTPO.blockSize), (character[0] * MCTPO.blockSize)+(animation * MCTPO.blockSize)+ (int) width, (character[1] * MCTPO.blockSize) + (int) height ,null);
	}
	
	public void tick(long d){
		isSprinting = MCTPO.controlDown;
		int firstHealth = health;
		
		if(!isJumping){
			this.move(0, d*fallingSpeed);
			if(!isFalling){
				startFalling=this.y;
				isFalling = true;
			}
		}
		if(wouldJump&&!stopJumping)
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
				destroyTime+=d;
			}else{
				destroyTime=0;
			}
			lastBlock = currentBlock;
			currentBlock = getCurrentBlock();
			if(currentBlock!=null)
				build(d);
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
		
		collisionWith = new HashSet<Collideable>();
		
		inv.tick();
		hud.tick();
	}
	
	public Block getCurrentBlock(){
		try {
			Block b = getBlockIncluding(MCTPO.mouse.x, MCTPO.mouse.y);
			return b;
		} catch (Exception e) {
			
		}
		return null;
	}
	
	public Block getBlockIncluding(double x, double y) {
		return World.blocks[(int) ((x + MCTPO.camX) / MCTPO.blockSize)][(int) ((y + MCTPO.camY) / MCTPO.blockSize)];
	}
	
	public boolean isBlockInBuildRange(Block block) {
		return 	(new Point((int)(this.x+width/2), (int)(this.y+height/2))).distance(new Point((int)block.getCenterX(), (int)block.getCenterY()))<=buildRange*MCTPO.blockSize;
	}
	
	public void build(long d){
		if(isBlockInBuildRange(currentBlock)){
			Material m = currentBlock.material;
			if(MCTPO.mouseLeftDown){
				if(destroyTime/d>=m.hardness&&!(m.hardness<0)){
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
		if(Utils.isColliding(new BB(new Rectangle2D.Double(this.x+dx, this.y+dy, this.width, this.height)))){
			return;
		}
		this.y += dy;
		this.x += dx;
		MCTPO.camY = this.y - (MCTPO.pixel.height / 2) + (height / 2);
		MCTPO.camX = this.x - (MCTPO.pixel.width / 2) + (width / 2);
	}

	@Override
	public void move(double dx, double dy) {
		if(Utils.isColliding(new BB(new Rectangle2D.Double(this.x+dx, this.y+dy, this.width, this.height)))){
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

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, (int)width, (int)height);
	}

	@Override
	public void onCollide(Collideable c) {
		collisionWith.add(c);
	}
}

