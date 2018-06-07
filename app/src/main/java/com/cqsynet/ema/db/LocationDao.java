package com.cqsynet.ema.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.cqsynet.ema.model.LocationObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库位置表的工具类
 */
public class LocationDao {

    private static LocationDao mInstance;
    private Context mContext;

    public static LocationDao getInstance(Context context) {
        synchronized (LocationDao.class) {
            if (mInstance == null) {
                if (mInstance == null) {
                    mInstance = new LocationDao(context);
                }
            }
        }
        return mInstance;
    }

    private LocationDao(Context context) {
        this.mContext = context;
    }


    /**
     * 保存位置记录
     * @param list
     */
    public void saveLocation(List<LocationObject> list) {
        DBHelper db = new DBHelper(mContext);
        db.getWritableDatabase().execSQL("delete from " + DBHelper.TABLE_LOCATION);
        for (LocationObject object : list) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.LOCATION_COL_ID, object.WZ_BM);
            contentValues.put(DBHelper.LOCATION_COL_NAME, object.WZ_MS);
            contentValues.put(DBHelper.LOCATION_COL_PARENT_ID, object.F_WZ_BM);
            db.getWritableDatabase().insert(DBHelper.TABLE_LOCATION, null, contentValues);
        }
        db.close();
    }

    /**
     * 查询位置记录
     * @return
     */
    public ArrayList<LocationObject> queryLocation(String parentId) {
        ArrayList<LocationObject> list = new ArrayList<>();
        DBHelper db = new DBHelper(mContext);
        Cursor cursor;
        if (TextUtils.isEmpty(parentId)) {
            cursor = db.getReadableDatabase().query(DBHelper.TABLE_LOCATION, null, DBHelper.LOCATION_COL_PARENT_ID + " is null", null, null, null, null);
        } else {
            cursor = db.getReadableDatabase().query(DBHelper.TABLE_LOCATION, null, DBHelper.LOCATION_COL_PARENT_ID + "=?", new String[] {parentId}, null, null, null);
        }
        if(cursor != null && cursor.moveToFirst()) {
            do {
                LocationObject obj = new LocationObject();
                obj.WZ_BM = cursor.getString(cursor.getColumnIndex(DBHelper.LOCATION_COL_ID));
                obj.WZ_MS = cursor.getString(cursor.getColumnIndex(DBHelper.LOCATION_COL_NAME));
                obj.F_WZ_BM = cursor.getString(cursor.getColumnIndex(DBHelper.LOCATION_COL_PARENT_ID));
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
        Cursor cursor = db.getReadableDatabase().query(DBHelper.TABLE_LOCATION, null, null, null, null, null, null);
        if(cursor != null) {
            count = cursor.getCount();
        }
        cursor.close();
        db.close();
        return count;
    }
}
