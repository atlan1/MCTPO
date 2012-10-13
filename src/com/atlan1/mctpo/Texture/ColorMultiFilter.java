package com.atlan1.mctpo.Texture;

import java.awt.Color;
import java.awt.image.RGBImageFilter;

public class ColorMultiFilter extends RGBImageFilter {
	private double redMultiplier, greenMultiplier, blueMultiplier;

	  private int newRed, newGreen, newBlue;

	  private Color color, newColor;

	  public ColorMultiFilter(double rm, double gm, double bm) {
	    canFilterIndexColorModel = true;
	    redMultiplier = rm;
	    greenMultiplier = gm;
	    blueMultiplier = bm;
	  }

	  private int multColor(int colorComponent, double multiplier) {
	    colorComponent = (int) (colorComponent * multiplier);
	    if (colorComponent < 0)
	      colorComponent = 0;
	    else if (colorComponent > 255)
	      colorComponent = 255;

	    return colorComponent;
	  }

	  public int filterRGB(int x, int y, int argb) {
	    color = new Color(argb);
	    newBlue = multColor(color.getBlue(), blueMultiplier);
	    newGreen = multColor(color.getGreen(), greenMultiplier);
	    newRed = multColor(color.getRed(), redMultiplier);
	    newColor = new Color(newRed, newGreen, newBlue);
	    return (newColor.getRGB());
	  }
}
