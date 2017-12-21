package com.vomont.yundudao.utils;

import java.util.List;

import com.vomont.yundudao.bean.Message;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库说明
 */
public class MySqliteHelp extends SQLiteOpenHelper
{
    
    private String tb_name = "yun_message";
    
    private Context context;
    
    public MySqliteHelp(Context context)
    {
        super(context, "yundd.db", null, 1);
        this.context = context;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tb_name
            + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, msgid  INTEGER,msgtitle  VARCHAR,type INTEGER,msgputtime VARCHAR,msgcontent VARCHAR,isRead INTEGER)");
        
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        
    }
    
    /**
     * 插入数据
     * 
     * @param message 消息
     * @param context 系统的上下文
     */
    public void onInsert(Message message)
    {
        SQLiteDatabase db = context.openOrCreateDatabase("yundd.db", Context.MODE_PRIVATE, null);
        db.execSQL("INSERT INTO " + tb_name + " VALUES (NULL,?,?,?,?,?,?)",
            new Object[] {message.getMsgid(), message.getMsgtitle(), message.getMsgtype(), message.getMsgpubtime(), message.getMsgcontent(), 0});
        db.close();
    
    }
    
    public List<Message> getInfo(List<Message> mlist)
    {
        SQLiteDatabase db = context.openOrCreateDatabase("yundd.db", Context.MODE_PRIVATE, null);
        Cursor cursor = db.query(tb_name, null, null, null, null, null, null);
        while (cursor.moveToNext())
        {
            Message aMessage = new Message();
            aMessage.setMsgtitle(cursor.getString(2));
            aMessage.setMsgtype(cursor.getInt(3));
            aMessage.setMsgpubtime(Long.parseLong(cursor.getString(4)));
            aMessage.setMsgcontent(cursor.getString(5));
            mlist.add(aMessage);
        }
        cursor.close();
        db.close();
        return mlist;
    }
    
    /**
     * 查询未读信息
     * 
     * @param mlist
     * @return 信息的集合
     */
    public List<Message> onSelectNoRead(List<Message> mlist)
    {
        String selection = " isRead = ? ";
        String[] selectionArgs = {"0"};
        SQLiteDatabase db = context.openOrCreateDatabase("yundd.db", Context.MODE_PRIVATE, null);
        Cursor cursor = db.query(tb_name, null, selection, selectionArgs, null, null, null);
        
        while (cursor.moveToNext())
        {
            Message aMessage = new Message();
            aMessage.setMsgtitle(cursor.getString(2));
            aMessage.setMsgtype(cursor.getInt(3));
            aMessage.setMsgpubtime(Long.parseLong(cursor.getString(4)));
            aMessage.setMsgcontent(cursor.getString(5));
            mlist.add(aMessage);
        }
        cursor.close();
        db.close();
        return mlist;
    }
    /**
     * 读取未读信息
     */
    public void changeStatue()
    {
        SQLiteDatabase db = context.openOrCreateDatabase("yundd.db", Context.MODE_PRIVATE , null);
        ContentValues values = new ContentValues();
        values.put("isRead", "1");// key为字段名，value为值
        db.update(tb_name, values, "isRead= ? ", new String[] {"0"});
        db.close();
    }
}
