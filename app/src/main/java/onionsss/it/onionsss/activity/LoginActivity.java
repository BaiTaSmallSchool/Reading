package onionsss.it.onionsss.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import onionsss.it.onionsss.R;
import onionsss.it.onionsss.bean.User;
import onionsss.it.onionsss.dao.UserDao;

public class LoginActivity extends Activity {

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
    private UserDao ud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        ud = new UserDao(this);
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
                sp.edit().putBoolean("save", true).putString("name", name).putString("password", password).commit();
            } else {
                sp.edit().putBoolean("save", false).remove("name").remove("password").commit();
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
            User user = ud.haveAccount(name);
            if (user != null && name.equals(user.getName()) && password.equals(user.getPassword())) {
                Toast.makeText(LoginActivity.this, "登录成功!", Toast.LENGTH_SHORT).show();
                sp.edit().putString("name", name).putString("password", password).commit();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "帐号或者密码输入错误!", Toast.LENGTH_SHORT).show();
                login_edt_password.setText("");
                login_edt_name.setText("");
            }
        } else {
            Toast.makeText(LoginActivity.this, "请输入帐号密码!", Toast.LENGTH_SHORT).show();
        }
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
            sp.edit().putBoolean("save",false).commit();
        }
        super.onDestroy();
    }
}
