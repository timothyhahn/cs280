package com.example.inquizition;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class QuizNameDialogFragment extends DialogFragment {

	String requestText;
	String defaultText;
	String dialogTitle;
	ArrayList<NameValuePair> params;
	
	public QuizNameDialogFragment()
	{
		
	}
	
	public static QuizNameDialogFragment newInstance(String dialogTitle, String requestText, String defaultText)
	{
		
		QuizNameDialogFragment frag  = new QuizNameDialogFragment();
		Bundle args = new Bundle();
		args.putString("dialogTitle", dialogTitle);
		args.putString("requestText", requestText);
		args.putString("defaultText", defaultText);
		
		frag.setArguments(args);
		return frag;
	}
	
	 @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		 Bundle bundle = this.getArguments();
		 requestText = bundle.getString("requestText");
		 defaultText = bundle.getString("defaultText");
		 dialogTitle = bundle.getString("dialogTitle");
		 getDialog().setTitle(dialogTitle);
		 return inflater.inflate(R.layout.dialog_username, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		 super.onActivityCreated(savedInstanceState);
			 
		 Button button = (Button)getView().findViewById(R.id.button1);
		 final EditText editText = (EditText)getView().findViewById(R.id.editTextUsername);
		 
		 editText.setOnFocusChangeListener(new OnFocusChangeListener()
		 {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				
				if(!hasFocus)
				{
				editText.clearFocus();
			    InputMethodManager imm = (InputMethodManager) v.getContext()
			            .getSystemService(Context.INPUT_METHOD_SERVICE);
			    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
			 
		 });
				 
		 
		 final Spinner spinner = (Spinner)getView().findViewById(R.id.spinner1);
		 TextView textView = (TextView)getView().findViewById(R.id.textUsername);
		 
		 textView.setText(requestText);
		 editText.setHint(defaultText);
			 
			 
		 button.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					
					final String editedText = editText.getText().toString();
					final String seconds = (String)spinner.getSelectedItem();
					final String hint = editText.getHint().toString();
					
					System.out.println(editedText);

					if(!editedText.equals(""))
						((MainActivity)getActivity()).quizNameConfirmed(editedText, seconds);
					else
						((MainActivity)getActivity()).quizNameConfirmed(hint, seconds);
								
				}
			 
		 });
			 
	
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		Dialog dialog = super.onCreateDialog(savedInstanceState);

		return dialog;	
	}

	
}



