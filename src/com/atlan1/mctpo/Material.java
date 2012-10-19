package com.atlan1.mctpo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.atlan1.mctpo.API.Thing;
import com.atlan1.mctpo.Physics.BlockPhysics.BlockPhysics;
import com.atlan1.mctpo.Physics.BlockPhysics.FirePhysics;
import com.atlan1.mctpo.Physics.BlockPhysics.LavaPhysics;
import com.atlan1.mctpo.Physics.BlockPhysics.LeavesPhysics;
import com.atlan1.mctpo.Physics.BlockPhysics.SandPhysics;
import com.atlan1.mctpo.Physics.BlockPhysics.WaterPhysics;
import com.atlan1.mctpo.Texture.SpriteImage;
import com.atlan1.mctpo.Texture.TextureLoader;

public enum Material {
	MASK(-2, true, -1),
	AIR(-1, true, -1),
	DIRT(0, false, 20), //20
	GRASS(1, false, 20),
	STONE(2, false, 35),//35
	SAND(3, false, 20),
	WOOD(4, false, 25),
	LEAVES(5, false, 10),
	FIRE(19, true, 1),
	WATER(20, true, -1),
	LAVA(21, true, -1);
	
	public static SpriteImage terrain;
	static{
		//Load sprite image of terrain
		terrain = TextureLoader.loadSpriteImage("/res/terrain.png");
		
		//Apply physics to materials
		try {
			addPhysics(Material.SAND, SandPhysics.class.newInstance());
			addPhysics(Material.LEAVES, LeavesPhysics.class.newInstance());
			addPhysics(Material.FIRE, FirePhysics.class.newInstance());
			addPhysics(Material.WATER, WaterPhysics.class.newInstance());
			addPhysics(Material.LAVA, LavaPhysics.class.newInstance());
		} catch (InstantiationException e) {e.printStackTrace();} catch (IllegalAccessException e) {e.printStackTrace();}
	}
	
	public final int id;
	public final boolean nonSolid;
	public final int hardness;
	public List<BlockPhysics> physics = new ArrayList<BlockPhysics>();
	
	private Material(int id, boolean bNonSolid, int hardness){
		this.id = id;
		this.nonSolid = bNonSolid;
		this.hardness = hardness;
	}
	
	private static void addPhysics(Material m, BlockPhysics... ph){
		m.physics.addAll(Arrays.asList(ph));
	}
	
	public static Material getById(int id){
		for(Material m : values()){
			if(m.id == id) return m;
		}
		return null;
	}
	
	public void doPhysics(Block b){
		for(BlockPhysics p : physics){
			p.performPhysics(b);
		}
	}
	
	public void collide(Block b, Thing ent){
		for(BlockPhysics p : physics){
			p.performCollision(b, ent);
		}
	}
}
