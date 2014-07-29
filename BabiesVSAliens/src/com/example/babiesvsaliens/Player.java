package com.example.babiesvsaliens;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class Player {
	
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
	
	public Matrix atualizar_player(Bitmap player){
		
		Matrix player_matrix = new Matrix();
		player_matrix.setTranslate(x, y);
		
		return player_matrix;
	}


}
