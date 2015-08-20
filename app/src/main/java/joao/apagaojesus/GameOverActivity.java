package joao.apagaojesus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class GameOverActivity extends Activity implements View.OnClickListener {

    private Button retry, mainScreen;
    private TextView total_points;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        total_points = (TextView) findViewById(R.id.total_points);
        retry = (Button) findViewById(R.id.button_retry);
        mainScreen = (Button) findViewById(R.id.button_main_screen);

        Bundle extras = (Bundle) getIntent().getExtras();
        int points = (int) extras.get("points");

        total_points.setText(points+" pontos");

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

    @Override
    public void onBackPressed() {


        return;
    }
}
