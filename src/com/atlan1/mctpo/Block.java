package com.atlan1.mctpo;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.atlan1.mctpo.API.Collideable;
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
		if(other instanceof Block)
			return false;
		return this.intersects(other.getBounds());
	}
	
	@Override
	public void onCollide(Collideable t) {
		if(t instanceof Thing)
			material.collide(this, (Thing)t);
	}
}
