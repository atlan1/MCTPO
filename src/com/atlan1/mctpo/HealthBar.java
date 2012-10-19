package com.atlan1.mctpo;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.atlan1.mctpo.Texture.TextureLoader;

public class HealthBar {

	private static BufferedImage hIcon = TextureLoader.loadImage("res/heart.png");
	private static BufferedImage hblackIcon = TextureLoader.loadImage("res/heart_black.png");
	private Character c;
	
	public boolean render = true;
	public int maxHearts = 10;
	public int invBorder = 10;
	public int heartSpace = 4;
	public int heartSize = 20;
	
	public HealthBar(Character c){
		this.c = c;
	}
	
	public void tick(){
		
	}
	
	public void render(Graphics g) {
		if(render){
			int heartsLeft = c.health/(c.maxHealth/maxHearts);
			for(int x=0;x<maxHearts;x++){
				boolean black = heartsLeft-x<0;
				g.drawImage(black?hblackIcon:hIcon, (MCTPO.pixel.width/2)-((maxHearts * (heartSize + heartSpace))/2)+((x * (heartSize + heartSpace))), MCTPO.pixel.height - (c.inventory.slotSize + c.inventory.borderSpace + invBorder + heartSize),heartSize,heartSize, null);
			}
		}
	}
}
