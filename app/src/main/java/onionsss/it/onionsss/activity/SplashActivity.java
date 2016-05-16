package onionsss.it.onionsss.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import onionsss.it.onionsss.R;
import onionsss.it.onionsss.dao.UserDao;

/**
 * Author  :  张琦
 * QQemial : 759308541@qq.com
 */
public class SplashActivity extends Activity {
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("config",MODE_PRIVATE);
        dbTest();
        initView();
    }

    private void dbTest() {
        UserDao ud = new UserDao(this);
    }

    private void initView() {
        View splashView = View.inflate(this, R.layout.activity_splash, null);
        AlphaAnimation aa = new AlphaAnimation(0.5f,1);
        aa.setDuration(2200);
        splashView.startAnimation(aa);
        setContentView(splashView);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            /**
             * 动画走完直接进主页面
             * @param animation
             */
            @Override
            public void onAnimationEnd(Animation animation) {
                /**
                 * 判断是否走过引导页面
                 * guidePage  true 代表走
                 */
                boolean guidePage = sp.getBoolean("guidePage", true);
                if(guidePage){
                    startActivity(new Intent(SplashActivity.this, ViewPagerActivity.class));
                }else{
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
}
