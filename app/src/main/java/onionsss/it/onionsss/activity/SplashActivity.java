package onionsss.it.onionsss.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import onionsss.it.onionsss.R;

/**
 * Author  :  张琦
 * QQemial : 759308541@qq.com
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        View splashView = View.inflate(this, R.layout.activity_splash, null);
        AlphaAnimation aa = new AlphaAnimation(0.5f,1);
        aa.setDuration(1500);
        splashView.startAnimation(aa);
        setContentView(splashView);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(SplashActivity.this, ViewPagerActivity.class));
                finish();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
}
