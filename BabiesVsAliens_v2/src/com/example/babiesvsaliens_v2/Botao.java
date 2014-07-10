package com.example.babiesvsaliens_v2;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class Botao {
	
	float x, y;
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public Matrix botao_matrix(Bitmap botao){
		
		Matrix matrix = new Matrix();
		matrix.setTranslate((x - botao.getWidth()), (y - botao.getHeight()));

		return matrix;
	}
	
	public boolean isTouch(float posx, float posy, Bitmap botao) {
		
    	if( (posx >= (x - botao.getWidth())) && (posx <= (x - botao.getWidth()) + botao.getWidth())
    	&& (posy >= (y - botao.getHeight())) && (posy <= (y - botao.getHeight()) + botao.getHeight()))
    		return true;
    	else    		 	
    	    return false;
	}

}
