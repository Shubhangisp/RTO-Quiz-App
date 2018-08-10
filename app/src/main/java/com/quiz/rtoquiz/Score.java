package com.quiz.rtoquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.quiz.rtoquiz.Common.CustomAdapter;
import com.quiz.rtoquiz.DbHelper.Dbhelper;
import com.quiz.rtoquiz.Model.Ranking;

import java.util.List;

public class Score extends AppCompatActivity {

    ListView lstView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        lstView = (ListView)findViewById(R.id.lstRanking);
        Dbhelper db = new Dbhelper(this);
        List<Ranking> lstRanking = db.getRanking();

        if(lstRanking.size() > 0)
        {

            CustomAdapter adapter = new CustomAdapter(this,lstRanking);
            lstView.setAdapter(adapter);

        }



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Score.this,MainActivity.class));
        finish();
    }
}
