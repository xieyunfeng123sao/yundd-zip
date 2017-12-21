package com.vomont.yundudao.db;

import java.util.ArrayList;
import java.util.List;
import com.vomont.yundudao.bean.ManualBean;
import com.vomont.yundudao.utils.ShareUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Db_Manual extends SQLiteOpenHelper
{
    private String name = "tb_manual";
    
    private static String Tbname = "yundd.db";
    
    private Context context;
    
    private ShareUtil shareUtil;
    
    /**
     * mode 0 不循环 1 循环
     * 
     * @param context
     */
    public Db_Manual(Context context)
    {
        super(context, Tbname, null, 1);
        this.context = context;
        shareUtil = new ShareUtil(context);
        name = name + "_" + shareUtil.getShare().getNum();
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + name + " (id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR,length INTEGER,mode INTEGER,devidlist VARCHAR)");
    }
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
       
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        
    }
    
    /**
     * 添加巡视方案
     * 
     * @param bean
     */
    public void addManual(ManualBean bean)
    {
        
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        db.execSQL("INSERT INTO " + name + " VALUES (NULL,?,?,?,?)", new Object[] {bean.getName(), bean.getLength(), bean.getMode(), bean.getDeviceIdList()});
    }
    
    /**
     * 查找巡视方案
     * 
     * @return
     */
    public List<ManualBean> selectAll()
    {
        
        List<ManualBean> mlist = new ArrayList<ManualBean>();
        String[] columns = {"id", "name", "length", "mode", "devidlist"};
        Cursor cursor;
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        cursor = db.query(name, columns, null, null, null, null, null);
        while (cursor.moveToNext())
        {
            ManualBean bean = new ManualBean();
            bean.setId(cursor.getInt(0));
            bean.setName(cursor.getString(1));
            bean.setLength(cursor.getInt(2));
            bean.setMode(cursor.getInt(3));
            bean.setDeviceIdList(cursor.getString(4));
            mlist.add(bean);
        }
        cursor.close();
        db.close();
        if (mlist != null && mlist.size() != 0)
        {
            return mlist;
        }
        return null;
    }
    
    /**
     * 更新巡视方案
     * 
     * @param bean
     */
    public void updataManual(ManualBean bean)
    {
        
        
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("name", bean.getName());
        values.put("length", bean.getLength());
        values.put("mode", bean.getMode());
        values.put("devidlist", bean.getDeviceIdList());
        db.update(name, values, "id= ? ", new String[] {bean.getId() + ""});
        db.close();
    }
    
    /**
     * 删除巡视方案
     * 
     * @param id
     */
    public void deleteManual(int id)
    {
        
        
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        String[] selectionArgs = {id + ""};
        String selection = "  id=? ";
        db.delete(name, selection, selectionArgs);
        db.close();
    }
}