package com.vomont.yundudao.upload;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class VideoItemHelper extends SQLiteOpenHelper
{
    private static String Tbname = "yundd.db";
    
    private Context mContext;
    
    private String name;
    
    public VideoItemHelper(Context context, String name)
    {
        super(context, Tbname, null, 1);
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + "tb_"+name.replaceAll("-", "") + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,context VARCHAR,contextid INTEGER,isload INTEGER)");
        this.name = name;
        this.mContext = context;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
     
        Log.d("tb", "tb_"+name);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        
    }
    
    public void addItem(VideoItem item)
    {
        SQLiteDatabase db = mContext.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        db.execSQL("INSERT INTO " +"tb_"+ name.replaceAll("-", "") + " VALUES (NULL,?,?,?)", new Object[] {item.getContext(), item.getContextid(), item.getIsload()});
    }
    
    public VideoItem selectByid(int contextid)
    {
        List<VideoItem> mlist = new ArrayList<VideoItem>();
        String[] columns = {"context", "contextid", "isload"};
        String selection;
        Cursor cursor;
        SQLiteDatabase db = mContext.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        selection = "  contextid=? ";
        String[] selectionArgs = {contextid + ""};
        cursor = db.query("tb_"+name.replaceAll("-", ""), columns, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext())
        {
            VideoItem videoBean = new VideoItem();
            videoBean.setContext(cursor.getString(0));
            videoBean.setContextid(cursor.getInt(1));
            videoBean.setIsload(cursor.getInt(2));
            mlist.add(videoBean);
        }
        cursor.close();
        db.close();
        if (mlist != null && mlist.size() != 0)
        {
            return mlist.get(0);
        }
        return null;
    }
    
    /**
     * 
     * <一句话功能简述>
     * <功能详细描述>
     * 
     * @param isload
     * @return
     * @see [类、类#方法、类#成员]
     */
    public List<VideoItem> selectByIsload(int isload)
    {
        List<VideoItem> mlist = new ArrayList<VideoItem>();
        String[] columns = {"context", "contextid", "isload"};
        String selection;
        Cursor cursor;
        SQLiteDatabase db = mContext.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        selection = "  isload=? ";
        String[] selectionArgs = {"0"};
        cursor = db.query("tb_"+name.replaceAll("-", ""), columns, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext())
        {
            VideoItem videoBean = new VideoItem();
            videoBean.setContext(cursor.getString(0));
            videoBean.setContextid(cursor.getInt(1));
            videoBean.setIsload(cursor.getInt(2));
            mlist.add(videoBean);
        }
        cursor.close();
        db.close();
        if (mlist != null && mlist.size() != 0)
        {
            return mlist;
        }
        return null;
    }
    
    public void updataContext(String context, int contextid)
    {
        SQLiteDatabase db = mContext.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("context", context);
        db.update("tb_"+name.replaceAll("-", ""), values, "contextid= ? ", new String[] {contextid + ""});
        db.close();
    }
    
    public void updataIsload(int contextid, int isload)
    {
        SQLiteDatabase db = mContext.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("isload", isload);
        db.update("tb_"+name.replaceAll("-", ""), values, "contextid= ? ", new String[] {contextid + ""});
        db.close();
    }
    
}
