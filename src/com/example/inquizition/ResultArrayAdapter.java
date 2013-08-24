package com.example.inquizition;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ResultArrayAdapter extends ArrayAdapter<Result> {

	ArrayList<Result> results;
	Context c;
	ResultsActivity activity;
	
	public ResultArrayAdapter(ArrayList<Result> results, Context c)
	{
		super(c, R.layout.layout_results, results); 
		this.results = results;
		this.c = c;
		activity = (ResultsActivity)c;
	}
	
	@Override
	public int getCount() {
		
		return results.size();
	}

	@Override
	public Result getItem(int position) {
		
		return results.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.listitem_result, parent, false);
		
		((TextView)view.findViewById(R.id.resultName)).setText(results.get(position).username);
		((TextView)view.findViewById(R.id.resultScore)).setText(Integer.toString(results.get(position).score));
		
		
		return view;
		
	}

}