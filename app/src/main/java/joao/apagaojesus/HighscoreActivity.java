package joao.apagaojesus;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.TextView;


public class HighscoreActivity extends ActionBarActivity {

    private TextView highscoreTextView;
    private ImageView imageview;
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        highscoreTextView = (TextView) findViewById(R.id.highscore_textView);
        imageview = (ImageView) findViewById(R.id.imageView);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.highscore);
        imageview.setImageBitmap(icon);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
        int highscore = sharedPreferences.getInt("highscore", 0);

        highscoreTextView.setText(highscore + " pontos");
    }


}
