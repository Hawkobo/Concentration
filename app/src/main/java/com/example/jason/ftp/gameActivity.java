package com.example.jason.ftp;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class gameActivity extends AppCompatActivity
{

    private int ROW_COUNT;
    private int COL_COUNT;
    private Context context;
    private Drawable backImage;
    private int[][] cards;
    private boolean[][] revealedCards;
    private List<Drawable> images;
    private Card firstCard;
    private Card secondCard;
    private ButtonListener buttonListener;
    private View[][] buttons;
    private TableRow[] buttonRows;

    private static Object lock = new Object();

    private int numWords;
    private TableLayout mainTable;
    private UpdateCardsHandler handler;
    private int score;
    private int name;

    @Override
    public void onBackPressed() {
        Intent i = new Intent(gameActivity.this, Manager.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        //Log.i("NUMWORDS IS ", String.valueOf(getIntent().getIntExtra("numwords", 0)));
        numWords = getIntent().getIntExtra("numWords", 10);
        score = getIntent().getIntExtra("score", 0);

        handler = new UpdateCardsHandler();
        loadImages();
        backImage = getResources().getDrawable(R.drawable.icon);

        switch(numWords)
        {
            case 2:
                newGame(2, 2);
                break;
            case 3:
                newGame(2, 3);
                break;
            case 4:
                newGame(2, 4);
                break;
            case 5:
                newGame(5, 2);
                break;
            case 6:
                newGame(3, 4);
                break;
            case 7:
                newGame(7, 2);
                break;
            case 8:
                newGame(4, 4);
                break;
            case 9:
                newGame(6, 3);
                break;
            case 10:
                newGame(5, 4);
                break;
            default:
                break;
        }

        ((Button) findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if(firstCard!=null & secondCard !=null) {
                    buttons[firstCard.x][firstCard.y].setBackgroundDrawable(backImage);
                    buttons[secondCard.x][secondCard.y].setBackgroundDrawable(backImage);
                    firstCard = null;
                    secondCard = null;
                }

            }


        });

        ((Button)findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                DialogFragment dialog = new PlayDialogFragment();

                dialog.show(getFragmentManager(), "play");
            }
        });

        ((Button) findViewById(R.id.button3)).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(gameActivity.this, scoreScreenActivity.class);
                i.putExtra("numWords", numWords);
                i.putExtra("score", score);
                startActivity(i);

            }


        });

    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putSerializable("cards", cards);
        savedInstanceState.putSerializable("revealedCards", revealedCards);
        savedInstanceState.putSerializable("firstCard", firstCard);
        savedInstanceState.putSerializable("secondCard", secondCard);
        //savedInstanceState.putSerializable("buttons", buttons);

        //for(int i = 0; i < buttonRows.length; i++)
        //{
        //    buttonRows[i].removeAllViews();
        //}

        savedInstanceState.putInt("score", score);
        Log.d("The score is ", "" + score);
        Log.i("THE INSTANCE ", "HAS BEEN SAVED");

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        cards = (int[][]) savedInstanceState.getSerializable("cards");
        revealedCards = (boolean[][]) savedInstanceState.getSerializable("revealedCards");
        //buttons = (View[][]) savedInstanceState.getSerializable("buttons");

        //mainTable.removeAllViews();

        //for(int i = 0; i < buttons.length; i++)
        //{
        //    buttonRows[i] = new TableRow(context);
        //    Log.d("Parent is ", "" + buttons[0][0].getParent());
        //    buttonRows[i].setHorizontalGravity(Gravity.CENTER);

        //    for(int j = 0; j < buttons[i].length; j++)
        //    {
        //        buttonRows[i].addView(buttons[i][j]);
        //    }
        //    mainTable.addView(buttonRows[i]);
        //}

        for(int i = 0; i < buttons.length; i++)
        {
            for(int j = 0; j < buttons[i].length; j++)
            {
                if(revealedCards[i][j] == true)
                {
                    buttons[i][j].setClickable(false);
                    buttons[i][j].setBackgroundDrawable(images.get((cards[i][j])));
                }
            }
        }

        firstCard = (Card) savedInstanceState.getSerializable("firstCard");
        if(firstCard != null)
        {
            buttons[firstCard.x][firstCard.y].setBackgroundDrawable(images.get(cards[firstCard.x][firstCard.y]));
        }

        secondCard = (Card) savedInstanceState.getSerializable("secondCard");
        if(secondCard != null)
        {
            buttons[secondCard.x][secondCard.y].setBackgroundDrawable(images.get(cards[secondCard.x][secondCard.y]));
        }

        score = savedInstanceState.getInt("score");
        ((TextView) findViewById(R.id.tv1)).setText("Score: " + score);
        Log.d("The score is ", "" + score);
        Log.i("THE INSTANCE ", "HAS BEEN LOADED");
    }


    public void newGame(int c, int r)
    {


        ROW_COUNT = r;
        COL_COUNT = c;

        setContentView(R.layout.game);
        buttonListener = new ButtonListener();

        mainTable = (TableLayout) findViewById(R.id.TableLayout03);


        context = mainTable.getContext();

        cards = new int[COL_COUNT][ROW_COUNT];
        revealedCards = new boolean[COL_COUNT][ROW_COUNT];
        buttons = new View[COL_COUNT][ROW_COUNT];
        buttonRows = new TableRow[COL_COUNT];

        TableRow tr = ((TableRow) findViewById(R.id.TableRow03));
        tr.removeAllViews();

        mainTable = new TableLayout(context);
        tr.addView(mainTable);

        for (int y = 0; y < ROW_COUNT; y++)
        {
            mainTable.addView(createRow(y));
        }

        //mainTable.removeAllViewsInLayout();

        for(int i = 0; i < buttons.length; i++)
        {
            buttonRows[i] = new TableRow(context);
            Log.d("Parent is ", "" + buttons[0][0].getParent());
            buttonRows[i].setHorizontalGravity(Gravity.CENTER);

            for(int j = 0; j < buttons[i].length; j++)
            {
                buttonRows[i].addView(buttons[i][j]);
            }
            mainTable.addView(buttonRows[i]);
        }

        firstCard = null;
        loadCards();

        ((TextView) findViewById(R.id.tv1)).setText("Score: " + score);


    }

    private void loadImages()
    {
        images = new ArrayList<Drawable>();

        images.add(getResources().getDrawable(R.drawable.card1));
        images.add(getResources().getDrawable(R.drawable.card2));
        images.add(getResources().getDrawable(R.drawable.card3));
        images.add(getResources().getDrawable(R.drawable.card4));
        images.add(getResources().getDrawable(R.drawable.card5));
        images.add(getResources().getDrawable(R.drawable.card6));
        images.add(getResources().getDrawable(R.drawable.card7));
        images.add(getResources().getDrawable(R.drawable.card8));
        images.add(getResources().getDrawable(R.drawable.card9));
        images.add(getResources().getDrawable(R.drawable.card10));
        images.add(getResources().getDrawable(R.drawable.card11));
        images.add(getResources().getDrawable(R.drawable.card12));
        images.add(getResources().getDrawable(R.drawable.card13));
        images.add(getResources().getDrawable(R.drawable.card14));
        images.add(getResources().getDrawable(R.drawable.card15));
        images.add(getResources().getDrawable(R.drawable.card16));
        images.add(getResources().getDrawable(R.drawable.card17));
        images.add(getResources().getDrawable(R.drawable.card18));
        images.add(getResources().getDrawable(R.drawable.card19));
        images.add(getResources().getDrawable(R.drawable.card20));
        images.add(getResources().getDrawable(R.drawable.card21));

    }

    private void loadCards()
    {
        try
        {
            int size = ROW_COUNT * COL_COUNT;

            Log.i("loadCards()", "size=" + size);

            ArrayList<Integer> list = new ArrayList<>();

            for (int i = 0; i < size; i++)
            {
                list.add(new Integer(i));
            }


            Random r = new Random();

            for (int i = size - 1; i >= 0; i--)
            {
                int t = 0;

                if (i > 0)
                {
                    t = r.nextInt(i);
                }

                t = list.remove(t).intValue();
                cards[i % COL_COUNT][i / COL_COUNT] = t % (size / 2);
                revealedCards[i % COL_COUNT][i / COL_COUNT] = false;

                Log.i("loadCards()", "card[" + (i % COL_COUNT) +
                        "][" + (i / COL_COUNT) + "]=" + cards[i % COL_COUNT][i / COL_COUNT]);
            }
        }
        catch (Exception e)
        {
            Log.e("loadCards()", e + "");
        }

    }

    private TableRow createRow(int y)
    {
        TableRow row = new TableRow(context);
        row.setHorizontalGravity(Gravity.CENTER);

        for (int x = 0; x < COL_COUNT; x++)
        {
            row.addView(createImageButton(x, y));
        }
        row.removeAllViews();
        return row;
    }

    private View createImageButton(int x, int y)
    {
        Button button = new Button(context);
        button.setBackgroundDrawable(backImage);
        button.setId(100 * x + y);
        button.setOnClickListener(buttonListener);
        buttons[x][y] = button;

        return button;
    }

    class ButtonListener implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {

            synchronized (lock)
            {
                if (firstCard != null && secondCard != null)
                {
                    return;
                }
                int id = v.getId();
                int x = id / 100;
                int y = id % 100;
                turnCard((Button) v, x, y);
            }

        }

        private void turnCard(Button button, int x, int y)
        {
            if (firstCard == null)
            {
                firstCard = new Card(button, x, y);
                button.setBackgroundDrawable(images.get(cards[x][y]));
            }
            else
            {

                if (firstCard.x == x && firstCard.y == y)
                {
                    return; //the user pressed the same card
                }

                secondCard = new Card(button, x, y);
                revealedCards[x][y] = true;
                button.setBackgroundDrawable(images.get(cards[x][y]));

                //turns++;
                //((TextView)findViewById(R.id.tv1)).setText("Score: "+score);

                //turns++;
                //((TextView) findViewById(R.id.tv1)).setText("Score: " + score);


                TimerTask tt = new TimerTask()
                {

                    @Override
                    public void run()
                    {
                        try
                        {
                            synchronized (lock)
                            {
                                handler.sendEmptyMessage(0);
                            }
                        }
                        catch (Exception e)
                        {
                            Log.e("E1", e.getMessage());
                        }
                    }
                };

                Timer t = new Timer(false);
                t.schedule(tt, 500);
            }


        }

    }

    class UpdateCardsHandler extends Handler
    {

        @Override
        public void handleMessage(Message msg)
        {
            synchronized (lock)
            {
                checkCards();
            }
        }

        public void checkCards()
        {
            boolean gameOver = true;
            if(firstCard !=null && secondCard !=null) {
                if (cards[firstCard.x][firstCard.y] == cards[secondCard.x][secondCard.y]) {
                    score += 2;
                    ((TextView) findViewById(R.id.tv1)).setText("Score: " + score);

                    //firstCard.button.setVisibility(View.INVISIBLE);
                    buttons[firstCard.x][firstCard.y].setClickable(false);
                    revealedCards[firstCard.x][firstCard.y] = true;

                    //secondCard.button.setVisibility(View.INVISIBLE);
                    buttons[secondCard.x][secondCard.y].setClickable(false);
                    revealedCards[secondCard.x][secondCard.y] = true;
                    firstCard = null;
                    secondCard = null;
                } else {
                    if (score > 0) {
                        score--;
                        ((TextView) findViewById(R.id.tv1)).setText("Score: " + score);
                    }

                    //buttons[firstCard.x][firstCard.y].setBackgroundDrawable(backImage);
                    revealedCards[firstCard.x][firstCard.y] = false;

                    //buttons[secondCard.x][secondCard.y].setBackgroundDrawable(backImage);
                    revealedCards[secondCard.x][secondCard.y] = false;

                }
            }



            for (int i = 0; i < revealedCards.length; i++)
            {
                for (int j = 0; j < revealedCards[i].length; j++)
                {
                    if (!revealedCards[i][j])
                    {
                        gameOver = false;
                    }
                }
            }

            if (gameOver)
            {
                Intent i = new Intent(gameActivity.this,scoreScreenActivity.class);
                i.putExtra("score", score);
                startActivity(i);
            }
        }
    }
}