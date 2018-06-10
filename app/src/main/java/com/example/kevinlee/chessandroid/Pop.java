package com.example.kevinlee.chessandroid;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class Pop extends Activity {
    private static final String TAG = "Pop";

    public Button yes, no;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.popwindow);

        yes = (Button) findViewById(R.id.yes);
        no = (Button) findViewById(R.id.no);

        DisplayMetrics dm = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.5), (int)(height*0.5));

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                String result = "yes";
                returnIntent.putExtra("result",result);

                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                String result = "no";
                returnIntent.putExtra("result",result);

                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });
    }
}
