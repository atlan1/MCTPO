package com.atlan1.mctpo.HUD;

import java.awt.Graphics;
import java.awt.Image;

import com.atlan1.mctpo.Character;
import com.atlan1.mctpo.MCTPO;
import com.atlan1.mctpo.API.Widget;
import com.atlan1.mctpo.Inventory.Inventory;
import com.atlan1.mctpo.Texture.TextureLoader;

public class HealthBar implements Widget{

	private static Image hIcon = TextureLoader.loadImage("/res/heart.png");
	private static Image hblackIcon = TextureLoader.loadImage("/res/heart_black.png");
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
				g.drawImage(black?hblackIcon:hIcon, (MCTPO.pixel.width/2)-((maxHearts * (heartSize + heartSpace))/2)+((x * (heartSize + heartSpace))), MCTPO.pixel.height - (Inventory.slotSize + Inventory.borderSpace + invBorder + heartSize),heartSize,heartSize, null);
			}
		}
	}

	@Override
	public void calcPosition() {
		//is done in render() already
	}
}
