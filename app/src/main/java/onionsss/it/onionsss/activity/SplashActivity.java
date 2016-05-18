package onionsss.it.onionsss.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Response;
import onionsss.it.onionsss.R;
import onionsss.it.onionsss.bean.UpdateJson;
import onionsss.it.onionsss.dao.UserDao;
import onionsss.it.onionsss.utils.OkUtils;
import onionsss.it.onionsss.utils.PackageUtil;

/**
 * Author  :  张琦
 */
public class SplashActivity extends AppCompatActivity {
    /**
     * 更新地址URL
     */
    private static final String UPDATEPAHT = "http://169.254.163.120:8080/update.json";
    private static final String TAG = "SplashActivity";
    public static final int SPLASH_URL = 1;    //URL错误
    public static final int SPLASH_IO = 2;    //IO网络异常
    public static final int SPLASH_JSON = 4;    //JSON异常
    public static final int SPLASH_OK = 5;    //需要更新
    public static final int SPLASH_NO = 6;    //不需要更新
    public static final int SPLASH_INSTALL = 7;    //不需要更新

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SPLASH_OK:
                    showDialog();
                    break;
                case SPLASH_URL:
                case SPLASH_IO:
                case SPLASH_JSON:
                    Toast.makeText(SplashActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                case SPLASH_NO:
                    enterHome();
                    break;

            }
        }
    };

    /**
     * 版本信息
     */
    private UpdateJson mUj;
    private ProgressDialog mProgressDialog;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        /**
         * 数据库的测试
         */
//        dbTest();
        initView();
    }

    private void dbTest() {
        UserDao ud = new UserDao(this);
    }

    private void initView() {
        View splashView = View.inflate(this, R.layout.activity_splash, null);
        AlphaAnimation aa = new AlphaAnimation(0.5f, 1);
        aa.setDuration(1000);
        splashView.startAnimation(aa);
        setContentView(splashView);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                checkVersion();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    /**
     * 检查是否有更新
     * HttpURLConnection
     */
    private void checkVersion() {
        new Thread() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                try {
                    Response response = OkUtils.getResponse(UPDATEPAHT);
                    String json = response.body().string();
                    JSONObject object = new JSONObject(json);
                    mUj = new UpdateJson(object.getString("versionName"), object.getInt("versionCode"),
                            object.getString("description"), object.getString("downloadUrl"));
                    if (mUj.getVersionCode() > PackageUtil.getVersionCode(SplashActivity.this)) {
                        msg.what = SPLASH_OK;
                    } else {
                        msg.what = SPLASH_NO;
                    }
                } catch (IOException e) {
                    msg.what = SPLASH_IO;
                    e.printStackTrace();
                } catch (JSONException e) {
                    msg.what = SPLASH_JSON;
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 弹出dialog更新
     */
    private void showDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle(R.string.update)
                .setMessage(mUj.getDesc())
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enterHome();
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        download();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {   //用户点击返回键的监听
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        enterHome();
                    }
                })
                .show();
    }

    /**
     * 用户点击了确定 开始下载新版本
     */
    public void download() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if(mProgressDialog == null){
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
            }


            new Thread(new downAPK()).start();
        } else {
            /**
             * 没有挂载好
             */
            Toast.makeText(this, "没有SD卡,无法下载!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 下载APK线程
     */
    class downAPK implements Runnable {
        @Override
        public void run() {

            InputStream is = null;
            OutputStream os = null;
            try {
                Response response = OkUtils.getResponse(mUj.getUrl());
                mProgressDialog.setMax((int) response.body().contentLength()/1024);
                is = response.body().byteStream();
                File rootFile = Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File file = new File(rootFile, "onionsss-v"+PackageUtil
                        .getVersionCode(SplashActivity .this)+".apk");
                os = new FileOutputStream(file);
                byte[] b = new byte[1024];
                int len = 0;
                int totalDown = 0;
                while ((len = is.read(b)) != -1) {
                    os.write(b, 0, len);
                    totalDown++;
                    mProgressDialog.setProgress(totalDown);
                }
                os.flush();
                mProgressDialog.dismiss();
                mProgressDialog =null;
                /**
                 * 下载完成的提示
                 */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SplashActivity.this,"下载完成,等待安装!", Toast.LENGTH_SHORT).show();
                    }
                });
                installApk(file);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                Closed(is);
                Closed(os);
            }

        }
    }

    /**
     * 安装Apk
     */
    private void installApk(File file) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        Uri data = Uri.fromFile(file);
        Log.d(TAG, "uri:" + data);
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        startActivityForResult(intent, SPLASH_INSTALL);
    }
    /**
     * 接收系统安装页面的反馈
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SPLASH_INSTALL){
            switch(resultCode){
                case Activity.RESULT_OK:
                break;
                case Activity.RESULT_CANCELED:
                    enterHome();
                    break;
            }
        }
    }

    /**
     * 关流的操作
     * @param close
     */
    private void Closed(Closeable close) {
        if (close != null) {
            try {
                close.close();
                close = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 进入主页面
     */
    public void enterHome() {
        boolean guidePage = sp.getBoolean("guidePage", true);
        if (guidePage) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        finish();
    }
}
