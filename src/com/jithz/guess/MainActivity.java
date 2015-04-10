package com.jithz.guess;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;

public class MainActivity extends Activity {
	
	DBClass db;
	GameControl control;
	TextView wordTextView;
	EditText hintEditText;
	TextView scoreTextView;
	TextView hintTextView;
	TextView lifeTextView;
	LinearLayout[] letterLinearLayouts= new LinearLayout[4];
	Button hint;
	String hiddenWord;
	int lifeNo;
	int availHintNo;
	int userScore;
	int winCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 db= new DBClass(this);
		try {
			db.createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
		db.openDataBase();
		control=new GameControl(db);
		wordTextView=(TextView) findViewById(R.id.wordTextView);
		hintEditText=(EditText) findViewById(R.id.hintEditText);
		scoreTextView=(TextView) findViewById(R.id.scoreTextView);
		hintTextView=(TextView) findViewById(R.id.hintTextView);
		lifeTextView=(TextView) findViewById(R.id.lifeTextView);
		hint=(Button) findViewById(R.id.hintButton);
		hint.setOnClickListener(hintButtonListener);
		letterLinearLayouts[0] = 
		         (LinearLayout) findViewById(R.id.letterRowLinearLayout1);
		      letterLinearLayouts[1] = 
		         (LinearLayout) findViewById(R.id.letterRowLinearLayout2);
		      letterLinearLayouts[2] = 
		         (LinearLayout) findViewById(R.id.letterRowLinearLayout3);
		      letterLinearLayouts[3] = 
		 	     (LinearLayout) findViewById(R.id.letterRowLinearLayout4);
		resetGame();
		
		hintTextView.setText(String.valueOf(availHintNo));
		lifeTextView.setText(String.valueOf(lifeNo));
		for (LinearLayout row : letterLinearLayouts)
	      {
	         for (int column = 0; column < row.getChildCount(); column++) 
	         {
	            Button button = (Button) row.getChildAt(column);
	            button.setOnClickListener(letterButtonListener);
	         }
	      } 
	}
	
	public void resetGame() {
		availHintNo=10;
		userScore=0;
		winCount=0;
		scoreTextView.setText(String.valueOf(userScore));
		nextWord();
		
	}
	public void nextWord() {
		lifeNo=7;
		hintTextView.setText(String.valueOf(availHintNo));
		lifeTextView.setText(String.valueOf(lifeNo));
		hiddenWord=control.newWord();
		wordTextView.setText(hiddenWord);
		hintEditText.setText("");
		for (LinearLayout r : letterLinearLayouts)
	      {
		for (int col = 0; col <r.getChildCount(); col++) 
	         { 
	            Button buttons = (Button) r.getChildAt(col);
	            buttons.setEnabled(true);
	            buttons.setTextColor(Color.BLACK);
	         }
	      }
		hint.setEnabled(true);
	}
	
	public int checkMatch(char letter) {
		int count=0;
		int state=0;
		for(int i=0; i<hiddenWord.length(); i++) {
			if(control.fetchWord().charAt(i)==letter) {
				hiddenWord = hiddenWord.substring(0,i)+letter+hiddenWord.substring(i+1);
				count+=1;
			}
		}
		wordTextView.setText(hiddenWord);
		if(count>0) {
			state=1;
			
		}
		else {
			lifeNo-=1;
		}
		result();
		
		return state;
	}
	
	public void result() {
		if(control.fetchWord().equals(hiddenWord)) {
			winCount+=1;
	        if((winCount%3)==0)
	        {
	            availHintNo+=1;
	        }
	        userScore=userScore+(lifeNo*10);
	        scoreTextView.setText(String.valueOf(userScore));
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("CongratZz.. click ok to continue")
			       .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			             public void onClick(DialogInterface dialog, int id) {
			                  nextWord();
			             }
			         });
			AlertDialog alertDialog = builder.create();
			alertDialog.show();
		}
		else if(lifeNo<1) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setMessage("Sorry you lost..\n Correct Word : "+control.fetchWord()+"\n Click ok to play again")
	               .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	  resetGame();
	                   }
	               })
	        .setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
             	  exitGame();
                }
            });
	        AlertDialog alertDialog = builder.create();
	        alertDialog.show();
			
		}
		lifeTextView.setText(String.valueOf(lifeNo));
	}
	
	private OnClickListener letterButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Button letterButton = ((Button) v); 
	         char letter = letterButton.getText().charAt(0);
	         letterButton.setEnabled(false);
			int state=checkMatch(letter);
			if(state==1) {
				letterButton.setTextColor(Color.GREEN);
			}
			else {
				letterButton.setTextColor(Color.RED);
			}
		}
	};
	
	private OnClickListener hintButtonListener =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(availHintNo>0) {
			hintEditText.setText(control.fetchHint());
			availHintNo-=1;
			hintTextView.setText(String.valueOf(availHintNo));
			hint.setEnabled(false);
			}
			else
			{
				hintEditText.setText("Sorry No Hints available for you");
				hint.setEnabled(false);
			}
		}
	};
	
	public void exitGame() {
		System.exit(0);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
