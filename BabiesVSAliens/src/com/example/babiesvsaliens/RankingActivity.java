package com.example.babiesvsaliens;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class RankingActivity extends Activity {
	
	private static SQLiteDatabase database;
	
	String url = "http://172.16.2.128:8182/rank";
	
	List<String> array_user = new ArrayList<String>();
	
	ListView listView;
	
	Context context;
	
	TextView item;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.activity_ranking);
		
		context = this;
		
		item = new TextView(this);
		
		listView = (ListView) findViewById(R.id.minha_lista);
		
		DictionaryOpenHelper helper = new DictionaryOpenHelper(this);
		database = helper.getReadableDatabase();
		if(!isConnected()){
			setList();
		}
		new HttpAsyncTask().execute("http://192.168.2.26:8182/rank");
	}
	

	public static String GET(String url){
		InputStream inputStream = null;
		String result = "";
		try {

			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
			inputStream = httpResponse.getEntity().getContent();
			
			if(inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected(){
    	ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
    	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    	    if (networkInfo != null && networkInfo.isConnected()) 
    	    	return true;
    	    else
    	    	return false;	
    }
    
    public void setList(){
        
        Cursor result = database.rawQuery("SELECT nick, points "+
        		"FROM RANKING", null);
        
        while (result.moveToNext()) {
        	array_user.add(result.getString(0)+" : "+result.getString(1));
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, array_user);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
    
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        
        protected void onPostExecute(String result) {
        	
        	try {
        		if(isConnected()){
				JSONArray jsonArray = new JSONArray(result);
				
					database.execSQL("delete from RANKING");
					List<String> list = new ArrayList<String>();
					for (int i=0; i<jsonArray.length(); i++) {
					    list.add( jsonArray.getString(i) );
					    JsonParser parser = new JsonParser();
					    
					    JsonObject json = (JsonObject) parser.parse(jsonArray.getString(i));
					    
					    //Toast.makeText(getBaseContext(),json.get("nick").getAsString()+" : "+json.get("points").getAsLong() , Toast.LENGTH_SHORT).show();
					    database.execSQL("INSERT INTO RANKING (nick,points) values "+
					    		"('"+json.get("nick").getAsString()+"','"+json.get("points").getAsLong()+"')");
					}
					setList();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       }
		
	
		
	}
		
}

