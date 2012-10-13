package com.atlan1.mctpo.Physics.BlockPhysics;

import com.atlan1.mctpo.Block;
import com.atlan1.mctpo.Thing;

public class SandPhysics implements BlockPhysics {

	private AbstractBlockPhysics abs ;
	
	public SandPhysics() {
		abs = new FallPhysics(10);
	}

	@Override
	public boolean performPhysics(Block b) {
		return abs.performPhysics(b);
	}

	@Override
	public boolean performCollision(Block b, Thing ent) {
		return false;
	}
}
