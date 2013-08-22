package com.example.inquizition;



import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

public class JoinDialogFragment extends DialogFragment {

	JSONTask getQuizTask, getSecondsLeftTask;
	JoinActivity activity;
	int id, secondsLeft, secondsInit;
	
	QuizGame quizGame;
	Handler handler;
	ProgressBar progressBar;
	TextView textSeconds;
	DialogFragment self;
	
	
	 public static JoinDialogFragment newInstance(int id, int secondsLeft, String name)
	 {
		 JoinDialogFragment frag = new JoinDialogFragment();
		 
		 Bundle args = new Bundle();
		 args.putInt("id", id);
		 args.putInt("secondsLeft", secondsLeft);
		 args.putString("name", name);
		 frag.setArguments(args);
		 
		 return frag;
		 
	 }
	 
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	 {
		 Bundle bundle = this.getArguments();
		 id = bundle.getInt("id");
		 secondsLeft = bundle.getInt("secondsLeft");
		 self = this;
		 
		 activity = (JoinActivity) this.getActivity();	
		 handler = new Handler();

		 secondsInit = secondsLeft;
   		 
		 System.out.println("onCreateView view = "+getView());

		 return inflater.inflate(R.layout.dialog_join, container, false);
		
	 }
	   
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		 super.onActivityCreated(savedInstanceState);
		 textSeconds  = (TextView)getView().findViewById(R.id.textSeconds);
  		 progressBar = (ProgressBar)getView().findViewById(R.id.progressBar1);
  		 
  		 progressBar.setMax(secondsInit);
		 
	}
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		return dialog;	
	}
	
	public void updateText(int secondsLeft)
	{
		textSeconds.setText("The game will begin in "+secondsLeft +"...");
		progressBar.setProgress(secondsInit - secondsLeft);
	}
}
