package onionsss.it.onionsss.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import onionsss.it.onionsss.db.UserHelper;

/**
 * 作者：张琦 on 2016/5/16 14:08
 * 邮箱：759308541@qq.com
 */
public class UserDao {
    public UserDao(Context context){
        UserHelper uh = new UserHelper(context);
        final SQLiteDatabase db = uh.getWritableDatabase();
    }

}
