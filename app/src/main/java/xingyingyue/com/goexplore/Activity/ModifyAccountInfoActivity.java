package xingyingyue.com.goexplore.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import xingyingyue.com.goexplore.R;

/**
 * Created by huanghaojian on 17/6/28.
 */

public class ModifyAccountInfoActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_account_information);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        final TextView pwdHint,pwdReHint;
        pwdHint = (TextView)findViewById(R.id.pwdHint);
        pwdReHint = (TextView)findViewById(R.id.pwdReHint);
        pwdHint.setVisibility(View.INVISIBLE);
        pwdReHint.setVisibility(View.INVISIBLE);
        final Button btnlogout;
        btnlogout = (Button)findViewById(R.id.btnlogout);
        btnlogout.setVisibility(View.INVISIBLE);
        final EditText ModPwd,ModPwdAgain;
        ModPwd = (EditText)findViewById(R.id.ModPwd);
        ModPwdAgain = (EditText)findViewById(R.id.ModPwdAgain);
        ModPwd.setVisibility(View.INVISIBLE);
        ModPwdAgain.setVisibility(View.INVISIBLE);

        Button btnchangepwd = (Button)findViewById(R.id.btnchangepsw);
        btnchangepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pwdHint.setVisibility(View.VISIBLE);
                pwdReHint.setVisibility(View.VISIBLE);
                btnlogout.setVisibility(View.VISIBLE);
                ModPwd.setVisibility(View.VISIBLE);
                ModPwdAgain.setVisibility(View.VISIBLE);

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public static void actionStart(Context context){
        Intent intent=new Intent(context,ModifyAccountInfoActivity.class);
        context.startActivity(intent);
    }
}
