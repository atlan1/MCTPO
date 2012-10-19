package com.atlan1.mctpo.Physics.BlockPhysics;

import com.atlan1.mctpo.Block;

public class SpreadPhysics extends AbstractBlockPhysics {

	
	public SpreadPhysics(int t) {
	}
	
	public boolean spread(Block b){
		return false;
	}

	@Override
	public boolean performPhysics(Block b) {
		return spread(b);
	}

}
