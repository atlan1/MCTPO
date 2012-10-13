package com.atlan1.mctpo.Texture;

import com.atlan1.mctpo.MCTPO;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class TextureLoader {

	public static SpriteImage loadSpriteImage(String path) {
		try {
			return new SpriteImage(ImageIO.read(new File(path)), MCTPO.tileSize);
		} catch (Exception e) {e.printStackTrace();}
		return null;
	}
	
	public static BufferedImage loadImage(String path) {
		try{
			return ImageIO.read(new File(path));
		}catch(Exception e){e.printStackTrace();}
		return null;
	}
}
