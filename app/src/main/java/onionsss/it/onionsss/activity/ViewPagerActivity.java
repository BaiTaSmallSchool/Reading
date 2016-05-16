package onionsss.it.onionsss.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import onionsss.it.onionsss.R;
/**
 * Author  :  张琦
 * QQemial : 759308541@qq.com
 */
public class ViewPagerActivity extends Activity {
    private ViewPager mViewPager_vp;
    private static final int[] Images = new int[]{R.mipmap.guide_1,R.mipmap.guide_2,R.mipmap.guide_3};
    private List<ImageView> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        initImages();
        initView();
    }

    private void initImages() {
        list = new ArrayList<ImageView>();
        for (int i = 0;i< Images.length;i++){
            ImageView iv = new ImageView(this);
            iv.setImageResource(Images[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            list.add(iv);
        }
    }

    private void initView() {
        mViewPager_vp = (ViewPager) findViewById(R.id.viewpager_vp);
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
                container.removeView((View)object);
            }
        });
    }
}
