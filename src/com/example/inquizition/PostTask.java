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
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.AsyncTask;

public abstract class PostTask extends AsyncTask<Void, Void, Void> {

	InputStream is;
	
	Activity callback;
	String urlstr;
	ArrayList<NameValuePair> params;
	
	public PostTask(Activity callback, String urlstr, ArrayList<NameValuePair> params)
	{
		this.callback = callback;
		this.urlstr = urlstr;
		this.params = params;
	}
	
	@Override
	protected Void doInBackground(Void... args) {

		HttpClient httpclient = new DefaultHttpClient();
	    InputStream is = null;
	    HttpPost httppost = new HttpPost(urlstr);
	    
	    try {
	    	
	        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
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

class PostUsernameTask extends PostTask
{
	MainActivity activity;
	
	public PostUsernameTask(Activity callback, String urlstr,
			ArrayList<NameValuePair> params) {
		super(callback, urlstr, params);
		activity = (MainActivity)callback;
		
	}

	@Override
	void callback() {
		
		activity.postedUsername();
		
	}
	
}


class PostQuizTask extends PostTask
{
	public PostQuizTask(Activity callback, String urlstr,
			ArrayList<NameValuePair> params) {
		super(callback, urlstr, params);
		// TODO Auto-generated constructor stub
	}

	@Override
	void callback() {
		// TODO Auto-generated method stub
		
	}
	
}