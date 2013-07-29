package com.example.quizapp;

import java.util.ArrayList;

public class MultipleChoiceQuestion extends Question {

	
	public ArrayList<String>answers;
	
	
	public MultipleChoiceQuestion(String questionText, ArrayList<String> answers)
	{
		this.answers = answers;
		this.questionText = questionText;
	}
}
