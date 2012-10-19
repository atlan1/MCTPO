package com.atlan1.mctpo;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.atlan1.mctpo.API.Thing;

public class Block extends Rectangle implements Thing{
	private static final long serialVersionUID = 1L;
	
	public Material material = Material.AIR;

	public List<Integer> framesSinceUpdate = new ArrayList<Integer>();
	public List<Thing> collisions = new ArrayList<Thing>();
	
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
			g.drawImage(Material.terrain.getSubImageById(material.id), (int)x - (int) MCTPO.sX, (int)y - (int) MCTPO.sY, (int)(x + width) - (int) MCTPO.sX, (int)(y + height) - (int) MCTPO.sY, 0, 0, MCTPO.blockSize, MCTPO.blockSize, null);
		}
	}
	
	public void update(int index) {
		framesSinceUpdate.set(index, 0);
	}

	public void tick(){
		if(!material.physics.isEmpty()){
			material.doPhysics(this);
			for(Thing t : new ArrayList<Thing>(collisions)){
				material.collide(this, t);
			}
			for(int i=0;i<framesSinceUpdate.size();i++){
				framesSinceUpdate.set(i, framesSinceUpdate.get(i)+1);
			}
		}
			
	}

	public Block addCollision(Thing ent) {
		this.collisions.add(ent);
		return this;
	}
	
	public Block removeCollision(Thing ent) {
		this.collisions.remove(ent);
		return this;
	}
	
	public synchronized int requestFramesId(){
		framesSinceUpdate.add(0);
		return framesSinceUpdate.get(framesSinceUpdate.size()-1);
	}
	
	public int getGridX(){
		return x/MCTPO.blockSize;
	}
	
	public int getGridY(){
		return y/MCTPO.blockSize;
	}
}
