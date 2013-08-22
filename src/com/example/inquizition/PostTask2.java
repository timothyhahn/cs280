package com.example.inquizition;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;

public abstract class PostTask2 extends AsyncTask<Void, Void, Void> {

	InputStream is;
	JSONObject json;
	Activity callback;
	String urlstr;
	
	public PostTask2(Activity callback, String urlstr)
	{
		this.callback = callback;
		this.urlstr = urlstr;
		json = new JSONObject();
	}
	
	@Override
	protected Void doInBackground(Void... args) {

		HttpClient httpclient = new DefaultHttpClient();
	    InputStream is = null;
	    HttpPost httppost = new HttpPost(urlstr);
	    
	    
	    try {
	    	
	    	StringEntity se = new StringEntity(json.toString());
	    	se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
	        httppost.setEntity(se);
	        HttpResponse response = httpclient.execute(httppost);
	        HttpEntity entity = response.getEntity();
		
	        if(entity != null)
	        	is = entity.getContent();
	        
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    }
	    

	    
		return null;
	}
	
	public InputStream getResults()
	{
		return is;
	}
	
	abstract void callback();

}

class PostUsernameTask extends PostTask2
{
	MainActivity activity;
	
	public PostUsernameTask(Activity callback, String urlstr,
			String username) {
		super(callback, urlstr);
		activity = (MainActivity)callback;
		
		try {
			json.put("username", Constants.username);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	void callback() {
		
		activity.postedUsername();
		
	}
	
}


class PostQuizTask extends PostTask2
{
	public PostQuizTask(Activity callback, String urlstr) {
		super(callback, urlstr);
		// TODO Auto-generated constructor stub
	}

	@Override
	void callback() {
		// TODO Auto-generated method stub
		
	}
	
}