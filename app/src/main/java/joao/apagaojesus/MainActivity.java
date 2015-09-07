package joao.apagaojesus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameUtils;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{

    private static int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private boolean mExplicitSignOut = false;
    private boolean mInSignInFlow = false; // set to true when you're in the middle of sign in flow, to know you should not attempt to connect in onStart()
    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;
    private ConnectivityManager connManager;

    private NetworkInfo mWifi, mMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        //AdRequest adRequest = new AdRequest.Builder()
        //        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
        //        .addTestDevice("7B31159C3C89560266CB231B4759805A")
        //        .addTestDevice("F7AEB1A79D4E502AA39818395456F2C0")
        //        .build();
        mAdView.loadAd(adRequest);

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        Button new_game = (Button) findViewById(R.id.button_new_game);
        Button highscore = (Button) findViewById(R.id.button_highscore);
        Button credits = (Button) findViewById(R.id.button_creditos);
        Button achievements = (Button) findViewById(R.id.button_achievements);
        Button leaderboard = (Button) findViewById(R.id.button_leaderboard);

        new_game.setOnClickListener(this);
        highscore.setOnClickListener(this);
        credits.setOnClickListener(this);
        achievements.setOnClickListener(this);
        leaderboard.setOnClickListener(this);

        connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((mMobile != null && mMobile.isConnected()) || mWifi.isConnected()){
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                    .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                    .build();
        }

    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch(v.getId()){
            case R.id.button_new_game:  intent = new Intent(MainActivity.this, GameActivity.class);
                                        if(mGoogleApiClient != null)
                                            intent.putExtra("logged", mGoogleApiClient.isConnected());
                                        else intent.putExtra("logged", false);

                                        startActivity(intent);

                                        break;

            case R.id.button_highscore: intent = new Intent(MainActivity.this, HighscoreActivity.class);
                                        startActivity(intent);

                                        break;

            case R.id.button_creditos:  intent = new Intent(MainActivity.this, CreditsActivity.class);
                                        startActivity(intent);

                                        break;

            case R.id.button_achievements:  if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                                                intent = Games.Achievements.getAchievementsIntent(mGoogleApiClient);
                                                startActivityForResult(intent, 1);
                                            }else{
                                                Toast.makeText(this, getResources().getString(R.string.needs_login), Toast.LENGTH_SHORT).show();
                                            }

                                            break;

            case R.id.button_leaderboard:   if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                                                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient, getResources().getString(R.string.leaderboard_id)), 1);
                                            }else{
                                               Toast.makeText(this, getResources().getString(R.string.needs_login), Toast.LENGTH_SHORT).show();
                                            }

                                            break;

            case R.id.sign_in_button:   mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                                        mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                                        if(mWifi.isConnected() || (mMobile != null && mMobile.isConnected())){
                                            mSignInClicked = true;
                                            mGoogleApiClient.connect();
                                        }else{
                                            Toast.makeText(this, getResources().getString(R.string.needs_internet), Toast.LENGTH_LONG).show();
                                        }


                                        break;

            case R.id.sign_out_button:  // user explicitly signed out, so turn off auto sign in
                                        mExplicitSignOut = true;
                                        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                                            mSignInClicked = false;
                                            Games.signOut(mGoogleApiClient);
                                            mGoogleApiClient.disconnect();
                                        }

                                        // show sign-in button, hide the sign-out button
                                        findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
                                        findViewById(R.id.sign_out_button).setVisibility(View.GONE);

                                        break;
        }
    }

    @Override
    public void onBackPressed() {
        //Display alert message when back button has been pressed
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle(getResources().getString(R.string.sair_da_aplicacao));
        alertDialog.setMessage(getResources().getString(R.string.ask_sair_da_aplicacao));

        alertDialog.setPositiveButton(getResources().getString(R.string.sim),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mExplicitSignOut = true;
                        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                            mSignInClicked = false;
                            Games.signOut(mGoogleApiClient);
                            mGoogleApiClient.disconnect();
                        }

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

    @Override
    protected void onStart() {
        super.onStart();

        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((mMobile != null && mMobile.isConnected()) || mWifi.isConnected()){
            if (!mInSignInFlow && !mExplicitSignOut) {
                // auto sign in
                mGoogleApiClient.connect();
            }
        }


    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        if(mGoogleApiClient != null)
//            mGoogleApiClient.disconnect();
//    }

    @Override
    public void onConnected(Bundle bundle) {
        // show sign-out button, hide the sign-in button
        findViewById(R.id.sign_in_button).setVisibility(View.GONE);
        findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);

        // (your code here: update UI, enable functionality that depends on sign in, etc)
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

        findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        findViewById(R.id.sign_out_button).setVisibility(View.GONE);
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
