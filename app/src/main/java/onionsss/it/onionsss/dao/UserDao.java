package onionsss.it.onionsss.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import onionsss.it.onionsss.bean.User;
import onionsss.it.onionsss.db.UserHelper;

/**
 * 作者：张琦 on 2016/5/16 14:08
 * 邮箱：759308541@qq.com
 */
public class UserDao {

    private final UserHelper mUh;

    public UserDao(Context context){
        mUh = new UserHelper(context);
    }

    /**
     * 增加一个注册人员
     * @param user
     */
    public boolean insert(User user){
        SQLiteDatabase db = mUh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",user.getName());
        values.put("password",user.getPassword());
        values.put("phone",user.getPhone());
        values.put("sex",user.getSex());
        values.put("registtime",user.getRegisttime());
        long ok = db.insert("app_user", null, values);
        db.close();
        if(ok != -1){
            return true;
        }
        return false;
    }

    /**
     * 注册页面判断用户名是否已经被注册
     * @param name  传入一个用户输入的用户名
     * @return
     */
    public boolean queryName(String name){
        SQLiteDatabase db = mUh.getWritableDatabase();
        Cursor cursor = db.query("app_user", null, "name=?", new String[]{name}, null, null, null);
        if(cursor.moveToNext()){
            db.close();
            cursor.close();
            return true;
        }
        db.close();
        cursor.close();
        return false;
    }

    /**
     * 登录页面查询帐号是否存在
     * @param name
     * @return
     */
    public User haveAccount(String name){
        User user = null;
        SQLiteDatabase db = mUh.getWritableDatabase();
        Cursor cursor = db.query("app_user", null, "name=?", new String[]{name}, null, null, null);
        while(cursor.moveToNext()){
            user = new User();
            user.setName(cursor.getString(cursor.getColumnIndex("name")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
        }
        db.close();
        cursor.close();
        return user;
    }

}
