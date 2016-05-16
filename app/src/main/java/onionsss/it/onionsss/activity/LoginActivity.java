package onionsss.it.onionsss.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import onionsss.it.onionsss.R;

public class LoginActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        sp = getSharedPreferences("config",MODE_PRIVATE);
        initView();
    }

    private void initView() {
//        sp.edit().putString("name","admin").putString("password","123456").commit();
        if(sp.getBoolean("save",false)){
            login_edt_name.setText(sp.getString("name",""));
            login_edt_password.setText(sp.getString("password",""));
            login_cb_remember.setChecked(true);
        }else{
            login_cb_remember.setChecked(false);
        }

    }

    @OnClick({R.id.login_btn_login, R.id.login_btn_regist,R.id.login_cb_remember})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn_login:
                login();
                break;
            case R.id.login_btn_regist:
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
        if(login_cb_remember.isChecked()){
            sp.edit().putBoolean("save",true).commit();
            login_cb_remember.setChecked(true);
            Toast.makeText(this,"保存密码", Toast.LENGTH_SHORT).show();
        }else{
            sp.edit().putBoolean("save",false).commit();
            login_cb_remember.setChecked(false);
            Toast.makeText(this,"不保存密码", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 简单的SP保存帐号密码逻辑
     */
    private void login() {
        String name = login_edt_name.getText().toString().trim();
        String password = login_edt_password.getText().toString().trim();
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)){
            String spName = sp.getString("name", "");
            String spPassword = sp.getString("password", "");
            if(spName.equals(name) && spPassword.equals(password)){
                Toast.makeText(this,"登录成功", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"帐号密码错误!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"帐号密码不能为空!", Toast.LENGTH_SHORT).show();
        }
    }
}
