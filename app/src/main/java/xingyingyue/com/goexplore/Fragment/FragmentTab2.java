package xingyingyue.com.goexplore.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import xingyingyue.com.goexplore.Adapter.AchievementAdapter;
import xingyingyue.com.goexplore.Bean.Achievement;
import xingyingyue.com.goexplore.Dao.AchievementDao;
import xingyingyue.com.goexplore.R;

/**
 * Created by huanghaojian on 17/6/28.
 */

public class FragmentTab2 extends Fragment{
    private AchievementDao achievementDao=new AchievementDao();
    private RecyclerView recyclerView;
    private List<Achievement>achievementList=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab2, container, false); //解析布局，此处第三个参数应为false,否则会返回父视图的布局，导致OOM
        initList(view.getContext());
        recyclerView=(RecyclerView)view.findViewById(R.id.get_achievement);
        LinearLayoutManager layoutManager=new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(),DividerItemDecoration.VERTICAL));
        AchievementAdapter achievementAdapter=new AchievementAdapter(achievementList);
        recyclerView.setAdapter(achievementAdapter);
        return view;
    }
    private void initList(Context context){
        achievementList=achievementDao.getAchievementFinish(context);
        if (achievementList.size()<0){
            achievementDao.loadAchievement(context);
            achievementList=achievementDao.getAllAchievement(context);
        }
    }
}
