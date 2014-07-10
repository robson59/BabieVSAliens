package gameutil2d.project;

import java.util.ArrayList;

import gameutil2d.framework.*;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import gameutil2d.classes.basic.*;
import gameutil2d.classes.scene.*;
import gameutil2d.classes.character.*;
import gameutil2d.classes.text.*;

 

public class GameMain {
	
	int altura;
	int largura;
	
	Image p1, back, botao, tiro;
	enum Sentido {CIMA, BAIXO, PARADO}
	Sentido sentido = Sentido.PARADO;
	
	Context context;
	Canvas canvas;
	
	ArrayList<Image> tiros; 
	
	
	public GameMain(Context context){
		
		this.context = context;
		
	}
	
	public void onScreenLoading(int w, int h, OrientationMode orientation)
	{
		/* M�todo que � executado logo ap�s o construtor da classe GameMain
		 * Aqui carregamos todos os elementos (imagens, anima��es, personagens) que dependam
		 * das informa��es de largura (w) e altura (h) da tela do jogo, incluindo tamb�m sua
		 * orienta��o (orientation)  
		 */
		altura = w;
		largura = h;
		
		p1 = new Image(context, R.drawable.p_dois, (h/8), (w/4), 100, 100);
		botao = new Image(context, R.drawable.s_um, h, (w/2), 100, 100);
		
		
		back = new Image(context, R.drawable.back,0,0,w,h);
	}
	
	public void Update(){
		
		//M�todo respons�vel pela execu��o da l�gica do jogo (movimento, a��o, colis�es e etc.)
		if(sentido == Sentido.CIMA){
			
			p1.MoveByY(15);
				if((p1.GetY() + p1.GetHeight()) > largura){
					p1.SetY(largura - p1.GetHeight());
				}	
		}
		else if(sentido == Sentido.BAIXO){
			
			p1.MoveByY(-15);
				if(p1.GetY() < 0){
					p1.SetY(0);
				}	
		}
		
	}
	
	public void Draw(Canvas canvas)
	{
		this.canvas = canvas;
		
		back.Draw(canvas);
		p1.Draw(canvas, FlipEffect.HORIZONTAL);
		botao.Draw(canvas);
		
	}
	
	public void onTouch(MotionEvent e){
		
		//M�todo executado quando ocorre algum evento de toque sobre a superficie da tela
		
		if(e.getAction() == MotionEvent.ACTION_DOWN){
			if(e.getX() < (altura/2)){	
				if(e.getY() > (largura/2)){
					sentido = Sentido.CIMA;
				}else if(e.getY() < (largura/2)){
					sentido = Sentido.BAIXO;
				}
			}	
		}else if(e.getAction() == MotionEvent.ACTION_UP){
			sentido = Sentido.PARADO;
		}
		
		if(botao.IsTouch(e.getX(), e.getY())){
				tiro = new Image(context, R.drawable.s_dois, (p1.GetX() + (p1.GetWidth() -10)), (p1.GetY()+(p1.GetHeight()/3)), 30, 30);
				tiro.Draw(canvas);	
		}
		
	}
	
	public void onExitGame(){
		
		/*M�todo que � executado quando o jogo � encerrado
		  Adequado para a finaliza��o de threads e m�sicas de fundo do jogo*/
		
		//Insira seu c�digo aqui
		
	}
	

}
