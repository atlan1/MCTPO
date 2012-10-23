package com.atlan1.mctpo;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import com.atlan1.mctpo.API.Collideable;

public class BB extends Rectangle2D.Double implements Collideable {
	private static final long serialVersionUID = 1L;

	public BB(Rectangle2D.Double d) {
		this.setRect(d);
	}
	
	@Override
	public Rectangle getBounds() {
		return new Rectangle(CollisionDetector.toRectangle(this));
	}

	@Override
	public boolean isColliding(Collideable t) {
		if(t instanceof Block&&((Block)t).material.nonSolid)
			return false;
		if(t instanceof Character)
			return false;
		return this.intersects(t.getBounds());
	}

	@Override
	public void onCollide(Collideable c) {
		
	}

}
