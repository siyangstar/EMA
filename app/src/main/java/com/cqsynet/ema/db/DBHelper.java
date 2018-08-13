package com.cqsynet.ema.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库工具类
 */
public class DBHelper extends SQLiteOpenHelper {

    // 数据库名
    public static final String DATABASE_NAME = "ema.db";
    // 数据库版本
    public final static int DATABASE_VERSION = 1;
    // 权限表名
    public static final String TABLE_AUTHORITY = "authority";
    // 字典表名
    public static final String TABLE_DICTIONARY = "dictionary";
    // 位置表名
    public static final String TABLE_LOCATION = "location";
    // 系统种类表名
    public static final String TABLE_SYSTEM_CATEGORY = "system_category";
    // 故障现象表
    public static final String TABLE_ERROR_APPEARANCE = "error_appearance";

    // 权限字段
    public static final String AUTHORITY_COL_ID = "id";
    public static final String AUTHORITY_COL_NAME = "name";
    public static final String AUTHORITY_COL_AUTHORITY = "authority";
    // 字典字段
    public static final String DICTIONARY_COL_ID = "id";
    public static final String DICTIONARY_COL_VALUE = "value";
    public static final String DICTIONARY_COL_TYPE = "type";
    public static final String DICTIONARY_COL_DESCRIPTION = "description";
    // 位置字段
    public static final String LOCATION_COL_ID = "id";
    public static final String LOCATION_COL_NAME = "name";
    public static final String LOCATION_COL_PARENT_ID = "pid";
    // 系统分类字段
    public static final String SYSTEM_CATEGORY_COL_ID = "id";
    public static final String SYSTEM_CATEGORY_COL_NAME = "name";
    public static final String SYSTEM_CATEGORY_COL_PARENT_ID = "pid";
    // 故障现象字段
    public static final String ERROR_APPEARANCE_COL_ID = "id";
    public static final String ERROR_APPEARANCE_COL_DESCRIPTION = "description";



    // 创建权限表的语句
    public static final String CREATE_TABLE_AUTHORITY = "CREATE TABLE IF NOT EXISTS " + TABLE_AUTHORITY + "("
            + AUTHORITY_COL_ID + " TEXT PRIMARY KEY,"
            + AUTHORITY_COL_NAME + " TEXT,"
            + AUTHORITY_COL_AUTHORITY + " TEXT)";
    // 创建字典表的语句
    public static final String CREATE_TABLE_DICTIONARY = "CREATE TABLE IF NOT EXISTS " + TABLE_DICTIONARY + "("
            + DICTIONARY_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DICTIONARY_COL_VALUE + " TEXT,"
            + DICTIONARY_COL_TYPE + " TEXT,"
            + DICTIONARY_COL_DESCRIPTION + " TEXT)";
    // 创建位置表的语句
    public static final String CREATE_TABLE_LOCATION = "CREATE TABLE IF NOT EXISTS " + TABLE_LOCATION + "("
            + LOCATION_COL_ID + " TEXT PRIMARY KEY,"
            + LOCATION_COL_NAME + " TEXT,"
            + LOCATION_COL_PARENT_ID + " TEXT)";
    // 创建系统分类表的语句
    public static final String CREATE_TABLE_SYSTEM_CATEGORY = "CREATE TABLE IF NOT EXISTS " + TABLE_SYSTEM_CATEGORY + "("
            + SYSTEM_CATEGORY_COL_ID + " TEXT PRIMARY KEY,"
            + SYSTEM_CATEGORY_COL_NAME + " TEXT,"
            + SYSTEM_CATEGORY_COL_PARENT_ID + " TEXT)";
    // 创建故障现象表的语句
    public static final String CREATE_TABLE_ERROR_APPEARANCE = "CREATE TABLE IF NOT EXISTS " + TABLE_ERROR_APPEARANCE + "("
            + ERROR_APPEARANCE_COL_ID + " TEXT PRIMARY KEY,"
            + ERROR_APPEARANCE_COL_DESCRIPTION + " TEXT)";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void cleanAllTable(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + DBHelper.TABLE_AUTHORITY);
        db.execSQL("drop table if exists " + DBHelper.TABLE_DICTIONARY);
        db.execSQL("drop table if exists " + DBHelper.TABLE_LOCATION);
        db.execSQL("drop table if exists " + DBHelper.TABLE_SYSTEM_CATEGORY);
        db.execSQL("drop table if exists " + DBHelper.TABLE_ERROR_APPEARANCE);
        createTable(db);
        db.close();
    }

    public void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_AUTHORITY);
        db.execSQL(CREATE_TABLE_DICTIONARY);
        db.execSQL(CREATE_TABLE_LOCATION);
        db.execSQL(CREATE_TABLE_SYSTEM_CATEGORY);
        db.execSQL(CREATE_TABLE_ERROR_APPEARANCE);
    }
}