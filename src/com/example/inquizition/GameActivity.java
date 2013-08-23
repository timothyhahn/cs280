package com.example.inquizition;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
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
		TextView t = (TextView) findViewById(R.id.questionText);
		t.setText(quizGame.questions[questionPos].text);
		ListView l = (ListView) findViewById(R.id.answerListView);
        AnswerArrayAdapter adapter = new AnswerArrayAdapter(quizGame.questions[questionPos].answers, this);
        l.setAdapter(adapter);
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
		System.out.println(task.readInputStream().toString());
		loadNextQuestion();
	}
	

}

