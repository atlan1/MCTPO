package com.atlan1.mctpo.Physics.BlockPhysics;

import com.atlan1.mctpo.Block;
import com.atlan1.mctpo.API.Thing;

public class LavaPhysics implements BlockPhysics {

	private AbstractBlockPhysics abs;
	private AbstractBlockPhysics c;
	
	public LavaPhysics() {
		abs = new FlowPhysics(30, false);
		c = new DamageCollision(10, 30);
	}

	@Override
	public boolean performPhysics(Block b) {
		return abs.performPhysics(b);
	}

	@Override
	public boolean performCollision(Block b, Thing ent) {
		return c.performCollision(b, ent);
	}

}
