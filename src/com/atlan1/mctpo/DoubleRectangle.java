package com.atlan1.mctpo;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D.Double;

public class DoubleRectangle {

	protected double width;
	protected double height;
	protected double x;
	protected double y;
	
	public DoubleRectangle() {
		this(0, 0, 0, 0);
	}
	
	public DoubleRectangle(double width, double height, double x, double y) {
		setBounds(width, height, x, y);
	}
	
	public void setBounds(double width, double height, double x, double y){
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
	}
	
	public void setBounds(double width, double height){
		this.width = width;
		this.height = height;
		this.x = 0;
		this.y = 0;
	}
	

	public void setBounds(Double l) {
		this.width = l.width;
		this.height = l.height;
		this.x = l.x;
		this.y = l.y;
	}
	
	public boolean intersects(DoubleRectangle rect){
		return intersects(rect.x, rect.y, rect.width,rect.height);
	}
	
	  public boolean intersects(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4){
	    if ((isEmpty()) || (paramDouble3 <= 0.0D) || (paramDouble4 <= 0.0D))
	      return false;
	    double d1 = x;
	    double d2 = y;
	    return (paramDouble1 + paramDouble3 > d1) && (paramDouble2 + paramDouble4 > d2) && (paramDouble1 < d1 + width) && (paramDouble2 < d2 + height);
	  }
	 
    public boolean isEmpty() {
      return (this.width <= 0.0D) || (this.height <= 0.0D);
    }
	
	public boolean intersects(Rectangle rect)
	  {
		return intersects(rect.x, rect.y, rect.width,rect.height);
		
//	    int i = (int)this.width;
//	    int j = (int)this.height;
//	    int k = rect.width;
//	    int m = rect.height;
//	    if ((k <= 0) || (m <= 0) || (i <= 0) || (j <= 0))
//	      return false;
//	    int n = (int)this.x;
//	    int i1 = (int)this.y;
//	    int i2 = rect.x;
//	    int i3 = rect.y;
//	    k += i2;
//	    m += i3;
//	    i += n;
//	    j += i1;
//	    return ((k < i2) || (k > n)) && ((m < i3) || (m > i1)) && ((i < n) || (i > i2)) && ((j < i1) || (j > i3));
	  }
}
