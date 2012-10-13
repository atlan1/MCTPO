package com.atlan1.mctpo.Physics.BlockPhysics;

import com.atlan1.mctpo.Block;
import com.atlan1.mctpo.LivingThing;
import com.atlan1.mctpo.Thing;

public class DamageCollision extends AbstractBlockPhysics{

	private int damage;
	private int tick=0;
	
	public DamageCollision(int tick, int damage) {
		this.damage = damage;
		this.tick = tick;
	}

	public boolean damage(Block b, Thing t){
		if(t instanceof LivingThing&&b.framesSinceUpdate<=tick)
			((LivingThing)t).setHealth(((LivingThing)t).getHealth() - damage);
		return true;
	}
	
	public boolean performCollision(Block b, Thing t){
		return damage(b, t);
	}
}
