package com.atlan1.mctpo.Physics.BlockPhysics;

import com.atlan1.mctpo.Block;
import com.atlan1.mctpo.Material;
import com.atlan1.mctpo.World;

public class FlowPhysics extends AbstractBlockPhysics {

	private int tick = 0; //time between physic updates
	private boolean infinite = false; //infinite fluid source from one block
	
	public FlowPhysics(int tick, boolean inf){
		this.tick = tick;
		this.infinite = inf;
	}
	
	//Calculates fluids movement
	public Boolean flow(Block b){
		if(b!=null){
			if(b.framesSinceUpdate>=tick){
				Material m = b.material;
				//infinite fluid
				if(infinite){
					//Flow down
					try{
						if(World.blocks[b.getGridX()][b.getGridY()+1].material.nonSolid && World.blocks[b.getGridX()][b.getGridY()+1].material != m){
							World.blocks[b.getGridX()][b.getGridY()+1].material = m;
							World.blocks[b.getGridX()][b.getGridY()+1].update();
							return true;
						}
					}catch(Throwable t){}
					//Flow right/left
					try{
						if(World.blocks[b.getGridX()][b.getGridY()+1].material!=m){
							try{
								if(World.blocks[b.getGridX()-1][b.getGridY()].material.nonSolid && World.blocks[b.getGridX()-1][b.getGridY()].material != m){
									World.blocks[b.getGridX()-1][b.getGridY()].material = m;
									World.blocks[b.getGridX()-1][b.getGridY()].update();
									return true;
								}
							}catch(Throwable t){}
							try{
								if(World.blocks[b.getGridX()+1][b.getGridY()].material.nonSolid && World.blocks[b.getGridX()+1][b.getGridY()].material != m){
									World.blocks[b.getGridX()+1][b.getGridY()].material = m;
									World.blocks[b.getGridX()+1][b.getGridY()].update();
									return true;
								}
							}catch(Throwable t){}
						}
					}catch(Throwable t){}
				//finite fluid
				}else{
					try{
						if(World.blocks[b.getGridX()][b.getGridY()+1].material.nonSolid && World.blocks[b.getGridX()][b.getGridY()+1].material != m){
							World.blocks[b.getGridX()][b.getGridY()+1].material = m;
							b.material = Material.AIR;
							World.blocks[b.getGridX()][b.getGridY()+1].update();
							return true;
						}
					}catch(Throwable t){}
					//Check if the block next to and one down is nonSolid
					try{
						if(World.blocks[b.getGridX()][b.getGridY()+1].material!=m){
							try{
								if((World.blocks[b.getGridX()-1][b.getGridY()].material.nonSolid &&World.blocks[b.getGridX()-1][b.getGridY()].material != m)&&(World.blocks[b.getGridX()-1][b.getGridY()+1].material.nonSolid && World.blocks[b.getGridX()-1][b.getGridY()+1].material != m)){
									World.blocks[b.getGridX()-1][b.getGridY()].material = m;
									b.material = Material.AIR;
									World.blocks[b.getGridX()-1][b.getGridY()].update();
									return true;
								}
							}catch(Throwable t){}
							try{
								if((World.blocks[b.getGridX()+1][b.getGridY()].material.nonSolid &&World.blocks[b.getGridX()+1][b.getGridY()].material != m)&&(World.blocks[b.getGridX()+1][b.getGridY()+1].material.nonSolid && World.blocks[b.getGridX()+1][b.getGridY()+1].material != m)){
									World.blocks[b.getGridX()+1][b.getGridY()].material = m;
									b.material = Material.AIR;
									World.blocks[b.getGridX()+1][b.getGridY()].update();
									return true;
								}
							}catch(Throwable t){}
						}
					}catch(Throwable t){}
				}
			}
		}
		return false;
	}

	@Override
	public boolean performPhysics(Block b) {
		return flow(b);
	}

}
