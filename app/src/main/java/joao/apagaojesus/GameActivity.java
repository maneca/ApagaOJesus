package joao.apagaojesus;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
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
    private static ImageMargins[] margins = { new ImageMargins(140, 70, 0, 0), new ImageMargins(140, 70, 0, 0), new ImageMargins(500, 120, 0, 0),
            new ImageMargins(700, 50, 0, 0), new ImageMargins(850, 20, 0, 0)};
    private InterstitialAd mInterstitialAd;
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        //AdRequest adRequest = new AdRequest.Builder().build();
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("7B31159C3C89560266CB231B4759805A")
                .build();

        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {

                gameOver();
            }
        });

        ViewGroup mRrootLayout = (ViewGroup) findViewById(R.id.rootView);
        final WScratchView to_erase = (WScratchView) mRrootLayout.findViewById(R.id.image_to_erase);
        background = (ImageView) mRrootLayout.findViewById(R.id.background);
        textview = (TextView) mRrootLayout.findViewById(R.id.time_missing);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        final int height = displaymetrics.heightPixels;
        final int width = displaymetrics.widthPixels;

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

                if(mPercentage >= 95){
                    if(next_image!=4){
                        background.setImageResource(img[next_image]);
                        next_image++;

                        totalPoints += 200;

                        to_erase.resetView();

                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT,
                                                                                             RelativeLayout.LayoutParams.WRAP_CONTENT);
                        // used as reference my smartphones (MOTO G) dimensions width=1196 - height=720
                        // and then I calculate a factor by which I multiple the user's screen size
                        // so the image appear in the correct position.
                        float y_factor = (float) ((double) height)/720;
                        float x_factor = (float) ((double) width)/1196;

                        ImageMargins next_image_margins = margins[next_image];

                        params.setMargins(Math.round((next_image_margins.getLeft())*x_factor),
                                          Math.round((next_image_margins.getTop())*y_factor),
                                          Math.round((next_image_margins.getRight())*x_factor),
                                          Math.round((next_image_margins.getBottom())*y_factor));

                        to_erase.setLayoutParams(params);


                        to_erase.setScratchAll(false);
                        updatePercentage(0f);

                    }else{
                        timer.cancel();
                        totalPoints = settingScore(false);

                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        } else {
                            gameOver();
                        }
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

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {

                    gameOver();
                }
            }
        };
    }

    protected void updatePercentage(float percentage) {
        mPercentage = percentage;
        //String percentage2decimal = String.format("%.2f", percentage) + " %";
        //percentageText.setText("" + percentage2decimal);
    }


    private void gameOver(){
        Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
        intent.putExtra("points", totalPoints);
        startActivity(intent);
        finish();
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
