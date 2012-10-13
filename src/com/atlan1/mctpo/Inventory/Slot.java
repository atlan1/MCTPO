package com.atlan1.mctpo.Inventory;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.atlan1.mctpo.MCTPO;
import com.atlan1.mctpo.Material;
import com.atlan1.mctpo.Texture.TextureLoader;

public class Slot extends Rectangle {
	private static final long serialVersionUID = 1L;
	private Inventory inv;
	
	public static BufferedImage slotNormal= TextureLoader.loadImage("res/slot_normal.png");
	public static BufferedImage slotSelected= TextureLoader.loadImage("res/slot_selected.png");
	
	public Material material = Material.AIR;
	public int stackSize = 0;
	
	public Slot(Inventory inv, Rectangle rectangle, Material m){
		this.inv = inv;
		setBounds(rectangle);
		material = m;
	}
	
	public void render(Graphics g, boolean selected){
		g.drawImage(slotNormal, x, y, width, height, null);
		if(selected){
			g.drawImage(slotSelected, x-1, y-1, width+2, height+2, null);
		}
		if(stackSize>0&&material!=Material.AIR){
			g.drawImage(Material.terrain.getSubImageById(material.id), (int)x+inv.itemBorder, (int)y+inv.itemBorder, (int)(x + (width-inv.itemBorder)), (int)(y + (height-inv.itemBorder)), 0, 0, MCTPO.tileSize, MCTPO.tileSize, null);
			g.setColor(new Color(255, 255, 200));
			g.setFont(Font.getFont("Tahoma"));
			g.drawString(stackSize+"", x+width-8, y+height);
		}
	}

}
