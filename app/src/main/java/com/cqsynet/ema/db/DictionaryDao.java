package com.cqsynet.ema.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.cqsynet.ema.model.DictionaryObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库字典表的工具类
 */
public class DictionaryDao {

    private static DictionaryDao mInstance;
    private Context mContext;

    public static DictionaryDao getInstance(Context context) {
        synchronized (DictionaryDao.class) {
            if (mInstance == null) {
                if (mInstance == null) {
                    mInstance = new DictionaryDao(context);
                }
            }
        }
        return mInstance;
    }

    private DictionaryDao(Context context) {
        this.mContext = context;
    }


    /**
     * 保存字典记录
     * @param list
     */
    public void saveDictionary(List<DictionaryObject> list) {
        DBHelper db = new DBHelper(mContext);
        db.getWritableDatabase().execSQL("delete from " + DBHelper.TABLE_DICTIONARY);
        for (DictionaryObject object : list) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.DICTIONARY_COL_ID, object.id);
            contentValues.put(DBHelper.DICTIONARY_COL_TYPE, object.type);
            contentValues.put(DBHelper.DICTIONARY_COL_DESCRIPTION, object.description);
            db.getWritableDatabase().insert(DBHelper.TABLE_DICTIONARY, null, contentValues);
        }
        db.close();
    }

    /**
     * 查询权限记录
     * @return
     */
    public ArrayList<DictionaryObject> queryDictionary() {
        ArrayList<DictionaryObject> list = new ArrayList<>();
        DBHelper db = new DBHelper(mContext);
        Cursor cursor = db.getReadableDatabase().query(DBHelper.TABLE_DICTIONARY, null, null, null, null, null, null);
        if(cursor != null && cursor.moveToFirst()) {
            do {
                DictionaryObject obj = new DictionaryObject();
                obj.id = cursor.getString(cursor.getColumnIndex(DBHelper.DICTIONARY_COL_ID));
                obj.type = cursor.getString(cursor.getColumnIndex(DBHelper.DICTIONARY_COL_TYPE));
                obj.description = cursor.getString(cursor.getColumnIndex(DBHelper.DICTIONARY_COL_DESCRIPTION));
                list.add(obj);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 查询字典记录条数
     * @return
     */
    public int getCount(String type) {
        int count = 0;
        DBHelper db = new DBHelper(mContext);
        Cursor cursor = db.getReadableDatabase().query(DBHelper.TABLE_DICTIONARY, null, DBHelper.DICTIONARY_COL_TYPE + "=?", new String[] {type}, null, null, null);
        if(cursor != null) {
            count = cursor.getCount();
        }
        cursor.close();
        db.close();
        return count;
    }
}
