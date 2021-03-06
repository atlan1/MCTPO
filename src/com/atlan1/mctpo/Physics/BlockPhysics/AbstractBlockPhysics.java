package com.atlan1.mctpo.Physics.BlockPhysics;

import java.util.HashMap;
import java.util.Map;

import com.atlan1.mctpo.Block;
import com.atlan1.mctpo.API.Thing;
import com.atlan1.mctpo.Physics.AbstractPhysics;

public abstract class AbstractBlockPhysics implements AbstractPhysics<Boolean>{

	private Map<Block, Integer> blockID = new HashMap<Block, Integer>();
	
	public boolean performPhysics(Block b){
		return false;
	}
	
	public boolean performCollision(Block b, Thing t){
		return false;
	}
	
	protected void addId(Block b){
		if(blockID.keySet().contains(b)){
			return;
		}else{
			blockID.put(b, b.requestFramesId());
		}
	}
	
	protected int getId(Block b){
		if(blockID.keySet().contains(b)){
			return blockID.get(b);
		}
		return 0;
	}

	@Override
	public Boolean doPhysics(Object... objs) {
		return performPhysics((Block)objs[0]);
	}
	
	
}
