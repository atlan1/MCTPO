package com.atlan1.mctpo;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.atlan1.mctpo.API.Collideable;
import com.atlan1.mctpo.API.Side;
import com.atlan1.mctpo.API.Thing;

public class Block extends Rectangle implements Thing{
	private static final long serialVersionUID = 1L;
	
	public Material material = Material.AIR;
	public List<Long> timeOfUpdate = new ArrayList<Long>();
	
	public Block(Rectangle size, int id) {
		setBounds(size);
		this.material = Material.getById(id);
	}
	
	public Block(Rectangle size, Material m) {
		setBounds(size);
		this.material = m;
	}
	
	public void render(Graphics g) {
		if(material != Material.AIR){
			g.drawImage(Material.terrain.getSubImageById(material.id), (int)x - (int) MCTPO.camX, (int)y - (int) MCTPO.camY, (int)(x + width) - (int) MCTPO.camX, (int)(y + height) - (int) MCTPO.camY, 0, 0, MCTPO.blockSize, MCTPO.blockSize, null);
		}
	}
	
	public void update(int i) {
		timeOfUpdate.set(i, System.currentTimeMillis());
	}

	public void tick(long d){
		if(!material.physics.isEmpty()){
			material.doPhysics(this);
		}
			
	}
	
	public synchronized int requestFramesId(){
		timeOfUpdate.add(System.currentTimeMillis());
		return timeOfUpdate.size()-1;
	}
	
	public int getGridX(){
		return x/MCTPO.blockSize;
	}
	
	public int getGridY(){
		return y/MCTPO.blockSize;
	}

	@Override
	public boolean isColliding(Collideable other) {
		return this.intersects(other.getBounds());
	}

	public Set<Side> getCollisionSides(Collideable otherC) {
		Rectangle other = CollisionDetector.toRectangle(otherC.getBounds());
		Set<Side> sides = new HashSet<Side>();
		if(this.y+this.height >= other.y)
			sides.add(Side.BOTTOM);
		if(this.y <= other.y+other.height)
			sides.add(Side.TOP);
		if(this.x+this.width <= other.x)
			sides.add(Side.RIGHT);
		if(this.x >= other.x+other.width)
			sides.add(Side.LEFT);
		if(sides.isEmpty())
			sides.add(Side.UNKNOWN);
		return sides;
	}
	
	@Override
	public void onCollide(Collideable t) {
		if(t instanceof Thing)
			material.collide(this, (Thing)t);
	}
}
