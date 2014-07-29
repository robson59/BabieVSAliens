package com.example.babiesvsaliens;


import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class GameActivity extends Activity implements OnTouchListener {
	
	Jogo jogo;
	
	float largura, altura;
	int cont_tiros = 0;
	enum Sentido {CIMA, BAIXO, PARADO}
	enum Tipo_tiro {CURVA_CIMA, CURVA_BAIXO, RETO}
	Sentido sentido = Sentido.PARADO;
	Botao botao_reto = new Botao();
	Botao botao_cima = new Botao();
	Botao botao_baixo = new Botao();
	Player player = new Player();
		
	Bitmap botao_atirar_reto;
	Bitmap botao_atirar_curvo_cima;
	Bitmap botao_atirar_curvo_baixo;
	Bitmap background;
	Bitmap tiro;
	Bitmap enemy;
	Bitmap lives_3;
	Bitmap lives_2;
	Bitmap lives_1;	
	Bitmap player_img;
	
	ArrayList<Tiro> tiros = new ArrayList<Tiro>();
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
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
		
		boolean play = false;
		int Contador = 100;
		int pontos = 0;
		int lives = 3;
		
		Random gerador = new Random();
		Paint paint = new Paint();
		
		public Jogo(Context context) {
			super(context);
			
			holder = getHolder();
			player_img = BitmapFactory.decodeResource(getResources(), R.drawable.player_um);
			background = BitmapFactory.decodeResource(getResources(), R.drawable.back);
			tiro = BitmapFactory.decodeResource(getResources(), R.drawable.mamadeira);
			botao_atirar_reto = BitmapFactory.decodeResource(getResources(), R.drawable.tiro_reto);
			botao_atirar_curvo_cima = BitmapFactory.decodeResource(getResources(), R.drawable.tiro_curvo_cima);
			botao_atirar_curvo_baixo = BitmapFactory.decodeResource(getResources(), R.drawable.tiro_curvo_baixo);
			enemy = BitmapFactory.decodeResource(getResources(), R.drawable.enemy);
			lives_3 = BitmapFactory.decodeResource(getResources(), R.drawable.lives_3);
			lives_2 = BitmapFactory.decodeResource(getResources(), R.drawable.lives_2);
			lives_1 = BitmapFactory.decodeResource(getResources(), R.drawable.lives_1);
			paint.setColor(Color.WHITE);
		    paint.setTextSize(30);
		    player.setX(75); player.setY((altura/2));

		}
		

		@Override
		public void run() {
			while(play){
				if(!holder.getSurface().isValid()){
					continue;
				}
				
				if(sentido == Sentido.BAIXO){
					if(player.getY()>(altura - player_img.getHeight())){
						//Nada a Fazer
					}else{
						player.setY(player.getY()+15);
					}
				}else if(sentido == Sentido.CIMA){
					if(player.getY()<(0)){
						//Nada a Fazer
					}else{
						player.setY(player.getY()-15);
					}
				}else if (sentido == Sentido.PARADO){
					//Nada a Fazer
				}
				
				Matrix background_matrix = new Matrix();
				background_matrix.setTranslate(((largura/2)*-1), ((altura/2))*-1);
				
				
			    Canvas tela_jogo = holder.lockCanvas();
			    largura = tela_jogo.getWidth();
			    altura = tela_jogo.getHeight();
			    
			    
			    botao_reto.setX((largura - 80));
			    botao_reto.setY((altura - 25));
			    
			    botao_cima.setX((largura - 80)-(botao_atirar_reto.getWidth()));
			    botao_cima.setY((altura - 25));
			    
			    botao_baixo.setX((largura - 80)-(botao_atirar_reto.getWidth()*2));
			    botao_baixo.setY((altura - 25));
			    
				tela_jogo.drawBitmap(background,background_matrix, null);
				tela_jogo.drawBitmap(player_img,player.atualizar_player(player_img), null);
				
				
				if(Contador == 0){
					Enemy enemy_obj = new Enemy();
					enemy_obj.setX(largura);
					enemy_obj.setY(gerador.nextInt((int) (altura-enemy.getWidth())));
					enemies.add(enemy_obj);
					Contador = 20;
				}else{
					Contador-=1;
				}
				
				if(!enemies.isEmpty()){
					for (int i = 0; i < enemies.size(); i++) {
						tela_jogo.drawBitmap(enemy, enemies.get(i).atualizar_enemy(enemy), null);
						if(enemies.get(i).getX() < 0){
							enemies.remove(i);
							break;
						}
						if(enemies.get(i).Collision(player, enemy,player_img)){
							enemies.remove(i);
							lives-=1;
							break;
						}
						if(!tiros.isEmpty()){
							for (int y = 0; y < tiros.size(); y++){
								if(enemies.get(i).Collision(tiros.get(y), enemy)){
									pontos+=(enemies.get(i).getX()*0.2);
									tiros.remove(y);
									enemies.remove(i);
									cont_tiros -= 1;
									break;
								}
							}
						}	
					}
				}
				
				
				if(lives == 3){
					tela_jogo.drawBitmap(lives_3, (largura - 350), 25, paint);
				}else if(lives == 2){
					tela_jogo.drawBitmap(lives_2, (largura - 350), 25, paint);
				}else if(lives == 1){
					tela_jogo.drawBitmap(lives_1, (largura - 350), 25, paint);
				}else if(lives == 0){
					Intent intent = new Intent(GameActivity.this,FimActivity.class);
					intent.putExtra("pontos", pontos);
					startActivity(intent);
					finish();
				}
				
				
				if(!tiros.isEmpty()){
					for (int i = 0; i < tiros.size(); i++){
						tela_jogo.drawBitmap(tiro, tiros.get(i).atualizar_tiro(tiro), null);
						if(tiros.get(i).getX() > (largura)){
							tiros.remove(i);
							cont_tiros -= 1;
						}
					}
				}
				tela_jogo.drawText("Ponto: "+pontos, (largura - 250), 25, paint);
				tela_jogo.drawBitmap(botao_atirar_reto, botao_reto.botao_matrix(botao_atirar_reto), null);
				tela_jogo.drawBitmap(botao_atirar_curvo_cima, botao_cima.botao_matrix(botao_atirar_curvo_cima), null);
				tela_jogo.drawBitmap(botao_atirar_curvo_baixo, botao_baixo.botao_matrix(botao_atirar_curvo_baixo), null);
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
		
		
		
		if(botao_reto.isTouch(e.getX(), e.getY(), botao_atirar_reto)){
			if(e.getAction() == MotionEvent.ACTION_DOWN){
				if( cont_tiros < 3){
					Tiro tiro = new Tiro();
					tiro.setTipo(Tipo_tiro.RETO);
					tiro.setX(player.getX()+(player_img.getWidth()/2));
					tiro.setY(player.getY()+(player_img.getHeight()/2));
					tiros.add(tiro);
					cont_tiros +=1;
				}	
			}
		}
		
		if(botao_baixo.isTouch(e.getX(), e.getY(), botao_atirar_curvo_baixo)){
			if(e.getAction() == MotionEvent.ACTION_DOWN){
				if( cont_tiros < 3){
					Tiro tiro = new Tiro();
					tiro.setTipo(Tipo_tiro.CURVA_BAIXO);
					tiro.setX(player.getX()+(player_img.getWidth()/2));
					tiro.setY(player.getY()+(player_img.getHeight()/2));
					tiros.add(tiro);
					cont_tiros +=1;
				}	
			}
		}
		
		if(botao_cima.isTouch(e.getX(), e.getY(), botao_atirar_curvo_cima)){
			if(e.getAction() == MotionEvent.ACTION_DOWN){
				if( cont_tiros < 3){
					Tiro tiro = new Tiro();
					tiro.setTipo(Tipo_tiro.CURVA_CIMA);
					tiro.setX(player.getX()+(player_img.getWidth()/2));
					tiro.setY(player.getY()+(player_img.getHeight()/2));
					tiros.add(tiro);
					cont_tiros +=1;
				}
			}
		}
			
		return true;
	}
}
