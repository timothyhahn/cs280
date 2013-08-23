package com.example.inquizition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
	HttpClient httpclient;
	HttpPost httppost;
	HttpResponse response;
	HttpEntity entity;
	
	public PostTask(Activity callback, String urlstr, ArrayList<NameValuePair> params)
	{
		this.callback = callback;
		this.urlstr = urlstr;
		this.params = params;
	}
	
	@Override
	protected void onPostExecute(Void args)
	{
		this.callback();
	}
	
	@Override
	protected Void doInBackground(Void... args) {

		httpclient = new DefaultHttpClient();
	    httppost = new HttpPost(urlstr);
	    
	    try {
	    	
	        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
	        response = httpclient.execute(httppost);
	        entity = response.getEntity();
		
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
	
	public StringBuilder readInputStream()
	{
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
 
		String line;
		try {
 
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb;
 
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
	MainActivity activity;
	public PostQuizTask(Activity callback, String urlstr,
			ArrayList<NameValuePair> params) {
		super(callback, urlstr, params);
		
		activity = (MainActivity)callback;
	}

	@Override
	void callback() {
		
		activity.postedQuiz();
		

	}
	
}

class PostJoinTask extends PostTask
{
	JoinActivity activity;
	
	public PostJoinTask(Activity callback, String urlstr,
			ArrayList<NameValuePair> params) {
		super(callback, urlstr, params);
		activity = (JoinActivity)callback;
	}

	@Override
	void callback() {
		
		activity.finalizeJoin();
	}

	
	
	
}


class PostAnswerTask extends PostTask
{
	StringBuilder sb;
	GameActivity activity;
	
	public PostAnswerTask(Activity callback, String urlstr, ArrayList<NameValuePair> params)
	{
		super(callback, urlstr, params);
		activity = (GameActivity)callback;
	}

	@Override
	void callback() {
		
		activity.answerPosted();
		
	}
	
	public StringBuilder getAnswerResults()
	{
		return sb;
	}

}
	
