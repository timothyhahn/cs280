package com.example.inquizition;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class JoinActivity extends Activity implements QuizCreator{

	private Handler handler;
	private Handler handler2;
	private Activity context;
	JSONTask quizNameTask, getRunningQuizzesTask, getQuizTask;
	PostTask postJoinTask, postQuizTask;
	ArrayList<GetSecondsLeftTask> getSecondsLeftTasks;
	ArrayList<QuizGame> quizGames;
	public int idToJoin;
	JoinDialogFragment joinGameDialog;
	QuizNameDialogFragment quizNameDialog;
	QuizGame gameToJoin;
	QuizGame joinedGame;
	FragmentTransaction ft;
	boolean dialogOpened = false;
	Lock dialogLock;
	ImageButton createGameButton;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    
		context = this;
        setContentView(R.layout.layout_joingame);
        super.onCreate(savedInstanceState);
        
        handler = new Handler();
        handler2 = new Handler();
        quizGames = new ArrayList<QuizGame>();
        dialogLock = new ReentrantLock();
        getSecondsLeftTasks = new ArrayList<GetSecondsLeftTask>();
        
        ListView l = (ListView)findViewById(R.id.listView1);
        //View footerView = ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_footer, null, false);
        //l.addFooterView(footerView);
        TextView t = (TextView)findViewById(R.id.pleaseSelect);
        t.setText("Please select a game, "+Constants.username+":");
        createGameButton = (ImageButton) findViewById(R.id.createGameButtonFromJoin);
        
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
        
        
        getRunningQuizzes.run();
		getSecondsLeftForQuizzes.run();

	}
	
	
	@Override
	protected void onResume()
	{
		super.onResume();
		createGameButton.setEnabled(true);
	}
	
	public void gotQuizName()
	{
		//After quiz name is received, ask the user to confirm quiz name
		String name = (String)quizNameTask.getResults();
		ft = getFragmentManager().beginTransaction();
		
		quizNameDialog = QuizNameDialogFragment.newInstance("New Game", "Enter a name and click OK:", name);
		quizNameDialog.show(ft, "New Game");
		createGameButton.setEnabled(true);
	}
	
	public void quizNameConfirmed(String name, String seconds)
	{
		//After quiz name is confirmed, post the new quiz
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		ArrayList<NameValuePair> usernameParams = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("quiz_name", name));
		params.add(new BasicNameValuePair("seconds", seconds));

		postQuizTask = new PostQuizTask(this, "http://inquizition.us/quiz/create", params);
		postQuizTask.execute();
		quizNameDialog.dismiss();
		createGameButton.setEnabled(true);
	}
	
	public void postedQuiz()
	{
		System.out.println(postQuizTask.readInputStream().toString());
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
    
    Runnable getSecondsLeftForQuizzes = new Runnable()
    {
    	@Override
    	public void run() {
    		
    		int count = 0;
    		for(QuizGame quizGame: quizGames)
    		{
    			GetSecondsLeftTask task = new GetSecondsLeftTask(context, "http://inquizition.us/quiz/seconds/"+quizGame.id, quizGame.id, count);
    			System.out.println("running task for "+quizGame.id);
    			getSecondsLeftTasks.add(task);
    			task.execute();
    			count += 1;
    		}
    		
    		handler2.postDelayed(getSecondsLeftForQuizzes, 500);
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

		dialogLock.lock();
		ft = getFragmentManager().beginTransaction();
		joinGameDialog = JoinDialogFragment.newInstance(idToJoin, gameToJoin.secondsLeft, gameToJoin.name);
		joinGameDialog.show(ft, "Join Game");
		dialogOpened = true;
		
		dialogLock.unlock();
		
		ArrayList<NameValuePair> usernameParams = new ArrayList<NameValuePair>();
		usernameParams.add(new BasicNameValuePair("user_id", Constants.user_id));
		postJoinTask = new PostJoinTask(this, "http://inquizition.us/quiz/join/"+idToJoin, usernameParams);
		postJoinTask.execute();
		
	}
	
	public void startGame(QuizGame quizGame)
	{
		dialogLock.lock();
		joinGameDialog.dismiss();
		dialogOpened = false;
		
		dialogLock.unlock();
		
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
    
    public void gotSecondsLeft(int taskIndex)
    {
    	try
    	{
    	
    	int[] result = (int[])getSecondsLeftTasks.get(taskIndex).getResults();
    	System.out.println(result);
    	for(int i = 0; i < quizGames.size(); i++)
    	{
    		
	    		if(quizGames.get(i).id == result[0])
	    		{
	    			quizGames.get(i).secondsLeft = result[1];
	    			
	    			dialogLock.lock();
	    			
	    			
	    			if(dialogOpened && idToJoin == quizGames.get(i).id)
	    			{
	    				joinGameDialog.updateText(quizGames.get(i).secondsLeft);
	    				if(quizGames.get(i).secondsLeft == 0 || quizGames.get(i).secondsLeft > 500)
	    				{
	    					getQuizTask = new GetQuizTask(this, "http://inquizition.us/quiz/"+idToJoin);
	    					getQuizTask.execute();
	    				}
	    			}
	    			
	    			if(quizGames.get(i).secondsLeft == 0 || quizGames.get(i).secondsLeft > 500)
	    				quizGames.remove(i);
	    			
	    			
	    			dialogLock.unlock();
	    		}
    		
    	}
    	
    	}
    	catch(NullPointerException e)
    	{
    		return;
    	}
    	
        QuizArrayAdapter adapter = new QuizArrayAdapter(this, quizGames);
        
        ListView l = (ListView)findViewById(R.id.listView1);

        l.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        getSecondsLeftTasks.remove(taskIndex);
		
    }
    


	public void gotQuiz() {
		
		System.out.println(getQuizTask.getResults());
		if(getQuizTask.getResults() != null)
		{		
			joinedGame = (QuizGame) getQuizTask.getResults();
			startGame(joinedGame);
			
		}
		
	}
	
	public void finalizeJoin()
	{
		System.out.println(postJoinTask.readInputStream().toString());
	}

}
