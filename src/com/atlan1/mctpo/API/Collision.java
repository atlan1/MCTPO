package com.atlan1.mctpo.API;

import java.util.Set;

public class Collision {

	private Collideable other;
	private Set<Side> sides;
	
	
	public Collision(Collideable c, Set<Side> set) {
		other = c;
		sides = set;
	}


	public Collideable getCollideable() {
		return other;
	}

	public Set<Side> getCollisionSides() {
		return sides;
	}
}
