package com.example.babiesvsaliens_v2;



import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnTouchListener {
	
	Jogo jogo;
	
	float largura, altura;
	float x , y;
	enum Sentido {CIMA, BAIXO, PARADO}
	Sentido sentido = Sentido.PARADO;
	Botao botao = new Botao();
	
	Bitmap botao_atirar;
	
	ArrayList<Tiro> tiros = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x = 50; y = 0;
		jogo = new Jogo(this);
		jogo.setOnTouchListener(this);
		
		setContentView(jogo);
		

	}

	@Override
	protected void onPause() {
		super.onPause();
		jogo.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		jogo.resume();
	}
	
	public class Jogo extends SurfaceView implements Runnable {
		
		Thread t = null;
		SurfaceHolder holder;
		
		float move;
		boolean play = false;
		
		Bitmap player;
		Bitmap background;
		Bitmap tiro;
		
		

		public Jogo(Context context) {
			super(context);
			
			holder = getHolder();
			player = BitmapFactory.decodeResource(getResources(), R.drawable.player_um);
			background = BitmapFactory.decodeResource(getResources(), R.drawable.back);
			tiro = BitmapFactory.decodeResource(getResources(), R.drawable.mamadeira);
			botao_atirar = BitmapFactory.decodeResource(getResources(), R.drawable.speed);
			
		}
		

		@Override
		public void run() {
			while(play){
				if(!holder.getSurface().isValid()){
					continue;
				}
				
				
				if(sentido == Sentido.BAIXO){
					if(y>(altura - player.getHeight())){
						y = y;
					}else{
						y += 15;
					}
				}else if(sentido == Sentido.CIMA){
					if(y<(0)){
						y = y;
					}else{
						y -= 15;
					}
				}else if (sentido == Sentido.PARADO){
					move = 0;
				}
				
				Matrix background_matrix = new Matrix();
				background_matrix.setTranslate(((largura/2)*-1), ((altura/2))*-1);
				
				Matrix player_matrix = new Matrix();
				
				

				player_matrix.setTranslate(x, y);
				
				
			    Canvas tela_jogo = holder.lockCanvas();
			    largura = tela_jogo.getWidth();
			    altura = tela_jogo.getHeight();
			    
			    //player_matrix.setRotate(100, x, y);
			    
			    botao.setX((largura - 80));
			    botao.setY((altura - 25));
			    
				tela_jogo.drawBitmap(background,background_matrix, null);
				tela_jogo.drawBitmap(player,player_matrix, null);
				
				if(!tiros.isEmpty()){
					for (int i = 0; i < tiros.size(); i++){
						tela_jogo.drawBitmap(tiro, tiros.get(i).atualizar_tiro(tiro), null);
						if(tiros.get(i).getX() > (largura)){
							tiros.remove(i);
						}
					}
				}	
				tela_jogo.drawBitmap(botao_atirar, botao.botao_matrix(botao_atirar), null);
				holder.unlockCanvasAndPost(tela_jogo);
			}
		}
		
		public void pause(){
			play = false;
			while (true) {
				
				try {
					t.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			}
			t = null;
		}
		
		public void resume(){
			play = true;
			t = new Thread(this);
			t.start();
		}

	}


	@Override
	public boolean onTouch(View v, MotionEvent e) {
		
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(e.getX() < (largura/2)){
				if(e.getY() < (altura/2) ){
					sentido = Sentido.CIMA;
				}else if(e.getY() > (altura/2) ){
					sentido = Sentido.BAIXO;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			sentido = Sentido.PARADO;
			break;
		case MotionEvent.ACTION_MOVE:
			if(e.getX() < (largura/2)){
				if(e.getY() < (altura/2) ){
					sentido = Sentido.CIMA;
				}else if(e.getY() > (altura/2) ){
					sentido = Sentido.BAIXO;
				}
			}
			break;	
		default:
			sentido = Sentido.PARADO;
			break;
		}
		
		
		
		if(botao.isTouch(e.getX(), e.getY(), botao_atirar)){
			if(e.getAction() == MotionEvent.ACTION_DOWN){
				Tiro tiro = new Tiro();
				tiro.setX(x);
				tiro.setY(y);
				tiros.add(tiro);
			}
		}
			
		return true;
	}

	

}
