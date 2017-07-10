package xingyingyue.com.goexplore.Bean;

import java.util.List;

/**
 * Created by huanghaojian on 17/7/7.
 */

public class Achievement {
    private String userAccount;
    private String achievementId;
    private String achievementName;
    private double completion;
    private int state;
    private String content;
    public String condition;
    public String conditionDesc;
    private String passList;

    public void setUserAccount(String userAccount){
        this.userAccount=userAccount;
    }
    public String getUserAccount(){
        return userAccount;
    }
    public void setAchievementId(String achievementId){
        this.achievementId=achievementId;
    }
    public String getAchievementId(){
        return achievementId;
    }
    public void setAchievementName(String achievementName){
        this.achievementName=achievementName;
    }
    public String getAchievementName(){
        return achievementName;
    }
    public void setCompletion(double completion){
        this.completion=completion;
    }
    public double getCompletion(){
        return completion;
    }
    public void setState(int state){
        this.state=state;
    }
    public int getState(){
        return state;
    }
    public void setContent(String content){
        this.content=content;
    }
    public String getContent(){
        return content;
    }
    public void setCondition(String condition){
        this.condition=condition;
    }
    public String getCondition(){
        return condition;
    }
    public void setConditionDesc(String conditionDesc){
        this.conditionDesc=conditionDesc;
    }
    public String getConditionDesc(){
        return conditionDesc;
    }
    public void setPassList(String passList){
        this.passList=passList;
    }
    public String getPassList(){
        return passList;
    }
}
