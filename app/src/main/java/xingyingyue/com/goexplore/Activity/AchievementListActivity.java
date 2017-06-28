package xingyingyue.com.goexplore.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import xingyingyue.com.goexplore.Adapter.FragmentAdapter;
import xingyingyue.com.goexplore.Fragment.FragmentTab1;
import xingyingyue.com.goexplore.Fragment.FragmentTab2;
import xingyingyue.com.goexplore.Fragment.FragmentTab3;
import xingyingyue.com.goexplore.R;

/**
 * Created by huanghaojian on 17/6/28.
 */

public class AchievementListActivity extends BaseActivity{
    private FrameLayout frameLayout;
    private TextView tab1;
    private TextView tab2;
    private TextView tab3;
    private FragmentTab1 fragmentTab1;
    private FragmentTab2 fragmentTab2;
    private FragmentTab3 fragmentTab3;
    private FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.achievement_list);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initView();

    }


    private void initView(){
        tab1=(TextView)findViewById(R.id.tab1);
        tab2=(TextView)findViewById(R.id.tab2);
        tab3=(TextView)findViewById(R.id.tab3);
        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        tab3.setOnClickListener(this);

        fragmentTransaction=getSupportFragmentManager().beginTransaction();
        tab1.setSelected(true);
        fragmentTab1=new FragmentTab1();
        fragmentTransaction.add(R.id.fragment_container,fragmentTab1);
        fragmentTransaction.commit();
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
    @Override
    public void onClick(View view){
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        switch (view.getId()){
            case R.id.tab1:
                selected();
                tab1.setSelected(true);
                if(fragmentTab1==null){
                    fragmentTab1= new FragmentTab1();
                    transaction.add(R.id.fragment_container,fragmentTab1);
                }else{
                    transaction.show(fragmentTab1);
                }
                break;
            case R.id.tab2:
                selected();
                tab2.setSelected(true);
                if(fragmentTab2==null){
                    fragmentTab2 = new FragmentTab2();
                    transaction.add(R.id.fragment_container,fragmentTab2);
                }else{
                    transaction.show(fragmentTab2);
                }
                break;
            case R.id.tab3:
                selected();
                tab3.setSelected(true);
                if(fragmentTab3==null){
                    fragmentTab3 = new FragmentTab3();
                    transaction.add(R.id.fragment_container,fragmentTab3);
                }else{
                    transaction.show(fragmentTab3);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }
    //重置所有文本的选中状态
    public void selected(){
        tab1.setSelected(false);
        tab2.setSelected(false);
        tab3.setSelected(false);
    }

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction){
        if(fragmentTab1!=null){
            transaction.hide(fragmentTab1);
        }
        if(fragmentTab2!=null){
            transaction.hide(fragmentTab2);
        }
        if(fragmentTab3!=null){
            transaction.hide(fragmentTab3);
        }
    }

    public static void actionStart(Context context){
        Intent intent=new Intent(context,AchievementListActivity.class);
        context.startActivity(intent);
    }
}
