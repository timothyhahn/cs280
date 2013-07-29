package com.example.quizapp;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class GridAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Quiz> quizzes;
	
	public GridAdapter(Context c, ArrayList<Quiz> quizzes)
	{
		this.quizzes = new ArrayList<Quiz>();
		this.quizzes.addAll(quizzes);
		mContext = c;
	}
	
	
	public int getCount() {
		return quizzes.size();
	}

	@Override
	public Object getItem(int position) {
		
		return quizzes.get(position);
		
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ImageView imageView;
		imageView = new ImageView(mContext);
		imageView.setLayoutParams(new GridView.LayoutParams(400, 400));
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setPadding(8, 8, 8, 8);
		
		imageView.setImageResource(R.drawable.quizbox);
		return imageView;
		
	}
	
}
