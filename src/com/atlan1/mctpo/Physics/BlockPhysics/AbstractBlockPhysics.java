package com.atlan1.mctpo.Physics.BlockPhysics;

import com.atlan1.mctpo.Block;
import com.atlan1.mctpo.Thing;

public class AbstractBlockPhysics {

	public boolean performPhysics(Block b){
		return false;
	}
	
	public boolean performCollision(Block b, Thing t){
		return false;
	}
	
}
