package onionsss.it.onionsss.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import onionsss.it.onionsss.R;
import onionsss.it.onionsss.bean.User;
import onionsss.it.onionsss.dao.UserDao;

public class RegistActivity extends Activity {

    @Bind(R.id.regist_edt_name)
    EditText regist_edt_name;
    @Bind(R.id.regist_edt_password)
    EditText regist_edt_password;
    @Bind(R.id.regist_btn_regist)
    Button regist_btn_regist;

    private UserDao ud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);
        ud = new UserDao(this);
        initListener();
    }

    private void initListener() {
        /**
         * 用户点击输入帐号时 清空hint
         */
        regist_edt_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regist_edt_name.setHint("请输入用户名");
                regist_edt_name.setHintTextColor(Color.GRAY);
            }
        });
        /**
         * 直接从账号输入框切换到密码输入框不会触发密码输入框的点击事件
         * 设置对editText的焦点变化监听
         * 当点击时  判断帐号是否已经被注册
         * 如果注册过 就提示用户显示红色
         *
         */
        regist_edt_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {isRegist();
            }
        });
        /**
         * 因为用户可能按回车 所以不会触发点击事件
         * 所以我们要监听editText改变时候对帐号进行检测
         */
        regist_edt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                isRegist();
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }
    public void isRegist(){
        String name = regist_edt_name.getText().toString().trim();
        if (!TextUtils.isEmpty(name)){
            if(ud.queryName(name)){
                regist_edt_name.setText("");
                regist_edt_name.setHint("对不起,该用户名已经被注册!");
                regist_edt_name.setHintTextColor(Color.RED);
                /**
                 * 因为帐号重复
                 * 所以让用户获得帐号的焦点继续 输入
                 */
                regist_edt_name.setFocusable(true);
                regist_edt_name.setFocusableInTouchMode(true);
                regist_edt_name.requestFocus();
            }
        }

    }
    @OnClick(R.id.regist_btn_regist)
    public void onClick() {
        String name = regist_edt_name.getText().toString().trim();
        String password = regist_edt_password.getText().toString().trim();
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)){
            User user = new User(name,password);
            boolean insert = ud.insert(user);
            if(insert){
                Toast.makeText(RegistActivity.this,"注册成功!", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(RegistActivity.this,"注册失败!", Toast.LENGTH_SHORT).show();
                regist_edt_name.setText("");
                regist_edt_password.setText("");
            }
        }else{
            Toast.makeText(RegistActivity.this,"注册信息不能为空!", Toast.LENGTH_SHORT).show();
        }
    }
}
