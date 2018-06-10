package com.example.kevinlee.chessandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class popgame extends Activity {

    EditText gameTitle;
    Button noTitle, save;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.popgamewindow);

        noTitle = (Button) findViewById(R.id.noTitle);
        save = (Button) findViewById(R.id.save);
        gameTitle = (EditText) findViewById(R.id.gameTitle);

        DisplayMetrics dm = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.5), (int)(height*0.5));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                String title = gameTitle.getText().toString();
                if(title.equals("")){
                    return;
                }

                returnIntent.putExtra("titleResult",title);

                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });

        noTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                String result = "-1";
                returnIntent.putExtra("titleResult",result);

                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });
    }
}
