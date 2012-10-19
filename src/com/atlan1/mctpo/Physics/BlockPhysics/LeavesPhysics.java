package com.atlan1.mctpo.Physics.BlockPhysics;

import com.atlan1.mctpo.Block;
import com.atlan1.mctpo.Material;
import com.atlan1.mctpo.API.Thing;

public class LeavesPhysics implements BlockPhysics{

	private AbstractBlockPhysics abs;
	
	public LeavesPhysics() {
		abs = new DecayPhysics(400, Material.WOOD, 40);
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
