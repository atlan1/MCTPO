package com.atlan1.mctpo.Listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.atlan1.mctpo.MCTPO;
import com.atlan1.mctpo.Character;
import com.atlan1.mctpo.HUD.InventoryBar;
import com.atlan1.mctpo.Inventory.Inventory;

public class MouseListener implements MouseMotionListener, MouseWheelListener,
		java.awt.event.MouseListener {

	private Character c;

	public MouseListener(Character c){
		this.c = c;
	}
	
	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1){
			MCTPO.mouseLeftDown = true;
		}else if(e.getButton() == MouseEvent.BUTTON3){
			MCTPO.mouseRightDown = true;
		}
		if(c.inv.isOpen()) {
			c.inv.onClick(e.getButton());
		}
	}

	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1){
			MCTPO.mouseLeftDown = false;
		}else if(e.getButton() == MouseEvent.BUTTON3){
			MCTPO.mouseRightDown = false;
		}
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		if(e.getWheelRotation()<0){//up
			if(c.hud.getWidget(InventoryBar.class).selected==0){
				c.hud.getWidget(InventoryBar.class).selected=Inventory.invLength-1;
			}else
				c.hud.getWidget(InventoryBar.class).selected-=1;
		}else if(e.getWheelRotation()>0){//down
			if(c.hud.getWidget(InventoryBar.class).selected==Inventory.invLength-1){
				c.hud.getWidget(InventoryBar.class).selected = 0;
			}else
				c.hud.getWidget(InventoryBar.class).selected+=1;
		}
	}

	public void mouseDragged(MouseEvent e) {
		MCTPO.mouse.x = e.getX()/MCTPO.pixelSize;
		MCTPO.mouse.y = e.getY()/MCTPO.pixelSize;
	}

	public void mouseMoved(MouseEvent e) {
		MCTPO.mouse.x = e.getX()/MCTPO.pixelSize;
		MCTPO.mouse.y = e.getY()/MCTPO.pixelSize;
	}

}
