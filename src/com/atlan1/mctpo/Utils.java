package com.atlan1.mctpo;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import com.atlan1.mctpo.API.Collideable;
import com.atlan1.mctpo.API.Thing;

public class Utils {
	public static Rectangle toRectangle(Rectangle2D r2d2){
		Rectangle r = new Rectangle();
		r.setBounds((int)Math.floor(r2d2.getX()), (int)Math.floor(r2d2.getY()), (int)Math.floor(r2d2.getWidth()), (int)Math.floor(r2d2.getHeight()));
		return r;
	}

	public static boolean isColliding(Collideable col) {
		Rectangle bounds = toRectangle(col.getBounds());
		for(Thing t : World.getNearbyThings((bounds.x+bounds.width/2), (bounds.y+bounds.height/2), 2)){
			if(t==col) continue;
			if(col.isColliding(t)){
				return true;
			}
		}
		return false;
	}
}
