package com.example.kevinlee.chessandroid;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
//import android.support.v7.widget.AppCompatTextView

import com.example.kevinlee.chessandroid.src.InternalStorage;
import com.example.kevinlee.chessandroid.src.board.Board;
import com.example.kevinlee.chessandroid.src.board.BoardList;
import com.example.kevinlee.chessandroid.src.piece.Piece;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class ReplayActivity extends AppCompatActivity {
    private static final String TAG = "ReplayActivity";
    private static final int FIRST_ACTIVITY_REQUEST_CODE = 1;

    public Button prev, next, pickGame, toMenu;
    public TextView messageReplay;

    BoardList curr;

    public Piece[][] pBoard;
    public GridView boardView;
    public GridAdapter adapter;
    public Board board;
    BoardList b;

    int index ;

    private void whatMessage(Board boardN) { //SET THE BOARD
//        printPiece(boardN.getBoard());
        if (boardN.getTurn() == 'w') {
            //White's turn
            if (boardN.checkmate()) {
                messageReplay.setText("Checkmate, White Wins");
                return;
            }
            if (boardN.check()) {
                messageReplay.setText("White Is In Check");
                return;
            }
            messageReplay.setText("White's Turn");
        } else {
            //Black's turn
            if (boardN.checkmate()) {
                messageReplay.setText("Checkmate, Black Wins");
                return;
            }
            if (boardN.check()) {
                messageReplay.setText("Black Is In Check");
                return;
            }
            messageReplay.setText("Black's Turn");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);

        //Initalize All Buttons
        prev = (Button) findViewById(R.id.prev);
        next = (Button) findViewById(R.id.next);
        pickGame = (Button) findViewById(R.id.pick);
        toMenu = (Button) findViewById(R.id.toMenu);

        boardView = (GridView) findViewById(R.id.gridviewReplay);

        messageReplay = (TextView) findViewById(R.id.messageReplay);
        //Done initalizing

        ReplayActivity activity = ReplayActivity.this;


        //Getting the list of BoardList
        File dir = this.getFilesDir();
        Log.i(TAG, "Dir:"+dir);

        File [] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".chess");
            }
        });

        List<BoardList> bl = new ArrayList<BoardList>();

        for(File f : files){
            Log.i(TAG, "FILE:"+f);
            BoardList b = InternalStorage.getInstance().load(f, this);
            if(b != null){
                b.setFile(f);
                bl.add(b);
            }
//            if(f.delete()){
//                Log.i(TAG, "DELETED");
//            }
        }

        if(bl.size() != 0){
            curr  = bl.get(0);
        }

        index = 0;

        if(curr != null){
            board = bl.get(0).getBoard(0);
            whatMessage(board);
            pBoard = board.getBoard();
            adapter = new GridAdapter(this, this.pBoard);
            boardView.setAdapter(adapter);
            adapter.setData(pBoard);
        }

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tempIndex = index - 1;

                if(curr == null){
                    return;
                }

                if ((tempIndex) >= 0){
//                    Log.i(TAG,"Previous Index:" + tempIndex);
                    //Now minus one...
                    board = curr.getBoard(tempIndex);
                    pBoard = board.getBoard();

                    whatMessage(board);

                    boardView.setAdapter(adapter);
                    adapter.setData(pBoard);
                    index = tempIndex;
                }
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tempIndex = index + 1;

                if(curr == null){
                    return;
                }

//                Log.i(TAG, "Size:"+ curr.getList().size());
                if ((tempIndex) < curr.getList().size()){
//                    Log.i(TAG,"Next Index:" + tempIndex);
                    //Now plus one...
                    board = curr.getBoard(tempIndex);
                    pBoard = board.getBoard();

                    whatMessage(board);

                    boardView.setAdapter(adapter);
                    adapter.setData(pBoard);
                    index = tempIndex;
                }
            }
        });

        pickGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(ReplayActivity.this, popgamelist.class);

                startActivityForResult(x, FIRST_ACTIVITY_REQUEST_CODE);
            }
        });

        toMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReplayActivity activity = ReplayActivity.this;
                activity.finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ReplayActivity activity = ReplayActivity.this;

        if(resultCode == RESULT_OK){
            if (requestCode == FIRST_ACTIVITY_REQUEST_CODE){
                File retFile = (File)data.getExtras().get("resultFile");
                Log.i(TAG, "Return String:"+ retFile.getPath());

                curr = InternalStorage.getInstance().load(retFile, this);

                Log.i(TAG, "TITLE:"+curr.getTitle());

                pBoard = curr.getBoard(0).getBoard();
                index = 0;

                whatMessage(curr.getBoard(0));

                boardView.setAdapter(adapter);
                adapter.setData(pBoard);
            }
        } else if (resultCode == RESULT_CANCELED){
            //DO NOTHING
        }

    }

}
