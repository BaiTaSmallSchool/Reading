package onionsss.it.onionsss.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 作者：张琦 on 2016/5/16 14:00
 * 邮箱：759308541@qq.com
 */
public class UserHelper extends SQLiteOpenHelper{
    public UserHelper(Context context) {
        super(context, "user.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table app_user(_id integer primary key autoincrement,name varchar(30) unique,password varchar(30),sex integer,registtime varchar(30),phone integer" +
                ",head varchar(50))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
