package xingyingyue.com.goexplore.Dao;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import xingyingyue.com.goexplore.Bean.Achievement;
import xingyingyue.com.goexplore.Bean.Pass;
import xingyingyue.com.goexplore.Bean.User;
import xingyingyue.com.goexplore.Util.DBOperation;
import xingyingyue.com.goexplore.View.MyToast;

/**
 * Created by huanghaojian on 17/7/8.
 */

public class AchievementDao {
    //从服务器端加载成就信息到数据库
    public void loadAchievement(Context context){
        DBOperation dbOperation=DBOperation.getInstance(context);
        List<Achievement>achievementList=new ArrayList<>();
        Achievement achievement=new Achievement();
        achievement.setUserAccount("18826075488");
        achievement.setAchievementId("华工之旅");
        achievement.setAchievementName("华工之旅");
        achievement.setCompletion(0);
        achievement.setState(0);
        achievement.setCondition("点亮华工大学城区域关节点");
        achievement.setConditionDesc("无");
        achievement.setPassList("华工b7教学楼,华工学生公寓,华工行政办公楼,华工第二饭堂,华工学术大讲堂");
        achievementList.add(achievement);

        Achievement achievement1=new Achievement();
        achievement1.setUserAccount("18826075488");
        achievement1.setAchievementId("中大之旅");
        achievement1.setAchievementName("中大之旅");
        achievement1.setCompletion(0);
        achievement1.setState(0);
        achievement1.setCondition("点亮中大大学城区域关节点");
        achievement1.setConditionDesc("无");
        achievement1.setPassList("中大公共教学楼,中大第二食堂");
        achievementList.add(achievement1);

        Achievement achievement2=new Achievement();
        achievement2.setUserAccount("18826075488");
        achievement2.setAchievementId("岭南印象园之旅");
        achievement2.setAchievementName("岭南印象园之旅");
        achievement2.setCompletion(100);
        achievement2.setState(1);
        achievement2.setCondition("点亮岭南印象园区域关节点");
        achievement2.setConditionDesc("无");
        achievement2.setPassList("岭南印象园公交站,岭南印象园小吃,岭南印象园包公庙");
        achievementList.add(achievement2);

        Achievement achievement3=new Achievement();
        achievement3.setUserAccount("18826075488");
        achievement3.setAchievementId("等级10");
        achievement3.setAchievementName("旅游新手");
        achievement3.setCompletion(10);
        achievement3.setState(0);
        achievement3.setCondition("等级达到10级");
        achievement3.setConditionDesc("10");
        achievement3.setPassList("无");
        achievementList.add(achievement3);

        Achievement achievement4=new Achievement();
        achievement4.setUserAccount("18826075488");
        achievement4.setAchievementId("等级50");
        achievement4.setAchievementName("旅游大师");
        achievement4.setCompletion(2);
        achievement4.setState(0);
        achievement4.setCondition("等级达到50级");
        achievement4.setConditionDesc("50");
        achievement4.setPassList("无");
        achievementList.add(achievement4);

        for (int i = 0; i < achievementList.size(); i++) {
            Achievement achievementTemp=achievementList.get(i);
            dbOperation.saveAchievementToDB(achievementTemp);
        }
    }

    //根据achievement_id获取成就信息
    public List<Achievement> getAchievementById(Context context,String achievementId){
        DBOperation dbOperation = DBOperation.getInstance(context);
        List<Achievement>achievements=dbOperation.readAchievementFromDB(achievementId);
        return achievements;
    }

    //获取全部成就信息
    public List<Achievement> getAllAchievement(Context context){
        DBOperation dbOperation=DBOperation.getInstance(context);
        List<Achievement>achievementList=dbOperation.readAllAchievementFromDB();
        return  achievementList;
    }

    //获取已完成的成就信息
    public List<Achievement> getAchievementFinish(Context context){
        DBOperation dbOperation=DBOperation.getInstance(context);
        List<Achievement>achievementList=dbOperation.readAllAchievementFinishFromDB();
        return  achievementList;
    }

    //获取未完成的成就信息
    public List<Achievement> getAchievementNoFinish(Context context){
        DBOperation dbOperation=DBOperation.getInstance(context);
        List<Achievement>achievementList=dbOperation.readAllAchievementNoFinishFromDB();
        return  achievementList;
    }

    //删除所有成就信息
    public void deleteAllAchievement(Context context){
        DBOperation dbOperation=DBOperation.getInstance(context);
        dbOperation.deleteAllAchievement();
    }

    //点亮关节点时更新相关信息
    public void updateAchievement(Context context,String userAccount){
        DBOperation dbOperation=DBOperation.getInstance(context);
        List<Achievement>achievementList=dbOperation.readAllAchievementFromDB();
        for(int i=0;i<achievementList.size();i++){
            Achievement achievement=achievementList.get(i);
            String[]passIdList=parsePassIdList(context,achievement.getAchievementId());
            int spotNumebr=0;//点亮的关节点数
            if (passIdList[0].equals("无")) {
                List<User>userList=dbOperation.readUserByUserAccountFromDB(userAccount);
                if (userList.size()>0){
                    User user=userList.get(0);
                    // 创建一个数值格式化对象
                    NumberFormat numberFormat = NumberFormat.getInstance();
                    // 设置精确到小数点后2位
                    numberFormat.setMaximumFractionDigits(2);
                    String result = numberFormat.format((float) user.getLevel() / (float) Integer.parseInt(achievement.getConditionDesc()) * 100);
                    double completion = Double.parseDouble(result);
                    achievement.setCompletion(completion);
                    if (completion == 100) {
                        achievement.setState(1);
                        MyToast.makeText(context, "恭喜获得\"" + achievement.getAchievementName() + "\"称号", Toast.LENGTH_LONG).show();
                    }
                    dbOperation.updateAchievement(achievement);
                }
            }else{
                for (int j = 0; j < passIdList.length; j++) {
                    List<Pass> passList = dbOperation.readPassByIdFromDB(passIdList[j]);
                    if (passList.size() > 0) {
                        Pass pass = passList.get(0);
                        if (pass.getState() != 0)
                            spotNumebr++;
                    }
                }
                // 创建一个数值格式化对象
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 设置精确到小数点后2位
                numberFormat.setMaximumFractionDigits(2);
                String result = numberFormat.format((float) spotNumebr / (float) passIdList.length * 100);
                double completion = Double.parseDouble(result);
                achievement.setCompletion(completion);
                if (completion == 100) {
                    achievement.setState(1);
                    MyToast.makeText(context, "恭喜获得\"" + achievement.getAchievementName() + "\"称号", Toast.LENGTH_LONG).show();
                }
                dbOperation.updateAchievement(achievement);
            }
        }
    }

    //解析pass_list的内容并返回
    public String[] parsePassIdList(Context context,String achievementId){
        DBOperation dbOperation=DBOperation.getInstance(context);
        List<Achievement>achievementList=dbOperation.readAchievementFromDB(achievementId);
        String[]passIdList=null;
        if(achievementList.size()>0){
            Achievement achievement=achievementList.get(0);
            String content=achievement.getPassList();
            if(!TextUtils.isEmpty(content)){
                Log.e("find","yes");
                passIdList=content.split(",");
            }
        }
        return passIdList;
    }
}
