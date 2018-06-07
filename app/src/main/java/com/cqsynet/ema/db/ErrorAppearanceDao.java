package com.cqsynet.ema.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.cqsynet.ema.model.ErrorAppearanceObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库故障现象表的工具类
 */
public class ErrorAppearanceDao {

    private static ErrorAppearanceDao mInstance;
    private Context mContext;

    public static ErrorAppearanceDao getInstance(Context context) {
        synchronized (ErrorAppearanceDao.class) {
            if (mInstance == null) {
                if (mInstance == null) {
                    mInstance = new ErrorAppearanceDao(context);
                }
            }
        }
        return mInstance;
    }

    private ErrorAppearanceDao(Context context) {
        this.mContext = context;
    }


    /**
     * 保存故障现象
     * @param list
     */
    public void saveErrorAppearance(List<ErrorAppearanceObject> list) {
        DBHelper db = new DBHelper(mContext);
        db.getWritableDatabase().execSQL("delete from " + DBHelper.TABLE_ERROR_APPEARANCE);
        for (ErrorAppearanceObject object : list) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.ERROR_APPEARANCE_COL_ID, object.gzdm_bm);
            contentValues.put(DBHelper.ERROR_APPEARANCE_COL_DESCRIPTION, object.gzdm_ms);
            db.getWritableDatabase().insert(DBHelper.TABLE_ERROR_APPEARANCE, null, contentValues);
        }
        db.close();
    }

    /**
     * 查询权限记录
     * @return
     */
    public ArrayList<ErrorAppearanceObject> queryErrorAppearance() {
        ArrayList<ErrorAppearanceObject> list = new ArrayList<>();
        DBHelper db = new DBHelper(mContext);
        Cursor cursor = db.getReadableDatabase().query(DBHelper.TABLE_ERROR_APPEARANCE, null, null, null, null, null, null);
        if(cursor != null && cursor.moveToFirst()) {
            do {
                ErrorAppearanceObject obj = new ErrorAppearanceObject();
                obj.gzdm_bm = cursor.getString(cursor.getColumnIndex(DBHelper.ERROR_APPEARANCE_COL_ID));
                obj.gzdm_ms = cursor.getString(cursor.getColumnIndex(DBHelper.ERROR_APPEARANCE_COL_DESCRIPTION));
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
        Cursor cursor = db.getReadableDatabase().query(DBHelper.TABLE_ERROR_APPEARANCE, null, null, null, null, null, null);
        if(cursor != null) {
            count = cursor.getCount();
        }
        cursor.close();
        db.close();
        return count;
    }
}
