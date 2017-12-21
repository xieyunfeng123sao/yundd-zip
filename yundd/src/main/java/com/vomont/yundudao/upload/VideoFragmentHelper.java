package com.vomont.yundudao.upload;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class VideoFragmentHelper
{
    private String tb_name = "tb_yundd";
    
    private static String Tbname = "yunddvideo.db";
    
    public VideoFragmentHelper(Context context)
    {
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tb_name
            + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,fragmentid  INTEGER,path  VARCHAR,fragmentcontext VARCHAR,creattime VARCHAR,isupLoad INTEGER，,ispack INTEGER)");
        
        db.close();
    }
    
    // fragmentid 录像标识
    // path 录像的路径名
    // fragmentcontext 上传的上下文
    // creattime 创建时间
    // isupLoad 是否上传
    // ispack 是否打包
    
    /**
     * 添加信息
     * 
     * @param context
     * @param file
     */
    public void insertInfo(Context context, VideoFragmentInfo file)
    {
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        db.execSQL("INSERT INTO " + tb_name + " VALUES (NULL,?,?,?,?,?,?)", new Object[] {file.getFragmentid(), file.getPath(), file.getFragmentcontext(), file.getCreattime(), 0, 0});
        db.close();
    }
    
    /**
     * 修改context
     * 
     * @param context
     * @param file
     */
    public void upDataContext(Context context, String path, int fragmentid, String fragmentcontext)
    {
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("fragmentcontext", fragmentcontext);// key为字段名，value为值
        values.put("isupLoad", 1);// 可以获取到context说明上传成功了 所以更改上传状态
        db.update(tb_name, values, "fragmentid= ? and path = ?", new String[] {fragmentid + "", path});
        db.close();
    }
    
    /**
     * 查找某个
     * 
     * @param context
     * @param fragmentid
     * @return
     */
    public VideoFragmentInfo selectById(Context context, String path, int fragmentid)
    {
        List<VideoFragmentInfo> mlist = new ArrayList<VideoFragmentInfo>();
        String[] columns = {"fragmentid", "path", "fragmentcontext", "creattime", "isupLoad", "ispack"};
        String selection;
        Cursor cursor;
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        selection = "  path=? and fragmentid=? and isupLoad=?";
        String[] selectionArgs = {path, fragmentid + "", "0"};
        cursor = db.query(tb_name, columns, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext())
        {
            VideoFragmentInfo file = new VideoFragmentInfo();
            file.setFragmentid(cursor.getInt(0));
            file.setPath(cursor.getString(1));
            file.setFragmentcontext(cursor.getString(2));
            file.setCreattime(cursor.getLong(3));
            file.setIsLoad(cursor.getInt(4));
            file.setIsPack(cursor.getInt(5));
            mlist.add(file);
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
     * 查找某个
     * 
     * @param context
     * @param fragmentid
     * @return
     */
    public VideoFragmentInfo selectHasNoUpload(Context context, String path)
    {
        List<VideoFragmentInfo> mlist = new ArrayList<VideoFragmentInfo>();
        String[] columns = {"fragmentid", "path", "fragmentcontext", "creattime", "isupLoad", "ispack"};
        String selection;
        Cursor cursor;
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        selection = "  path=? and  isupLoad=?";
        String[] selectionArgs = {path, "0"};
        cursor = db.query(tb_name, columns, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext())
        {
            VideoFragmentInfo file = new VideoFragmentInfo();
            file.setFragmentid(cursor.getInt(0));
            file.setPath(cursor.getString(1));
            file.setFragmentcontext(cursor.getString(2));
            file.setCreattime(cursor.getLong(3));
            file.setIsLoad(cursor.getInt(4));
            file.setIsPack(cursor.getInt(5));
            mlist.add(file);
        }
        cursor.close();
        db.close();
        if (mlist != null && mlist.size() != 0)
        {
            return mlist.get(0);
        }
        return null;
    }
    
    public int selectByPath(Context context, String path)
    {
        List<VideoFragmentInfo> mlist = new ArrayList<VideoFragmentInfo>();
        String[] columns = {"fragmentid", "path", "fragmentcontext", "creattime", "isupLoad", "ispack"};
        String selection;
        Cursor cursor;
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        selection = " path=?";
        String[] selectionArgs = {path};
        cursor = db.query(tb_name, columns, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext())
        {
            VideoFragmentInfo file = new VideoFragmentInfo();
            file.setFragmentid(cursor.getInt(0));
            file.setPath(cursor.getString(1));
            file.setFragmentcontext(cursor.getString(2));
            file.setCreattime(cursor.getLong(3));
            file.setIsLoad(cursor.getInt(4));
            file.setIsPack(cursor.getInt(5));
            mlist.add(file);
        }
        cursor.close();
        db.close();
        if (mlist != null && mlist.size() != 0)
        {
            return mlist.size();
        }
        return 0;
    }
    
    public List<VideoFragmentInfo> getByPath(Context context, String path)
    {
        List<VideoFragmentInfo> mlist = new ArrayList<VideoFragmentInfo>();
        String[] columns = {"fragmentid", "path", "fragmentcontext", "creattime", "isupLoad", "ispack"};
        String selection;
        Cursor cursor;
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        selection = " path=?";
        String[] selectionArgs = {path};
        cursor = db.query(tb_name, columns, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext())
        {
            VideoFragmentInfo file = new VideoFragmentInfo();
            file.setFragmentid(cursor.getInt(0));
            file.setPath(cursor.getString(1));
            file.setFragmentcontext(cursor.getString(2));
            file.setCreattime(cursor.getLong(3));
            file.setIsLoad(cursor.getInt(4));
            file.setIsPack(cursor.getInt(5));
            mlist.add(file);
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
     * 修改上传的状态
     * 
     * @param context
     * @param file
     */
    public void upDataPack(Context context, String path)
    {
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("ispack", 1);// key为字段名，value为值
        db.update(tb_name, values, "path=?", new String[] {path});
        db.close();
    }
    
    public int getNoUpdataContext(Context context, String path)
    {
        int i = 0;
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        String[] columns = {"fragmentid", "isupLoad"};
        String selection;
        Cursor cursor;
        selection = "  path=? and  isupLoad=?";
        String[] selectionArgs = {path, "0"};
        
        cursor = db.query(tb_name, columns, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext())
        {
            i++;
        }
        cursor.close();
        db.close();
        return i;
    }
    
    public boolean getNoPackContext(Context context, String path)
    {
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        String[] columns = {"ispack"};
        String selection;
        Cursor cursor;
        selection = "  path=? and  ispack=?";
        String[] selectionArgs = {path, "0"};
        cursor = db.query(tb_name, columns, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext())
        {
            return true;
        }
        cursor.close();
        db.close();
        
        return false;
    }
    /**
     * 获取未上传视频
     * 
     * @param context
     */
    public List<VideoFragmentInfo> getNoUpdataVideo(Context context)
    {
        List<VideoFragmentInfo> mlist = new ArrayList<VideoFragmentInfo>();
        String[] columns = {"fragmentid", "path", "fragmentcontext", "creattime", "isupLoad", "ispack"};
        String selection;
        Cursor cursor;
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        selection = "  ispack=? ";
        String[] selectionArgs = {"0"};
        cursor = db.query(tb_name, columns, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext())
        {
            VideoFragmentInfo file = new VideoFragmentInfo();
            file.setFragmentid(cursor.getInt(0));
            file.setPath(cursor.getString(1));
            file.setFragmentcontext(cursor.getString(2));
            file.setCreattime(cursor.getLong(3));
            file.setIsLoad(cursor.getInt(4));
            file.setIsPack(cursor.getInt(5));
            mlist.add(file);
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
     * 获取已上传视频
     * 
     * @param context
     */
    public List<VideoFragmentInfo> getHasUpdataVideo(Context context)
    {
        List<VideoFragmentInfo> mlist = new ArrayList<VideoFragmentInfo>();
        String[] columns = {"fragmentid", "path", "fragmentcontext", "creattime", "isupLoad", "ispack"};
        String selection;
        Cursor cursor;
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        selection = "  ispack=? ";
        String[] selectionArgs = {"1"};
        cursor = db.query(tb_name, columns, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext())
        {
            VideoFragmentInfo file = new VideoFragmentInfo();
            file.setFragmentid(cursor.getInt(0));
            file.setPath(cursor.getString(1));
            file.setFragmentcontext(cursor.getString(2));
            file.setCreattime(cursor.getLong(3));
            file.setIsLoad(cursor.getInt(4));
            file.setIsPack(cursor.getInt(5));
            mlist.add(file);
        }
        cursor.close();
        db.close();
        if (mlist != null && mlist.size() != 0)
        {
            return mlist;
        }
        return null;
    }
    
    public void deleteName(Context context, String path)
    {
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        String selection;
        selection = "  path=? ";
        String[] selectionArgs = {path};
        db.delete(tb_name, selection, selectionArgs);
        db.close();
    }
    
}
