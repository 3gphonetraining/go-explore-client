package xingyingyue.com.goexplore.Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by huanghaojian on 17/6/30.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_PASS="create table Pass("
            +"pass_id text primary key,"
            +"user_account text,"
            +"longitude double,"
            +"latitude double,"
            +"content text,"
            +"address text,"
            +"experience integer,"
            +"state text)";
    public static final String CREATE_USER="create table User("
            +"user_account text primary key,"
            +"user_name text,"
            +"sex text,"
            +"age integer,"
            +"level integer,"
            +"experience integer)";
    public static final String CREATE_ACHIEVEMENT="create table Achievement("
            +"user_account text,"
            +"achievement_id text primary key,"
            +"achievement_name text,"
            +"completion double,"
            +"state integer,"
            +"content text,"
            +"condition text,"
            +"condition_desc text,"
            +"pass_list text)";
    public static final String CREATE_SHARECONTENT="create table ShareContent("
            +"id integer primary key autoincrement,"
            +"user_account text,"
            +"user_name text,"
            +"title text,"
            +"publish_time text,"
            +"content text,"
            +"place text,"
            +"latitude text,"
            +"longitude text)";
    private Context context;

    public MyDatabaseHelper(Context context,String name,SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_PASS);
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_ACHIEVEMENT);
        db.execSQL(CREATE_SHARECONTENT);
        Log.v("response","数据库表已创建");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}
