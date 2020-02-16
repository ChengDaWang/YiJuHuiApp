package com.example.yipartyapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.yipartyapp.bean.Info;
import com.example.yipartyapp.bean.headImage;

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
                " newUserFlag TEXT," +
                " realName TEXT," +
                " gender TEXT," +
                " bornData TEXT," +
                " homeTown TEXT," +
                " school TEXT," +
                " headImage TEXT)"
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
     * 添加真实姓名,性别等信息
     */
    public void addRealName(String realName,String gender,String bornData,String homeTown,String school){
        db.execSQL("INSERT INTO user (realName,gender,bornData,homeTown,school) VALUES(?,?,?,?,?)",new Object[]{realName,gender,bornData,homeTown,school});
    }
    /**
     * 添加头像
     */
    public void addHeadImage(String headImage){
        db.execSQL("INSERT INTO user (headImage) VALUES(?)",new Object[]{headImage});
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
     * 数据库更新数据（场景：更新用户标记）
     * @param newUserFlag
     */
    public void updata(String newUserFlag){
        db.execSQL("UPDATE user SET newUserFlag = ?",new Object[]{newUserFlag});
    }
    /**
     * 查询用户名和密码以及新老用户标记
     * 用于登陆
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
    /**
     * 查询学校,姓名
     * 用于设置
     */
   public ArrayList<Info> getInfo(){
       ArrayList<Info> list=new ArrayList<Info>();
       Cursor cursor=db.query("user",new String[] { "realName","school" },"_id=2",null,null,null,null);
       while (cursor.moveToNext()) {
           String name = cursor.getString(cursor.getColumnIndex("realName"));
           String school = cursor.getString(cursor.getColumnIndex("school"));
           list.add(new Info(name,school));
       }
       return list;
   }
    /**
     * 查询头像
     */
    public ArrayList<headImage> getHeadImage(){
        ArrayList<headImage> list=new ArrayList<>();
        Cursor cursor=db.query("user",new String[]{"headImage"},"_id=3",null,null,null,null);
        while (cursor.moveToNext()){
            String headImage=cursor.getString(cursor.getColumnIndex("headImage"));
            list.add(new headImage(headImage));
        }
        return list;
    }

}

