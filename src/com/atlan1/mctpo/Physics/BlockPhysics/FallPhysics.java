package com.atlan1.mctpo.Physics.BlockPhysics;

import com.atlan1.mctpo.Block;
import com.atlan1.mctpo.MCTPO;
import com.atlan1.mctpo.Material;
import com.atlan1.mctpo.World;

public class FallPhysics extends AbstractBlockPhysics {

	private int tick = 0;
	
	public FallPhysics(int tick) {
		this.tick = tick;
	}

	public boolean fall(Block b){
		addId(b);
		if(b!=null){
			if(MCTPO.thisTime - b.timeOfUpdate.get(getId(b))>=tick){
				Material m = b.material;
				try{
					if(World.blocks[b.getGridX()][b.getGridY()+1].material.nonSolid && World.blocks[b.getGridX()][b.getGridY()+1].material != m){
						World.blocks[b.getGridX()][b.getGridY()+1].material = m;
						b.material = Material.AIR;
						World.blocks[b.getGridX()][b.getGridY()+1].update(getId(b));
					}
				}catch(Throwable t){}
			}
		}
		return true;
	}

	@Override
	public boolean performPhysics(Block b) {
		return fall(b);
	}

}
