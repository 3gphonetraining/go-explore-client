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
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xingyingyue.com.goexplore.Adapter.ShareContentAdapter;
import xingyingyue.com.goexplore.Bean.ShareContent;
import xingyingyue.com.goexplore.Dao.ShareContentDao;
import xingyingyue.com.goexplore.R;

/**
 * Created by huanghaojian on 17/6/19.
 */

public class ShareContentListActivity extends BaseActivity{
    private EditText search;
    private Button searchButton;
    private RecyclerView recyclerView;
    private ShareContentAdapter adapter;
    private List<ShareContent> myshareList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstancedState){
        super.onCreate(savedInstancedState);
        setContentView(R.layout.share_content_list);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        initList();
        initView();
    }
    void initView(){
        TextView title=(TextView) findViewById(R.id.title_text);
        title.setText("我的分享");
        search=(EditText)findViewById(R.id.share_content_search);
        searchButton=(Button)findViewById(R.id.share_content_search_button);
        searchButton.setOnClickListener(this);
        Button back=(Button)findViewById(R.id.title_back);
        back.setOnClickListener(this);
        recyclerView=(RecyclerView)findViewById(R.id.share_content_list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter=new ShareContentAdapter(myshareList);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.share_content_search_button:
                searchResult();
                break;
            default:
                break;
        }
    }
    private void searchResult(){
        ShareContentDao shareContentDao=new ShareContentDao();
        myshareList=shareContentDao.queryShareContentListByTitle(ShareContentListActivity.this,search.getText().toString().trim());
        ShareContentAdapter newAdapter=new ShareContentAdapter(myshareList);
        recyclerView.setAdapter(newAdapter);
    }
    private void initList(){
        ShareContentDao shareContentDao=new ShareContentDao();
        myshareList=shareContentDao.queryShareContentList(ShareContentListActivity.this);
    }
    public static void actionStart(Context context){
        Intent intent=new Intent(context,ShareContentListActivity.class);
        context.startActivity(intent);
    }

}
