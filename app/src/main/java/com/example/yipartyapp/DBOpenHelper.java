package com.example.yipartyapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
public class DBOpenHelper extends SQLiteOpenHelper {
    /**
     * 声明数据库变量  (亿聚会用户数据库)
     */
    private SQLiteDatabase db;

    public DBOpenHelper(Context context){
        super(context,"PartyUser_SQL",null,2);
        db = getReadableDatabase();
    }

    /**
     * 重写增删方法
     * 因为该类继承了SQLiteOpenHelper 类
     * 而这两个方法是 abstract（抽象） 类中的抽象方法，所以必须重写（详情请看JAVA API文档）
     * @param db
     */
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS user(" +
                " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " userName TEXT," +
                " passWord TEXT," +
                " newUserFlag TEXT)"
                  );
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }

    /**
     * 数据库增加数据
     * @param userName
     * @param passWord
     */
    public void add(String userName, String passWord,String newUserFlag){
        db.execSQL("INSERT INTO user (userName,passWord,newUserFlag) VALUES(?,?,?)",new Object[]{userName,passWord,newUserFlag});
    }

    /**
     * 数据库删除数据
     * @param userName
     * @param passWord
     */
    public void delete(String userName,String passWord){
        db.execSQL("DELETE FROM user WHERE userName = AND passWord ="+userName+passWord);
    }

    /**
     * 数据库更新数据（场景：用户修改密码）
     * @param passWord
     */
    public void updata(String passWord){
        db.execSQL("UPDATE user SET passWord = ?",new Object[]{passWord});
    }
    /**
     * 全库查询数据
     */
    public ArrayList<User> getAllData(){

        ArrayList<User> list = new ArrayList<User>();
        Cursor cursor = db.query("user",null,null,null,null,null,"userName DESC");
        while(cursor.moveToNext()){
            String userName = cursor.getString(cursor.getColumnIndex("userName"));
            String passWord = cursor.getString(cursor.getColumnIndex("passWord"));
            String newUserFlag = cursor.getString(cursor.getColumnIndex("newUserFlag"));
            list.add(new User(userName,passWord,newUserFlag));
        }
        return list;
    }
}

