package com.atlan1.mctpo;

import java.awt.Color;
import java.awt.Graphics;

public class Sky {

	public long dayTime=System.currentTimeMillis(), dayLength=120000;
	private int dayR=60, dayG=110, dayB=255;
	private int nightR=50, nightG=40, nightB=145;
	private int nowR, nowG, nowB;
	private boolean isDay = true;
	
	public Sky(){
		nowR=dayR; nowG=dayG; nowB=dayB;
	}
	
	public void tick(long d){
		if(MCTPO.thisTime - dayTime>dayLength){
			if(isDay){
				isDay=false;
			}else{
				isDay=true;
			}
			dayTime=0;
		}
		if(isDay){
			if(!(nowR==nightR&&nowG==nightG&&nowB==nightB)){
				if(nowR<nightR)
					nowR++;
				else
					nowR--;
				if(nowG<nightG)
					nowG++;
				else
					nowG--;
				if(nowB<nightB)
					nowB++;
				else
					nowB--;
			}
		}else{
			if(!(nowR==dayR&&nowG==dayG&&nowB==dayB)){
				if(nowR<dayR)
					nowR++;
				else
					nowR--;
				if(nowG<dayG)
					nowG++;
				else
					nowG--;
				if(nowB<dayB)
					nowB++;
				else
					nowB--;
			}
		}
	}
	
	public void render(Graphics  g){
		g.setColor(new Color(nowR, nowG, nowB));
		g.fillRect(0, 0, MCTPO.pixel.width, MCTPO.pixel.height);
	}
}
