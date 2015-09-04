package joao.apagaojesus;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class HighscoreActivity extends AppCompatActivity{

    public static final String PREFS_NAME = "MyPrefsFile";
    private SharedPreferences sharedPreferences;
    private TextView highscoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        highscoreTextView = (TextView) findViewById(R.id.highscore_textView);
        ImageView imageview = (ImageView) findViewById(R.id.imageView);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.highscore);
        imageview.setImageBitmap(icon);

        sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
        int highscore = sharedPreferences.getInt("highscore", 0);

        highscoreTextView.setText(highscore + " pontos");

        // CREATE THE ADD REQUEST, THAT WILL BE SHOWN IN THE END OF THE GAME.
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        //AdRequest adRequest = new AdRequest.Builder()
        //        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
        //        .addTestDevice("7B31159C3C89560266CB231B4759805A")
        //        .addTestDevice("F7AEB1A79D4E502AA39818395456F2C0")
        //        .build();
        mAdView.loadAd(adRequest);

    }


    public void onBackPressed() {

        finish();
    }


}
