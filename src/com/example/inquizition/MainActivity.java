package com.example.inquizition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


//Home screen activity

public class MainActivity extends Activity {

	JSONTask quizNameTask, getRunningQuizzesTask;
	PostTask postQuizTask, postUsernameTask;
	Activity context;
	QuizNameDialogFragment quizNameDialog;
	FragmentTransaction ft;
	boolean isQuizPosted, isUsernamePosted = false;
	
    @SuppressWarnings("unchecked")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
                   
    	context = this;
        setContentView(R.layout.new_home);
        super.onCreate(savedInstanceState);

        ImageButton createGameButton = (ImageButton) findViewById(R.id.createGameButton);
        ImageButton joinGameButton = (ImageButton) findViewById(R.id.joinGameButton);
        
        createGameButton.setOnClickListener(new OnClickListener()
        {

			@Override
			public void onClick(View v) {

				//If create game is clicked, get the quiz name
				quizNameTask = new GetQuizNameTask(context, "http://www.inquizition.us/name");
				quizNameTask.execute();	
			}
        	
        });
        
        
        joinGameButton.setOnClickListener(new OnClickListener()
        {

			@Override
			public void onClick(View v) {
				
				//If join game is clicked, start the task to post username. 
				isQuizPosted = true; //We don't need to get quiz name.
				EditText editText = (EditText) findViewById(R.id.editTextUsername);
				Constants.username = editText.getText().toString();
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username", Constants.username));
				postUsernameTask = new PostUsernameTask(context, "http://inquizition.us/login", params);
				postUsernameTask.execute();
			}
        	
        });

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
	}
	
	public void quizNameConfirmed(String name)
	{
		//After quiz name is confirmed, post the new quiz and the username.
		EditText editText = (EditText) findViewById(R.id.editTextUsername);
		Constants.username = editText.getText().toString();
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		ArrayList<NameValuePair> usernameParams = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("quiz_name", name));
		usernameParams.add(new BasicNameValuePair("username", Constants.username));
		
		postQuizTask = new PostQuizTask(this, "http://inquizition.us/quiz/create", params);
		postQuizTask.execute();
		postUsernameTask = new PostUsernameTask(this, "http://inquizition.us/login", usernameParams);
		postUsernameTask.execute();
	}
	
	
    public void postedQuiz()
    {
	
		isQuizPosted = true;
		
		//Avoids race condition. Waits for user and quiz (if necessary) to be posted
		if(isQuizPosted && isUsernamePosted)
			goToJoin();
    }
	
	public void postedUsername()
	{
		isUsernamePosted = true;
		
		//Avoids race condition. Waits for user and quiz (if necessary) to be posted
		if(isQuizPosted && isUsernamePosted)
			goToJoin();
	}
	



    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }






    
}
