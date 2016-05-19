package onionsss.it.onionsss.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;
import onionsss.it.onionsss.R;
import onionsss.it.onionsss.utils.OkUtils;

public class LoginActivity extends AppCompatActivity {

    private static final int LOGIN_URL = 1;
    private static final int LOGIN_IO = 2;
    private static final int LOGIN_JSON = 3;
    private static final int LOGIN_OK = 4;
    private static final int LOGIN_NO = 5;
    @Bind(R.id.login_iv_head)
    ImageView login_iv_head;
    @Bind(R.id.login_edt_name)
    EditText login_edt_name;
    @Bind(R.id.login_edt_password)
    EditText login_edt_password;
    @Bind(R.id.login_cb_remember)
    CheckBox login_cb_remember;
    @Bind(R.id.login_btn_login)
    Button login_btn_login;
    @Bind(R.id.login_btn_regist)
    Button login_btn_regist;
    @Bind(R.id.state)
    TextView state;
    @Bind(R.id.login_layout)
    LinearLayout login_layout;

    private SharedPreferences sp;
    private static final String LOGINPATH = "http://169.254.163.120:8080/onionsss/OnionsssServlet";

    private ProgressDialog mProgressDialog;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN_URL:
                    Toast.makeText(LoginActivity.this, "地址错误", Toast.LENGTH_SHORT).show();
                    break;
                case LOGIN_IO:
                    Toast.makeText(LoginActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    break;
                case LOGIN_JSON:
                    Toast.makeText(LoginActivity.this, "JSON错误", Toast.LENGTH_SHORT).show();
                    break;
                case LOGIN_OK:
                    Toast.makeText(LoginActivity.this, "登录成功!", Toast.LENGTH_SHORT).show();
                    save();
                    mProgressDialog.dismiss();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                    break;
                case LOGIN_NO:
                    Toast.makeText(LoginActivity.this, "帐号或者密码输入错误!", Toast.LENGTH_SHORT).show();
                    login_edt_password.setText("");
                    login_edt_name.setText("");
                    break;

            }
            mProgressDialog.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        initView();
    }

    /**
     * 判断是否点击过帐号密码
     */
    private void initView() {
        boolean save = sp.getBoolean("save", false);
        if (save) {
            login_cb_remember.setChecked(save);
            login_edt_name.setText(sp.getString("name", ""));
            login_edt_password.setText(sp.getString("password", ""));
        } else {
            login_cb_remember.setChecked(save);
        }
    }

    @OnClick({R.id.login_btn_login, R.id.login_btn_regist, R.id.login_cb_remember})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn_login:
                login();
                break;
            case R.id.login_btn_regist:
                //注册页面
                startActivity(new Intent(LoginActivity.this, RegistActivity.class));
                break;
            case R.id.login_cb_remember:
                save();
                break;
        }
    }

    /**
     * 判断是否保存帐号密码
     */
    private void save() {
        String name = login_edt_name.getText().toString().trim();
        String password = login_edt_password.getText().toString().trim();
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
            if (login_cb_remember.isChecked()) {
                sp.edit().putBoolean("save", true).putString("name", name).putString("password", password).apply();
            } else {
                sp.edit().putBoolean("save", false).remove("name").remove("password").apply();
            }
        } else {
            Toast.makeText(LoginActivity.this, "请先输入帐号密码!", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 判断帐号密码是否存在
     */
    private void login() {
        String name = login_edt_name.getText().toString().trim();
        String password = login_edt_password.getText().toString().trim();
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("登录中......");
                mProgressDialog.setCancelable(false);
            }
            mProgressDialog.show();
            LoginThread(name, password);

        } else {
            Toast.makeText(LoginActivity.this, "请输入帐号密码!", Toast.LENGTH_SHORT).show();
        }

    }



    /**
     * 登录子线程
     */
    private void LoginThread(final String name, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //TODO 添加cookie

                String json = "{'requestCode':'2','name':'" + name + "','password':'" + password + "'}";
                try {
                    Response response = OkUtils.postResponse(LOGINPATH, json);
                    if (response.code() == 200) {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if (jsonObject.getString("requestCode").equals("2") && jsonObject.getString("responseCode").equals("1")) {

                            switch (jsonObject.getString("result")) {
                                case "true":
                                    sp.edit().putString("name", name).putString("password", password).apply();
                                    handler.sendEmptyMessageDelayed(LOGIN_OK,1000);
                                    break;
                                case "false":
                                    handler.sendEmptyMessage(LOGIN_NO);
                                    break;
                                default:
                                    handler.sendEmptyMessage(LOGIN_JSON);
                                    break;
                            }
                        } else {
                            handler.sendEmptyMessage(LOGIN_URL);
                        }
                    } else {
                        handler.sendEmptyMessage(LOGIN_JSON);
                    }

                } catch (IOException e) {
                    handler.sendEmptyMessage(LOGIN_IO);
                    e.printStackTrace();
                } catch (Exception e) {
                    handler.sendEmptyMessage(LOGIN_IO);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * onDestroy
     * 如果退出时,name和password都为空
     * 没有必要保存帐号密码
     */
    @Override
    protected void onDestroy() {
        String name = login_edt_name.getText().toString().trim();
        String password = login_edt_password.getText().toString().trim();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
            sp.edit().putBoolean("save", false).apply();
        }
        super.onDestroy();
    }
}
