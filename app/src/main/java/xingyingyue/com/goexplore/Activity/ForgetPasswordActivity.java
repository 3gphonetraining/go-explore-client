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
 * Created by huanghaojian on 17/6/28.
 */

public class ForgetPasswordActivity extends BaseActivity {
    private EditText inputPhoneNumber;
    private EditText inputPassword;
    private EditText inputVerificationCode;
    private Button getVerificationCode;
    private Button modifyPassword;
    private Button back;
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        setContentView(R.layout.forget_password);
        initView();
    }
    private void initView(){
        inputPhoneNumber=(EditText)findViewById(R.id.forget_password_input_phone_number);
        inputPassword=(EditText)findViewById(R.id.forget_password_input_password);
        inputVerificationCode=(EditText)findViewById(R.id.forget_password_input_verification_code);
        getVerificationCode=(Button)findViewById(R.id.forget_password_get_verification_code);
        modifyPassword=(Button)findViewById(R.id.forget_password_button);
        back=(Button)findViewById(R.id.title_back);
        title=(TextView)findViewById(R.id.title_text);

        title.setText("注册");

        getVerificationCode.setOnClickListener(this);
        modifyPassword.setOnClickListener(this);
        back.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.forget_password_get_verification_code:
                Toast.makeText(this,"click getVerificationCode",Toast.LENGTH_SHORT).show();
                break;
            case R.id.forget_password_button:
                LoginActvity.actionStart(ForgetPasswordActivity.this);
                finish();
                break;
            default:
                break;
        }
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ForgetPasswordActivity.class);
        context.startActivity(intent);
    }
}
