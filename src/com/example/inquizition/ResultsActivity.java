package com.example.inquizition;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

public class ResultsActivity extends Activity {

	GetQuizResultsTask getQuizResultstask;
	Handler handler;
	int quiz_id;
	ResultsActivity activity;
	ArrayList<Result> results;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    
		setContentView(R.layout.layout_results);
	    super.onCreate(savedInstanceState);
	        
	    activity = this;
	    handler = new Handler();
	    results = new ArrayList<Result>();
	    Bundle bundle = this.getIntent().getExtras();
	    quiz_id = bundle.getInt("quiz_id");
	    getResults.run();
		
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
		
		for(Result newResult: newResults)
		{
			boolean alreadyHave = false;
			
			for(Result result: results)
			{
				if(result.id == newResult.id)
				{
					result.score = newResult.score;
				}
					
			}

		}
		
		
        ResultArrayAdapter adapter = new ResultArrayAdapter(results, this);      
        ListView l = (ListView)findViewById(R.id.resultsList);
        l.setAdapter(adapter);
        adapter.notifyDataSetChanged();
	}
	
}
