package onionsss.it.onionsss.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import onionsss.it.onionsss.R;
import onionsss.it.onionsss.fragment.ChatFragment;
import onionsss.it.onionsss.fragment.HomeFragment;
import onionsss.it.onionsss.fragment.LeftFragment;
import onionsss.it.onionsss.fragment.RambleFragment;
import onionsss.it.onionsss.fragment.SettingFragment;
import onionsss.it.onionsss.utils.SpUtil;

/**
 * @author 张琦  2016年5月19日 00:46:11
 */
public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener {
    @Bind(R.id.main_content)
    FrameLayout main_content;
    @Bind(R.id.main_rb_home)
    RadioButton main_rb_home;
    @Bind(R.id.main_rb_ramble)
    RadioButton main_rb_ramble;
    @Bind(R.id.main_rb_dialog)
    RadioButton main_rb_dialog;
    @Bind(R.id.main_rb_my)
    RadioButton main_rb_my;
    @Bind(R.id.main_radiogroup)
    RadioGroup main_radiogroup;

    public static final String TAG = "MainActivity";
    /**
     * 两次返回退出程序
     */
    private long exitTime = 0;
    private List<Fragment> list;
    private FragmentManager fm;

    /**
     * 记录用户最后一次切换的Fragment角标
     */
    private int lastIndex;

    /**
     * 手势动作
     * 用来手势Fragment的切换
     * @param savedInstanceState
     */
    private GestureDetector gesture;
    /**
     * 判断切换Fragment是否是手势动作造成的
     * 如果是则只执行一次
     */
    public static boolean isGesture = false;
    public static final int HOME = 0;
    public static final int RAMBLE = 1;
    public static final int CHAT = 2;
    public static final int MY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slidingMenu();


        ButterKnife.bind(this);
        initData();
        initListener();
        initView();
//        gesture();
    }

    /**
     * SlidingMenu绑定
     */
    private void slidingMenu() {
        SlidingMenu leftMenu = new SlidingMenu(this);
        leftMenu.setMode(SlidingMenu.LEFT);
        leftMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        leftMenu.setShadowWidthRes(R.dimen.shadow_width);
        leftMenu.setShadowDrawable(R.drawable.shadow);
        leftMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        leftMenu.setFadeDegree(0.35f);
        leftMenu.setFadeEnabled(true);
        leftMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        leftMenu.setMenu(R.layout.left_menu);
    }

    /**
     * 加入简单的手势动作   不做了
     * TODO  手势动作有BUG  会执行两次
     */
    private void gesture() {
        gesture = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e2.getRawX() - e1.getRawX() > 200 && Math.abs(e2.getRawY() - e1.getRawY()) < 200) {
                    isGesture = true;
                    switch (lastIndex) {
                        case HOME:
                            break;
                        case RAMBLE:
                            changeFragment(list.get(HOME), HOME);
                            setIconCheck();
                            break;
                        case CHAT:
                            changeFragment(list.get(RAMBLE), RAMBLE);
                            setIconCheck();
                            break;
                        case MY:
                            changeFragment(list.get(CHAT), CHAT);
                            setIconCheck();
                            break;
                    }
                    isGesture = false;
                }
                if (e1.getRawX() - e2.getRawX() > 200 && Math.abs(e1.getRawY() - e2.getRawY()) < 200) {
                    isGesture = true;
                    switch (lastIndex) {
                        case HOME:
                            changeFragment(list.get(RAMBLE), RAMBLE);
                            Log.d(TAG, "onFling: ");
                            setIconCheck();
                            break;
                        case RAMBLE:
                            changeFragment(list.get(CHAT), CHAT);
                            setIconCheck();
                            break;
                        case CHAT:
                            changeFragment(list.get(MY), MY);
                            setIconCheck();
                            break;
                        case MY:
                            break;
                    }
                    isGesture = false;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    /**
     * 拿到Fragment管理者
     * 以及加载Fragment
     */
    private void initData() {
        /**
         * 回显 用户上一次停留的Fragment
         */
        lastIndex = SpUtil.getInt(this, "lastIndex", this.lastIndex);
        fm = getSupportFragmentManager();

        list = new ArrayList<>();
        list.add(new HomeFragment());
        list.add(new RambleFragment());
        list.add(new ChatFragment());
        list.add(new SettingFragment());
        list.add(new LeftFragment());
    }

    private void initListener() {
        /**
         * 给radioGroup设置监听
         * 非常重要!!  不然fragment不能完成在Activity中的切换
         */
        main_radiogroup.setOnCheckedChangeListener(this);
    }

    /**
     * view显示
     */
    private void initView() {
        initFragment();
    }

    /**
     * 默认初始化界面
     */
    private void initFragment() {
        /**
         * 设置初始化图标
         */
        setIconCheck();

        fm.beginTransaction().add(R.id.main_content, list.get(0), "home")
                .add(R.id.main_content, list.get(1), "ramble")
                .add(R.id.main_content, list.get(2), "chat")
                .add(R.id.main_content, list.get(3), "setting")
                .add(R.id.left_menu,list.get(4),"left")
                .hide(list.get(0))
                .hide(list.get(1))
                .hide(list.get(2))
                .hide(list.get(3))
                .show(list.get(lastIndex))   //设置默认界面
                .commit();
    }

    /**
     * 默认界面
     */
    private void setIconCheck() {
        switch (lastIndex) {
            case 0:
                main_rb_home.setChecked(true);
                break;
            case 1:
                main_rb_ramble.setChecked(true);
                break;
            case 2:
                main_rb_dialog.setChecked(true);
                break;
            case 3:
                main_rb_my.setChecked(true);
                break;
        }
    }

    /**
     * 当radioButton发生变化时
     * 回调给系统 用来切换Fragment
     *
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(isGesture){
            return;
        }
        switch (checkedId) {
            case R.id.main_rb_home:
                changeFragment(list.get(0), 0);
                break;
            case R.id.main_rb_ramble:
                changeFragment(list.get(1), 1);
                break;
            case R.id.main_rb_dialog:
                changeFragment(list.get(2), 2);
                break;
            case R.id.main_rb_my:
                changeFragment(list.get(3), 3);
                break;
        }
    }

    /**
     * 切换Fragment
     *
     * @param fragment 需要被显示的Fragment
     * @param flag     原来标识在list集合角标
     */
    public void changeFragment(Fragment fragment, int flag) {
        Log.d(TAG, "onFling:111 ");
        /*
        记录角标
         */
        lastIndex = flag;
        SpUtil.putInt(this, "lastIndex", lastIndex);

        FragmentTransaction ft = fm.beginTransaction();
        ft.hide(list.get(0))
                .hide(list.get(1))
                .hide(list.get(2))
                .hide(list.get(3))
                .show(list.get(flag));    //通过传过来的flag来判断是哪个fragment
        ft.commit();
        Toast.makeText(this, "切换到" + fragment.toString(), Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        gesture.onTouchEvent(event);
//        return super.onTouchEvent(event);
//    }

    /**
     * 用户需要点击两次退出程序
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}


/**
 * 备用 用replace 进行切换Fragment
 * 效率低
 */
//public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener {
//    @Bind(R.id.main_content)
//    FrameLayout main_content;
//    @Bind(R.id.main_rb_home)
//    RadioButton main_rb_home;
//    @Bind(R.id.main_rb_ramble)
//    RadioButton main_rb_ramble;
//    @Bind(R.id.main_rb_dialog)
//    RadioButton main_rb_dialog;
//    @Bind(R.id.main_rb_my)
//    RadioButton main_rb_my;
//    @Bind(R.id.main_radiogroup)
//    RadioGroup main_radiogroup;
//
//    public static final String TAG = "MainActivity";
//    /**
//     * 两次返回退出程序
//     */
//    private long exitTime = 0;
//    private List<Fragment> list;
//    private FragmentManager fm;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        ButterKnife.bind(this);
//        initView();
//    }
//
//
//    private void initView() {
//        /**
//         * 给radiobutton设置check改变事件监听
//         * 如果不选择 fragment无法完成切换
//         */
//        main_radiogroup.setOnCheckedChangeListener(this);
//        fm = getSupportFragmentManager();
//        /**
//         * 设置main页面的默认Fragment
//         */
//        main_rb_home.setChecked(true);
////        changeFragment(new HomeFragment(),false,0);
//    }
//
//    /**
//     * 当RadioButton发生改变时  监听是哪个Button变化
//     * 便切换到哪个Fragment
//     *
//     * @param group
//     * @param checkedId
//     */
//    @Override
//    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        switch(checkedId){
//            case R.id.main_rb_home:
//                changeFragment(new HomeFragment(),true,0);
//                break;
//            case R.id.main_rb_ramble:
//                changeFragment(new RambleFragment(),true,1);
//                break;
//            case R.id.main_rb_dialog:
//                changeFragment(new ChatFragment(),true,2);
//                break;
//            case R.id.main_rb_my:
//                changeFragment(new SettingFragment(),true,3);
//                break;
//        }
//    }
//
//    /**
//     * 当onCheckedChanged监听到哪个ID改变时
//     * 调用此方法来 替换某个Fragment
//     * @param fragment   需要替换的Fragment,
//     * @param flag       为true就不回退到栈
//     * @param tag        味哦Fragment设置TAG
//     */
//    public void changeFragment(Fragment fragment,boolean flag,int tag){
//        Log.d(TAG, fragment.toString());
//        /**
//         * 根据传过来的tag来判断是哪个Fragment
//         */
//        String fTag = null;
//        switch(tag){
//            case 0:
//                fTag = "home";
//                break;
//            case 1:
//                fTag = "ramble";
//                break;
//            case 2:
//                fTag = "chat";
//                break;
//            case 3:
//                fTag = "my";
//                break;
//        }
//
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.main_content,fragment,fTag);
//        /**
//         * 第一次进入main时加载HomeFragment 当再次点击HomeFragment时会发生异常
//         * 所以
//         * 设置需要回退至栈 丢弃
//         */
//        if(!flag){
//            ft.addToBackStack(null);
//        }
//        ft.commit();
//    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            if ((System.currentTimeMillis() - exitTime) > 2000) {
//                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
//                exitTime = System.currentTimeMillis();
//            } else {
//                finish();
//                System.exit(0);
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//}
