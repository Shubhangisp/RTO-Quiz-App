package com.quiz.rtoquiz;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.quiz.rtoquiz.DbHelper.Dbhelper;
import com.quiz.rtoquiz.Model.Question;

import java.util.ArrayList;
import java.util.List;

public class Playing extends AppCompatActivity implements View.OnClickListener {

    final static long INTERVAL = 1500;
    final static long TIMEOUT = 30000;
    int progressValue = 0;

    CountDownTimer mCountDown;
    List<Question> questionPlay = new ArrayList<>();
    Dbhelper db;
    int index=0,score=0,thisQuestion=1,totalQuestion,correctAnswer;


    String mode="";

    ProgressBar progressBar;
    ImageView imageView;
    Button btnA,btnB,btnC,btnD;
    TextView txtScore;
    TextView txtQuestion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        Bundle extra = getIntent().getExtras();

        if(extra != null)
            mode=extra.getString("MODE");


        db = new Dbhelper(this);

        txtScore = (TextView)findViewById(R.id.txtScore);
        txtQuestion = (TextView)findViewById(R.id.txtQuestion);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        imageView = (ImageView)findViewById(R.id.question_flag);
        btnA=(Button)findViewById(R.id.btnAnswerA);
        btnB=(Button)findViewById(R.id.btnAnswerB);
        btnC=(Button)findViewById(R.id.btnAnswerC);
        btnD=(Button)findViewById(R.id.btnAnswerD);


        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);



    }

    @Override
    protected void onResume() {
        super.onResume();

        questionPlay = db.getQuestionMode(mode);
        totalQuestion = questionPlay.size();

        mCountDown = new CountDownTimer(TIMEOUT,INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {

                progressBar.setProgress(progressValue);

                progressValue++;

            }

            @Override
            public void onFinish() {


                mCountDown.cancel();

                showQuestion(++index);

            }
        };

        showQuestion(index);

    }

    private void showQuestion(int index) {

        if(index < totalQuestion)
        {

            txtQuestion.setText(String.format("%d/%d",thisQuestion,totalQuestion));

            thisQuestion++;


            progressBar.setProgress(0);

            progressValue = 0;



            int ImageId=this.getResources().getIdentifier(questionPlay.get(index).getImage().toLowerCase(),"drawable",getPackageName());

            imageView.setBackgroundResource(ImageId);


            btnA.setText(questionPlay.get(index).getAnswerA());
            btnB.setText(questionPlay.get(index).getAnswerB());
            btnC.setText(questionPlay.get(index).getAnswerC());
            btnD.setText(questionPlay.get(index).getAnswerD());

            mCountDown.start();



        }

        else
        {
            //Intent intent=new Intent(this,Done.class);
            Intent intent = new Intent(getApplicationContext(),Done.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("SCORE",score);
            dataSend.putInt("TOTAL",totalQuestion);
            dataSend.putInt("CORRECT",correctAnswer);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {

        mCountDown.cancel();
        if(index < totalQuestion)
        {

            Button clickedButton = (Button)v;

            if(clickedButton.getText().equals(questionPlay.get(index).getCorrectAnswer()))
            {

                score+=5;
                correctAnswer++ ;
                showQuestion(++index);

            }
            else

                showQuestion(++index);





            txtScore.setText(String.format("%d",score));


        }

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 4000);

    }
}
