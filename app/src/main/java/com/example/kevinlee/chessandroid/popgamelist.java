package com.example.kevinlee.chessandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.util.Log;

import java.io.FilenameFilter;

import com.example.kevinlee.chessandroid.src.InternalStorage;
import com.example.kevinlee.chessandroid.src.board.BoardList;
import com.example.kevinlee.chessandroid.src.board.Board;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class popgamelist extends Activity {
    private static final String TAG = "popgamelist";

    ListView lv ;
    Button sortTitle, sortDate, exit;

    List<BoardList> bl;
    String [] currFile;

    File [] files;

    File dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popgamelist);

        exit = (Button) findViewById(R.id.exit);

        lv = (ListView) findViewById(R.id.listOfGame);
        sortTitle = (Button) findViewById(R.id.byTitle);
        sortDate = (Button) findViewById(R.id.byDate);

        dir = this.getFilesDir();
        Log.i(TAG, "Dir:"+dir);

        files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".chess");
            }
        });

        bl = new ArrayList<BoardList>();

        for(File f : files){
            Log.i(TAG, "FILE:"+f);
            BoardList b = InternalStorage.getInstance().load(f, this);
            if(b != null){
                b.setFile(f);
                bl.add(b);
            }
        }

        currFile = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            Log.i(TAG, "File:"+bl.get(i).getTitle());
            currFile[i] = bl.get(i).getTitle();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.item,
                currFile
        );
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {

                Intent returnIntent = new Intent();

                File finalF = null;
                for(BoardList l : bl){
                    Log.i(TAG, "CURRFILE" + currFile[position] + "and l.getTitle():"+l.getTitle());
                    if(l.getTitle().equals(currFile[position])){
                        Log.i(TAG, "SAME"+l.getTitle());
                        finalF = l.getFile();
                        Log.i(TAG, "SAME"+l.getFile());

                    }
                }

                Log.i(TAG, "THE STRING:"+finalF);
                returnIntent.putExtra("resultFile", finalF);

                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("resultFile", "");

                setResult(RESULT_CANCELED,returnIntent);
                finish();
            }
        });

        sortDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(bl, COMPARATORDATE);

                currFile = new String[files.length];
                for (int i = 0; i < files.length; i++) {
                    Log.i(TAG, "File:"+bl.get(i).getTitle());
                    currFile[i] = bl.get(i).getTitle();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        popgamelist.this,
                        R.layout.item,
                        currFile
                );

                lv.setAdapter(adapter);
            }
        });

        sortTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(bl, COMPARATORSTRING);

                currFile = new String[files.length];
                for (int i = 0; i < files.length; i++) {
                    Log.i(TAG, "File:"+bl.get(i).getTitle());
                    currFile[i] = bl.get(i).getTitle();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        popgamelist.this,
                        R.layout.item,
                        currFile
                );

                lv.setAdapter(adapter);
            }
        });
    }
    private static Comparator<BoardList> COMPARATORSTRING = new Comparator<BoardList>()  {
        public int compare(BoardList o1, BoardList o2)
        {
            return o1.getTitle().compareTo(o2.getTitle());
        }
    };

    private static Comparator<BoardList> COMPARATORDATE = new Comparator<BoardList>()  {
        public int compare(BoardList o1, BoardList o2)
        {
            return o1.getDate().compareTo(o2.getDate());
        }
    };

}

