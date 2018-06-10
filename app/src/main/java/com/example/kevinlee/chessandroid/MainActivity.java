package com.example.kevinlee.chessandroid;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

import com.example.kevinlee.chessandroid.src.InternalStorage;
import com.example.kevinlee.chessandroid.src.board.Board;

public class MainActivity extends AppCompatActivity {

    Button btnNewGame, btnQuit, btnHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNewGame = (Button)findViewById(R.id.gameStart);
        btnQuit = (Button)findViewById(R.id.quit);
        btnHistory  = (Button)findViewById(R.id.listGame);

        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newGame = new Intent(MainActivity.this, GameActivity.class);
                startActivity(newGame);
            }
        });


        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.finish();
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newGame = new Intent(MainActivity.this, ReplayActivity.class);
                startActivity(newGame);
            }
        });

    }

}
