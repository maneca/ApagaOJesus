package joao.apagaojesus;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class HighscoreActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String PREFS_NAME = "MyPrefsFile";
    private SharedPreferences sharedPreferences;
    private TextView highscoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        highscoreTextView = (TextView) findViewById(R.id.highscore_textView);
        ImageView imageview = (ImageView) findViewById(R.id.imageView);
        Button reset = (Button) findViewById(R.id.button_reset_highscore);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.highscore);
        imageview.setImageBitmap(icon);

        sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
        int highscore = sharedPreferences.getInt("highscore", 0);

        highscoreTextView.setText(highscore + " pontos");
        reset.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.button_reset_highscore:   SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putInt("highscore", 0);
                                                editor.commit();

                                                highscoreTextView.setText("0 pontos");
                                                break;

        }
    }
}
