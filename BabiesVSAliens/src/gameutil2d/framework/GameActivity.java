package gameutil2d.framework;
/*
 *  [GameActivity.java] - Classe (Activity) principal, que irá executar todo o motor de jogo
 *  da GameUtil2D
 *  
 *  Desenvolvida por : Luciano Alves da Silva
 *  e-mail : lucianopascal@yahoo.com.br
 *  site : http://www.gameutil2d.org
 */




import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends Activity {
	
	GameRunnable gameRunnable;
	
	GameState gameState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        gameRunnable = new GameRunnable(this);
        
        gameState = GameState.PLAY_RESUME;
        
		setContentView(gameRunnable);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.activity_game, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
   	 
	    case R.id.menu_pause_resume_game: { 
	    	
	    	if(gameState == GameState.PAUSE) {
	    		gameRunnable.resumeGame();
	    		gameState = GameState.PLAY_RESUME;	    			    		
  	    	    item.setTitle("Pausar Jogo");
	    	}
	    	else {
	    		
	    	  gameRunnable.pauseGame();
	    	  gameState = GameState.PAUSE;
	    		
	   		  item.setTitle("Continuar Jogo");
	    		
	    	}
	    	
	    } break;
	    
	    
	    case R.id.menu_exit_game : {
	    	
	    	AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
	    	
	    	dialogo.setTitle("Aviso");	
	    	dialogo.setMessage("Deseja sair do Jogo ?");
	    	dialogo.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
				
			public void onClick(DialogInterface dialog, int which) {
					  GameActivity.this.finish();	
				}
			} );
	    	dialogo.setNegativeButton("Não", null);
	    	
	    	dialogo.show();
	    	
	    } break;
	
	}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onPause() {		
	    super.onPause();
	    gameRunnable.pauseGame();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	    gameRunnable.resumeGame();
	}
	
	@Override
	protected void onDestroy() {
	   super.onDestroy();
	   gameRunnable.onExitGame();
	 
	}

}
