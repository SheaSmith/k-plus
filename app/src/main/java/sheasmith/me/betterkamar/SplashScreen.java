package sheasmith.me.betterkamar;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;


/**
 * Created by Shay on 21/03/2017.
 */

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ImageView mImageViewFilling = (ImageView) findViewById(R.id.animation);
        ((AnimationDrawable) mImageViewFilling.getDrawable()).start();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, Servers.class);
                startActivity(intent);
                finish();
            }
        }, 2600);

    }
}
