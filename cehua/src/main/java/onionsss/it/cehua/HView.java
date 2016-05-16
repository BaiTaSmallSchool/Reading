package onionsss.it.cehua;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * 作者：张琦 on 2016/5/16 10:13
 * 邮箱：759308541@qq.com
 */
public class HView extends HorizontalScrollView {

    private int mScreenWidth;
    private LinearLayout mWapper;
    private ViewGroup mMenu;
    private ViewGroup mContent;
    private int mMenuRightPadding = 50;
    private int mMenuWidth;

    private boolean once;

    public HView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //拿到屏幕的宽度
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(!once){
            mWapper = (LinearLayout) getChildAt(0);
            mMenu = (ViewGroup) mWapper.getChildAt(0);
            mContent = (ViewGroup) mWapper.getChildAt(1);
            mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth - mMenuRightPadding;
            mContent.getLayoutParams().width = mScreenWidth;
            once = true;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!changed)
        {
            this.scrollTo(mMenuWidth, 0);
        }
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action){
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                if(scrollX >= mMenuWidth/2){
                    smoothScrollTo(mMenuWidth,0);
                }else{
                    smoothScrollTo(0,0);
                }
                return true;
        }

        return super.onTouchEvent(ev);
    }
}
