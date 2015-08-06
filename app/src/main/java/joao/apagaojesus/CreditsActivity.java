package joao.apagaojesus;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;


public class CreditsActivity extends ActionBarActivity {

    private TextView ideia, desenvolvimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        ideia = (TextView) findViewById(R.id.ideia);
        desenvolvimento = (TextView) findViewById(R.id.desenvolvimento);

        ideia.setMovementMethod(LinkMovementMethod.getInstance());
        desenvolvimento.setMovementMethod(LinkMovementMethod.getInstance());

        String text_ideia = "<a href='https://www.linkedin.com/in/ricardonogueira/pt'> Ricardo Nogueira </a>";
        ideia.setText(Html.fromHtml(text_ideia));

        String text_desenvolvimento = "<a href='https://pt.linkedin.com/in/joaomanuelferreira'> João Ferreira </a>";
        desenvolvimento.setText(Html.fromHtml(text_desenvolvimento));
    }


}
