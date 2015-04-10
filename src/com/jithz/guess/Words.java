package com.jithz.guess;

public class Words {
	
	private String word;
	private String hint;
	
	public Words() {
		this.word="";
		this.hint="";
	}
	
	public Words(String word, String hint) {
		this.word=word;
		this.hint=hint;
	}
	
	public String getWord() {
		return word;
	}
	
	public void setWord(String word) {
		this.word=word;
	}
	
	public String getHint() {
		return hint;
	}
	
	public void setHint(String hint) {
		this.hint=hint;
	}

}
