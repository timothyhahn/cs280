package com.example.inquizition;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
		
		if(questionPos >= quizGame.questions.length)
		{
			Intent intent = new Intent(this, ResultsActivity.class);
			intent.putExtra("quiz_id", quizGame.id);
			this.startActivity(intent);
		}
		
		else
		{
			TextView t = (TextView) findViewById(R.id.questionText);
			t.setText(quizGame.questions[questionPos].text);
			ListView l = (ListView) findViewById(R.id.answerListView);
			AnswerArrayAdapter adapter = new AnswerArrayAdapter(quizGame.questions[questionPos].answers, this);
			l.setAdapter(adapter);
		}
	}
	
	public void answerClicked(int answerId)
	{    
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user_id", Constants.user_id));
        params.add(new BasicNameValuePair("question_id", Integer.toString(quizGame.questions[questionPos].id)));
        params.add(new BasicNameValuePair("answer", Integer.toString(answerId)));
        
		task = new PostAnswerTask(this, "http://inquizition.us/quiz/answer/"+quizGame.id, params);
		task.execute();
		
	}
	
	public void answerPosted()
	{	
		TextView correctText = (TextView) findViewById(R.id.correctText);
		TextView scoreText = (TextView) findViewById(R.id.scoreText);
		String json = task.readInputStream().toString();
		System.out.println(json);
		
		Gson gson = new Gson();
		JsonObject j = gson.fromJson(json, JsonObject.class);
		
		try
		{
			String correct = j.get("correct").getAsString();
			int score = j.get("score").getAsInt();
			String text = j.get("text").getAsString();
			
			System.out.println(correct);
			
			if(correct.equals("True"))
			{
				System.out.println("setting");
				correctText.setText("Correct!");
				//correctText.setTextColor(0x000055);
			}
			else
			{
				correctText.setText("Incorrect; "+text);
				//correctText.setTextColor(0x880000);
			}
			
			scoreText.setText("Score: "+Integer.toString(score));
		}
		
		catch(Exception e)
		{
			System.err.println("error parsing response");
		}
		
		loadNextQuestion();
	}
	

}

