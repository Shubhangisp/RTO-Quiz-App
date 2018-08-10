package com.quiz.rtoquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quiz.rtoquiz.DbHelper.Dbhelper;

public class Done extends AppCompatActivity {

    Button btnTryAgain;
    TextView txtTotalScore,txtTotalQuestion;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        Dbhelper db = new Dbhelper(this);

        btnTryAgain = (Button)findViewById(R.id.btnTryAgain);
        txtTotalQuestion = (TextView)findViewById(R.id.txtTotalQuestion);
        txtTotalScore = (TextView)findViewById(R.id.txtTotalScore);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Bundle extra = getIntent().getExtras();

        if(extra != null) {
            int score = extra.getInt("SCORE");
            int totalQuestion = extra.getInt("TOTAL");
            int correctAnswer = extra.getInt("CORRECT");
            txtTotalScore.setText(String.format("SCORE : %d", score));
            txtTotalQuestion.setText(String.format("CORRECT : %d/%d", correctAnswer, totalQuestion));

            // progressBar.setMax(totalQuestion);
            //progressBar.setProgress(correctAnswer);

            db.insertScore(score);
        }


        }
}
