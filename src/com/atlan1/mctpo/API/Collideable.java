package com.atlan1.mctpo.API;

import java.awt.geom.Rectangle2D;


public interface Collideable {
	public Rectangle2D getBounds();
	public boolean isColliding(Collideable t);
	public void onCollide(Collideable c);
}
