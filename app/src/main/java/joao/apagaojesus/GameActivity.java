package joao.apagaojesus;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GameActivity extends Activity implements View.OnTouchListener{

    private ImageView rubber, to_erase;
    private ViewGroup mRrootLayout;
    private int _xDelta;
    private int _yDelta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mRrootLayout = (ViewGroup) findViewById(R.id.rootView);
        rubber = (ImageView) mRrootLayout.findViewById(R.id.rubber);
        to_erase = (ImageView) mRrootLayout.findViewById(R.id.image_to_erase);
        final TextView textview = (TextView) mRrootLayout.findViewById(R.id.textView);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(100, 100);
        rubber.setLayoutParams(layoutParams);
        rubber.setOnTouchListener(this);



        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                textview.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                textview.setText("Acabou");
                //Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
                //startActivity(intent);
                //finish();
            }
        }.start();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:   RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                                            _xDelta = X - lParams.leftMargin;
                                            _yDelta = Y - lParams.topMargin;

                                            Log.d("eraser_initial", rubber.getX() + " " + rubber.getY());
                                            Log.d("to_erase_initial", to_erase.getX() + " " + to_erase.getY());
                                            break;

            case MotionEvent.ACTION_UP:             break;

            case MotionEvent.ACTION_POINTER_DOWN:   break;

            case MotionEvent.ACTION_POINTER_UP:     break;

            case MotionEvent.ACTION_MOVE:   RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                                            layoutParams.leftMargin = X - _xDelta;
                                            layoutParams.topMargin = Y - _yDelta;
                                            layoutParams.rightMargin = -250;
                                            layoutParams.bottomMargin = -250;
                                            v.setLayoutParams(layoutParams);

                                            RelativeLayout.LayoutParams lP = (RelativeLayout.LayoutParams) to_erase.getLayoutParams();

                                            Log.d("eraser", rubber.getX() + " " + (rubber.getX() + rubber.getWidth()) + " " + rubber.getY() + " " + (rubber.getY()+rubber.getHeight()));
                                            Log.d("to_erase", to_erase.getX() + " " + (to_erase.getX() + to_erase.getWidth()) + " " + to_erase.getY() + " " + (to_erase.getY()+to_erase.getHeight()));

                                            float to_erase_x1 = to_erase.getX(), to_erase_x2 = (to_erase.getX() + to_erase.getWidth());
                                            float to_erase_y1 = to_erase.getY(), to_erase_y2 = (to_erase.getY()+to_erase.getHeight());
                                            float rubber_x1 = rubber.getX(), rubber_x2 = (rubber.getX() + rubber.getWidth());
                                            float rubber_y1 = to_erase.getY(), rubber_y2 = (to_erase.getY()+to_erase.getHeight());

                                            if(((rubber_x1 > to_erase_x1 && rubber_x1 < to_erase_x2)
                                                    && (rubber_y1 > to_erase_y1 && rubber_y1 < to_erase_y2))
                                                    || ((rubber_x2 > to_erase_x1 && rubber_x2 < to_erase_x2) &&
                                                             (rubber_y2 > to_erase_y1 && rubber_y2 < to_erase_y2)))
                                                Log.d("overlapped", "overlapped");


                                            break;
        }

        mRrootLayout.invalidate();

        return true;
    }

    @Override
    public void onBackPressed() {
        //Display alert message when back button has been pressed
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Sair do jogo");
        alertDialog.setMessage("Deseja sair do jogo?");

        alertDialog.setPositiveButton("Sim",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(GameActivity.this, MainActivity.class);
                        startActivity(intent);

                        finish();
                    }
                });

        alertDialog.setNegativeButton("Não",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }


}
