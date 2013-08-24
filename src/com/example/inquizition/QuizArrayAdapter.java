package com.example.inquizition;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class QuizArrayAdapter extends ArrayAdapter<QuizGame> {

	private Context mContext;
	private ArrayList<QuizGame> quizGames;
	private View previousSelected = null;
	Typeface face;
	JoinActivity activity;
	
	public QuizArrayAdapter(Context c, ArrayList<QuizGame> quizGames)
	{
		super(c, R.layout.listitem_quizgame, quizGames); 
		this.quizGames = quizGames;
		mContext = c;
		activity = (JoinActivity)c;
		
		face = Typeface.createFromAsset(c.getAssets(),"fonts/Lato-Light.ttf");
	}
	
	
	public int getCount() {
		return quizGames.size();
	}


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.listitem_quizgame, parent, false);
		TextView quizName = ((TextView)view.findViewById(R.id.quizName));
		TextView secondsLeft = ((TextView)view.findViewById(R.id.secondsLeft));
		secondsLeft.setText("Game starts in "+quizGames.get(position).secondsLeft+" seconds");
		secondsLeft.setTypeface(face);
		quizName.setText(quizGames.get(position).name);
		quizName.setTypeface(face);
		
		OnTouchListener otl = new OnTouchListener()
        {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
	
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					v.setBackgroundColor(Color.argb(255, 88, 135, 179));
				}
				
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					v.setEnabled(false);
					v.setBackgroundColor(Color.argb(255, 57, 88, 117));
					activity.joinGame(quizGames.get(position).id);
				}
				
				return true;
			}
			
        };
		
        view.findViewById(R.id.quizGameItem).setOnTouchListener(otl);
        
        
		return view;
	}
	
	
}
