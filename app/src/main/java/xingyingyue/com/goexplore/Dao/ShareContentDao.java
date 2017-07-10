package xingyingyue.com.goexplore.Dao;

import android.content.Context;

import java.util.List;

import xingyingyue.com.goexplore.Bean.ShareContent;
import xingyingyue.com.goexplore.Util.DBOperation;

/**
 * Created by huanghaojian on 17/7/9.
 */

public class ShareContentDao {
    public void saveShareContent(ShareContent shareContent, Context context){
        DBOperation dbOperation=DBOperation.getInstance(context);
        dbOperation.saveShareContentToDB(shareContent);
    }

    public void deleteShareContent(int id,Context context){
        DBOperation dbOperation=DBOperation.getInstance(context);
        dbOperation.deleteShareContent(id);
    }

    public List<ShareContent> queryShareContentList(Context context){
        DBOperation dbOperation=DBOperation.getInstance(context);
        List<ShareContent> shareContentList=dbOperation.queryAllShareContent();
        return shareContentList;
    }

    public List<ShareContent> queryShareContentListById(Context context,int id){
        DBOperation dbOperation=DBOperation.getInstance(context);
        List<ShareContent> shareContentList=dbOperation.queryShareContentById(id);
        return shareContentList;
    }
    public List<ShareContent> queryShareContentListByTitle(Context context,String title){
        DBOperation dbOperation=DBOperation.getInstance(context);
        List<ShareContent> shareContentList=dbOperation.queryShareContentByTitle(title);
        return shareContentList;
    }
}
