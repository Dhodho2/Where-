package rdsol.whereat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import me.wangyuwei.particleview.ParticleView;
import rdsol.whereat.R;
import rdsol.whereat.database.preferences.MyPreferences;

public class SplashScreen extends BaseActivity {
    private ParticleView mPv1;
    private final Handler HANDLER_THREAD = new Handler();
    private LinearLayout loading;
    private Animation animShow, animHide;
    private TextView realLoadigtxt;
    private ProgressBar realLoadigimg;
    private ImageView imageView;
    private MyPreferences myPreferences;
    private int INTERNET_PERMISSION = 23;
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_splash_screen );
        mPv1 =  findViewById(R.id.pv_2);
        loading = findViewById(R.id.splashLoading);
        realLoadigtxt =  findViewById(R.id.realLoadigtxt);
        realLoadigimg =  findViewById(R.id.realLoadigimg);
        imageView =  findViewById(R.id.stamp);
        myPreferences = new MyPreferences( this );
        initAnimation();
        mPv1.setOnParticleAnimListener( ( ) -> {
            Thread mythread = new Thread() {
                @Override
                public void run() {
                    try {
                        HANDLER_THREAD.post( ( ) -> {
                            loading.setVisibility( View.VISIBLE);
                            realLoadigimg.setVisibility(View.VISIBLE);
                            realLoadigimg.startAnimation(animShow);
                            realLoadigtxt.setVisibility(View.VISIBLE);

                            realLoadigtxt.startAnimation(animShow);
                           // realLoadigtxt.replayAnimation();

                            imageView.setVisibility(View.VISIBLE);
                            imageView.setAlpha(0.f);
                            imageView.setScaleX(0.f);
                            imageView.setScaleY(0.f);
                            imageView.animate()
                                    .alpha(1.f)
                                    .scaleX(1.f).scaleY(1.f)
                                    .setDuration(2000)
                                    .start();
                        } );

                        sleep(6200);

if(myPreferences.isUserLoggedIn()){
    startActivity(new Intent(getApplicationContext(), MainActivity.class));
    finish();
}else{
    startActivity(new Intent(getApplicationContext(), LogIn.class));
    finish();
}


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            };
            mythread.start();
        } );
        mPv1.postDelayed(new Runnable() {
            @Override
            public void run() {

                mPv1.startAnim();
            }
        }, 300);
    }

    private void initAnimation() {

        animShow = AnimationUtils.loadAnimation(this, R.anim.view_show);
        animHide = AnimationUtils.loadAnimation(this, R.anim.view_hide);
    }

}
