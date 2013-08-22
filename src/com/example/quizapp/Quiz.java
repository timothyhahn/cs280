package com.example.quizapp;

import java.util.ArrayList;


public class Quiz {

	public int id;
	public ArrayList<Question> questions;
	public String name;
	public String creator;
	
	public Quiz(String name, ArrayList<Question> questions)
	{
		
		//this.id = id;
		this.questions = questions;
		this.name = name;
		//this.creator = creator;
	}
}
