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

import xingyingyue.com.goexplore.R;

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
                LoginActvity.actionStart(RegisterActivity.this);
                break;
            default:
                break;
        }
    }
    public static void actionStart(Context context){
        Intent intent=new Intent(context,RegisterActivity.class);
        context.startActivity(intent);
    }
}
