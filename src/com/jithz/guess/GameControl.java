package com.jithz.guess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;

public class GameControl {
	
	DBClass db;
	Words w;
	List<Integer> generatedNumbers= new ArrayList<Integer>();
	public GameControl(DBClass db)
	{
		this.db=db;
	}
	
	public int randomNumber()
	{
		
		int randomNumber;
		do
		{
		 Random randomGenerator = new Random();
		 randomNumber = randomGenerator.nextInt(49);
		} while(generatedNumbers.contains(randomNumber));
		generatedNumbers.add(randomNumber);
		if(generatedNumbers.size()>49) {
			generatedNumbers.clear();
		}
		return randomNumber+1;
	}
	
	public String newWord()
	{
		String hiddenWord="";
		w=db.getWords(randomNumber());
		for(int i=0; i<fetchWord().length(); i++)
		{
			hiddenWord=hiddenWord+"*";
		}
		return hiddenWord;
	}
	
	public String fetchWord()
	{		
		return w.getWord();
	}
	
	public String fetchHint()
	{
		return w.getHint();
	}
	
}
