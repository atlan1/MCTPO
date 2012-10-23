package com.atlan1.mctpo;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.atlan1.mctpo.API.Collideable;
import com.atlan1.mctpo.API.Thing;

public class CollisionDetector{

	private List<Collideable> c = new ArrayList<Collideable>();
	public boolean check = true;
	
	public void checkCollisions() {
		if(check)
		for(Collideable col:c){
			Rectangle bounds = Utils.toRectangle(col.getBounds());
			for(Thing t : World.getNearbyThings((bounds.x+bounds.width/2), (bounds.y+bounds.height/2), 2)){
				if(t==col) continue;
				if(col.isColliding(t)){
					col.onCollide(t);
					t.onCollide(col);
				}
			}
			
		}
	}
	
	public void add(Collideable c){
		this.c.add(c);
	}

	public void remove(Collideable c){
		this.c.remove(c);
	}
}
