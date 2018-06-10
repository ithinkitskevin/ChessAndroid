package com.example.kevinlee.chessandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.kevinlee.chessandroid.src.InternalStorage;
import com.example.kevinlee.chessandroid.src.board.BoardList;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class popgamepromotion extends Activity{
    Button queen, rook, bishop, knight, exit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popgamepromotion);

        queen = (Button) findViewById(R.id.queenP);
        rook = (Button) findViewById(R.id.rookP);
        bishop = (Button) findViewById(R.id.bishopP);
        knight = (Button) findViewById(R.id.knightP);
        exit = (Button) findViewById(R.id.exit);

        DisplayMetrics dm = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(dm);


        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.8), (int)(height*0.8));

        queen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("promote","Q");

                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });

        rook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("promote","R");

                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });

        bishop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("promote","B");

                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });

        knight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("promote","N");

                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("promote", "0");

                setResult(RESULT_CANCELED,returnIntent);
                finish();
            }
        });

    }

}
