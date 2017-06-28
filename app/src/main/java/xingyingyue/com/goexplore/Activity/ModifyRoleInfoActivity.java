package xingyingyue.com.goexplore.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import xingyingyue.com.goexplore.R;

/**
 * Created by huanghaojian on 17/6/28.
 */

public class ModifyRoleInfoActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        setContentView(R.layout.modify_role_information);
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
        Intent intent=new Intent(context,ModifyRoleInfoActivity.class);
        context.startActivity(intent);
    }
}
