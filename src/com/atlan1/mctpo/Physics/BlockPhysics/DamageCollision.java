package com.atlan1.mctpo.Physics.BlockPhysics;

import com.atlan1.mctpo.Block;
import com.atlan1.mctpo.MCTPO;
import com.atlan1.mctpo.API.LivingThing;
import com.atlan1.mctpo.API.Thing;

public class DamageCollision extends AbstractBlockPhysics{

	private int damage;
	private int tick=0;
	
	public DamageCollision(int tick, int damage) {
		this.damage = damage;
		this.tick = tick;
	}

	public boolean damage(Block b, Thing t){
		addId(b);
		if(t instanceof LivingThing&&MCTPO.thisTime - b.timeOfUpdate.get(getId(b))<=tick){
			((LivingThing)t).setHealth(((LivingThing)t).getHealth() - damage);
			b.update(getId(b));
		}
		return true;
	}
	
	public boolean performCollision(Block b, Thing t){
		return damage(b, t);
	}
}
