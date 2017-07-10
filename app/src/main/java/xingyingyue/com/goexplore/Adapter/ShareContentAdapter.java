package xingyingyue.com.goexplore.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import xingyingyue.com.goexplore.Activity.ShareContentActivity;
import xingyingyue.com.goexplore.Activity.ShareContentListActivity;
import xingyingyue.com.goexplore.Bean.ShareContent;
import xingyingyue.com.goexplore.R;

/**
 * Created by huanghaojian on 17/6/19.
 */

public class ShareContentAdapter extends RecyclerView.Adapter<ShareContentAdapter.ViewHolder>{
    private List<ShareContent> shareContentList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView head;
        TextView title;
        TextView publicPlace;
        TextView publishTime;
        View shareContentView;
        public ViewHolder(View view){
            super(view);
            shareContentView=view;
            head=(ImageView)view.findViewById(R.id.head);
            title=(TextView)view.findViewById(R.id.item_title);
            publicPlace=(TextView)view.findViewById(R.id.item_publish_place);
            publishTime=(TextView)view.findViewById(R.id.item_publish_time);
        }
    }
    public ShareContentAdapter(List<ShareContent> shareContentList){
        this.shareContentList=shareContentList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.share_content_list_item,parent,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.shareContentView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position=viewHolder.getAdapterPosition();
                ShareContent shareContent=shareContentList.get(position);
                ShareContentActivity.actionStart(v.getContext(),shareContent.getId());
            }
        });
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder,int position){
        ShareContent shareContent=shareContentList.get(position);
        viewHolder.head.setImageResource(R.mipmap.ic_launcher);
        viewHolder.title.setText(shareContent.getTitle());
        viewHolder.publicPlace.setText(shareContent.getPlace());
        viewHolder.publishTime.setText(shareContent.getPublishTime());
    }
    @Override
    public int getItemCount(){
        return shareContentList.size();
    }
}

