package joao.apagaojesus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameUtils;


public class GameOverActivity extends Activity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private boolean mExplicitSignOut = false;
    private boolean mInSignInFlow = false; // set to true when you're in the middle of sign in flow, to know you should not attempt to connect in onStart()
    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        TextView total_points = (TextView) findViewById(R.id.total_points);
        Button retry = (Button) findViewById(R.id.button_retry);
        Button mainScreen = (Button) findViewById(R.id.button_main_screen);

        Bundle extras = getIntent().getExtras();
        int points = (int) extras.get("points");

        total_points.setText(points+" pontos");

        retry.setOnClickListener(this);
        mainScreen.setOnClickListener(this);

        // CONNECTION TO THE GOOGLE PLUS AND GOOGLE PLAY GAMES
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();


        mSignInClicked = true;
        mGoogleApiClient.connect();


        // LEADERBOARD

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Games.Leaderboards.submitScore(mGoogleApiClient, getResources().getString(R.string.leaderboard_id), points);
            // ACHIEVEMENTS
            gameAchievements(points);
        }


    }

    private void gameAchievements(int totalPoints){

        Log.d("Entrei", "aqui");
        // FIRST GAME
        Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.nova_contratacao));

        // INCREMENTAL ACHIEVEMENTS
        Games.Achievements.increment(mGoogleApiClient, getResources().getString(R.string.equipa_B), 1);
        Games.Achievements.increment(mGoogleApiClient, getResources().getString(R.string.equipa_A), 1);

        if(totalPoints > 300)
            Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.suplente));

        if(totalPoints > 600)
            Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.titular));

        if(totalPoints >= 900)
            Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.estrela));

    }


    @Override
    public void onClick(View v) {


        switch(v.getId()){

            case R.id.button_retry: Intent intent = new Intent(GameOverActivity.this, GameActivity.class);
                                    startActivity(intent);
                                    finish();

                                    break;

            case R.id.button_main_screen:   Intent intent_home = new Intent(GameOverActivity.this, MainActivity.class);
                                            startActivity(intent_home);
                                            finish();

                                            break;
        }
    }

    @Override
    public void onBackPressed() {

        Toast.makeText(this, getResources().getString(R.string.botao_off), Toast.LENGTH_SHORT).show();

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
