package joao.apagaojesus;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;


public class CreditsActivity extends ActionBarActivity {

    private TextView ideia, desenvolvimento;
    private ImageView ricardo, joao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        ideia = (TextView) findViewById(R.id.ideia);
        desenvolvimento = (TextView) findViewById(R.id.desenvolvimento);
        ricardo = (ImageView) findViewById(R.id.imageView1);
        joao = (ImageView) findViewById(R.id.imageView2);

        ideia.setMovementMethod(LinkMovementMethod.getInstance());
        desenvolvimento.setMovementMethod(LinkMovementMethod.getInstance());

        Bitmap icon_ricardo = BitmapFactory.decodeResource(getResources(), R.drawable.ricardo_small);
        ricardo.setImageBitmap(icon_ricardo);
        Bitmap icon_joao = BitmapFactory.decodeResource(getResources(), R.drawable.joao_small);
        joao.setImageBitmap(icon_joao);

        String text_ideia = "<a href='https://www.linkedin.com/in/ricardonogueira/pt'> Ricardo Nogueira </a>";
        ideia.setText(Html.fromHtml(text_ideia));

        String text_desenvolvimento = "<a href='https://pt.linkedin.com/in/joaomanuelferreira'> João Ferreira </a>";
        desenvolvimento.setText(Html.fromHtml(text_desenvolvimento));

    }


}
