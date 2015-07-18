package joao.apagaojesus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class GameOverActivity extends AppCompatActivity implements View.OnClickListener {

    private Button retry, mainScreen;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        retry = (Button) findViewById(R.id.button_retry);
        mainScreen = (Button) findViewById(R.id.button_main_screen);

        retry.setOnClickListener(this);
        mainScreen.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.button_retry: intent = new Intent(GameOverActivity.this, GameActivity.class);
                                    startActivity(intent);
                                    finish();

                                    break;

            case R.id.button_main_screen:   intent = new Intent(GameOverActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();

                                            break;
        }
    }
}
