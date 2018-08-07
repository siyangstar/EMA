package com.cqsynet.ema.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.cqsynet.ema.model.DictionaryObject;
import com.cqsynet.ema.model.DictionaryResponseObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
     * @param dataObject
     */
    public void saveDictionary(DictionaryResponseObject.DataObject dataObject) {
        DBHelper db = new DBHelper(mContext);
        db.getWritableDatabase().execSQL("delete from " + DBHelper.TABLE_DICTIONARY);
        //保存流程状态数据
        HashMap<String, HashMap<String, String>> processMap = dataObject.process;
        if(processMap != null) {
            Iterator<String> keyIterator = processMap.keySet().iterator();
            while (keyIterator.hasNext()) {
                HashMap<String, String> valueMap = processMap.get(keyIterator.next());
                Iterator<String> iterator = valueMap.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String value = valueMap.get(key);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBHelper.DICTIONARY_COL_ID, key);
                    contentValues.put(DBHelper.DICTIONARY_COL_TYPE, "process");
                    contentValues.put(DBHelper.DICTIONARY_COL_DESCRIPTION, value);
                    db.getWritableDatabase().insert(DBHelper.TABLE_DICTIONARY, null, contentValues);
                }
            }
        }
        //保存字典数据
        HashMap<String, ArrayList<DictionaryObject>> dictMap = dataObject.dict;
        if(dictMap != null) {
            Iterator<String> keyIterator = dictMap.keySet().iterator();
            while (keyIterator.hasNext()) {
                ArrayList<DictionaryObject> valueList = dictMap.get(keyIterator.next());
                Iterator<DictionaryObject> iterator = valueList.iterator();
                while (iterator.hasNext()) {
                    DictionaryObject dictionaryObject = iterator.next();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBHelper.DICTIONARY_COL_ID, dictionaryObject.id);
                    contentValues.put(DBHelper.DICTIONARY_COL_TYPE, dictionaryObject.type);
                    contentValues.put(DBHelper.DICTIONARY_COL_DESCRIPTION, dictionaryObject.description);
                    db.getWritableDatabase().insert(DBHelper.TABLE_DICTIONARY, null, contentValues);
                }
            }
        }

        db.close();
    }

    /**
     * 查询字典表记录
     * @return
     */
    public ArrayList<DictionaryObject> queryDictionary(String type) {
        ArrayList<DictionaryObject> list = new ArrayList<>();
        DBHelper db = new DBHelper(mContext);
        Cursor cursor = db.getReadableDatabase().query(DBHelper.TABLE_DICTIONARY, null,"type=?", new String[] {type}, null, null, null);
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
     * 根据id查询字典表的单个值
     * @param type
     * @param id
     * @return
     */
    public String queryDictionary(String type, String id) {
        String des = "";
        if(!TextUtils.isEmpty(type) && !TextUtils.isEmpty(id)) {
            DBHelper db = new DBHelper(mContext);
            Cursor cursor = db.getReadableDatabase().query(DBHelper.TABLE_DICTIONARY, null, DBHelper.DICTIONARY_COL_TYPE + "=? and " + DBHelper.DICTIONARY_COL_ID + "=?", new String[]{type, id}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                des = cursor.getString(cursor.getColumnIndex(DBHelper.DICTIONARY_COL_DESCRIPTION));
            }
            cursor.close();
            db.close();
        }
        return des;
    }

    /**
     * 查询某类字典记录条数
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

    /**
     * 查询所有字典记录条数
     * @return
     */
    public int getCount() {
        int count = 0;
        DBHelper db = new DBHelper(mContext);
        Cursor cursor = db.getReadableDatabase().query(DBHelper.TABLE_DICTIONARY, null, null, null, null, null, null);
        if(cursor != null) {
            count = cursor.getCount();
        }
        cursor.close();
        db.close();
        return count;
    }
}
