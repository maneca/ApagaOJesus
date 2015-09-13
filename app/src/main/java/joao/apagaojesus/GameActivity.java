package joao.apagaojesus;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.winsontan520.WScratchView;

import joao.apagaojesus.custom.ImageMargins;
import joao.apagaojesus.custom.ImageSize;

public class GameActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private ImageView background;
    private float mPercentage;
    private int totalPoints = 0;
    private long secondsMissing = 0;
    private CountDownTimer timer;
    private int next_image = 0;
    private boolean logged;

    private static int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private boolean mExplicitSignOut = false;
    private boolean mInSignInFlow = false; // set to true when you're in the middle of sign in flow, to know you should not attempt to connect in onStart()
    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;

    private TextView textview;
    private static int[] img = {R.drawable.background_6, R.drawable.background_1, R.drawable.background_2, R.drawable.background_7, R.drawable.background_3, R.drawable.background_4, R.drawable.background_8, R.drawable.background_5};
    private static ImageMargins[] margins = { new ImageMargins(680, 30, 0, 0),  // background_6
                                              new ImageMargins(720, 60, 0, 0),  // background_1
                                              new ImageMargins(700, 30, 0, 0),  // background_2
                                              new ImageMargins(700, 160, 0, 0), // background_7
                                              new ImageMargins(780, 200, 0, 0), // background_3
                                              new ImageMargins(600, 70, 0, 0),  // background_4
                                              new ImageMargins(680, 30, 0, 0),  // background_8
                                              new ImageMargins(650, 70, 0, 0)}; // background_5
    private static ImageSize[] sizes = {new ImageSize(500, 1300),  // background_6
                                        new ImageSize(450, 1150),  // background_1
                                        new ImageSize(500, 1300),  // background_2
                                        new ImageSize(300, 900),   // background_7
                                        new ImageSize(170, 500),   // background_3
                                        new ImageSize(400, 1100),  // background_4
                                        new ImageSize(500, 1300),  // background_8
                                        new ImageSize(400, 1100)}; // background_5
    private InterstitialAd mInterstitialAd;
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        // CREATE THE ADD REQUEST, THAT WILL BE SHOWN IN THE END OF THE GAME.
        mInterstitialAd = new InterstitialAd(this);
        //mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ad_unit_id));
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_final));
        AdRequest adRequest = new AdRequest.Builder().build();
        //AdRequest adRequest = new AdRequest.Builder()
        //        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
        //        .addTestDevice("7B31159C3C89560266CB231B4759805A")
        //        .addTestDevice("F7AEB1A79D4E502AA39818395456F2C0")
        //        .build();

        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {

                gameOver();
            }
        });


        // CONNECTION TO THE GOOGLE PLUS AND GOOGLE PLAY GAMES
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        Bundle bundle = getIntent().getExtras();

        logged = bundle.getBoolean("logged");

        if(logged == true && (mWifi.isConnected() || (mMobile != null && mMobile.isConnected()))){
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                    .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                    .build();


            mSignInClicked = true;
            mGoogleApiClient.connect();
        }



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
                    if(next_image!=img.length){
                        background.setImageResource(img[next_image]);


                        totalPoints += 200;

                        to_erase.resetView();

                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        // used as reference my smartphones (MOTO G) dimensions width=1196 - height=720
                        // and then I calculate a factor by which I multiple the user's screen size
                        // so the image appear in the correct position.
                        float y_factor = (float) ((double) height)/720;
                        float x_factor = (float) ((double) width)/1196;

                        ImageMargins next_image_margins = margins[next_image];

                        params.setMargins(Math.round((next_image_margins.getLeft()) * x_factor),
                                Math.round((next_image_margins.getTop()) * y_factor),
                                Math.round((next_image_margins.getRight()) * x_factor),
                                Math.round((next_image_margins.getBottom()) * y_factor));

                        Drawable dr = getResources().getDrawable(R.drawable.imagem_jj);
                        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();

                        // width - height
                        Drawable d = new BitmapDrawable(getResources(),
                                Bitmap.createScaledBitmap(bitmap,Math.round(sizes[next_image].getWidth()*x_factor),
                                                                 Math.round(sizes[next_image].getHeight()*y_factor), true));

                        to_erase.setScratchDrawable(d);
                        to_erase.setLayoutParams(params);

                        next_image++;
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

    private void gameAchievements(int totalPoints){

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            // FIRST GAME
            Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.nova_contratacao));

            // INCREMENTAL ACHIEVEMENTS
            Games.Achievements.increment(mGoogleApiClient, getResources().getString(R.string.equipa_B), 1);
            Games.Achievements.increment(mGoogleApiClient, getResources().getString(R.string.equipa_A), 1);


            if (totalPoints >= 1000) {
                Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.suplente));
                Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.titular));
                Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.estrela));
            }

            if (600 <= totalPoints && totalPoints < 1000) {
                Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.suplente));
                Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.titular));
            }

            if (300 <= totalPoints && totalPoints < 600) {
                Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.suplente));
            }
        }

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

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Games.Leaderboards.submitScore(mGoogleApiClient, getResources().getString(R.string.leaderboard_id), totalPoints);
            // ACHIEVEMENTS
            gameAchievements(totalPoints);
        }

        return totalPoints;
    }

    private CountDownTimer startTimer(long seconds){

        return new CountDownTimer(seconds*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                textview.setText(getResources().getString(R.string.seconds)+ " " + millisUntilFinished / 1000);
                secondsMissing = millisUntilFinished / 1000;
            }

            public void onFinish() {
                textview.setText(getResources().getString(R.string.acabou));

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
    }


    private void gameOver(){
        Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
        intent.putExtra("points", totalPoints);
        intent.putExtra("logged", logged);
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

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        // Attempt to reconnect
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            // Already resolving
            return;
        }

        // If the sign in button was clicked or if auto sign-in is enabled,
        // launch the sign-in flow
        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            // Attempt to resolve the connection failure using BaseGameUtils.
            // The R.string.signin_other_error value should reference a generic
            // error string in your strings.xml file, such as "There was
            // an issue with sign in, please try again later."
            if (!BaseGameUtils.resolveConnectionFailure(this,
                    mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, getResources().getString(R.string.signin_other_error))) {
                mResolvingConnectionFailure = false;
            }
        }

    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                // Bring up an error dialog to alert the user that sign-in
                // failed. The R.string.signin_failure should reference an error
                // string in your strings.xml file that tells the user they
                // could not be signed in, such as "Unable to sign in."
                BaseGameUtils.showActivityResultError(this,
                        requestCode, resultCode, R.string.signin_failure);
            }
        }
    }

}
