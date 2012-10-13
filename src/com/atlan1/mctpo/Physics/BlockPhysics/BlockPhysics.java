package com.atlan1.mctpo.Physics.BlockPhysics;

import com.atlan1.mctpo.Block;
import com.atlan1.mctpo.Thing;

public interface BlockPhysics {

	public boolean performPhysics(Block b);

	public boolean performCollision(Block b, Thing ent);
	
}
