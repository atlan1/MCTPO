package com.atlan1.mctpo.API;

public enum Side {
	TOP(0), BOTTOM(1), RIGHT(2), LEFT(3), UNKNOWN(-1);
	
	private int id = -1;
	
	private Side(int i) {
		this.id = i;
	}
	
	public int getId(){
		return id;
	}
}
