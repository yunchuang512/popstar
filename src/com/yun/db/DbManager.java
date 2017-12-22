package com.yun.db;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * 操作数据�?
 * 
 * @author Administrator
 * 
 */
public class DbManager {

	// 全局
	private SQLiteDb db;// 数据�?
	private final String DB_NAME = "com.yun.temp_datas.db";// 数据库名�?

	// 考试模块
	public static class mygame {
		public final static String TABLE_NAME = "game";// 表名
		// 列名
		public static final String COLUMN_ID = "_id";
		public static final String COLUMN_X = "x";
		public static final String COLUMN_Y = "y";
		public static final String COLUMN_C = "c";
		public static final String[] columns = { COLUMN_ID, COLUMN_X,
				COLUMN_Y, COLUMN_C };

	}


	public DbManager(Context context) {
		db = new SQLiteDb(context, DB_NAME);
	}

	// 查询�?��记录
	public ArrayList<HashMap<String, String>> query(String tableName,
			String[] columns) {
		ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
		// 查询数据
		Cursor cursor = db.query(tableName, columns, null, null, null, null,
				null, null);
		// 遍历数据
		while (cursor.moveToNext()) {
			HashMap<String, String> map = new HashMap<String, String>();
			for (int i = 0; i < columns.length; i++) {
				String string = cursor.getString(cursor
						.getColumnIndex(columns[i]));
				map.put(columns[i], string);
			}
			arrayList.add(map);
		}
		cursor.close();
		return arrayList;
	}

	// 插入记录
	public void insert(String tableName, String[] columns,
			HashMap<String, String> datas) {
		ContentValues cv = new ContentValues();
		for (int i = 0; i < columns.length; i++) {
			cv.put(columns[i], datas.get(columns[i]));
		}
		db.insert(tableName, cv);
	}

	// 删除记录
	public void delete(String tableName, String id) {
		db.delete(tableName, "_id=?", new String[] { id });
	}

	// 删除记录-�?��
	public void deleteAll(String tableName) {
		try {
			db.delete(tableName, null, null);
		} catch (Exception e) {
			System.out.println("There is no such table.");
		}
	}

	// 删除记录-指定条件
	public void deleteAll(String tableName, String where, String[] values) {
		db.delete(tableName, where, values);
	}

	// 关闭
	public void close() {
		db.close();
	}
}
