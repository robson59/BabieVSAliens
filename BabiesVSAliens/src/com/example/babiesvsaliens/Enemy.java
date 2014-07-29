package com.example.babiesvsaliens;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class Enemy {
	
	private float x,y;

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
	
	public Matrix atualizar_enemy(Bitmap enemy){
		
		Matrix enemy_matrix = new Matrix();
		
		//tiro_matrix.postRotate(rodar+=10,(tiro.getWidth()/2), ((tiro.getHeight()/2)) );
		enemy_matrix.setTranslate(x-=10, y);
		return enemy_matrix;
	}
	
	public boolean Collision(Tiro tiro,Bitmap enemy) {
    	
    	if( (tiro.getX() >= x) && (tiro.getX() <= x + enemy.getWidth())
    	&& (tiro.getY() >= y) && (tiro.getY() <= y + enemy.getHeight()))
    		return true;
    	else    		 	
    	    return false;
	}
	
public boolean Collision(Player player,Bitmap enemy,Bitmap player_img) {
    	
    	if( (player.getX() >= x - (player_img.getWidth()/2)) && (player.getX() <= x + enemy.getWidth())
    	&& (player.getY() >= y - (player_img.getHeight())) && (player.getY() <= y + enemy.getHeight()))
    		return true;
    	else    		 	
    	    return false;
	}
}
