package com.example.babiesvsaliens;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class FimActivity extends Activity {
	
	Button restart;
	Button menu;
	Button submit;
	TextView pontuacao;
	EditText edit_nick;
	int pontos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_fim);
		restart = (Button) findViewById(R.id.restart_button);
		menu = (Button) findViewById(R.id.menu_button);
		submit = (Button) findViewById(R.id.submit_button);
		pontuacao = (TextView) findViewById(R.id.pontos);
		edit_nick = (EditText) findViewById(R.id.nick);
		
		Intent intent = getIntent();
		pontos = intent.getExtras().getInt("pontos");
		
		pontuacao.setText("Pontos:"+ pontos);
		
		menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(FimActivity.this,MenuActivity.class);
				startActivity(i);
				finish();
			}
		});
		
		
		restart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(FimActivity.this,GameActivity.class);
				startActivity(i);
				finish();
			}
		});
		
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isConnected()){
					if(edit_nick.getText() != null){
						submeter(pontos, edit_nick.getText().toString());
						showmessage(2);
						Intent i = new Intent(FimActivity.this,MenuActivity.class);
						startActivity(i);
						finish();
					}else{
						showmessage(3);
					}
				}else{
					showmessage(1);
				}
			}
		});
	
	}
	
	
	public static boolean submeter(final int pontos, final String nick){
		
	        Thread t = new Thread() {

	            public void run() {
	                Looper.prepare(); //For Preparing Message Pool for the child Thread
	                HttpClient client = new DefaultHttpClient();
	                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
	                HttpResponse response;
	                JSONObject json = new JSONObject();

	                try {
	                    HttpPost post = new HttpPost("http://192.168.2.26:8182/submit");
	                    json.put("nick", nick);
	                    json.put("points", pontos);
	                    StringEntity se = new StringEntity( json.toString());  
	                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
	                    post.setEntity(se);
	                    response = client.execute(post);

	                    /*Checking response */
	                    if(response!=null){
	                        InputStream in = response.getEntity().getContent(); //Get the data in the entity
	                    }

	                } catch(Exception e) {
	                    e.printStackTrace();
	                    
	                }

	                Looper.loop(); //Loop in the message queue
	            }
	        };

	        t.start();      
	    
	    return true;
	}
	
	public boolean isConnected(){
    	ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
    	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    	    if (networkInfo != null && networkInfo.isConnected()) 
    	    	return true;
    	    else
    	    	return false;	
    }
	public void showmessage(int tipo){
		if(tipo == 1){
			Toast.makeText(this, "Você Não está conectado !!",Toast.LENGTH_LONG).show();
		}else if(tipo == 2){
			Toast.makeText(this, "Pontuação submetida !!",Toast.LENGTH_LONG).show();
		}else if(tipo == 3){
			Toast.makeText(this, "Campo deve ser preenchido !!",Toast.LENGTH_LONG).show();
		}
		
	}
	
}	