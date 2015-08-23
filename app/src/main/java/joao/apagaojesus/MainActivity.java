package joao.apagaojesus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button new_game = (Button) findViewById(R.id.button_new_game);
        Button highscore = (Button) findViewById(R.id.button_highscore);
        Button credits = (Button) findViewById(R.id.button_creditos);

        new_game.setOnClickListener(this);
        highscore.setOnClickListener(this);
        credits.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch(v.getId()){
            case R.id.button_new_game:  intent = new Intent(MainActivity.this, GameActivity.class);
                                        startActivity(intent);
                                        finish();

                                        break;

            case R.id.button_highscore: intent = new Intent(MainActivity.this, HighscoreActivity.class);
                                        startActivity(intent);

                                        break;

            case R.id.button_creditos:  intent = new Intent(MainActivity.this, CreditsActivity.class);
                                        startActivity(intent);

                                        break;
        }
    }

    @Override
    public void onBackPressed() {
        //Display alert message when back button has been pressed
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle(getResources().getString(R.string.sair_da_aplicacao));
        alertDialog.setMessage(getResources().getString(R.string.ask_sair_da_aplicacao));

        alertDialog.setPositiveButton(getResources().getString(R.string.sim),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                    }
                });

        alertDialog.setNegativeButton(getResources().getString(R.string.nao),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
                });

        alertDialog.show();

    }
}
