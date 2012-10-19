package com.atlan1.mctpo.API;

import java.awt.Graphics;

public interface Widget{

	public void render(Graphics g);
	public void tick();
	public void calcPosition();
	
}
