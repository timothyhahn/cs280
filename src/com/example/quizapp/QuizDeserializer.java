package com.example.quizapp;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;


public class QuizDeserializer implements JsonDeserializer {

	@Override
	public Object deserialize(JsonElement json, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		
		ArrayList<Quiz> quizzes = new ArrayList<Quiz>();
		
		JsonArray array = json.getAsJsonObject().get("quizzes").getAsJsonArray();
		
		for(int i = 0; i < array.size(); i++)
		{
			String name = array.get(i).getAsJsonObject().get("name").getAsString();
			JsonArray jquestions = array.get(i).getAsJsonObject().get("questions").getAsJsonArray();
			
			ArrayList<Question> questions = new ArrayList<Question>();
			
			for(int j = 0; j < jquestions.size(); j++)
			{			
				String questionText = jquestions.get(j).getAsJsonObject().get("questionText").getAsString();
				int questionType = jquestions.get(j).getAsJsonObject().get("questionType").getAsInt();
				
				switch(questionType)
				{
					case 0:
						JsonArray janswers = jquestions.get(j).getAsJsonObject().get("answers").getAsJsonArray();
						ArrayList<String> answers = new ArrayList<String>();
						
						for(int x = 0; x < janswers.size(); x++)
						{
							answers.add(janswers.get(x).getAsString());
						}
						questions.add(new MultipleChoiceQuestion(questionText, answers));
						break;
					
					default:
						break;
				}
				
				
				
			}
			
			quizzes.add(new Quiz(name, questions));
			
		}
		
		return quizzes;
	}

}
