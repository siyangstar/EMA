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
     * @param authorityList
     */
    public void saveAuthority(List<AuthorityObject> authorityList) {
        DBHelper db = new DBHelper(mContext);
        db.getWritableDatabase().execSQL("delete from " + DBHelper.AUTHORITY_TABLE);
        for (AuthorityObject authorityObject : authorityList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.AUTHORITY_COL_ID, authorityObject.id);
            contentValues.put(DBHelper.AUTHORITY_COL_NAME, authorityObject.name);
            contentValues.put(DBHelper.AUTHORITY_COL_IS_SHOW, authorityObject.isShow);
            db.getWritableDatabase().insert(DBHelper.AUTHORITY_TABLE, null, contentValues);
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
        Cursor cursor = db.getReadableDatabase().query(DBHelper.AUTHORITY_TABLE, null, null, null, null, null, null);
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
