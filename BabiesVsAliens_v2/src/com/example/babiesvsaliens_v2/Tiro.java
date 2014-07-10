package com.example.babiesvsaliens_v2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class Tiro {
	
	Bitmap tiro;
	
	private float x, y;

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
	
	public Matrix atualizar_tiro(){
		
		Matrix tiro_matrix = new Matrix();
		tiro_matrix.setTranslate( x +=15, y);

		return tiro_matrix;
	}
	
	/*public boolean Collision() {
    	
    	if( (posx >= x) && (posx <= x + width)
    	&& (posy >= y) && (posy <= y + height))
    		return true;
    	else    		 	
    	    return false;
    }*/


}
