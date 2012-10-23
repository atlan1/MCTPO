package com.atlan1.mctpo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.atlan1.mctpo.API.Thing;
import com.atlan1.mctpo.WGen.RectangleGenerator;
import com.atlan1.mctpo.WGen.MountainGenerator;
import com.atlan1.mctpo.WGen.OverlayGenerator;
import com.atlan1.mctpo.WGen.TreeGenerator;
import com.atlan1.mctpo.WGen.YSectorGenerator;

public class World {

	public static int worldW = 512, worldH = 256;
	private Character character;
	public Point spawnPoint = new Point();
	
	public static List<Thing> allthethingsinhere = new ArrayList<Thing>();
	public static Block[][] blocks = new Block[worldW][worldH];
	
	public World(Character c){
		character = c;
		allthethingsinhere.add(character);
		for(int x=0;x<blocks.length;x++){
			for(int y=0;y<blocks[0].length;y++){
				blocks[x][y] = new Block(new Rectangle(x * MCTPO.blockSize, y * MCTPO.blockSize, MCTPO.blockSize, MCTPO.blockSize), Material.AIR);
				allthethingsinhere.add(blocks[x][y]);
			}
		}
		generateLevel();
		setPlayerSpawn();
	}
	
	private void setPlayerSpawn(){
		Random r = new Random();
		int rW  = r.nextInt(worldW-1);
		for(int y = 0; y<blocks[0].length;y++){
			try{
				if(blocks[rW][y].material==Material.AIR&&blocks[rW][y-1].material==Material.AIR&&!(blocks[rW][y+1].material==Material.AIR)){
					character.y = y*MCTPO.blockSize - (MCTPO.blockSize+3);
					character.x = rW*MCTPO.blockSize;
					spawnPoint.y = (int) character.y;
					spawnPoint.x = (int) character.x;
					MCTPO.camY = y*MCTPO.blockSize - (MCTPO.pixel.height / 2) + (character.height / 2);
					MCTPO.camX = rW*MCTPO.blockSize - (MCTPO.pixel.width / 2) + (character.width / 2);
				}
			}catch(Throwable t){}
		}
	}

	private void generateLevel(){
		Map<Integer, Integer> id_probs = new HashMap<Integer, Integer>();
		id_probs.put(0, 40);
		id_probs.put(2, 60);
		Map<Integer, Integer> id_probs2 = new HashMap<Integer, Integer>();
		id_probs2.put(1, 60);
		id_probs2.put(0, 20);
		id_probs2.put(3, 20);
		Map<Integer, Integer> id_probs3 = new HashMap<Integer, Integer>();
		id_probs3.put(21, 5);
		id_probs3.put(20, 95);
		Map<Integer, Integer> id_probs4 = new HashMap<Integer, Integer>();
		id_probs4.put(21, 95);
		id_probs4.put(20, 5);
		blocks = new TreeGenerator(3, 6, 30, 45).generate(new OverlayGenerator(id_probs2).generate(new RectangleGenerator(worldH/4*3, worldH, 5, 80, 5, 10, 2, 5, false, id_probs4).generate(new RectangleGenerator(worldH/3*2, worldH/4*3, 3, 70, 5, 7, 2, 3, false, id_probs3).generate(new YSectorGenerator(id_probs, 0, worldH, true).generate(new MountainGenerator(worldH/2, worldH).generate(blocks))))));
	}
	
	public void render(Graphics g){
		for(int x=(int) (MCTPO.camX/MCTPO.blockSize);x<(MCTPO.camX/MCTPO.blockSize) + MCTPO.pixel.width/MCTPO.blockSize +2;x++){
			for(int y=(int) (MCTPO.camY/MCTPO.blockSize);y<(MCTPO.camY/MCTPO.blockSize) + MCTPO.pixel.height/MCTPO.blockSize +2;y++){
				if(x>=0 && y>=0 && x<worldW && y<worldH){
					blocks[x][y].render(g);
					if(!character.inv.isOpen()&&blocks[x][y].contains(new Point(MCTPO.mouse.x + (int)MCTPO.camX, MCTPO.mouse.y + (int)MCTPO.camY))&&character.isBlockInBuildRange(blocks[x][y])){
						g.setColor(new Color(230, 230, 230, 60));
						g.fillRect(blocks[x][y].x-(int)MCTPO.camX, blocks[x][y].y-(int)MCTPO.camY, blocks[x][y].width, blocks[x][y].height);
					}
				}
			}
		}
	}

	public void tick(long d) {
		for(int x=(int) (MCTPO.camX/MCTPO.blockSize);x<(MCTPO.camX/MCTPO.blockSize) + MCTPO.pixel.width/MCTPO.blockSize +8;x++){
			for(int y=(int) (MCTPO.camY/MCTPO.blockSize);y<(MCTPO.camY/MCTPO.blockSize)  + MCTPO.pixel.height/MCTPO.blockSize +8;y++){
				if(x>=0 && y>=0 && x<worldW && y<worldH)
					blocks[x][y].tick(d);
			}
		}
	}
	
	public static List<Thing> getNearbyThings(int x, int y, int radius){
		List<Thing> things = new ArrayList<Thing>();
		for(Thing t : allthethingsinhere){
			if(Math.floor(t.getBounds().getX())>=x-radius*MCTPO.blockSize&&Math.floor(t.getBounds().getX())<=x+radius*MCTPO.blockSize)
				if(Math.floor(t.getBounds().getY())>=y-radius*MCTPO.blockSize&&Math.floor(t.getBounds().getY())<=y+radius*MCTPO.blockSize)
					things.add(t);
		}
		return things;
	}
}
