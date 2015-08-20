package joao.apagaojesus;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.winsontan520.WScratchView;

public class GameActivity extends Activity {

    private ImageView background;
    private ViewGroup mRrootLayout;
    private float mPercentage;
    private int totalPoints = 0;
    private long secondsMissing = 0;
    private CountDownTimer timer;
    private int next_image = 0;
    private TextView percentageText;
    private static int[] img = {R.drawable.benfica_segunda, R.drawable.benfica_terceira, R.drawable.benfica_quarta, R.drawable.benfica_quinta};
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mRrootLayout = (ViewGroup) findViewById(R.id.rootView);
        final WScratchView to_erase = (WScratchView) mRrootLayout.findViewById(R.id.image_to_erase);
        background = (ImageView) mRrootLayout.findViewById(R.id.background);
        final TextView textview = (TextView) mRrootLayout.findViewById(R.id.textView);
        percentageText = (TextView) mRrootLayout.findViewById(R.id.percentage);

        // set drawable to scratchview
        to_erase.setScratchDrawable(getResources().getDrawable(R.drawable.ic_launcher));

        // add callback for update scratch percentage
        to_erase.setOnScratchCallback(new WScratchView.OnScratchCallback() {

            @Override
            public void onScratch(float percentage) {
                updatePercentage(percentage);
            }

            @Override
            public void onDetach(boolean fingerDetach) {
                if (mPercentage > 50) {
                    to_erase.setScratchAll(true);
                    updatePercentage(100);
                }

                if(mPercentage > 90){
                    if(next_image!=4){
                        background.setImageResource(img[next_image]);
                        next_image++;

                        totalPoints += 200;

                        to_erase.resetView();
                        ViewGroup.LayoutParams params = to_erase.getLayoutParams();
                        params.height += 30;
                        params.width += 30;

                        to_erase.setScratchAll(false); // todo: should include to resetView?
                        updatePercentage(0f);

                    }else{
                        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
                        int highscore = sharedPreferences.getInt("highscore", 0);

                        totalPoints += secondsMissing*10;

                        if(highscore < totalPoints){

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("highscore", totalPoints);
                            editor.commit();
                        }


                        Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
                        timer.cancel();
                        intent.putExtra("points", totalPoints);
                        startActivity(intent);
                        finish();
                    }

                }
            }
        });

        updatePercentage(0f);


        // TIMER
        timer = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                textview.setText("seconds remaining: " + millisUntilFinished / 1000);
                secondsMissing = millisUntilFinished / 1000;
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

    protected void updatePercentage(float percentage) {
        mPercentage = percentage;
        String percentage2decimal = String.format("%.2f", percentage) + " %";
        percentageText.setText("" + percentage2decimal);
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
                        timer.cancel();
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
