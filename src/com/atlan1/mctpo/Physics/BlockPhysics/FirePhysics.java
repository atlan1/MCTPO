package com.atlan1.mctpo.Physics.BlockPhysics;

import com.atlan1.mctpo.Block;
import com.atlan1.mctpo.API.Thing;

public class FirePhysics implements BlockPhysics {

	private AbstractBlockPhysics abs;
	
	public FirePhysics() {
		abs = new SpreadPhysics(20);
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
