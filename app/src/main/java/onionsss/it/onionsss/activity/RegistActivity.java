package onionsss.it.onionsss.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    @Bind(R.id.regist_edt_name)
    EditText regist_edt_name;
    @Bind(R.id.regist_edt_password)
    EditText regist_edt_password;
    @Bind(R.id.regist_btn_regist)
    Button regist_btn_regist;

    // private UserDao ud;
    private static final String REGISTPATH = "http://169.254.163.120:8080/onionsss/RegistServlet";
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
        //ud = new UserDao(this);
        //initListener();
    }

    //用户名重复校验暂时放到提交的时候,后期修改

//    private void initListener() {
//        /**
//         * 用户点击输入帐号时 清空hint
//         */
//        regist_edt_name.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                regist_edt_name.setHint("请输入用户名");
//                regist_edt_name.setHintTextColor(Color.GRAY);
//            }
//        });
//        /**
//         * 直接从账号输入框切换到密码输入框不会触发密码输入框的点击事件
//         * 设置对editText的焦点变化监听
//         * 当点击时  判断帐号是否已经被注册
//         * 如果注册过 就提示用户显示红色
//         *
//         */
//        regist_edt_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                isRegist();
//            }
//        });
//        /**
//         * 因为用户可能按回车 所以不会触发点击事件
//         * 所以我们要监听editText改变时候对帐号进行检测
//         */
//        regist_edt_password.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                isRegist();
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//
//    }
//
//    public void isRegist() {
//        String name = regist_edt_name.getText().toString().trim();
//        if (!TextUtils.isEmpty(name)) {
//            if (ud.queryName(name)) {
//                regist_edt_name.setText("");
//                regist_edt_name.setHint("对不起,该用户名已经被注册!");
//                regist_edt_name.setHintTextColor(Color.RED);
//                /**
//                 * 因为帐号重复
//                 * 所以让用户获得帐号的焦点继续 输入
//                 */
//                regist_edt_name.setFocusable(true);
//                regist_edt_name.setFocusableInTouchMode(true);
//                regist_edt_name.requestFocus();
//            }
//        }
//
//    }

    @OnClick(R.id.regist_btn_regist)
    public void onClick() {
        final String name = regist_edt_name.getText().toString().trim();
        final String password = regist_edt_password.getText().toString().trim();
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("注册中......");
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
                String json = "{'name':'" + name + "','password':'" + password + "'}";
                try {
                    Response response = OkUtils.postResponse(REGISTPATH, json);
                    if (response.code() == 200) {
                        //TODO  将服务端response返回值改为json类型
                        switch (Integer.parseInt(response.toString())) {
                            //服务端解析错误
                            case -1:
                                handler.sendEmptyMessage(REGIST_JSON);
                                break;
                            //用户已注册
                            case 1:
                                handler.sendEmptyMessage(REGIST_NO);
                                break;
                            //注册成功
                            case 2:
                                handler.sendEmptyMessage(REGIST_OK);
                                break;
                            //其他错误
                            default:
                                handler.sendEmptyMessage(REGIST_IO);
                                break;
                        }
                    } else {
                        handler.sendEmptyMessage(REGIST_URL);
                    }

                } catch (IOException e) {
                    handler.sendEmptyMessage(REGIST_IO);
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
