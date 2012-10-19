package com.atlan1.mctpo.Inventory;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import com.atlan1.mctpo.ItemStack;
import com.atlan1.mctpo.Texture.TextureLoader;

public class Slot extends Rectangle{
	private static final long serialVersionUID = 1L;
	
	public static Image slotNormal= TextureLoader.loadImage("/res/slot_normal.png");
	public static Image slotSelected= TextureLoader.loadImage("/res/slot_selected.png");
	
	public ItemStack itemstack;
	
	public Slot(ItemStack i){
		itemstack = i;
	}
	
	public Slot(Rectangle rectangle,  ItemStack i){
		setBounds(rectangle);
		itemstack = i;
	}
	
	public void render(Graphics g, boolean selected){
		g.drawImage(slotNormal, x, y, width, height, null);
		if(selected){
			g.drawImage(slotSelected, x-1, y-1, width+2, height+2, null);
		}
		itemstack.render(g, x+Inventory.itemBorder, y+Inventory.itemBorder, x+width-Inventory.itemBorder, y+height-Inventory.itemBorder);
	}

	public void tick() {
	}
	
}
