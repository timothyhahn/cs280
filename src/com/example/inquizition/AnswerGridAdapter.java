package com.example.inquizition;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AnswerGridAdapter extends BaseAdapter {

	Answer[] answers;
	Context c;
	
	public AnswerGridAdapter(Answer[] answers, Context c)
	{
		this.answers = answers;
		this.c = c;
	}
	
	@Override
	public int getCount() {
		
		return answers.length;
	}

	@Override
	public Object getItem(int position) {
		
		return answers[position];
	}

	@Override
	public long getItemId(int position) {
		
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.listitem_answer, parent, false);
		
		String answerId = "";
		
		switch(position)
		{
			case 0:
				answerId = "A";
				break;
				
			case 1:
				answerId = "B";
				break;
			
			case 2:
				answerId = "C";
				break;
				
			case 3:
				answerId = "D";
				break;
			
		}
		
		((TextView)view.findViewById(R.id.answerId)).setText(answerId);
		((TextView)view.findViewById(R.id.answerText)).setText(answers[position].text);
		
		
		return view;
		
	}

}
