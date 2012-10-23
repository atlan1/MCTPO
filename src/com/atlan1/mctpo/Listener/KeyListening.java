package com.atlan1.mctpo.Listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.atlan1.mctpo.Character;
import com.atlan1.mctpo.MCTPO;
import com.atlan1.mctpo.Material;
import com.atlan1.mctpo.HUD.InventoryBar;

public class KeyListening implements KeyListener {

	private Character c;
	
	public KeyListening(Character c){
		this.c = c;
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key){
			case KeyEvent.VK_A:
				c.isMoving = true;
				c.dir = -c.movementSpeed;
				break;
			case KeyEvent.VK_D:
				c.isMoving = true;
				c.dir = c.movementSpeed;
				break;
			case KeyEvent.VK_W:
				c.wouldJump = true;
				break;
			case KeyEvent.VK_Q:
				c.hud.getWidget(InventoryBar.class).slots[c.hud.getWidget(InventoryBar.class).selected].itemstack.material = Material.AIR;
				c.hud.getWidget(InventoryBar.class).slots[c.hud.getWidget(InventoryBar.class).selected].itemstack.stacksize = 0;
				break;
			case KeyEvent.VK_CONTROL:
				MCTPO.controlDown = true;
				break;
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
				break;
			case KeyEvent.VK_E:
				c.inv.setOpen(!c.inv.isOpen());
				break;
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key){
			case KeyEvent.VK_A:
				if(c.dir == -c.movementSpeed){
					c.isMoving = false;
				}
				break;
			case KeyEvent.VK_D:
				if(c.dir == c.movementSpeed){
					c.isMoving = false;
				}
				break;
			case KeyEvent.VK_W:
				c.wouldJump = false;
				break;
			case KeyEvent.VK_CONTROL:
				MCTPO.controlDown = false;
				break;
		}
	}

	public void keyTyped(KeyEvent e) {}

}
