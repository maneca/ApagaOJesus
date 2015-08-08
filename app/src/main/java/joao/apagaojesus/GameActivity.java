package joao.apagaojesus;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends Activity {

    private ImageView to_erase, background;
    private ViewGroup mRrootLayout;
    private Rect imageRect;
    private long mAnimationTime;
    private ObjectAnimator anim;
    private Boolean terminou=false;
    private int totalPoints = 0;
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mRrootLayout = (ViewGroup) findViewById(R.id.rootView);
        to_erase = (ImageView) mRrootLayout.findViewById(R.id.image_to_erase);
        background = (ImageView) mRrootLayout.findViewById(R.id.background);
        final TextView textview = (TextView) mRrootLayout.findViewById(R.id.textView);

        //animation = AnimationUtils.loadAnimation(this, R.anim.animation);
        //animation.setAnimationListener(this);

        anim = ObjectAnimator.ofFloat(to_erase, "alpha", 1, 0);
        anim.setDuration(10000);
        anim.setRepeatCount(0);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                    terminou = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d("Acabou", "acabou");
                to_erase.setVisibility(View.GONE);
                background.setImageResource(R.drawable.benfica_segunda);
                terminou = true;
                totalPoints += (int) anim.getDuration()/100;

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                textview.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                textview.setText("Acabou");

                SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
                int highscore = sharedPreferences.getInt("highscore", 0);

                if(highscore < totalPoints){

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("highscore", totalPoints);
                    editor.commit();
                }


                Intent intent = new Intent(GameActivity.this, GameOverActivity.class);

                intent.putExtra("points", totalPoints);
                startActivity(intent);
                finish();
            }
        }.start();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();

        /*switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:   RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                                            _xDelta = X - lParams.leftMargin;
                                            _yDelta = Y - lParams.topMargin;

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

                                            break;
        }

        mRrootLayout.invalidate();*/
        super.onTouchEvent(event);
        if (imageRect == null) {
            imageRect = new Rect();
            to_erase.getGlobalVisibleRect(imageRect);
        }
        int x = (int) event.getX();
        int y = (int) event.getY();

        if(!terminou) {
            if (imageRect.contains(x, y)) {
                Log.d("tempo0", (mAnimationTime * 1000) + " seg");

                if (!anim.isRunning())
                    startAnimation();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                stopAnimation();
                Log.d("tempo1", (mAnimationTime * 1000) + " seg");
            } else if (event.getAction() == MotionEvent.ACTION_DOWN && imageRect.contains(x, y)) {
                Log.d("tempo2", (mAnimationTime * 1000) + " seg");
                if (!anim.isRunning())
                startAnimation();
            } else {
                stopAnimation();
                Log.d("tempo3", (mAnimationTime * 1000) + " seg");
            }
        }
        return true;
    }

    private void stopAnimation(){

        mAnimationTime = anim.getCurrentPlayTime();
        anim.cancel();
    }

    private void startAnimation(){

        anim.start();
        anim.setCurrentPlayTime(mAnimationTime);
    }

    @Override
    public void onBackPressed() {
        //Display alert message when back button has been pressed
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle(getResources().getString(R.string.sair_do_jogo));
        alertDialog.setMessage(getResources().getString(R.string.ask_sair_do_jogo));

        alertDialog.setPositiveButton(getResources().getString(R.string.sim),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(GameActivity.this, MainActivity.class);
                        startActivity(intent);

                        finish();
                    }
                });

        alertDialog.setNegativeButton(getResources().getString(R.string.nao),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }



}
