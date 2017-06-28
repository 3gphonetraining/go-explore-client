package xingyingyue.com.goexplore.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import xingyingyue.com.goexplore.R;
import xingyingyue.com.goexplore.Util.OkHttpUtil;

/**
 * Created by huanghaojian on 17/6/14.
 */

public class LoginActvity extends BaseActivity  {
    private EditText inputAccount;
    private EditText inputPassword;
    private Button login;
    private Button register;
    private TextView forgetPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        setContentView(R.layout.login);
        initView();
    }
    private void initView(){
        inputAccount=(EditText)findViewById(R.id.input_account);
        inputPassword=(EditText)findViewById(R.id.input_password);
        login=(Button)findViewById(R.id.button_login);
        register=(Button)findViewById(R.id.button_register);
        forgetPassword=(TextView)findViewById(R.id.button_forget_password);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button_login:
                /*final String phoneNumber=inputAccount.getText().toString().trim();
                final String password=inputPassword.getText().toString().trim();
                if(phoneNumber.trim().equals("")||phoneNumber.length()<0||password.trim().equals("")||password.length()<0){
                    Toast.makeText(this,"请输入完整信息",Toast.LENGTH_SHORT).show();
                    break;
                }else{
                    loginFunc(phoneNumber,password);
                }*/
                MainActivity.actionStart(LoginActvity.this);
                break;
            case R.id.button_register:
                RegisterActivity.actionStart(LoginActvity.this);
                break;
            case R.id.button_forget_password:
                ForgetPasswordActivity.actionStart(LoginActvity.this);
                break;
            default:
                break;
        }
    }
    public static void actionStart(Context context){
        Intent intent=new Intent(context,LoginActvity.class);
        context.startActivity(intent);
    }
    public void loginFunc(final String userAccount,final String password){
        String registerUrl="http://110.64.90.22:8080/login/login?userAccount="+userAccount+"&password="+password;
        OkHttpUtil.sendOkHttpRequest(registerUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActvity.this,"登录失败",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String response1=response.body().string();
                System.out.println(response1);
                if(response1.trim().equals("0")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActvity.this, "用户不存在或密码不正确", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                if(response1.trim().equals("1")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActvity.this, "该账号正在登录", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                if(response1.trim().equals("2")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActvity.this, "登录成功", Toast.LENGTH_LONG).show();
                            MainActivity.actionStart(LoginActvity.this);
                            finish();
                        }
                    });
                }
            }
        });
    }
}
