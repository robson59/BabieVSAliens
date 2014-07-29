package com.example.babiesvsaliens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MenuActivity extends Activity {
	
	Button start;
	Button ranking;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_menu);
		start = (Button) findViewById(R.id.jogar);
		ranking = (Button) findViewById(R.id.ranking);
		
		start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v){
				Intent i = new Intent(MenuActivity.this,GameActivity.class);
				startActivity(i);
				finish();
			}
		});
		
		ranking.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v){
				Intent i = new Intent(MenuActivity.this,RankingActivity.class);
				startActivity(i);
			}
		});

	}
}
