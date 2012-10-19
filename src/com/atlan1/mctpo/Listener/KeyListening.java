package com.atlan1.mctpo.Listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.atlan1.mctpo.Character;
import com.atlan1.mctpo.MCTPO;
import com.atlan1.mctpo.Material;

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
				c.inventory.barSlots[c.inventory.selected].itemstack.material = Material.AIR;
				c.inventory.barSlots[c.inventory.selected].itemstack.stacksize = 0;
				break;
			case KeyEvent.VK_CONTROL:
				MCTPO.mctpo.controlDown = true;
				break;
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
				break;
			case KeyEvent.VK_E:
				c.inventory.setOpen(!c.inventory.isOpen());
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
				MCTPO.mctpo.controlDown = false;
				break;
		}
	}

	public void keyTyped(KeyEvent e) {}

}
