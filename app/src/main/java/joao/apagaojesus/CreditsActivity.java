package joao.apagaojesus;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class CreditsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("7B31159C3C89560266CB231B4759805A")
                .build();
        mAdView.loadAd(adRequest);

        TextView ideia = (TextView) findViewById(R.id.ideia);
        TextView dev_joao = (TextView) findViewById(R.id.dev_joao);
        TextView dev_nuno = (TextView) findViewById(R.id.dev_nuno);
        ImageView ricardo = (ImageView) findViewById(R.id.imageView1);
        ImageView joao = (ImageView) findViewById(R.id.imageView2);
        ImageView nuno = (ImageView) findViewById(R.id.imageView3);

        ideia.setMovementMethod(LinkMovementMethod.getInstance());
        dev_joao.setMovementMethod(LinkMovementMethod.getInstance());
        dev_nuno.setMovementMethod(LinkMovementMethod.getInstance());

        Bitmap icon_ricardo = BitmapFactory.decodeResource(getResources(), R.drawable.ricardo_small);
        ricardo.setImageBitmap(icon_ricardo);
        Bitmap icon_joao = BitmapFactory.decodeResource(getResources(), R.drawable.joao_small);
        joao.setImageBitmap(icon_joao);
        Bitmap icon_nuno = BitmapFactory.decodeResource(getResources(), R.drawable.nuno_small);
        nuno.setImageBitmap(icon_nuno);

        String text_ideia = "<a href='https://www.linkedin.com/in/ricardonogueira/pt'> Ricardo Nogueira </a>";
        ideia.setText(Html.fromHtml(text_ideia));

        String text_dev_joao = "<a href='https://pt.linkedin.com/in/joaomanuelferreira'> João Ferreira </a>";
        dev_joao.setText(Html.fromHtml(text_dev_joao));

        String text_dev_nuno = "<a href='https://www.linkedin.com/pub/nuno-rodrigues/7a/509/656'> Nuno Rodrigues </a>";
        dev_nuno.setText(Html.fromHtml(text_dev_nuno));



    }


}
