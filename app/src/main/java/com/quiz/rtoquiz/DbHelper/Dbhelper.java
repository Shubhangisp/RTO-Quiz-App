package com.quiz.rtoquiz.DbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.quiz.rtoquiz.Common.Common;
import com.quiz.rtoquiz.Model.Question;
import com.quiz.rtoquiz.Model.Ranking;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Dbhelper extends SQLiteOpenHelper{
    //private static SQLiteDatabase sqliteDb;

   // private static final int DATABASE_VERSION = 2;

    private static String DB_NAME="MyDB.db";
    //private static String DB_PATH = "/data/data/com.quiz.rtoquiz/databases/";
    private static String DB_PATH = "";

    private static String TABLE_NAME="Question";
    private SQLiteDatabase mDatabase;
    private Context mContext = null;

    //private boolean isDBExists;//shubham pise modification

    public Dbhelper(Context context) {

        super(context, DB_NAME,null, 2);

            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";

            Log.e("DBPATH",DB_PATH);

            //openDatabase();
        this.mContext = context;
    }

    public void  openDatabase(){
        String myPath = DB_PATH+DB_NAME;
        mDatabase = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);

    }

    public void copyDatabase() throws IOException{
        try{
            InputStream myInput = mContext.getAssets().open(DB_NAME);
            String outputFilename = DB_PATH+DB_NAME;
            OutputStream myOutput = new FileOutputStream(outputFilename);

            byte[] buffer = new byte[1024];
            int length;
            while((length = myInput.read(buffer)) > 0)
                myOutput.write(buffer,0,length);
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean checkDatabase(){
        SQLiteDatabase tempDB = null;
        try {
            String myPath = DB_PATH+DB_NAME;
            tempDB = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        if(tempDB != null)
            tempDB.close();
        return tempDB!=null?true:false;
    }

    public void createDataBase() throws IOException{
        boolean isDBExists = checkDatabase();

        if(isDBExists)
        {

        }
    else {
        this.getReadableDatabase();
        try{
            copyDatabase();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        }
    }

    @Override
    public synchronized void close()
    {
        if(mDatabase != null)
            mDatabase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public List<Question> getAllQuestion(){
    List<Question>  listQuestion = new ArrayList<>();
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor c;
    try{
         c=db.rawQuery(String.format("SELECT * FROM Question ORDER BY Random()"),null);
         if(c==null) return null;
         c.moveToFirst();
         do{
            int Id=c.getInt(c.getColumnIndex("ID"));
            String Image=c.getString(c.getColumnIndex("Image"));
            String AnswerA=c.getString(c.getColumnIndex("AnswerA"));
            String AnswerB=c.getString(c.getColumnIndex("AnswerB"));
            String AnswerC=c.getString(c.getColumnIndex("AnswerC"));
            String AnswerD=c.getString(c.getColumnIndex("AnswerD"));
            String CorrectAnswer=c.getString(c.getColumnIndex("CorrectAnswer"));

            Question question = new Question(Id,Image,AnswerA,AnswerB,AnswerC,AnswerD,CorrectAnswer);
            listQuestion.add(question);
            }
            while (c.moveToNext());
         c.close();
        }
       catch (Exception e){
        e.printStackTrace();
         }
    db.close();
    return listQuestion;
    }

    public List<Question> getQuestionMode(String mode){
        List<Question>  listQuestion = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        int limit = 0;
        if(mode.equals(Common.MODE.EASY.toString()))
            limit = 10;
        else if(mode.equals(Common.MODE.MEDIUM.toString()))
            limit = 20;
        else  if(mode.equals(Common.MODE.HARD.toString()))
            limit = 30;
        else  if(mode.equals(Common.MODE.HARDEST.toString()))
            limit = 50;
        try{
            c=db.rawQuery(String.format("SELECT * FROM Question ORDER BY Random() LIMIT %d",limit),null);
            if(c==null) return null;
            c.moveToFirst();
            do{
                int Id=c.getInt(c.getColumnIndex("ID"));
                String Image=c.getString(c.getColumnIndex("Image"));
                String AnswerA=c.getString(c.getColumnIndex("AnswerA"));
                String AnswerB=c.getString(c.getColumnIndex("AnswerB"));
                String AnswerC=c.getString(c.getColumnIndex("AnswerC"));
                String AnswerD=c.getString(c.getColumnIndex("AnswerD"));
                String CorrectAnswer=c.getString(c.getColumnIndex("CorrectAnswer"));

                Question question = new Question(Id,Image,AnswerA,AnswerB,AnswerC,AnswerD,CorrectAnswer);
                listQuestion.add(question);
            }
            while (c.moveToNext());
            c.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        db.close();
        return listQuestion;
    }


    public void insertScore(int Score){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Score",Score);
        db.insert("Ranking",null,contentValues);
    }

    public List<Ranking> getRanking(){
        List<Ranking> listRanking = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        try{
            c=db.rawQuery(String.format("SELECT * FROM Ranking ORDER BY Score DESC LIMIT 10;"),null);
            if(c==null)return null;
            c.moveToNext();
            do {
                int Id = c.getInt(c.getColumnIndex("Id"));
                int Score = c.getInt(c.getColumnIndex("Score"));

                Ranking ranking = new Ranking(Id,Score);
                listRanking.add(ranking);
            }
            while(c.moveToNext());
            c.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        db.close();
        return listRanking;
    }
}
