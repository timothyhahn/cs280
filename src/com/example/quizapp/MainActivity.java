package com.example.quizapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import android.os.Bundle;
import android.os.Debug;
import android.app.Activity;
import android.view.Menu;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.widget.GridView;
import android.widget.TextView;

public class MainActivity extends Activity {

    @SuppressWarnings("unchecked")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        
        
        InputStream is = getResources().openRawResource(R.raw.quiz);
        String json = "";

        Scanner scanFile = new Scanner(is);
			
        while(scanFile.hasNext())
        {
        	json += scanFile.nextLine();
        }

        
        ArrayList<Quiz> quizzes = new ArrayList<Quiz>();
        Gson gson = new GsonBuilder().registerTypeAdapter(quizzes.getClass(), new QuizDeserializer()).create();
        quizzes = gson.fromJson(json, quizzes.getClass());
        
        GridView g = (GridView)findViewById(R.id.gridView1);
        g.setAdapter(new GridAdapter(this, quizzes));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
