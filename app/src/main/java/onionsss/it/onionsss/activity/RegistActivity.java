package onionsss.it.onionsss.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;
import onionsss.it.onionsss.R;
import onionsss.it.onionsss.utils.OkUtils;

public class RegistActivity extends AppCompatActivity {

    private static final int REGIST_URL = 1;
    private static final int REGIST_IO = 2;
    private static final int REGIST_JSON = 3;
    private static final int REGIST_OK = 4;
    private static final int REGIST_NO = 5;
    private static final int GET_HEAD = 400;
    @Bind(R.id.regist_edt_name)
    EditText regist_edt_name;
    @Bind(R.id.regist_edt_password)
    EditText regist_edt_password;
    @Bind(R.id.regist_btn_regist)
    Button regist_btn_regist;
    @Bind(R.id.regist_iv_head)
    ImageView login_iv_head;

    private static final String REGISTPATH = "http://169.254.163.120:8080/onionsss/OnionsssServlet";
    private ProgressDialog mProgressDialog;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REGIST_URL:
                    Toast.makeText(RegistActivity.this, "地址错误", Toast.LENGTH_SHORT).show();
                    break;
                case REGIST_IO:
                    Toast.makeText(RegistActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    break;
                case REGIST_JSON:
                    Toast.makeText(RegistActivity.this, "JSON错误", Toast.LENGTH_SHORT).show();
                    regist_edt_name.setText("");
                    regist_edt_password.setText("");
                    break;
                case REGIST_OK:
                    Toast.makeText(RegistActivity.this, "注册成功!", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    finish();
                    break;
                case REGIST_NO:
                    Toast.makeText(RegistActivity.this, "对不起,该用户名已经被注册!", Toast.LENGTH_SHORT).show();
                    regist_edt_name.setText("");
                    regist_edt_password.setText("");
                    break;
            }
            mProgressDialog.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.regist_btn_regist, R.id.regist_iv_head})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.regist_btn_regist:
                registSubmit();
                break;
            case R.id.regist_iv_head:
                changeHead();
                break;
        }

    }

    private void changeHead() {
        Intent intent = new Intent(this, ExplorerActivity.class);
        intent.setDataAndType(Uri.parse("设置头像"), "image/");
        startActivityForResult(intent, GET_HEAD);
    }

    private void registSubmit() {
        final String name = regist_edt_name.getText().toString().trim();
        final String password = regist_edt_password.getText().toString().trim();
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("提交中......");
                mProgressDialog.setCancelable(false);
            }
            mProgressDialog.show();
            RegistThread(name, password);
        } else {
            Toast.makeText(RegistActivity.this, "注册信息不能为空!", Toast.LENGTH_SHORT).show();
        }
    }



    private void RegistThread(final String name, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String json = "{'requestCode':'3','name':'" + name + "','password':'" + password + "'}";
                try {
                    Response response = OkUtils.postResponse(REGISTPATH, json);
                    if (response.code() == 200) {
                        String string = response.body().string();
                        JSONObject jsonObject = new JSONObject(string);
                        if (jsonObject.getString("requestCode").equals("3")) {
                            switch (jsonObject.getString("result")) {
                                //服务端解析错误
                                case "EMPTY":
                                    handler.sendEmptyMessage(REGIST_JSON);
                                    break;
                                //用户已注册
                                case "false":
                                    handler.sendEmptyMessage(REGIST_NO);
                                    break;
                                //注册成功
                                case "true":
                                    handler.sendEmptyMessageDelayed(REGIST_OK, 800);
                                    break;
                                //其他错误
                                default:
                                    handler.sendEmptyMessage(REGIST_IO);
                                    break;
                            }
                        } else {
                            handler.sendEmptyMessage(REGIST_JSON);
                        }
                    } else {
                        handler.sendEmptyMessage(REGIST_URL);
                    }
                } catch (IOException e) {
                    handler.sendEmptyMessage(REGIST_IO);
                    e.printStackTrace();
                } catch (Exception e) {
                    handler.sendEmptyMessage(REGIST_IO);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GET_HEAD:
                setHead(resultCode, data);
                break;
            default:
                break;
        }

    }

    private void setHead(int resultCode, Intent data) {
        String originPath;
        Bitmap bitmap;
        //设置头像
        if (resultCode == RESULT_OK) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            originPath = data.getData().getPath();
            BitmapFactory.decodeFile(originPath, options);
            int outHeight = options.outHeight;
            int outWidth = options.outWidth;
            if ((outWidth > outHeight ? outWidth : outHeight) > 800) {
                int scale = (outWidth > outHeight ? outWidth : outHeight) / 800;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeFile(originPath, options);

            } else {
                bitmap = BitmapFactory.decodeFile(originPath);
            }
            File cacheDir = new File(this.getCacheDir(),"headPic.jpg") ;
            if (cacheDir.exists()) {
                cacheDir.delete();
                Log.d("RegistActivity", "delete");
            }
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, new BufferedOutputStream(new FileOutputStream(cacheDir)));
                login_iv_head.setImageResource(R.mipmap.ic_launcher);
                login_iv_head.setImageURI(Uri.parse(cacheDir.toString()));
            } catch (Exception e) {
                Toast.makeText(this, "图片格式不正确,请重新选择", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            return;
        }
    }
}
