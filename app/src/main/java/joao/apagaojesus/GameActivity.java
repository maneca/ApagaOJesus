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
    private float mPercentage;
    private int totalPoints = 0;
    private long secondsMissing = 0;
    private CountDownTimer timer;
    private int next_image = 0;
    private TextView textview, percentageText;
    private static int[] img = {R.drawable.background_1, R.drawable.background_2, R.drawable.background_3, R.drawable.background_4};
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ViewGroup mRrootLayout = (ViewGroup) findViewById(R.id.rootView);
        final WScratchView to_erase = (WScratchView) mRrootLayout.findViewById(R.id.image_to_erase);
        background = (ImageView) mRrootLayout.findViewById(R.id.background);
        textview = (TextView) mRrootLayout.findViewById(R.id.time_missing);
        //percentageText = (TextView) mRrootLayout.findViewById(R.id.percentage);

        // set drawable to scratchview
        to_erase.setScratchDrawable(getResources().getDrawable(R.drawable.imagem_jj));

        // add callback for update scratch percentage
        to_erase.setOnScratchCallback(new WScratchView.OnScratchCallback() {

            @Override
            public void onScratch(float percentage) {
                updatePercentage(percentage);
            }

            @Override
            public void onDetach(boolean fingerDetach) {

                if(mPercentage == 100){
                    if(next_image!=4){
                        background.setImageResource(img[next_image]);
                        next_image++;

                        totalPoints += 200;

                        to_erase.resetView();
                        ViewGroup.LayoutParams params = to_erase.getLayoutParams();
                        params.height += 30;
                        params.width += 30;

                        to_erase.setScratchAll(false);
                        updatePercentage(0f);

                    }else{

                        totalPoints = settingScore(false);

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
        timer = startTimer(30).start();
    }

    private int settingScore(boolean finished_time){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
        int highscore = sharedPreferences.getInt("highscore", 0);

        if(!finished_time)
            totalPoints += secondsMissing*10;
        else totalPoints += (int) mPercentage;

        if(highscore < totalPoints){

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("highscore", totalPoints);
            editor.commit();
        }

        return totalPoints;
    }

    private CountDownTimer startTimer(long seconds){

        return new CountDownTimer(seconds*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                textview.setText("seconds remaining: " + millisUntilFinished / 1000);
                secondsMissing = millisUntilFinished / 1000;
            }

            public void onFinish() {
                textview.setText("Acabou");

                totalPoints = settingScore(true);

                Intent intent = new Intent(GameActivity.this, GameOverActivity.class);

                intent.putExtra("points", totalPoints);
                startActivity(intent);
                finish();
            }
        };
    }

    protected void updatePercentage(float percentage) {
        mPercentage = percentage;
        //String percentage2decimal = String.format("%.2f", percentage) + " %";
        //percentageText.setText("" + percentage2decimal);
    }


    @Override
    public void onBackPressed() {

        // paramos o cronometro

        timer.cancel();
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

                        dialog.cancel();
                        // fazemos restart do cronometro a partir segundos de onde parou.
                        timer = startTimer(secondsMissing).start();
                    }
                });

        alertDialog.show();
    }


}
