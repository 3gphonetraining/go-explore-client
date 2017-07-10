package xingyingyue.com.goexplore.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import xingyingyue.com.goexplore.Bean.Achievement;
import xingyingyue.com.goexplore.Bean.Pass;
import xingyingyue.com.goexplore.Bean.ShareContent;
import xingyingyue.com.goexplore.Bean.User;

/**
 * Created by huanghaojian on 17/6/30.
 */

public class DBOperation {
    public static final String DB_NAME="goexplore";
    public static final int version=1;
    private SQLiteDatabase db;
    private static DBOperation dbOperation;

    private DBOperation(Context context) {
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context, DB_NAME, null, version);
        db=dbHelper.getWritableDatabase();
    }

    public  static synchronized DBOperation getInstance(Context context) {
        if (dbOperation == null) {
            dbOperation = new DBOperation(context);
        }
        return dbOperation;
    }

    //储存User实例到数据库
    public void saveUserToDB(User user){
        if(user!=null){
            ContentValues contentValues=new ContentValues();
            contentValues.put("user_account",user.getUserAccount());
            contentValues.put("user_name",user.getUserName());
            contentValues.put("age",user.getAge());
            contentValues.put("experience",user.getExperience());
            contentValues.put("sex",user.getSex());
            contentValues.put("level",user.getLevel());
            db.insert("User",null,contentValues);
        }
    }
    //从数据库读取特定的User信息
    public List<User> readUserByUserAccountFromDB(String userAccount){
        List<User>userList=new ArrayList<>();
        Cursor cursor=db.query("User",null,"user_account=?",new String[]{userAccount},null,null,null);
        if(cursor.moveToFirst()){
            do {
                User user=new User();
                user.setUserAccount(cursor.getString(cursor.getColumnIndex("user_account")));
                user.setUserName(cursor.getString(cursor.getColumnIndex("user_name")));
                user.setAge(cursor.getInt(cursor.getColumnIndex("age")));
                user.setSex(cursor.getString(cursor.getColumnIndex("sex")));
                user.setExperience(cursor.getInt(cursor.getColumnIndex("experience")));
                user.setLevel(cursor.getInt(cursor.getColumnIndex("level")));
                userList.add(user);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return userList;
    }
    //更新User信息
    public void updateUser(String userAccount,User user){
        ContentValues values=new ContentValues();
        values.put("user_name",user.getUserName());
        values.put("age",user.getAge());
        values.put("sex",user.getSex());
        db.update("User",values,"user_account=?",new String[]{userAccount});
    }

    //获得经验值时更新User信息
    public void getExperience(String userAccount){
        List<User>userList=readUserByUserAccountFromDB(userAccount);
        ContentValues values=new ContentValues();
        if (userList.size()>0) {
            User user=userList.get(0);
            int experience=user.getExperience();
            if (experience + 10 > 100) {
                user.setLevel(user.getLevel() + 1);
                user.setExperience(experience + 10 - 100);
                values.put("level", user.getLevel());
                values.put("experience", user.getExperience());
                db.update("User", values, "user_account=?", new String[]{userAccount});
                Log.e("getandup", "success");
            } else {
                user.setExperience(experience + 10);
                values.put("experience", user.getExperience());
                db.update("User", values, "user_account=?", new String[]{userAccount});
                Log.e("getexp", Integer.toString(user.getExperience()));
                Log.e("getexp1", Integer.toString(user.getLevel()));
            }
        }
    }

    //储存Pass实例到数据库
    public void savePassToDB(Pass pass){
        if(pass!=null){
            ContentValues contentValues=new ContentValues();
            contentValues.put("pass_id",pass.getPassId());
            contentValues.put("user_account",pass.getUserAccount());
            contentValues.put("longitude",pass.getLongitude());
            contentValues.put("latitude",pass.getLatitude());
            contentValues.put("content",pass.getContent());
            contentValues.put("address",pass.getAddress());
            contentValues.put("experience",pass.getExperience());
            contentValues.put("state",pass.getState());
            db.insert("Pass",null,contentValues);
        }
    }

    //从数据库读取所有Pass信息
    public List<Pass> readPassFromDB(){
        List<Pass>passList=new ArrayList<>();
        Cursor cursor=db.query("Pass",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Pass pass=new Pass();
                pass.setPassId(cursor.getString(cursor.getColumnIndex("pass_id")));
                pass.setUserAccount(cursor.getString(cursor.getColumnIndex("user_account")));
                pass.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
                pass.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
                pass.setContent(cursor.getString(cursor.getColumnIndex("content")));
                pass.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                pass.setExperience(cursor.getInt(cursor.getColumnIndex("experience")));
                pass.setState(cursor.getInt(cursor.getColumnIndex("state")));
                passList.add(pass);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return passList;
    }

    //根据经纬度从数据库取出某个关节点的信息
    public List<Pass> readPassByPlaceFromDB(double latitude,double longitude){
        List<Pass>passList=new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from Pass where latitude=? and longitude=?",new String[]{String.valueOf(latitude),String.valueOf(longitude)});
        if(cursor.moveToFirst()){
            do{
                Pass pass=new Pass();
                pass.setPassId(cursor.getString(cursor.getColumnIndex("pass_id")));
                pass.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
                pass.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
                pass.setContent(cursor.getString(cursor.getColumnIndex("content")));
                pass.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                pass.setExperience(cursor.getInt(cursor.getColumnIndex("experience")));
                pass.setState(cursor.getInt(cursor.getColumnIndex("state")));
                passList.add(pass);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return passList;
    }

    //根据经纬度从数据库取出某个关节点的信息
    public List<Pass> readPassByIdFromDB(String passId){
        List<Pass>passList=new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from Pass where pass_id=?",new String[]{passId});
        if(cursor.moveToFirst()){
            do{
                Pass pass=new Pass();
                pass.setPassId(cursor.getString(cursor.getColumnIndex("pass_id")));
                pass.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
                pass.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
                pass.setContent(cursor.getString(cursor.getColumnIndex("content")));
                pass.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                pass.setExperience(cursor.getInt(cursor.getColumnIndex("experience")));
                pass.setState(cursor.getInt(cursor.getColumnIndex("state")));
                passList.add(pass);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return passList;
    }

    //根据passId更新pass信息
    public void updatePassInfo(String passId){
        ContentValues values=new ContentValues();
        values.put("state",1);
        db.update("Pass",values,"pass_id=?",new String[]{passId});
    }

    //清除数据库中所有的Pass信息
    public void deleteAllPass(){
        db.delete("Pass",null,null);
    }

    //储存成就信息到数据库
    public void saveAchievementToDB(Achievement achievement){
        ContentValues values=new ContentValues();
        values.put("user_account",achievement.getUserAccount());
        values.put("achievement_id",achievement.getAchievementId());
        values.put("achievement_name",achievement.getAchievementName());
        values.put("completion",achievement.getCompletion());
        values.put("state",achievement.getState());
        values.put("content",achievement.getContent());
        values.put("condition",achievement.getCondition());
        values.put("condition_desc",achievement.getConditionDesc());
        values.put("pass_list",achievement.getPassList());
        db.insert("Achievement",null,values);
    }

    //从数据库中取出成就信息
    public List<Achievement> readAchievementFromDB(String achievementId){
        List<Achievement>achievements=new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from Achievement where achievement_id=?",new String[]{achievementId});
        if(cursor.moveToFirst()){
            do{
                Achievement achievement=new Achievement();
                achievement.setUserAccount(cursor.getString(cursor.getColumnIndex("user_account")));
                achievement.setAchievementId(cursor.getString(cursor.getColumnIndex("achievement_id")));
                achievement.setAchievementName(cursor.getString(cursor.getColumnIndex("achievement_name")));
                achievement.setCompletion(cursor.getDouble(cursor.getColumnIndex("completion")));
                achievement.setState(cursor.getInt(cursor.getColumnIndex("state")));
                achievement.setContent(cursor.getString(cursor.getColumnIndex("content")));
                achievement.setCondition(cursor.getString(cursor.getColumnIndex("condition")));
                achievement.setConditionDesc(cursor.getString(cursor.getColumnIndex("condition_desc")));
                achievement.setPassList(cursor.getString(cursor.getColumnIndex("pass_list")));
                achievements.add(achievement);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return achievements;
    }

    //从数据库读取所有成就信息
    public List<Achievement> readAllAchievementFromDB(){
        List<Achievement>achievements=new ArrayList<>();
        Cursor cursor=db.query("Achievement",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Achievement achievement=new Achievement();
                achievement.setUserAccount(cursor.getString(cursor.getColumnIndex("user_account")));
                achievement.setAchievementId(cursor.getString(cursor.getColumnIndex("achievement_id")));
                achievement.setAchievementName(cursor.getString(cursor.getColumnIndex("achievement_name")));
                achievement.setCompletion(cursor.getDouble(cursor.getColumnIndex("completion")));
                achievement.setState(cursor.getInt(cursor.getColumnIndex("state")));
                achievement.setContent(cursor.getString(cursor.getColumnIndex("content")));
                achievement.setCondition(cursor.getString(cursor.getColumnIndex("condition")));
                achievement.setConditionDesc(cursor.getString(cursor.getColumnIndex("condition_desc")));
                achievement.setPassList(cursor.getString(cursor.getColumnIndex("pass_list")));
                achievements.add(achievement);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return achievements;
    }

    //从数据库读取已获得成就信息
    public List<Achievement> readAllAchievementFinishFromDB(){
        List<Achievement>achievements=new ArrayList<>();
        Cursor cursor=db.query("Achievement",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Achievement achievement=new Achievement();
                achievement.setUserAccount(cursor.getString(cursor.getColumnIndex("user_account")));
                achievement.setAchievementId(cursor.getString(cursor.getColumnIndex("achievement_id")));
                achievement.setAchievementName(cursor.getString(cursor.getColumnIndex("achievement_name")));
                achievement.setCompletion(cursor.getDouble(cursor.getColumnIndex("completion")));
                achievement.setState(cursor.getInt(cursor.getColumnIndex("state")));
                achievement.setContent(cursor.getString(cursor.getColumnIndex("content")));
                achievement.setCondition(cursor.getString(cursor.getColumnIndex("condition")));
                achievement.setConditionDesc(cursor.getString(cursor.getColumnIndex("condition_desc")));
                achievement.setPassList(cursor.getString(cursor.getColumnIndex("pass_list")));
                if (achievement.getState()!=0) {
                    achievements.add(achievement);
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
        return achievements;
    }

    //从数据库读取未获得成就信息
    public List<Achievement> readAllAchievementNoFinishFromDB(){
        List<Achievement>achievements=new ArrayList<>();
        Cursor cursor=db.query("Achievement",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Achievement achievement=new Achievement();
                achievement.setUserAccount(cursor.getString(cursor.getColumnIndex("user_account")));
                achievement.setAchievementId(cursor.getString(cursor.getColumnIndex("achievement_id")));
                achievement.setAchievementName(cursor.getString(cursor.getColumnIndex("achievement_name")));
                achievement.setCompletion(cursor.getDouble(cursor.getColumnIndex("completion")));
                achievement.setState(cursor.getInt(cursor.getColumnIndex("state")));
                achievement.setContent(cursor.getString(cursor.getColumnIndex("content")));
                achievement.setCondition(cursor.getString(cursor.getColumnIndex("condition")));
                achievement.setConditionDesc(cursor.getString(cursor.getColumnIndex("condition_desc")));
                achievement.setPassList(cursor.getString(cursor.getColumnIndex("pass_list")));
                if (achievement.getState()==0) {
                    achievements.add(achievement);
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
        return achievements;
    }

    //根据achievement_id更新成就信息
    public void updateAchievement(Achievement achievement){
        ContentValues values=new ContentValues();
        values.put("user_account",achievement.getUserAccount());
        values.put("achievement_id",achievement.getAchievementId());
        values.put("achievement_name",achievement.getAchievementName());
        values.put("completion",achievement.getCompletion());
        values.put("state",achievement.getState());
        values.put("content",achievement.getContent());
        values.put("pass_list",achievement.getPassList());
        values.put("condition",achievement.getCondition());
        values.put("condition_desc",achievement.getConditionDesc());
        db.update("Achievement",values,"achievement_id=?",new String[]{achievement.getAchievementId()});
    }

    //删除成就信息
    public void deleteAllAchievement(){
        db.delete("Achievement",null,null);
    }

    //储存分享内容到数据库
    public void saveShareContentToDB(ShareContent shareContent){
        ContentValues values=new ContentValues();
        values.put("user_account",shareContent.getUserAccount());
        values.put("user_name",shareContent.getUserName());
        values.put("title",shareContent.getTitle());
        values.put("publish_time",shareContent.getPublishTime());
        values.put("content",shareContent.getContent());
        values.put("place",shareContent.getPlace());
        values.put("latitude",shareContent.getLatitude());
        values.put("longitude",shareContent.getLongitude());
        db.insert("ShareContent",null,values);
    }

    //删除分享内容
    public void deleteShareContent(int id){
        db.delete("ShareContent","id=?",new String[]{Integer.toString(id)});
    }

    //读取所有的分享内容
    public List<ShareContent> queryAllShareContent(){
        List<ShareContent>shareContentList=new ArrayList<>();
        Cursor cursor=db.query("ShareContent",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                ShareContent shareContent=new ShareContent();
                shareContent.setUserAccount(cursor.getString(cursor.getColumnIndex("user_account")));
                shareContent.setUserName(cursor.getString(cursor.getColumnIndex("user_name")));
                shareContent.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                shareContent.setPublishTime(cursor.getString(cursor.getColumnIndex("publish_time")));
                shareContent.setContent(cursor.getString(cursor.getColumnIndex("content")));
                shareContent.setPlace(cursor.getString(cursor.getColumnIndex("place")));
                shareContent.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
                shareContent.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
                shareContentList.add(shareContent);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return shareContentList;
    }

    //根据标题读取分享内容
    public List<ShareContent> queryShareContentByTitle(String title){
        List<ShareContent>shareContentList=new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from ShareContent where title=?",new String[]{title});
        if(cursor.moveToFirst()){
            do{
                ShareContent shareContent=new ShareContent();
                shareContent.setUserAccount(cursor.getString(cursor.getColumnIndex("user_account")));
                shareContent.setUserName(cursor.getString(cursor.getColumnIndex("user_name")));
                shareContent.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                shareContent.setPublishTime(cursor.getString(cursor.getColumnIndex("publish_time")));
                shareContent.setContent(cursor.getString(cursor.getColumnIndex("content")));
                shareContent.setPlace(cursor.getString(cursor.getColumnIndex("place")));
                shareContent.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
                shareContent.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
                shareContentList.add(shareContent);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return shareContentList;
    }

    //根据id读取分享内容
    public List<ShareContent> queryShareContentById(int id){
        List<ShareContent>shareContentList=new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from ShareContent where id=?",new String[]{Integer.toString(id)});
        if(cursor.moveToFirst()){
            do{
                ShareContent shareContent=new ShareContent();
                shareContent.setUserAccount(cursor.getString(cursor.getColumnIndex("user_account")));
                shareContent.setUserName(cursor.getString(cursor.getColumnIndex("user_name")));
                shareContent.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                shareContent.setPublishTime(cursor.getString(cursor.getColumnIndex("publish_time")));
                shareContent.setContent(cursor.getString(cursor.getColumnIndex("content")));
                shareContent.setPlace(cursor.getString(cursor.getColumnIndex("place")));
                shareContent.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
                shareContent.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
                shareContentList.add(shareContent);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return shareContentList;
    }
}
