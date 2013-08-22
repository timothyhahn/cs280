package com.example.inquizition;

import java.util.ArrayList;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

public class JoinActivity extends Activity{

	private Handler handler;
	private Activity context;
	JSONTask quizNameTask, getRunningQuizzesTask, getSecondsLeftTask;
	ArrayList<QuizGame> quizGames;
	public int idToJoin;
	JoinDialogFragment joinGameDialog;
	QuizGame gameToJoin;
	FragmentTransaction ft;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    
		context = this;
        setContentView(R.layout.layout_joingame);
        super.onCreate(savedInstanceState);
        
        handler = new Handler();
        quizGames = new ArrayList<QuizGame>();
        
        ListView l = (ListView)findViewById(R.id.listView1);
        View footerView = ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_footer, null, false);
        l.addFooterView(footerView);
        TextView t = (TextView)findViewById(R.id.pleaseSelect);
        t.setText("Please select a game, "+Constants.username+":");
        
        getRunningQuizzes.run();
		
	}
	
    Runnable getRunningQuizzes = new Runnable()
    {

		@Override
		public void run() {
			
			getRunningQuizzesTask = new GetRunningQuizzesTask(context, "http://inquizition.us/quiz");
			getRunningQuizzesTask.execute();
			handler.postDelayed(getRunningQuizzes, 2500);
			
		}
    	
    };
    
    
	public void gotRunningQuizzes() {
		
		ArrayList<QuizGame> newQuizGames = (ArrayList<QuizGame>) getRunningQuizzesTask.getResults();
		
		for(QuizGame newQuizGame: newQuizGames)
		{
			boolean alreadyHave = false;
			
			for(QuizGame quizGame: quizGames)
			{
				if(quizGame.id == newQuizGame.id)
				{
					alreadyHave = true;
					break;
				}
					
			}
			
			if(!alreadyHave)
				quizGames.add(newQuizGame);

		}
		
		for(int i = 0; i < quizGames.size(); i++)
		{

			boolean needToDelete = true;
			
			for(QuizGame newQuizGame: newQuizGames)
			{
				if(quizGames.get(i).id == newQuizGame.id)
				{
					needToDelete = false;
				}
			}
			
			if(needToDelete)
				quizGames.remove(i);
		}

		
        QuizArrayAdapter adapter = new QuizArrayAdapter(this, quizGames);
        
        ListView l = (ListView)findViewById(R.id.listView1);
        

        
        

        l.setAdapter(adapter);
        adapter.notifyDataSetChanged();
		
		
	}
    
	public void joinGame(int id) {
		
		gameToJoin = null;
		
		for(int i = 0; i < quizGames.size(); i++)
		{
			if(quizGames.get(i).id == id)
			{
				gameToJoin = quizGames.get(i);
				break;
			}
		}
		
		if(gameToJoin == null)
		{
			return;
		}
		
		idToJoin = id;
		
		getSecondsLeftTask = new GetSecondsLeftTask(this, "http://inquizition.us/quiz/seconds/"+id);
		getSecondsLeftTask.execute();
		
	}
	
	public void startGame(QuizGame quizGame)
	{
		ft.remove(joinGameDialog);
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra("quizGame", quizGame);
		startActivity(intent);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void gotSecondsLeft()
    {
    	ft = getFragmentManager().beginTransaction();
    	String result = (String)getSecondsLeftTask.getResults();
    	int secondsLeft =  Integer.parseInt(result);
		openJoinDialog(secondsLeft);
    }
    
    private void openJoinDialog(int secondsLeft)
    {
    	joinGameDialog = JoinDialogFragment.newInstance(idToJoin, secondsLeft, gameToJoin.name);
		joinGameDialog.show(ft, "Join Game");
    	
    }

	public void gotQuiz() {
		
		joinGameDialog.gotQuiz();
		
	}

}
