package onionsss.it.onionsss.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import onionsss.it.onionsss.R;

/**
 * Author  :  张琦
 */
public class ViewPagerActivity extends AppCompatActivity {
    private static final String TAG = "ViewPagerActivity";

    @Bind(R.id.viewpager_vp)
    ViewPager mViewPager_vp;
    @Bind(R.id.viewpager_btn_start)
    Button mViewpager_btn_start;
    @Bind(R.id.viewpager_ll_shape)
    LinearLayout mViewPager_ll_shape;
    @Bind(R.id.viewpager_v_red)
    View viewpager_v_red;

    private SharedPreferences sp;
    private static final int[] Images = new int[]{R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};
    private List<ImageView> list;
    private int mOvalRange;

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

        for (int i = 0; i < Images.length; i++) {
            View view = new View(this);
            view.setBackgroundResource(R.drawable.viewpager_shape_gray);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10,10);
            if(i > 0){
                params.leftMargin = 10;
            }
            view.setLayoutParams(params);
            mViewPager_ll_shape.addView(view);
        }

        mViewPager_ll_shape.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                /**
                 * 获取圆点的距离
                 */
                mOvalRange = mViewPager_ll_shape.getChildAt(1).getLeft() - mViewPager_ll_shape.getChildAt(0).getLeft();
                Log.d(TAG, ""+mOvalRange);
                mViewPager_ll_shape.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
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

        mViewPager_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int width = (int) (mOvalRange * positionOffset + (position * mOvalRange));
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewpager_v_red.getLayoutParams();
                params.leftMargin = width;
                viewpager_v_red.setLayoutParams(params);
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
