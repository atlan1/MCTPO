package com.atlan1.mctpo.Inventory;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.atlan1.mctpo.Character;
import com.atlan1.mctpo.MCTPO;
import com.atlan1.mctpo.Material;

public class Inventory {
	
	private boolean open = false;
	private ItemStack onCursor = new ItemStack(Material.AIR);
	
	public int invLength = 8;
	public int invHeight = 5;
	public int slotSize = 25;
	public int slotSpace = 5;
	public int borderSpace = 20;
	public int itemBorder = 3;
	public int maxStackSize = 64;
	public int cursorSize = 20;
	
	public Slot[] barSlots = new Slot[invLength];
	public Slot[] slots = new Slot[invLength*invHeight];
	public int selected=0;
	
	public Inventory(Character c){
		for(int i=0;i<barSlots.length;i++){
			barSlots[i] = new Slot(this, null, new Rectangle((MCTPO.pixel.width/2)-((invLength * (slotSize + slotSpace))/2)+((i * (slotSize + slotSpace))), MCTPO.pixel.height - (slotSize + borderSpace), slotSize, slotSize), Material.AIR);
		}
		int x=0, y=0;
		for(int i=0;i<slots.length;i++){
			slots[i] = new Slot(this ,i<barSlots.length?barSlots[i]:null, new Rectangle((MCTPO.pixel.width/2)-((invLength * (slotSize + slotSpace))/2)+((x * (slotSize + slotSpace))), (MCTPO.pixel.height + (slotSize + borderSpace) - (invHeight * (slotSize + slotSpace))) - (y * (slotSize+slotSpace))-(MCTPO.pixel.height/6), slotSize, slotSize), Material.AIR);
			x++;
			if(x>=invLength){
				x=0;
				y++;
			}
		}
	}
	
	public void onClick(int b){
		Slot clicked = null;
		for(int i=0;i<slots.length;i++){
			if(slots[i].contains(new Point((MCTPO.mouse.x), (MCTPO.mouse.y)))){
				clicked = slots[i];
			}
		}
		if(b==1&&clicked!=null){
			if(onCursor.material == Material.AIR){
				onCursor = clicked.itemstack;
				clicked.itemstack = new ItemStack(Material.AIR);
			}else if(onCursor.material == clicked.itemstack.material){
				if(clicked.itemstack.stacksize+onCursor.stacksize<=maxStackSize){
					clicked.itemstack.stacksize += onCursor.stacksize;
					onCursor.stacksize = 0;
				} else if(clicked.itemstack.stacksize+onCursor.stacksize>maxStackSize){
					int before = clicked.itemstack.stacksize;
					clicked.itemstack.stacksize = maxStackSize;
					onCursor.stacksize = maxStackSize - before;
				}
			}else if(onCursor.material != Material.AIR){
				ItemStack temp = clicked.itemstack;
				clicked.itemstack = onCursor;
				if(clicked.itemstack.material!=Material.AIR){
					onCursor = temp;
				}else{
					onCursor = new ItemStack(Material.AIR);
				}
			}
		}else if(b==3&&clicked!=null){
			if(onCursor.material == Material.AIR){
				int half = clicked.itemstack.stacksize/2;
				int rest = clicked.itemstack.stacksize-half;
				onCursor = new ItemStack(clicked.itemstack.material);
				onCursor.stacksize = half;
				clicked.itemstack.stacksize = rest;
			}else if(onCursor.material != Material.AIR){
				if(clicked.itemstack.material == Material.AIR){
					clicked.itemstack.material = onCursor.material;
					clicked.itemstack.stacksize = 1;
					onCursor.stacksize--;
				}else if(clicked.itemstack.material == onCursor.material) {
					clicked.itemstack.stacksize++;
					onCursor.stacksize--;
				}else if(clicked.itemstack.material != Material.AIR){
					ItemStack temp = clicked.itemstack;
					clicked.itemstack = onCursor;
					if(clicked.itemstack.material!=Material.AIR){
						onCursor = temp;
					}else{
						onCursor = new ItemStack(Material.AIR);
					}
				}
			}
		}
	}
	
	public void render(Graphics g){
		for(int i=0;i<barSlots.length;i++){
			barSlots[i].render(g, i==selected);
		}
		if(open){
			g.setColor(new Color(200, 200, 200, 130));
			g.fillRect(0, 0, MCTPO.pixel.width, MCTPO.pixel.height);
			for(int i=0;i<slots.length;i++){
				slots[i].render(g, false);
			}
			if(onCursor!=null) {
				onCursor.render(g, MCTPO.mouse.x-cursorSize/2, MCTPO.mouse.y-cursorSize/2, MCTPO.mouse.x+cursorSize/2, MCTPO.mouse.y+cursorSize/2);
			}
		}
	}

	public void tick() {
		for(Slot s : slots) {
			s.tick();
		}
	}
	
	public boolean containsMaterial(Material m){
		for(Slot s : slots){
			if(m==s.itemstack.material) return true;
		}
		return false;
	}
	
	public Slot getSlot(Material m){
		for(Slot s : slots){
			if(m==s.itemstack.material) return s;
		}
		return null;
	}
	
	public Slot[] getSlotsContaining(Material m){
		List<Slot> slots2 = new ArrayList<Slot>();
		for(int i=0;i<slots.length;i++){
			if(slots[i].itemstack.material == m)
				slots2.add(slots[i]);
		}
		return slots2.toArray(new Slot[slots2.size()]);
	}
	
	public void clear(){
		for(Slot s : slots){
			s.itemstack.material = Material.AIR;
			s.itemstack.stacksize = 0;
		}
	}
	
	public boolean isOpen() {
		return open;
	}
	
	public void setOpen(boolean o) {
		open = o;
	}
}
