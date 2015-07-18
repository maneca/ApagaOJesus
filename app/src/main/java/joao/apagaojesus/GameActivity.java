package joao.apagaojesus;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GameActivity extends Activity implements View.OnTouchListener{

    private ImageView mImageView;
    private ViewGroup mRrootLayout;
    private int _xDelta;
    private int _yDelta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mRrootLayout = (ViewGroup) findViewById(R.id.rootView);
        mImageView = (ImageView) mRrootLayout.findViewById(R.id.imageView);
        final TextView textview = (TextView) mRrootLayout.findViewById(R.id.textView);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(150, 150);
        mImageView.setLayoutParams(layoutParams);
        mImageView.setOnTouchListener(this);

        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                textview.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                //textview.setText("done!");
                Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                layoutParams.leftMargin = X - _xDelta;
                layoutParams.topMargin = Y - _yDelta;
                layoutParams.rightMargin = -250;
                layoutParams.bottomMargin = -250;
                v.setLayoutParams(layoutParams);
                break;
        }
        mRrootLayout.invalidate();

        return true;
    }
}
