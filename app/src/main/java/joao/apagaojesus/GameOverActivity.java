package joao.apagaojesus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class GameOverActivity extends Activity implements View.OnClickListener{

    private boolean logged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        TextView total_points = (TextView) findViewById(R.id.total_points);
        Button retry = (Button) findViewById(R.id.button_retry);
        Button mainScreen = (Button) findViewById(R.id.button_main_screen);

        Bundle extras = getIntent().getExtras();

        logged = extras.getBoolean("logged");
        int points = (int) extras.get("points");

        total_points.setText(points+" pontos");

        retry.setOnClickListener(this);
        mainScreen.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


        switch(v.getId()){

            case R.id.button_retry: Intent intent = new Intent(GameOverActivity.this, GameActivity.class);
                                    intent.putExtra("logged", logged);
                                    startActivity(intent);
                                    finish();

                                    break;

            case R.id.button_main_screen:   finish();

                                            break;
        }
    }

    @Override
    public void onBackPressed() {

        Toast.makeText(this, getResources().getString(R.string.botao_off), Toast.LENGTH_LONG).show();
    }

}
