package com.example.kevinlee.chessandroid;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.view.View;

import android.util.Log;
import android.widget.Toast;

import com.example.kevinlee.chessandroid.src.InternalStorage;
import com.example.kevinlee.chessandroid.src.board.BoardList;
import com.example.kevinlee.chessandroid.src.board.Board;
import com.example.kevinlee.chessandroid.src.board.GameList;
import com.example.kevinlee.chessandroid.src.piece.Piece;

import java.io.File;
import java.io.ObjectOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.lang.reflect.Array;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    private static final int FIRST_ACTIVITY_REQUEST_CODE = 1;
    private static final int THIRD_ACTIVITY_REQUEST_CODE = 2;

    //    public GridAdapter adapter;
    public GridView boardView;
    public GridAdapter adapter;
    public View v2[];
    public int pos2[];

    String choice = "";

    String moveM = "";

    public TextView message;

    public Board board;

    public BoardList boardList;
    String titleString = "";

    int index;

    Piece[][] pBoard;

    public Button undo, ai, draw, resign, toMenu, exit;

    private void resetColor(int x, int y, int e) {
        if (x % 2 == 0) {
            if (y % 2 == 0) {
                v2[e].setBackgroundColor(Color.parseColor("#513414"));
            } else {
                v2[e].setBackgroundColor(Color.parseColor("#d6a671"));
            }
        } else {
            if (y % 2 == 1) {
                v2[e].setBackgroundColor(Color.parseColor("#513414"));
            } else {
                v2[e].setBackgroundColor(Color.parseColor("#d6a671"));
            }
        }
    }

    private void whatMessage(Board boardN, GameActivity activity) { //SET THE BOARD
//        printPiece(boardN.getBoard());
        if (boardN.getTurn() == 'w') {
            //White's turn
            if (boardN.checkmate()) {
                message.setText("Checkmate, White Wins");
                pauseButton(activity);
                activity.boardView.setEnabled(false);

                Intent x = new Intent(GameActivity.this, popgame.class);

                startActivityForResult(x, FIRST_ACTIVITY_REQUEST_CODE);
                return;
            }
            if (boardN.check()) {
                message.setText("White Is In Check");
                return;
            }
            message.setText("White's Turn");
        } else {
            //Black's turn
            if (boardN.checkmate()) {
                message.setText("Checkmate, Black Wins");
                pauseButton(activity);
                activity.boardView.setEnabled(false);

                Intent x = new Intent(GameActivity.this, popgame.class);

                startActivityForResult(x, FIRST_ACTIVITY_REQUEST_CODE);
                return;
            }
            if (boardN.check()) {
                message.setText("Black Is In Check");
                return;
            }
            message.setText("Black's Turn");
        }
    }

    private void pauseButton(GameActivity activity) {
        activity.undo.setEnabled(false);
        activity.ai.setEnabled(false);
        activity.draw.setEnabled(false);
        activity.resign.setEnabled(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        this.board = new Board();
        this.boardList = new BoardList();
        index = 0;
        Board x = new Board();
        boardList.addBoard(0, x);

        this.pBoard = board.getBoard();
        v2 = new View[2];
        pos2 = new int[2];

        //Initalize All Buttons
        message = (TextView) findViewById(R.id.messageID);

        undo = (Button) findViewById(R.id.undoID);
        ai = (Button) findViewById(R.id.aiID);
        draw = (Button) findViewById(R.id.drawID);
        resign = (Button) findViewById(R.id.resignID);
        toMenu = (Button) findViewById(R.id.menuID);
        exit = (Button) findViewById(R.id.quitToMenu);

        boardView = (GridView) findViewById(R.id.gridview);
        //Done initalizing

//        boardView.setAdapter(new CustomAdapter(this));
        adapter = new GridAdapter(this, this.pBoard);
        boardView.setAdapter(adapter);
        adapter.setData(board.getBoard());
        GameActivity activity = GameActivity.this;

//        board = loadSerializedObject(new File("data/data.ser"));

        if (board.getTurn() == 'w') {
            //White's turn
            message.setText("White's Turn");
        } else {
            //Black's turn
            message.setText("Black's Turn");
        }

        //Ok, so this is just for normal moving, right? Cool, just making sure
        boardView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {
                GameActivity activity = GameActivity.this;
                Board currentBoard = activity.board;
                GridAdapter adapter = activity.adapter;

                pBoard = currentBoard.getBoard();

                if (v2[0] != null) {
                    v2[1] = v;
                    pos2[1] = position;
                    v.setBackgroundColor(Color.parseColor("#2575dd"));
                } else {
                    v2[0] = v;
                    pos2[0] = position;
                    v.setBackgroundColor(Color.parseColor("#25DF7B"));

                }
                int startX = 0, startY = 0, endX = 0, endY = 0;

                //Put the ai/draw/everything here in the if statement
                if (v2[1] != null) {
                    int start = pos2[0];
                    startX = (start % 8);
                    startY = 7 - (start / 8);

                    int end = pos2[1];
                    endX = (end % 8);
                    endY = 7 - (end / 8);

                    resetColor(startX, startY, 0);
                    resetColor(endX, endY, 1);

                    //Cool, now we should get everything from chess

                    moveM = "" + startX + startY + endX + endY;

                    if(checkPromotion(pBoard, currentBoard.getTurn(), startX, startY, endX, endY)){

                    } else {
                        Log.i(TAG,"SHOULD NOT APPEAR:"+moveM);

                        if (!currentBoard.takeTurn(moveM)) {
//                        Log.i(TAG, "Didn't move...");
                            Toast.makeText(GameActivity.this, "Invalid Movement", Toast.LENGTH_SHORT).show();
                        } else {
                            pBoard = currentBoard.getBoard();

                            adapter.setData(pBoard);
                            boardView.setAdapter(adapter);

                            String moveU = board.intToRealMove(moveM);

                            board.addMove(moveU);

                            index++;
                            Board txt = new Board(board);
                            boardList.addBoard(index, txt);

                            activity.undo.setEnabled(true);

                            whatMessage(currentBoard, activity);
                        }
                        //End of the Chess.java code

                        //Resetting
                        v2 = new View[2];
                        pos2 = new int[2];
                    }



                }
            }
        });

        ai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameActivity activity = GameActivity.this;
                Board currentBoard = activity.board;
                GridAdapter adapter = activity.adapter;

                currentBoard.ai();

                index++;
                Board txt = new Board(currentBoard);
                boardList.addBoard(index, txt);

                whatMessage(currentBoard, activity);

                pBoard = currentBoard.getBoard();
                adapter.setData(pBoard);
                boardView.setAdapter(adapter);

                activity.undo.setEnabled(true);
            }
        });


        resign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameActivity activity = GameActivity.this;
                Board currentBoard = activity.board;
                GridAdapter adapter = activity.adapter;

                if (currentBoard.getTurn() == 'w') {
                    //White's turn
                    message.setText("White Resigns, Black Wins");
                    pauseButton(activity);
                } else {
                    //Black's turn
                    message.setText("Black Resigns, White Wins");
                    pauseButton(activity);
                }

                Intent x = new Intent(GameActivity.this, popgame.class);

                startActivityForResult(x, FIRST_ACTIVITY_REQUEST_CODE);
            }
        });

        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(GameActivity.this, Pop.class);

                startActivityForResult(x, SECOND_ACTIVITY_REQUEST_CODE);
            }
        });

        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameActivity activity = GameActivity.this;
                GridAdapter adapter = activity.adapter;

                int thisIndex = index - 1;

                if (thisIndex >= 0) {
                    index--;
                    Board prevBoard = boardList.getBoard(index);

                    Piece[][] print = prevBoard.getBoard();
                    adapter.setData(print);
                    boardView.setAdapter(adapter);

                    board = new Board(prevBoard);

                    activity.undo.setEnabled(false);
                }

                whatMessage(board, activity);
            }
        });

        toMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameActivity activity = GameActivity.this;

                Intent x = new Intent(GameActivity.this, popgame.class);

                startActivityForResult(x, FIRST_ACTIVITY_REQUEST_CODE);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameActivity activity = GameActivity.this;

                activity.finish();
            }
        });

    }

    private boolean checkPromotion(Piece[][] board, char turn, int startX, int startY, int endX, int endY){

        Log.i(TAG,"TURN:"+turn + " startX " + startX + startY + endX + endY);

        if(turn == 'w' && endY == 7){

            if (board[startX][startY].toString().equals("wp")) {
                // Call in Intent here
                Log.i(TAG, "PROMOTION WHITE: at 7," +"::"+ board[startX][startY].toString());
                Intent x = new Intent(GameActivity.this, popgamepromotion.class);

                startActivityForResult(x, THIRD_ACTIVITY_REQUEST_CODE);
                return true;
            }
        }

        if(turn == 'b' && endY == 0) {
            if (board[startX][startY].toString().equals("wp")) {
                // Call in Intent here
                Log.i(TAG, "PROMOTION BLACK: at 0," +"::"+ board[startX][startY].toString());
                Intent x = new Intent(GameActivity.this, popgamepromotion.class);

                startActivityForResult(x, THIRD_ACTIVITY_REQUEST_CODE);
                return true;
            }
        }
        return false;
    }

    private void printPiece(Piece[][] board) {
        for (int y = 7; y >= 0; y--) {
            String s = "";
            for (int x = 0; x < 8; x++) {

                if (board[x][y] != null) {
                    s += board[x][y].toString() + " ";
                } else {
                    s += "xx" + " ";
                }

            }
            Log.i(TAG, "" + s);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        GameActivity activity = GameActivity.this;
        Board currentBoard = activity.board;
        GridAdapter adapter = activity.adapter;

        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String returnString = data.getStringExtra("result");

                if (returnString.equals("no")) {
                    //
                } else if (returnString.equals("yes")) {
                    message.setText("Draw");
                    pauseButton(activity);
                    activity.boardView.setEnabled(false);

                    Intent y = new Intent(GameActivity.this, popgame.class);

                    startActivityForResult(y, FIRST_ACTIVITY_REQUEST_CODE);
                }
            }
        } else if (requestCode == FIRST_ACTIVITY_REQUEST_CODE) {
            titleString = data.getStringExtra("titleResult");

            Log.i(TAG, "TitleString:" + titleString);
            if (titleString.equals("-1")) {

            } else {
                boardList.setTitle(titleString);
                boardList.setTime();
                //Save Here
                InternalStorage.getInstance().save(titleString, boardList, this);

                activity.finish();
            }
        } if (requestCode == THIRD_ACTIVITY_REQUEST_CODE) {
            titleString = data.getStringExtra("promote");

            Log.i(TAG, "IN THE PROMOTION:" + moveM);

            if(resultCode == RESULT_OK){
                moveM += titleString;
            }

            if (!currentBoard.takeTurn(moveM)) {
//                        Log.i(TAG, "Didn't move...");
                Toast.makeText(GameActivity.this, "Invalid Movement", Toast.LENGTH_SHORT).show();
            } else {
                if(titleString.equals("") || resultCode == RESULT_CANCELED){
                    Toast.makeText(GameActivity.this, "Invalid Promotion", Toast.LENGTH_SHORT).show();
                }

                pBoard = currentBoard.getBoard();

                adapter.setData(pBoard);
                boardView.setAdapter(adapter);

                String moveU = board.intToRealMove(moveM);

                board.addMove(moveU);

                index++;
                Board txt = new Board(board);
                boardList.addBoard(index, txt);

                activity.undo.setEnabled(true);

                whatMessage(currentBoard, activity);
            }

            //End of the Chess.java code

            //Resetting
            v2 = new View[2];
            pos2 = new int[2];

            if (titleString != null) {
                choice = titleString;
            } else {
                choice = "";
            }
        }

    }
}
