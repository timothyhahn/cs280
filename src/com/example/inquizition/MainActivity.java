package com.example.inquizition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.StrictMode;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


//Home screen activity

public class MainActivity extends Activity implements QuizCreator {

	JSONTask quizNameTask, getRunningQuizzesTask;
	PostTask postQuizTask, postUsernameTask;
	Activity context;
	QuizNameDialogFragment quizNameDialog;
	FragmentTransaction ft;
	boolean isQuizPosted, isUsernamePosted = false;
	ImageButton createGameButton, joinGameButton;
	
    @SuppressWarnings("unchecked")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
                   
    	context = this;
        setContentView(R.layout.new_home);
        super.onCreate(savedInstanceState);

        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);
        
        createGameButton = (ImageButton) findViewById(R.id.createGameButton);
        joinGameButton = (ImageButton) findViewById(R.id.joinGameButton);    
        
        LinearLayout layout = (LinearLayout)findViewById(R.id.linearLayout1);       
        layout.setOnClickListener(new OnClickListener()
        {

			@Override
			public void onClick(View view) {
				EditText editText = (EditText) findViewById(R.id.editTextUsername);
				editText.clearFocus();
			    InputMethodManager imm = (InputMethodManager) view.getContext()
			            .getSystemService(Context.INPUT_METHOD_SERVICE);
			    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}
        	
        });
        
        createGameButton.setOnTouchListener(new OnTouchListener()
        {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
	
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					createGameButton.setBackgroundResource(R.drawable.creategamebuttontouched);
				}
				
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					createGameButton.setEnabled(false);
					createGameButton.setBackgroundResource(R.drawable.creategamebutton);
					//If create game is clicked, get the quiz name
					quizNameTask = new GetQuizNameTask(context, "http://inquizition.us/name");
					quizNameTask.execute();	
				}
				
				return true;
			}
        	
        	
        });
        
        
        joinGameButton.setOnTouchListener(new OnTouchListener()
        {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					joinGameButton.setBackgroundResource(R.drawable.joingamebuttontouched);
				}
				
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					joinGameButton.setBackgroundResource(R.drawable.joingamebutton);
					joinGameButton.setEnabled(false);
					//If join game is clicked, start the task to post username. 
					isQuizPosted = true; //We don't need to get quiz name.
					EditText editText = (EditText) findViewById(R.id.editTextUsername);
					
					if(!editText.equals(""))
						Constants.username = "Guest";
					else
						Constants.username = editText.getText().toString();
					
					ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("username", Constants.username));
					
					postUsernameTask = new PostUsernameTask(context, "http://inquizition.us/login", params);
					postUsernameTask.execute();
				}
				
				return true;
			}
        	
        });

    }   

    @Override
    protected void onResume()
    {
    	super.onResume();
    	joinGameButton.setEnabled(true);
    	createGameButton.setEnabled(true);
    }
    
    
    public void goToJoin()
    {
    	Intent goToJoinActivity = new Intent(context, JoinActivity.class);
		context.startActivity(goToJoinActivity);
    }
    
    
	public void gotQuizName() {

		//After quiz name is received, ask the user to confirm quiz name
		String name = (String)quizNameTask.getResults();
		ft = getFragmentManager().beginTransaction();
		
		quizNameDialog = QuizNameDialogFragment.newInstance("New Game", "Enter a name and click OK:", name);
		quizNameDialog.show(ft, "New Game");
		createGameButton.setEnabled(true);
	}
	
	public void quizNameConfirmed(String name, String seconds)
	{
		//After quiz name is confirmed, post the new quiz and the username.
		EditText editText = (EditText) findViewById(R.id.editTextUsername);
		if(!editText.equals(""))
			Constants.username = "Guest";
		else
			Constants.username = editText.getText().toString();
		
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		ArrayList<NameValuePair> usernameParams = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("quiz_name", name));
		params.add(new BasicNameValuePair("seconds", seconds));
		usernameParams.add(new BasicNameValuePair("username", Constants.username));
		
		postQuizTask = new PostQuizTask(this, "http://inquizition.us/quiz/create", params);
		postQuizTask.execute();
		postUsernameTask = new PostUsernameTask(this, "http://inquizition.us/login", usernameParams);
		postUsernameTask.execute();
		quizNameDialog.dismiss();
	}
	
	
    public void postedQuiz()
    {

		isQuizPosted = true;
		System.out.println(postQuizTask.readInputStream().toString());
		
		//Avoids race condition. Waits for user and quiz (if necessary) to be posted
		if(isQuizPosted && isUsernamePosted)
			goToJoin();
    }
	
	public void postedUsername()
	{
		isUsernamePosted = true;
		String json = postUsernameTask.readInputStream().toString();
		Gson gson = new Gson();
		JsonObject j = gson.fromJson(json, JsonObject.class);
		
		try
		{
			Constants.user_id = j.get("id").getAsString();
		
			//Avoids race condition. Waits for user and quiz (if necessary) to be posted
			if(isQuizPosted && isUsernamePosted)
				goToJoin();
		}
		
		catch(NullPointerException e)
		{
			System.err.println("There was an error calling login.");
		}
	}

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    





    
}
