package com.example.migamecenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.migamecenter.httpUtils.HttpManager;
import com.example.migamecenter.httpUtils.LoginCallBack;


public class LoginActivity extends FragmentActivity {

    private Boolean flag_phone_entered = false;
    private TextView tv_user_privacy;

    private EditText et_user_code;

    private EditText et_phone;

    private RadioButton radioButton;

    private Button getCodeButton;

    private Button btn_login;
    private CountDownTimer countDownTimer;
    private final long COUNTDOWN_TIME = 60000; // 60秒倒计时
    private final long INTERVAL = 1000; // 每次间隔1秒

    boolean isToastDisplayed = false;
    boolean isToastDisplayed_phone = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        tv_user_privacy = findViewById(R.id.tv_user_privacy);
        getCodeButton = findViewById(R.id.btn_get_code);
        et_user_code = findViewById(R.id.et_user_code);
        radioButton = findViewById(R.id.radioButton);
        et_phone = findViewById(R.id.et_phone);
        btn_login = findViewById(R.id.btn_login);

        bindListener();
        click_user_privacy();
    }

    private void bindListener(){
        getCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode();

            }
        });

        // 手机号文本框的监听
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 当手机号码输入框的内容发生变化时调用
                String phoneNumber = s.toString();
                if (phoneNumber.length() == 11) {
                    // 如果手机号码长度为11位，则启用获取验证码按钮
                    getCodeButton.setEnabled(true);
                    flag_phone_entered = true;
                } else {
                    // 否则禁用获取验证码按钮
                    getCodeButton.setEnabled(false);

                    if(!isToastDisplayed_phone){
                        Toast.makeText(LoginActivity.this,"请输入完整的手机号",Toast.LENGTH_SHORT).show();
                        isToastDisplayed_phone = true;
                    }


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 验证码文本框的监听
        et_user_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (flag_phone_entered) {
                    getCodeButton.setEnabled(true);
                    btn_login.setEnabled(true);
                } else {
                    // 否则禁用获取验证码按钮
                    getCodeButton.setEnabled(false);

                    if(!isToastDisplayed){
                        Toast.makeText(LoginActivity.this,"验证码为6位",Toast.LENGTH_SHORT).show();
                        isToastDisplayed = true;
                    }

                }
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!radioButton.isChecked()){
                    Toast.makeText(LoginActivity.this,"先同意隐私协议才可以登录哟~",Toast.LENGTH_SHORT).show();
                }else {

                    String per_phone = et_phone.getText().toString().trim();
                    String per_smsCode = et_user_code.getText().toString().trim();

                    HttpManager.getInstance().login(per_phone, per_smsCode, new LoginCallBack() {
                        @Override
                        public void onSuccess(String content) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String code = extractCodeString(content);
                                    Log.i("LoginActivity","content"+content);
                                    Log.i("LoginActivity","content"+code);
                                    if (code.equals("\"code\":200")){
                                        Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                        //设置按钮不可点击
                                        btn_login.setEnabled(false);
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Log.i("LoginActivity","content"+errorMessage);
                        }
                    });

                }
            }
        });

    }
    private String extractCodeString(String response) {

        int startIndex = response.indexOf("\"code\":");
        if (startIndex == -1) {
            // 如果未找到匹配的内容，返回空字符串或者抛出异常
            return "";
        }

        // 从起始位置开始提取 "code":400 这一部分字符串
        int endIndex = response.indexOf(",", startIndex); // 逗号为分隔符
        if (endIndex == -1) {
            // 如果未找到分隔符，直接使用字符串的长度作为结束位置
            endIndex = response.length();
        }

        // 提取子字符串
        return response.substring(startIndex, endIndex);
    }

    private void click_user_privacy(){
        String text = tv_user_privacy.getText().toString().trim();
        SpannableString spannableString = new SpannableString(text);

        // 找到 "用户协议" 和 "隐私协议" 在文本中的起始和结束位置
        int startUserAgreement = text.indexOf("用户协议");
        int endUserAgreement = startUserAgreement + "用户协议".length();
        int startPrivacyPolicy = text.indexOf("隐私协议");
        int endPrivacyPolicy = startPrivacyPolicy + "隐私协议".length();

        // 创建 ClickableSpan 对象
        ClickableSpan clickableUserAgreement = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                // 用户协议点击事件
                Toast.makeText(LoginActivity.this, "查看用户协议", Toast.LENGTH_SHORT).show();
            }
        };

        ClickableSpan clickablePrivacyPolicy = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                // 隐私协议点击事件
                Toast.makeText(LoginActivity.this, "查看隐私协议", Toast.LENGTH_SHORT).show();
            }
        };

        // 将 ClickableSpan 应用到相应的文本区域
        spannableString.setSpan(clickableUserAgreement, startUserAgreement, endUserAgreement, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(clickablePrivacyPolicy, startPrivacyPolicy, endPrivacyPolicy, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 将 SpannableString 设置到 TextView 中
        tv_user_privacy.setText(spannableString);
        tv_user_privacy.setMovementMethod(LinkMovementMethod.getInstance()); // 设置文本可点击
    }

    private void sendVerificationCode() {
        // 模拟发送验证码网络请求
        Toast.makeText(this, "验证码已发送", Toast.LENGTH_SHORT).show();

        if (getCodeButton.isEnabled()){
            String phoneNumber = et_phone.getText().toString().trim();
//            Toast.makeText(LoginActivity.this,"phoneNumber:"+phoneNumber,Toast.LENGTH_SHORT).show();
            Log.i("LoginActivity","开始发送验证码~~~");
            HttpManager.getInstance().sendCode(phoneNumber);
        }
        // 禁用获取验证码按钮
        getCodeButton.setEnabled(false);

        // 开始倒计时
        startCountDownTimer();
    }

    private void startCountDownTimer() {
        countDownTimer = new CountDownTimer(COUNTDOWN_TIME, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 更新按钮文字，显示倒计时
                getCodeButton.setText(getString(R.string.resend_code, millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                // 倒计时结束，恢复按钮状态
                getCodeButton.setEnabled(true);
                getCodeButton.setText("获取验证码");
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 销毁Activity时停止倒计时
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
