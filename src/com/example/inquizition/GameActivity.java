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
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.GridView;

public class GameActivity extends Activity  {

	QuizGame quizGame;
	PostAnswerTask task;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    
        setContentView(R.layout.layout_game);
        super.onCreate(savedInstanceState);
        
        Bundle bundle = this.getIntent().getExtras();


        quizGame = (QuizGame) bundle.getSerializable("quizGame");
        		
        GridView g = (GridView) findViewById(R.id.gridView1);
        AnswerGridAdapter adapter = new AnswerGridAdapter(quizGame.questions[0].answers, this);
        g.setAdapter(adapter);
        
        
        
	}
	
	public void answerClicked(int questionId, int answerId)
	{
		task = new PostAnswerTask(questionId, answerId);
		task.execute();
	}
	
	class PostAnswerTask extends AsyncTask<Void, Void, Void>
	{
		int questionId, answerId;
		
		public PostAnswerTask(int questionId, int answerId)
		{
			this.questionId = questionId;
			this.answerId = answerId;
		}

		@Override
		protected Void doInBackground(Void... params) {
			HttpClient httpclient = new DefaultHttpClient();
		    InputStream is = null;
		    HttpPost httppost = new HttpPost("http://www.inquizition.us/quiz/answer/"+quizGame.id);
		    try {
		        
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		        nameValuePairs.add(new BasicNameValuePair("user_id", Integer.toString(quizGame.userId)));
		        nameValuePairs.add(new BasicNameValuePair("question_id", Integer.toString(questionId)));
		        nameValuePairs.add(new BasicNameValuePair("answer_id", Integer.toString(answerId)));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
	    
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
		
		@Override
		protected void onPostExecute(Void params)
		{
			
		}
		
	}
	

	
}
