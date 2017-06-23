package xingyingyue.com.goexplore.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xingyingyue.com.goexplore.Adapter.ShareContentAdapter;
import xingyingyue.com.goexplore.Bean.ShareContent;
import xingyingyue.com.goexplore.R;

/**
 * Created by huanghaojian on 17/6/19.
 */

public class ShareContentListActivity extends BaseActivity{
    private List<ShareContent> myshareList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstancedState){
        super.onCreate(savedInstancedState);
        setContentView(R.layout.share_content_list);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        initView();
        initList();
    }
    void initView(){
        TextView title=(TextView) findViewById(R.id.title_text);
        title.setText("我的分享");
        Button back=(Button)findViewById(R.id.title_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.share_content_list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        ShareContentAdapter adapter=new ShareContentAdapter(myshareList);
        recyclerView.setAdapter(adapter);
    }
    private void initList(){
        ShareContent shareContent=new ShareContent(0,"xingyingyue",R.mipmap.ic_launcher,"no_title","2017-04-16"," ","华南理工大学");
        myshareList.add(shareContent);
        ShareContent shareContent1=new ShareContent(1,"xingyingyue",R.drawable.nav_image,"no_title","2017-04-16"," ","华南理工大学");
        myshareList.add(shareContent1);
        myshareList.add(shareContent1);
        myshareList.add(shareContent1);
        myshareList.add(shareContent1);
    }
    public static void actionStart(Context context){
        Intent intent=new Intent(context,ShareContentListActivity.class);
        context.startActivity(intent);
    }

}
