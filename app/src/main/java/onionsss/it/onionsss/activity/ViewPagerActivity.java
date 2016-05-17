package onionsss.it.onionsss.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import onionsss.it.onionsss.R;

/**
 * Author  :  张琦
 * QQemial : 759308541@qq.com
 */
public class ViewPagerActivity extends AppCompatActivity {
    @Bind(R.id.viewpager_vp)
    ViewPager mViewPager_vp;
    @Bind(R.id.viewpager_btn_start)
    Button mViewpager_btn_start;


    private SharedPreferences sp;
    private static final int[] Images = new int[]{R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};
    private List<ImageView> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        ButterKnife.bind(this);
        sp =  getSharedPreferences("config",MODE_PRIVATE);
        initImages();
        initView();
    }

    private void initImages() {
        list = new ArrayList<ImageView>();
        for (int i = 0; i < Images.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setImageResource(Images[i]);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            list.add(iv);
        }
    }

    private void initView() {
        mViewPager_vp.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return Images.length;
            }
            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                container.addView(list.get(position));
                return list.get(position);
            }
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        mViewPager_vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position == list.size()-1){
                    mViewpager_btn_start.setVisibility(View.VISIBLE);
                }else{
                    mViewpager_btn_start.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(R.id.viewpager_btn_start)
    public void onClick() {
        startActivity(new Intent(ViewPagerActivity.this,LoginActivity.class));
        sp.edit().putBoolean("guidePage",false).commit();
        finish();
    }
}
