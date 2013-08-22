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
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

public class GameActivity extends Activity  {

	QuizGame quizGame;
	PostAnswerTask task;
	int questionPos = -1;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    
       setContentView(R.layout.layout_game);
       super.onCreate(savedInstanceState);
        
       Bundle bundle = this.getIntent().getExtras();

       quizGame = (QuizGame) bundle.getSerializable("quizGame");       		
       loadNextQuestion();     
        
	}
	
	private void loadNextQuestion()
	{
		questionPos += 1;
		TextView t = (TextView) findViewById(R.id.questionText);
		t.setText(quizGame.questions[questionPos].text);
		ListView l = (ListView) findViewById(R.id.answerListView);
        AnswerArrayAdapter adapter = new AnswerArrayAdapter(quizGame.questions[questionPos].answers, this);
        l.setAdapter(adapter);
	}
	
	public void answerClicked(int answerId)
	{    
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("user_id", Integer.toString(Constants.user_id)));
        params.add(new BasicNameValuePair("question_id", Integer.toString(quizGame.questions[questionPos].id)));
        params.add(new BasicNameValuePair("answer_id", Integer.toString(answerId)));
        
		task = new PostAnswerTask(this, "http://www.inquizition.us/quiz/answer/"+quizGame.id, params);
		task.execute();
		loadNextQuestion();
	}
	
	public void answerPosted()
	{
		System.out.println(task.getResults());
	}
	

	

	
}
