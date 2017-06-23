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
                MainActivity.actionStart(LoginActvity.this);
                finish();
                break;
            case R.id.button_register:
                RegisterActivity.actionStart(LoginActvity.this);
                break;
            case R.id.button_forget_password:
                Toast.makeText(this,"click forgetPassword",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
    public static void actionStart(Context context){
        Intent intent=new Intent(context,LoginActvity.class);
        context.startActivity(intent);
    }
}
