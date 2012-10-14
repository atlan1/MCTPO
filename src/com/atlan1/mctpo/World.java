package com.atlan1.mctpo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.atlan1.mctpo.WGen.RectangleGenerator;
import com.atlan1.mctpo.WGen.MountainGenerator;
import com.atlan1.mctpo.WGen.OverlayGenerator;
import com.atlan1.mctpo.WGen.TreeGenerator;
import com.atlan1.mctpo.WGen.YSectorGenerator;

public class World {

	public static int worldW = 512, worldH = 256;
	private Character character;
	public Point spawnPoint = new Point();
	
	public static Block[][] blocks = new Block[worldW][worldH];
	
	public World(Character c){
		character = c;
		for(int x=0;x<blocks.length;x++){
			for(int y=0;y<blocks[0].length;y++){
				blocks[x][y] = new Block(new Rectangle(x * MCTPO.tileSize, y * MCTPO.tileSize, MCTPO.tileSize, MCTPO.tileSize), Material.AIR);
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
					character.y = y*MCTPO.tileSize - (MCTPO.tileSize+3);
					character.x = rW*MCTPO.tileSize;
					spawnPoint.y = (int) character.y;
					spawnPoint.x = (int) character.x;
					MCTPO.sY = y*MCTPO.tileSize - (MCTPO.pixel.height / 2) + (character.height / 2);
					MCTPO.sX = rW*MCTPO.tileSize - (MCTPO.pixel.width / 2) + (character.width / 2);
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
	
	public void render(Graphics g, int camX, int camY, int renW, int renH){
		for(int x=(camX/MCTPO.tileSize);x<(camX/MCTPO.tileSize) + renW;x++){
			for(int y=(camY/MCTPO.tileSize);y<(camY/MCTPO.tileSize) + renH;y++){
				if(x>=0 && y>=0 && x<worldW && y<worldH){
					blocks[x][y].render(g);
					if(blocks[x][y].contains(new Point(MCTPO.mouse.x + (int)MCTPO.sX, MCTPO.mouse.y + (int)MCTPO.sY))&&character.isBlockInBuildRange(blocks[x][y])){
						g.setColor(new Color(0, 0, 0));
						g.drawRect(blocks[x][y].x-camX, blocks[x][y].y-camY, blocks[x][y].width-1, blocks[x][y].height-1);
					}
				}
			}
		}
	}

	public void tick(int camX, int camY, int renW, int renH) {
		for(int x=(camX/MCTPO.tileSize);x<(camX/MCTPO.tileSize) + renW;x++){
			for(int y=(camY/MCTPO.tileSize);y<(camY/MCTPO.tileSize) + renH;y++){
				if(x>=0 && y>=0 && x<worldW && y<worldH)
					blocks[x][y].tick();
			}
		}
	}
	
}
