package com.atlan1.mctpo.Inventory;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.atlan1.mctpo.Material;
import com.atlan1.mctpo.Texture.TextureLoader;

public class Slot extends Rectangle {
	private static final long serialVersionUID = 1L;
	private Inventory inv;
	private Slot barSlot;
	
	public static BufferedImage slotNormal= TextureLoader.loadImage("res/slot_normal.png");
	public static BufferedImage slotSelected= TextureLoader.loadImage("res/slot_selected.png");
	
	public ItemStack itemstack;
	private ItemStack old;
	
	public Slot(Inventory inv, Slot b, Rectangle rectangle, Material m){
		this.inv = inv;
		setBounds(rectangle);
		barSlot = b;
		itemstack = new ItemStack(m);
	}
	
	public void render(Graphics g, boolean selected){
		g.drawImage(slotNormal, x, y, width, height, null);
		if(selected){
			g.drawImage(slotSelected, x-1, y-1, width+2, height+2, null);
		}
		itemstack.render(g, x+inv.itemBorder, y+inv.itemBorder, x+width-inv.itemBorder, y+height-inv.itemBorder);
	}

	public void tick() {
		if(barSlot != null){
			if(!itemstack.equals(old)){
				this.barSlot.itemstack = new ItemStack(this.itemstack);
			}
		}
	}
	
}
