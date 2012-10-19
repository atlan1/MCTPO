package com.atlan1.mctpo.HUD;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.atlan1.mctpo.ItemStack;
import com.atlan1.mctpo.MCTPO;
import com.atlan1.mctpo.Material;
import com.atlan1.mctpo.API.Widget;
import com.atlan1.mctpo.Inventory.Inventory;
import com.atlan1.mctpo.Inventory.Slot;

public class InventoryBar implements Widget{

	public int selected = 0;
	public Slot[] slots = new Slot [Inventory.invLength];
	
	public InventoryBar() {
		for(int i=0;i<slots.length;i++){
			slots[i] = new Slot(new Rectangle((MCTPO.pixel.width/2)-((Inventory.invLength * (Inventory.slotSize + Inventory.slotSpace))/2)+((i * (Inventory.slotSize + Inventory.slotSpace))), MCTPO.pixel.height - (Inventory.slotSize + Inventory.borderSpace), Inventory.slotSize, Inventory.slotSize), new ItemStack(Material.AIR));
		}
	}
	
	@Override
	public void render(Graphics g) {
		for(int i=0;i<slots.length;i++){
			slots[i].render(g, i==selected);
		}
	}

	@Override
	public void tick() {
		for(Slot s : slots){
			s.tick();
		}
	}

	@Override
	public void calcPosition() {
		for(int i=0;i<slots.length;i++){
			slots[i].setBounds(new Rectangle((MCTPO.pixel.width/2)-((Inventory.invLength * (Inventory.slotSize + Inventory.slotSpace))/2)+((i * (Inventory.slotSize + Inventory.slotSpace))), MCTPO.pixel.height - (Inventory.slotSize + Inventory.borderSpace), Inventory.slotSize, Inventory.slotSize));
		}
	}
	
	

}
