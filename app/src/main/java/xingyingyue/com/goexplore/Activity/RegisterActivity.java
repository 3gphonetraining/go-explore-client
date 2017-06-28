package xingyingyue.com.goexplore.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import xingyingyue.com.goexplore.Dao.UserDao;
import xingyingyue.com.goexplore.R;
import xingyingyue.com.goexplore.Util.OkHttpUtil;

/**
 * Created by huanghaojian on 17/6/15.
 */

public class RegisterActivity extends BaseActivity {
    private EditText inputPhoneNumber;
    private EditText inputPassword;
    private EditText inputVerificationCode;
    private Button getVerificationCode;
    private Button register;
    private Button back;
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        setContentView(R.layout.register);
        initView();
    }
    private void initView(){
        inputPhoneNumber=(EditText)findViewById(R.id.register_input_phone_number);
        inputPassword=(EditText)findViewById(R.id.register_input_password);
        inputVerificationCode=(EditText)findViewById(R.id.register_input_verification_code);
        getVerificationCode=(Button)findViewById(R.id.register_get_verification_code);
        register=(Button)findViewById(R.id.register_button_reigster);
        back=(Button)findViewById(R.id.title_back);
        title=(TextView)findViewById(R.id.title_text);

        title.setText("注册");

        getVerificationCode.setOnClickListener(this);
        register.setOnClickListener(this);
        back.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.register_get_verification_code:
                Toast.makeText(this,"click getVerificationCode",Toast.LENGTH_SHORT).show();
                break;
            case R.id.register_button_reigster:
                final String phoneNumber=inputPhoneNumber.getText().toString().trim();
                final String password=inputPassword.getText().toString().trim();
                String code=inputVerificationCode.getText().toString().trim();
                if(phoneNumber.trim().equals("")||phoneNumber.length()<0||password.trim().equals("")||password.length()<0||code.trim().equals("")||code.length()<0){
                    Toast.makeText(this,"请输入完整信息",Toast.LENGTH_SHORT).show();
                    break;
                }else{
                    registerFunc(phoneNumber,password);
                }
                break;
            default:
                break;
        }
    }

    public static void actionStart(Context context){
        Intent intent=new Intent(context,RegisterActivity.class);
        context.startActivity(intent);
    }
    public void registerFunc(final String userAccount,final String password){
        String registerUrl="http://110.64.90.22:8080/login/register?userAccount="+userAccount+"&password="+password;
        OkHttpUtil.sendOkHttpRequest(registerUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String response1=response.body().string();
                System.out.println(response1);
                if(response1.trim().equals("0")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "号码已被注册", Toast.LENGTH_LONG).show();
                            LoginActvity.actionStart(RegisterActivity.this);
                        }
                    });
                }
                if(response1.trim().equals("1")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                            LoginActvity.actionStart(RegisterActivity.this);
                            finish();
                        }
                    });
                }
                if(response1.trim().equals("2")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_LONG).show();
                            LoginActvity.actionStart(RegisterActivity.this);
                        }
                    });
                }
            }
        });
    }
}
