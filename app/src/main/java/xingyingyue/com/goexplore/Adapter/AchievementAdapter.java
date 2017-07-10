package xingyingyue.com.goexplore.Adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import xingyingyue.com.goexplore.Activity.AchievementListActivity;
import xingyingyue.com.goexplore.Bean.Achievement;
import xingyingyue.com.goexplore.Bean.ShareContent;
import xingyingyue.com.goexplore.R;

/**
 * Created by huanghaojian on 17/7/7.
 */

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.ViewHolder> {
    private List<Achievement>achievementList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView head;
        TextView achievementTitle;
        TextView condition;
        TextView completion;
        TextView state;
        View achievementView;
        public ViewHolder(View view){
            super(view);
            achievementView=view;
            head=(ImageView) view.findViewById(R.id.achievement_item_head);
            achievementTitle=(TextView) view.findViewById(R.id.achievement_item_name);
            condition=(TextView)view.findViewById(R.id.achievement_item_condition);
            completion=(TextView)view.findViewById(R.id.achievement_item_completion);
            state=(TextView)view.findViewById(R.id.achievement_item_state);
        }
    }
    public AchievementAdapter(List<Achievement>achievementList){
        this.achievementList=achievementList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.achievement_item,parent,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position){
        Achievement achievement=achievementList.get(position);
        viewHolder.achievementTitle.setText(achievement.getAchievementName());
        viewHolder.condition.setText(achievement.getCondition());
        viewHolder.completion.setText(Double.toString(achievement.getCompletion())+"%");
        if(achievement.getState()==0){
            viewHolder.state.setText("未获得");
        }else{
            viewHolder.state.setText("已获得");
            viewHolder.state.setTextColor(AchievementListActivity.mContext.getResources().getColor(R.color.text_green));
        }
    }
    @Override
    public int getItemCount(){
        return achievementList.size();
    }
}
