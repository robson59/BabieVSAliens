package com.example.babiesvsaliens;

import com.example.babiesvsaliens.GameActivity.Tipo_tiro;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class Tiro {
	
	Bitmap tiro;
	
	private int rodar = 0;
	private float x, y;
	private Tipo_tiro tipo;

	public float getX() {
		return x;
	}

	public void setX(float x){
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public Tipo_tiro getTipo() {
		return tipo;
	}

	public void setTipo(Tipo_tiro tipo) {
		this.tipo = tipo;
	}
	
	public Matrix atualizar_tiro(Bitmap tiro){
		
		Matrix tiro_matrix = new Matrix();
		
		if(this.tipo == Tipo_tiro.RETO){
			tiro_matrix.postRotate(rodar+=10,(tiro.getWidth()/2), ((tiro.getHeight()/2)) );
			tiro_matrix.postTranslate(x+=15, y);
		}else if(this.tipo == Tipo_tiro.CURVA_CIMA){
			tiro_matrix.postRotate(rodar+=30,(tiro.getWidth()/2), ((tiro.getHeight()/2)) );
			tiro_matrix.postTranslate(x+=15, y-=5);
		}else if(this.tipo == Tipo_tiro.CURVA_BAIXO){
			tiro_matrix.postRotate(rodar+=30,(tiro.getWidth()/2), ((tiro.getHeight()/2)) );
			tiro_matrix.postTranslate(x+=15, y+=5);
		}
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
