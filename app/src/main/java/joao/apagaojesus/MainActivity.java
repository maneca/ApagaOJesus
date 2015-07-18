package joao.apagaojesus;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity implements View.OnClickListener{

    private Button new_game, highscore, credits;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new_game = (Button) findViewById(R.id.button_new_game);
        highscore = (Button) findViewById(R.id.button_highscore);
        credits = (Button) findViewById(R.id.button_creditos);

        new_game.setOnClickListener(this);
        highscore.setOnClickListener(this);
        credits.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.button_new_game:  intent = new Intent(MainActivity.this, GameActivity.class);
                                        startActivity(intent);
                                        finish();

                                        break;

            case R.id.button_highscore: intent = new Intent(MainActivity.this, HighscoreActivity.class);
                                        startActivity(intent);
                                        finish();

                                        break;

            case R.id.button_creditos:  intent = new Intent(MainActivity.this, CreditsActivity.class);
                                        startActivity(intent);
                                        finish();

                                        break;
        }
    }
}
