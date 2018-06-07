package com.cqsynet.ema.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.cqsynet.ema.model.SystemCategoryObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库系统分类表的工具类
 */
public class SystemCategoryDao {

    private static SystemCategoryDao mInstance;
    private Context mContext;

    public static SystemCategoryDao getInstance(Context context) {
        synchronized (SystemCategoryDao.class) {
            if (mInstance == null) {
                if (mInstance == null) {
                    mInstance = new SystemCategoryDao(context);
                }
            }
        }
        return mInstance;
    }

    private SystemCategoryDao(Context context) {
        this.mContext = context;
    }


    /**
     * 保存位置记录
     * @param list
     */
    public void saveSystemCategory(List<SystemCategoryObject> list) {
        DBHelper db = new DBHelper(mContext);
        db.getWritableDatabase().execSQL("delete from " + DBHelper.TABLE_SYSTEM_CATEGORY);
        for (SystemCategoryObject object : list) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.SYSTEM_CATEGORY_COL_ID, object.ZL_BM);
            contentValues.put(DBHelper.SYSTEM_CATEGORY_COL_NAME, object.ZL_MS);
            contentValues.put(DBHelper.SYSTEM_CATEGORY_COL_PARENT_ID, object.F_ZL_BM);
            db.getWritableDatabase().insert(DBHelper.TABLE_SYSTEM_CATEGORY, null, contentValues);
        }
        db.close();
    }

    /**
     * 查询位置记录
     * @return
     */
    public ArrayList<SystemCategoryObject> querySystemCategory(String parentId) {
        ArrayList<SystemCategoryObject> list = new ArrayList<>();
        DBHelper db = new DBHelper(mContext);
        Cursor cursor;
        if (TextUtils.isEmpty(parentId)) {
            cursor = db.getReadableDatabase().query(DBHelper.TABLE_SYSTEM_CATEGORY, null, DBHelper.SYSTEM_CATEGORY_COL_PARENT_ID + " is null", null, null, null, null);
        } else {
            cursor = db.getReadableDatabase().query(DBHelper.TABLE_SYSTEM_CATEGORY, null, DBHelper.SYSTEM_CATEGORY_COL_PARENT_ID + "=?", new String[] {parentId}, null, null, null);
        }
        if(cursor != null && cursor.moveToFirst()) {
            do {
                SystemCategoryObject obj = new SystemCategoryObject();
                obj.ZL_BM = cursor.getString(cursor.getColumnIndex(DBHelper.SYSTEM_CATEGORY_COL_ID));
                obj.ZL_MS = cursor.getString(cursor.getColumnIndex(DBHelper.SYSTEM_CATEGORY_COL_NAME));
                obj.F_ZL_BM = cursor.getString(cursor.getColumnIndex(DBHelper.SYSTEM_CATEGORY_COL_PARENT_ID));
                list.add(obj);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 查询记录条数
     * @return
     */
    public int getCount() {
        int count = 0;
        DBHelper db = new DBHelper(mContext);
        Cursor cursor = db.getReadableDatabase().query(DBHelper.TABLE_SYSTEM_CATEGORY, null, null, null, null, null, null);
        if(cursor != null) {
            count = cursor.getCount();
        }
        cursor.close();
        db.close();
        return count;
    }
}
