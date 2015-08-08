package joao.apagaojesus;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;


public class HighscoreActivity extends ActionBarActivity {

    private TextView highscoreTextView;
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        highscoreTextView = (TextView) findViewById(R.id.highscore_textView);


        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
        int highscore = sharedPreferences.getInt("highscore", 0);

        highscoreTextView.setText(highscore + " pontos");
    }


}
