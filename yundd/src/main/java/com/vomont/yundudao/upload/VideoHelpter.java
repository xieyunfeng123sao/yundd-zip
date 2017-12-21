package com.vomont.yundudao.upload;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VideoHelpter extends SQLiteOpenHelper
{
    private String tb_name = "yvideo";
    
    private static String Tbname = "yun.db";
    
    private Context context;
    
    // //loadstate 上传的状态 0暂停 1正在上传 2上传完成 3压缩
    public VideoHelpter(Context context)
    {
        super(context, Tbname, null, 1);
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tb_name + " (_id INTEGER PRIMARY KEY AUTOINCREMENT," + "name  VARCHAR," + "subname  VARCHAR," + "subfatoryid INTEGER," + "creattime LONG,"
            + "desp VARCHAR," + "looker VARCHAR," + "lookername VARCHAR," + "loadstate INTEGER," + "videoid  INTEGER," + "videoPath VARCHAR ," + "fileId INTEGER," + "imgFileId INTEGER, "
            + "videoUrl VARCHAR," + "imgUrl VARCHAR," + "progress INTEGER,isSave INTEGER,creatId INTEGER)");
        db.close();
        this.context = context;
    }
    
    public void addData(VideoBean videoBean)
    {
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        db.execSQL("INSERT INTO " + tb_name + " VALUES (NULL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
            new Object[] {videoBean.getName(), videoBean.getSubname(), videoBean.getSubfatoryid(), videoBean.getCreattime(), videoBean.getDesp(), videoBean.getLooker(), videoBean.getLookername(), 0,
                videoBean.getVideoid(), videoBean.getVideoPath(), videoBean.getFileId(), videoBean.getImageFileId(), videoBean.getVideoUrl(), videoBean.getFaceImageUrl(), videoBean.getPos(),
                videoBean.getIsSave(),videoBean.getCreateId()});
        db.close();
    }
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // 之前建国数据库了 这个方法不会走的 一个Android项目 最好只建立一个继承自SQLiteOpenHelper的管理类
        // db.execSQL("CREATE TABLE IF NOT EXISTS "
        // + tb_name
        // +
        // " (_id INTEGER PRIMARY KEY AUTOINCREMENT,img VARCHAR,name  VARCHAR,subname  VARCHAR,subfatoryid INTEGER,creattime LONG,desp VARCHAR,looker VARCHAR,lookername VARCHAR,isPack INTEGER,loadstate INTEGER,videoid  INTEGER)");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        
    }
    
    public VideoBean selectByName(String name)
    {
        List<VideoBean> mlist = new ArrayList<VideoBean>();
        String[] columns =
            {"name", "subname", "subfatoryid", "creattime", "desp", "looker", "lookername", "loadstate", "videoid", "videoPath", "fileId", "imgFileId", "videoUrl", "imgUrl", "progress", "isSave","creatId"};
        String selection;
        Cursor cursor;
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        selection = "name=? ";
        String[] selectionArgs = {name};
        cursor = db.query(tb_name, columns, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext())
        {
            VideoBean videoBean = new VideoBean();
            videoBean.setName(cursor.getString(0));
            videoBean.setSubname(cursor.getString(1));
            videoBean.setSubfatoryid(cursor.getInt(2));
            videoBean.setCreattime(cursor.getLong(3));
            videoBean.setDesp(cursor.getString(4));
            videoBean.setLooker(cursor.getString(5));
            videoBean.setLookername(cursor.getString(6));
            videoBean.setLoadstate(cursor.getInt(7));
            videoBean.setVideoid(cursor.getInt(8));
            videoBean.setVideoPath(cursor.getString(9));
            videoBean.setFileId(cursor.getInt(10));
            videoBean.setImageFileId(cursor.getInt(11));
            videoBean.setVideoUrl(cursor.getString(12));
            videoBean.setFaceImageUrl(cursor.getString(13));
            videoBean.setPos(cursor.getInt(14));
            videoBean.setIsSave(cursor.getInt(15));
            videoBean.setCreateId(cursor.getInt(16));
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
    
    public VideoBean selectByFileId(int fileId)
    {
        List<VideoBean> mlist = new ArrayList<VideoBean>();
        String[] columns =
            {"name", "subname", "subfatoryid", "creattime", "desp", "looker", "lookername", "loadstate", "videoid", "videoPath", "fileId", "imgFileId", "videoUrl", "imgUrl", "progress", "isSave","creatId"};
        String selection;
        Cursor cursor;
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        selection = "  fileId=? ";
        String[] selectionArgs = {fileId + ""};
        cursor = db.query(tb_name, columns, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext())
        {
            VideoBean videoBean = new VideoBean();
            videoBean.setName(cursor.getString(0));
            videoBean.setSubname(cursor.getString(1));
            videoBean.setSubfatoryid(cursor.getInt(2));
            videoBean.setCreattime(cursor.getLong(3));
            videoBean.setDesp(cursor.getString(4));
            videoBean.setLooker(cursor.getString(5));
            videoBean.setLookername(cursor.getString(6));
            videoBean.setLoadstate(cursor.getInt(7));
            videoBean.setVideoid(cursor.getInt(8));
            videoBean.setVideoPath(cursor.getString(9));
            videoBean.setFileId(cursor.getInt(10));
            videoBean.setImageFileId(cursor.getInt(11));
            videoBean.setVideoUrl(cursor.getString(12));
            videoBean.setFaceImageUrl(cursor.getString(13));
            videoBean.setPos(cursor.getInt(14));
            videoBean.setIsSave(cursor.getInt(15));
            videoBean.setCreateId(cursor.getInt(16));
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
    
    public List<VideoBean> selectAll()
    {
        List<VideoBean> mlist = new ArrayList<VideoBean>();
        String[] columns =
            {"name", "subname", "subfatoryid", "creattime", "desp", "looker", "lookername", "loadstate", "videoid", "videoPath", "fileId", "imgFileId", "videoUrl", "imgUrl", "progress", "isSave","creatId"};
        Cursor cursor;
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        cursor = db.query(tb_name, columns, null, null, null, null, null);
        while (cursor.moveToNext())
        {
            VideoBean videoBean = new VideoBean();
            videoBean.setName(cursor.getString(0));
            videoBean.setSubname(cursor.getString(1));
            videoBean.setSubfatoryid(cursor.getInt(2));
            videoBean.setCreattime(cursor.getLong(3));
            videoBean.setDesp(cursor.getString(4));
            videoBean.setLooker(cursor.getString(5));
            videoBean.setLookername(cursor.getString(6));
            videoBean.setLoadstate(cursor.getInt(7));
            videoBean.setVideoid(cursor.getInt(8));
            videoBean.setVideoPath(cursor.getString(9));
            videoBean.setFileId(cursor.getInt(10));
            videoBean.setImageFileId(cursor.getInt(11));
            videoBean.setVideoUrl(cursor.getString(12));
            videoBean.setFaceImageUrl(cursor.getString(13));
            videoBean.setPos(cursor.getInt(14));
            videoBean.setIsSave(cursor.getInt(15));
            videoBean.setCreateId(cursor.getInt(16));
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
    
    public VideoBean selectImageFileId(int imageFileId)
    {
        List<VideoBean> mlist = new ArrayList<VideoBean>();
        String[] columns =
            {"name", "subname", "subfatoryid", "creattime", "desp", "looker", "lookername", "loadstate", "videoid", "videoPath", "fileId", "imgFileId", "videoUrl", "imgUrl", "progress", "isSave","creatId"};
        String selection;
        Cursor cursor;
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        selection = "  imgFileId=? ";
        String[] selectionArgs = {imageFileId + ""};
        cursor = db.query(tb_name, columns, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext())
        {
            VideoBean videoBean = new VideoBean();
            videoBean.setName(cursor.getString(0));
            videoBean.setSubname(cursor.getString(1));
            videoBean.setSubfatoryid(cursor.getInt(2));
            videoBean.setCreattime(cursor.getLong(3));
            videoBean.setDesp(cursor.getString(4));
            videoBean.setLooker(cursor.getString(5));
            videoBean.setLookername(cursor.getString(6));
            videoBean.setLoadstate(cursor.getInt(7));
            videoBean.setVideoid(cursor.getInt(8));
            videoBean.setVideoPath(cursor.getString(9));
            videoBean.setFileId(cursor.getInt(10));
            videoBean.setImageFileId(cursor.getInt(11));
            videoBean.setVideoUrl(cursor.getString(12));
            videoBean.setFaceImageUrl(cursor.getString(13));
            videoBean.setPos(cursor.getInt(14));
            videoBean.setIsSave(cursor.getInt(15));
            videoBean.setCreateId(cursor.getInt(16));
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
    
    public List<VideoBean> selectNoPack()
    {
        List<VideoBean> mlist = new ArrayList<VideoBean>();
        String[] columns =
            {"name", "subname", "subfatoryid", "creattime", "desp", "looker", "lookername", "loadstate", "videoid", "videoPath", "fileId", "imgFileId", "videoUrl", "imgUrl", "progress", "isSave","creatId"};
        String selection;
        Cursor cursor;
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        selection = "  videoid=? ";
        String[] selectionArgs = {0 + ""};
        cursor = db.query(tb_name, columns, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext())
        {
            VideoBean videoBean = new VideoBean();
            videoBean.setName(cursor.getString(0));
            videoBean.setSubname(cursor.getString(1));
            videoBean.setSubfatoryid(cursor.getInt(2));
            videoBean.setCreattime(cursor.getLong(3));
            videoBean.setDesp(cursor.getString(4));
            videoBean.setLooker(cursor.getString(5));
            videoBean.setLookername(cursor.getString(6));
            videoBean.setLoadstate(cursor.getInt(7));
            videoBean.setVideoid(cursor.getInt(8));
            videoBean.setVideoPath(cursor.getString(9));
            videoBean.setFileId(cursor.getInt(10));
            videoBean.setImageFileId(cursor.getInt(11));
            videoBean.setVideoUrl(cursor.getString(12));
            videoBean.setFaceImageUrl(cursor.getString(13));
            videoBean.setPos(cursor.getInt(14));
            videoBean.setIsSave(cursor.getInt(15));
            videoBean.setCreateId(cursor.getInt(16));
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
    
    public List<VideoBean> selectAllPack()
    {
        List<VideoBean> mlist = new ArrayList<VideoBean>();
        String[] columns =
            {"name", "subname", "subfatoryid", "creattime", "desp", "looker", "lookername", "loadstate", "videoid", "videoPath", "fileId", "imgFileId", "videoUrl", "imgUrl", "progress", "isSave","creatId"};
        String selection;
        Cursor cursor;
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        selection = "  videoid!=0 ";
        cursor = db.query(tb_name, columns, selection, null, null, null, null);
        while (cursor.moveToNext())
        {
            VideoBean videoBean = new VideoBean();
            videoBean.setName(cursor.getString(0));
            videoBean.setSubname(cursor.getString(1));
            videoBean.setSubfatoryid(cursor.getInt(2));
            videoBean.setCreattime(cursor.getLong(3));
            videoBean.setDesp(cursor.getString(4));
            videoBean.setLooker(cursor.getString(5));
            videoBean.setLookername(cursor.getString(6));
            videoBean.setLoadstate(cursor.getInt(7));
            videoBean.setVideoid(cursor.getInt(8));
            videoBean.setVideoPath(cursor.getString(9));
            videoBean.setFileId(cursor.getInt(10));
            videoBean.setImageFileId(cursor.getInt(11));
            videoBean.setVideoUrl(cursor.getString(12));
            videoBean.setFaceImageUrl(cursor.getString(13));
            videoBean.setPos(cursor.getInt(14));
            videoBean.setIsSave(cursor.getInt(15));
            videoBean.setCreateId(cursor.getInt(16));
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
    
    // public void updatePack(String name)
    // {
    // SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
    // ContentValues values = new ContentValues();
    // values.put("isPack", 1);
    // db.update(tb_name, values, "name= ? ", new String[] {name});
    // db.close();
    //
    // }
    
    public void updateProgress(String name, int pro, int fileId)
    {
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("progress", pro);
        db.update(tb_name, values, "name= ? and fileId=?", new String[] {name, fileId + ""});
        db.close();
    }
    
    public void updateBean(String name, VideoBean bean)
    {
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("subname", bean.getSubname());
        values.put("subfatoryid", bean.getSubfatoryid());
        values.put("desp", bean.getDesp());
        values.put("looker", bean.getLooker());
        values.put("lookername", bean.getLookername());
        values.put("fileId", bean.getFileId());
        values.put("imgFileId", bean.getImageFileId());
        values.put("loadstate", bean.getLoadstate());
        values.put("isSave", bean.getIsSave());
        values.put("videoPath",bean.getVideoPath());
        values.put("name",bean.getName());
        if (bean.getVideoUrl() != null)
            values.put("videoUrl", bean.getVideoUrl());
        if (bean.getFaceImageUrl() != null)
            values.put("imgUrl", bean.getFaceImageUrl());
        db.update(tb_name, values, "name= ? ", new String[] {name});
        db.close();
    }
    
    public void updateVideoUrl(String name, String videoUrl, int id)
    {
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        
        values.put("imgFileId", id);
        if (videoUrl != null)
            values.put("videoUrl", videoUrl);
        db.update(tb_name, values, "name= ? ", new String[] {name});
        db.close();
    }
    
    public void updateImageUrl(String name, String imgUrl)
    {
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        
        // "videoUrl", "imgUrl"
        if (imgUrl != null)
            values.put("imgUrl", imgUrl);
        
        values.put("loadstate", 4);
        // if (bean.getFaceImageUrl() != null)
        // values.put("imgUrl", bean.getFaceImageUrl());
        db.update(tb_name, values, "name= ? ", new String[] {name});
        db.close();
    }
    
    public void updateLoadState(String name, int state, int videoid)
    {
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("loadstate", state);
        values.put("videoid", videoid);
        db.update(tb_name, values, "name= ? ", new String[] {name});
        db.close();
    }
    
    public int getPathLoading(String name)
    {
        List<VideoBean> mlist = new ArrayList<VideoBean>();
        String[] columns = {"loadstate"};
        String selection;
        Cursor cursor;
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        selection = "name=? ";
        String[] selectionArgs = {name};
        cursor = db.query(tb_name, columns, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext())
        {
            VideoBean videoBean = new VideoBean();
            videoBean.setLoadstate(cursor.getInt(0));
            mlist.add(videoBean);
        }
        cursor.close();
        db.close();
        if (mlist != null && mlist.size() != 0)
        {
            return mlist.get(0).getLoadstate();
        }
        return 0;
    }
    
    public void updataLoad()
    {
        List<VideoBean> mlist = selectAll();
        if (mlist != null && mlist.size() != 0)
            for (VideoBean bean : mlist)
            {
                SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
                ContentValues values = new ContentValues();
                values.put("loadstate", 0);
                db.update(tb_name, values, "name= ? and videoid=?", new String[] {bean.getName(), "0"});
                db.close();
            }
    }
    
    public void deleteVideo(String name)
    {
        SQLiteDatabase db = context.openOrCreateDatabase(Tbname, Context.MODE_PRIVATE, null);
        String[] selectionArgs = {name};
        String selection = "  name=? ";
        db.delete(tb_name, selection, selectionArgs);
        db.close();
    }
    
}
