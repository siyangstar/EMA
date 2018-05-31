package com.cqsynet.ema.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.cqsynet.ema.model.AuthorityObject;

import java.util.ArrayList;
import java.util.List;

public class AuthorityDao {

    private static AuthorityDao mInstance;
    private Context mContext;

    public static AuthorityDao getInstance(Context context) {
        if (mInstance == null) {
            if (mInstance == null) {
                mInstance = new AuthorityDao(context);
            }
        }
        return mInstance;
    }

    private AuthorityDao(Context context) {
        this.mContext = context;
    }


    /**
     * 保存权限记录
     * @param list
     */
    public void saveAuthority(List<AuthorityObject> list) {
        DBHelper db = new DBHelper(mContext);
        db.getWritableDatabase().execSQL("delete from " + DBHelper.TABLE_AUTHORITY);
        for (AuthorityObject object : list) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.AUTHORITY_COL_ID, object.id);
            contentValues.put(DBHelper.AUTHORITY_COL_NAME, object.name);
            contentValues.put(DBHelper.AUTHORITY_COL_IS_SHOW, object.isShow);
            db.getWritableDatabase().insert(DBHelper.TABLE_AUTHORITY, null, contentValues);
        }
        db.close();
    }

    /**
     * 查询权限记录
     * @return
     */
    public ArrayList<AuthorityObject> queryAuthority() {
        ArrayList<AuthorityObject> list = new ArrayList<>();
        DBHelper db = new DBHelper(mContext);
        Cursor cursor = db.getReadableDatabase().query(DBHelper.TABLE_AUTHORITY, null, null, null, null, null, null);
        if(cursor != null && cursor.moveToFirst()) {
            do {
                AuthorityObject obj = new AuthorityObject();
                obj.id = cursor.getString(cursor.getColumnIndex(DBHelper.AUTHORITY_COL_ID));
                obj.name = cursor.getString(cursor.getColumnIndex(DBHelper.AUTHORITY_COL_NAME));
                obj.isShow = cursor.getString(cursor.getColumnIndex(DBHelper.AUTHORITY_COL_IS_SHOW));
                list.add(obj);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
}
