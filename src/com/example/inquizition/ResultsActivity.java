package com.example.inquizition;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class ResultsActivity extends Activity {

	GetQuizResultsTask getQuizResultstask;
	Handler handler;
	int quiz_id;
	ResultsActivity activity;
	ArrayList<Result> results;
	ImageButton joinGameButton;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    
		setContentView(R.layout.layout_results);
	    super.onCreate(savedInstanceState);
	        
	    joinGameButton = (ImageButton)findViewById(R.id.joinGameButtonFromResults);
	    activity = this;
	    handler = new Handler();
	    results = new ArrayList<Result>();
	    Bundle bundle = this.getIntent().getExtras();
	    quiz_id = bundle.getInt("quiz_id");
	    getResults.run();
	    
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
			    	Intent goToJoinActivity = new Intent((Context)activity, JoinActivity.class);
			    	goToJoinActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					((Context)activity).startActivity(goToJoinActivity);
				}
				
				return true;
			}
        	
        });
		
	}
	
	Runnable getResults = new Runnable()
    {
		@Override
		public void run() {
			
			getQuizResultstask = new GetQuizResultsTask(activity, "http://inquizition.us/quiz/results/"+quiz_id);
			getQuizResultstask.execute();
			handler.postDelayed(getResults, 2500);
		}   
    };
	
	public void gotResults()
	{
		ArrayList<Result> newResults = (ArrayList<Result>) getQuizResultstask.getResults();
		System.out.println(getQuizResultstask.getResults());
		
		for(Result newResult: newResults)
		{
			boolean alreadyHave = false;
			
			for(Result result: results)
			{
				if(result.id == newResult.id)
				{
					result.score = newResult.score;
					alreadyHave = true;
				}
					
			}
			
			if(!alreadyHave)
				results.add(newResult);

		}
		
		
        ResultArrayAdapter adapter = new ResultArrayAdapter(results, this);      
        ListView l = (ListView)findViewById(R.id.resultsList);
        l.setAdapter(adapter);
        adapter.notifyDataSetChanged();
	}
	
}
