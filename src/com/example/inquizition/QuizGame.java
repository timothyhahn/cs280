package com.example.inquizition;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class QuizGame implements Serializable {

	public String startTime;
	public String name;
	public int id;
	public int secondsLeft;
	
	public Question[] questions;
	public Result[] results;

	
	public QuizGame(int id, String name, int secondsLeft)
	{
		this.id = id;
		this.name = name;
		this.secondsLeft = secondsLeft;
	}
	


}

class Result implements Serializable
{
	int id;
	String username;
	int score;
}


class Question implements Serializable {

	int id;
	String text;
	Answer[] answers;
	
}

class Answer implements Serializable{
	
	int id;
	String text;
}


