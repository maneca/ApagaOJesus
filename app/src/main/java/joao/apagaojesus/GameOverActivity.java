package joao.apagaojesus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class GameOverActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("7B31159C3C89560266CB231B4759805A")
                .build();
        mAdView.loadAd(adRequest);


        TextView total_points = (TextView) findViewById(R.id.total_points);
        Button retry = (Button) findViewById(R.id.button_retry);
        Button mainScreen = (Button) findViewById(R.id.button_main_screen);

        Bundle extras = getIntent().getExtras();
        int points = (int) extras.get("points");

        total_points.setText(points+" pontos");

        retry.setOnClickListener(this);
        mainScreen.setOnClickListener(this);

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


}
