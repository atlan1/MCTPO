package com.atlan1.mctpo.HUD;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.atlan1.mctpo.API.Widget;

public class HUD{

	private List<Widget> widgets = new ArrayList<Widget>();

	public HUD(Widget...widgets){
		for(Widget w : widgets){
			addWidget(w);
		}
	}
	
	public void render(Graphics g) {
		for(Widget w : widgets){
			w.render(g);
		}
	}

	public void tick() {
		for(Widget w : widgets) {
			w.tick();
		}
	}

	public void calcPosition() {
		for(Widget w : widgets){
			w.calcPosition();
		}
	}
	
	public void addWidget(Widget w){
		widgets.add(w);
	}
	
	public void removeWidget(Widget w){
		widgets.remove(w);
	}
	
	public void removeAll() {
		widgets = new ArrayList<Widget>();
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Widget> T getWidget(Class<T> c) {
		for(Widget w : widgets){
			if(c.isAssignableFrom(w.getClass()))
				return (T)w;
		}
		return null;
	}
}
