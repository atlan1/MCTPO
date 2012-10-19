package com.atlan1.mctpo.Inventory;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.atlan1.mctpo.MCTPO;
import com.atlan1.mctpo.Material;

public class ItemStack{
	public Material material;
	public int stacksize=0;
	
	public ItemStack(Material mat) {
		material = mat;
	}
	
	public ItemStack(ItemStack i){
		material = i.material;
		stacksize = i.stacksize;
	}
	
	public void render(Graphics g, int x, int y, int width, int height) {
		if(stacksize>0&&material!=Material.AIR){
			g.drawImage(Material.terrain.getSubImageById(material.id), (int)x, (int)(y), (int)(width), (int)(height), 0, 0, MCTPO.tileSize, MCTPO.tileSize, null);
			g.setColor(new Color(255, 255, 200));
			g.setFont(Font.getFont("Tahoma"));
			g.drawString(stacksize+"", width-8, height);
		}else if(stacksize<=0||material==Material.AIR){
			material = Material.AIR;
			stacksize = 0 ;
		}
	}
	
	public boolean equals(Object o){
		if(!(o instanceof ItemStack)) {
			return false;
		}
		ItemStack i = (ItemStack) o;
		if(i.material == material&&i.stacksize==stacksize){
			return true;
		}
		return false;
	}
}
