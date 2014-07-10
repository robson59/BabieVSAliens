package gameutil2d.framework;

/*
 *  [GameRunnable.java] - Classe intermediária, responsável pela execução da instância
 *  da classe GameMain (que é a classe onde escrevemos o código do nosso jogo) 
 *  Ela processa todos os eventos como  Update, Draw, Touch e etc.
 *  
 *  
 *  Desenvolvida por : Luciano Alves da Silva
 *  e-mail : lucianopascal@yahoo.com.br
 *  site : http://www.gameutil2d.org
 */



import gameutil2d.project.*;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameRunnable extends SurfaceView implements Runnable {
	
	GameState gameState;
	
	GameMain gameMain;
	
	Thread thread = null;
	SurfaceHolder surfaceHolder;
    volatile boolean running = false;
    Context context;

	public GameRunnable(Context context) {
		super(context);
		
		gameMain = new GameMain(context);
		this.context = context;
		surfaceHolder = getHolder();
			
		setFocusable(true);
	}
	
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	
		super.onSizeChanged(w, h, oldw, oldh);
		
		OrientationMode om = (h > w) ? OrientationMode.PORTRAIT : OrientationMode.LANDSCAPE;
		
		gameMain.onScreenLoading(w, h, om);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Update();
		canvas.drawColor(Color.BLACK);
		
		gameMain.Draw(canvas);
	}
	
	public void Update()
	{
		gameMain.Update();
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    
		gameMain.onTouch(event);
		
		return true;
	}

	@Override
	public void run() {
		
		while(running){
			
		    if(surfaceHolder.getSurface().isValid()){
		     Canvas canvas = surfaceHolder.lockCanvas();
		    
		     
		     onDraw(canvas);
		      
		     surfaceHolder.unlockCanvasAndPost(canvas);
	    }
	  }
	}
	
	
	public void onExitGame()
	{
		gameMain.onExitGame();
	}
	
	
	public void resumeGame()
	{
		gameState = GameState.PLAY_RESUME;
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public void pauseGame()
	{
        gameState = GameState.PAUSE;
		
		boolean retry = true;
	    running = false;
		while(retry){
		  try {
		   thread.join(); 
		    retry = false;
		  } catch (InterruptedException e) {
		 	     e.printStackTrace();
		    }
		}
		
	}

}
