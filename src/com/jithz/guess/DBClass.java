package com.jithz.guess;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBClass extends SQLiteOpenHelper{
	
	private static String DB_PATH = "";
	private static final String DATABASE_NAME = "WORDGUESS.db";
	private static final int DATABASE_VERSION = 1;
	static final String TABLE_NAME = "words";
	public Context context;
	static SQLiteDatabase sqliteDataBase;
	
	public DBClass(Context context) {       
	    super(context, DATABASE_NAME, null ,DATABASE_VERSION);
	    if(android.os.Build.VERSION.SDK_INT >= 17){
		       DB_PATH = context.getApplicationInfo().dataDir + "/databases/";         
		    }
		    else
		    {
		       DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
		    }
	    this.context = context;
	}

	public void createDataBase() throws IOException{
	    boolean databaseExist = checkDataBase();

	    if(!databaseExist){
	        this.getWritableDatabase();         
	        copyDataBase(); 
	    }
	}

	public boolean checkDataBase(){
	    File databaseFile = new File(DB_PATH + DATABASE_NAME);
	    return databaseFile.exists();        
	}

	private void copyDataBase() throws IOException{ 
	    InputStream myInput = context.getAssets().open(DATABASE_NAME); 
	    String outFileName = DB_PATH + DATABASE_NAME; 
	    OutputStream myOutput = new FileOutputStream(outFileName); 
	    byte[] buffer = new byte[1024];
	    int length;
	    while ((length = myInput.read(buffer))>0){
	        myOutput.write(buffer, 0, length);
	    }
	    myOutput.flush();
	    myOutput.close();
	    myInput.close(); 
	}

	public void openDataBase() throws SQLException{      
	    String myPath = DB_PATH + DATABASE_NAME;
	    sqliteDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);  
	}

	@Override
	public synchronized void close() { 
	    if(sqliteDataBase != null)
	        sqliteDataBase.close(); 
	    super.close(); 
	}
	
	public Words getWords(int no){
		Words w=new Words();
		String id=String.valueOf(no);
	    String query = "select word, hint From "+TABLE_NAME+" where wordno=?";
	    Cursor cursor = sqliteDataBase.rawQuery(query, new String[] {id});
	    if(cursor.getCount()>0){
	        if(cursor.moveToFirst()){
	                w.setWord(cursor.getString(0));
	                w.setHint(cursor.getString(1));  
	        }
	    }
	    return w;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
