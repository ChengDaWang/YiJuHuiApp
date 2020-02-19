package com.example.yipartyapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.yipartyapp.bean.Info;
import com.example.yipartyapp.bean.headImage;
import com.example.yipartyapp.bean.order;

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
        /**
         * 用户信息表
         */
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
        /**
         * 用户订单信息表
         */
        db.execSQL("CREATE TABLE IF NOT EXISTS orderInfo(" +
                " _no INTEGER PRIMARY KEY AUTOINCREMENT," +
                " orderName TEXT," +
                " orderPrice TEXT)");
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
        //db.execSQL("UPDATE user SET(realName,gender,bornData,homeTown,school)",new Object[]{realName,gender,bornData,homeTown,school});
        db.execSQL("UPDATE user SET realName = ?",new Object[]{realName});
        db.execSQL("UPDATE user SET gender = ?",new Object[]{gender});
        db.execSQL("UPDATE user SET bornData = ?",new Object[]{bornData});
        db.execSQL("UPDATE user SET homeTown = ?",new Object[]{homeTown});
        db.execSQL("UPDATE user SET school = ?",new Object[]{school});
    }
    /**
     * 添加头像
     */
    public void addHeadImage(String headImage){
        db.execSQL("UPDATE user SET headImage=?",new Object[]{headImage});
    }
    /**
     * 加入订单
     */
    public void addOrderDetails(String mName,String mPrice){
        db.execSQL("INSERT INTO orderInfo (orderName,orderPrice) VALUES(?,?)",new Object[]{mName,mPrice});
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
     * 更新学校信息
     */
    public void updataschool(String school){
        db.execSQL("UPDATE user SET school = ?",new Object[]{school});
    }
    /**
     * 更新密码
     */
    public void upPassword(String passWord){
        db.execSQL("UPDATE user SET passWord = ?",new Object[]{passWord});
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
       Cursor cursor=db.query("user",new String[] { "realName","school" },null,null,null,null,null);
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
        Cursor cursor=db.query("user",new String[]{"headImage"},null,null,null,null,null);
        while (cursor.moveToNext()){
            String headImage=cursor.getString(cursor.getColumnIndex("headImage"));
            list.add(new headImage(headImage));
        }
        return list;
    }

    /**
     * 订单查询
     */
    public ArrayList<order> getOrder(){
        ArrayList<order> list=new ArrayList<>();
        Cursor cursor=db.query("orderInfo",new String[]{"orderName","orderPrice"},null,null,null,null,null);
        while (cursor.moveToNext()){
            String name=cursor.getString(cursor.getColumnIndex("orderName"));
            String money=cursor.getString(cursor.getColumnIndex("orderPrice"));
            list.add(new order(name,money));
        }
        return list;
    }
}

