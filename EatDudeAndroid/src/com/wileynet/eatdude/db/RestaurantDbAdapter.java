/*
 * 
    Copyright (C) 2012  Wiley Snyder

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or 
     any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
	
	Any other questions or concerns contact wiley@wileynet.com

*/

package com.wileynet.eatdude.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RestaurantDbAdapter {
	
	private static final String TAG = "RestaurantDbAdapter";
	private final Context mCtx;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
	
    //all tables
    private static final String KEY_ROWID = "_id";
    
    //restaurant table
    private static final String DB_RESTAURANT_TABLE = "restaurant";
	private static final String KEY_RESTAURANT_ID = "id";
    private static final String KEY_RESTAURANT_NAME = "name";
    private static final String KEY_RESTAURANT_PHONE = "phone";
    private static final String KEY_RESTAURANT_ADDRESS = "address";
    private static final String DB_RESTAURANT_CREATE = 
    	"create table "+DB_RESTAURANT_TABLE+" (_id integer primary key autoincrement, "
        + "id text not null, name text not null,phone text not null,address text not null);";
    
    //menu table
    private static final String DB_MENU_TABLE = "menu";
    private static final String KEY_MENU_ID = "id";
    private static final String KEY_MENU_NAME = "name";
    private static final String DB_MENU_CREATE =
    	"create table "+DB_MENU_TABLE+" (_id integer primary key autoincrement, "
        + "id text not null, name text not null);";
    
  //category table
    private static final String DB_CATEGORY_TABLE = "category";
    private static final String KEY_CATEGORY_ID = "id";
    private static final String KEY_CATEGORY_MENU_ID = "menu_id";
    private static final String KEY_CATEGORY_NAME = "name";
    private static final String DB_CATEGORY_CREATE =
    	"create table "+DB_CATEGORY_TABLE+" (_id integer primary key autoincrement, "
        + "id text not null, menu_id text not null, name text not null);";
    
    //item table
    private static final String DB_ITEM_TABLE = "item";
    private static final String KEY_ITEM_ID = "id";
    private static final String KEY_ITEM_MENU_ID = "menu_id";
    private static final String KEY_ITEM_CAT_ID = "cat_id";
    private static final String KEY_ITEM_NAME = "name";
    private static final String KEY_ITEM_DESC = "desc";
    private static final String KEY_ITEM_PRICE = "price";
    private static final String KEY_ITEM_NUMBER = "number";
    private static final String DB_ITEM_CREATE =
    	"create table "+DB_ITEM_TABLE+" (_id integer primary key autoincrement, "
        + "id text not null, menu_id text not null, cat_id text not null, name text not null, desc text not null, "
        + "price text not null, number text not null);";
    
    //db
    private static final String DATABASE_NAME = "eatdudemenu";
    private static final int DATABASE_VERSION=2;
	
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		
        DatabaseHelper(Context context) {
        	super(context,DATABASE_NAME,null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)throws SQLException {
        	db.execSQL(DB_RESTAURANT_CREATE);
        	db.execSQL(DB_MENU_CREATE);
        	db.execSQL(DB_CATEGORY_CREATE);
        	db.execSQL(DB_ITEM_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DB_RESTAURANT_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DB_MENU_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DB_CATEGORY_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DB_ITEM_TABLE);
            onCreate(db);
        }
    }
	
	 /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public RestaurantDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }


	/**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
	public RestaurantDbAdapter open() throws SQLException {
			mDbHelper = new DatabaseHelper(mCtx);
			mDb = mDbHelper.getWritableDatabase();
			return this;
    }
	
	public void close() {
        mDbHelper.close();
    }
	
	
	public void clearTables()throws SQLException{
    	mDb.delete(DB_RESTAURANT_TABLE, null,null);
    	mDb.delete(DB_MENU_TABLE, null,null);
    	mDb.delete(DB_CATEGORY_TABLE, null,null);
    	mDb.delete(DB_ITEM_TABLE, null,null);
	}
	
	 /**
     * Create a new restaurant using the id and name provided. Yadda Yadda ...
     * Yadda.
     * a -1 to indicate failure.
     * 
     * @param id the id of the restaurant
     * @param name the name of the restaurant
     * @return rowId or -1 if failed
     */
    public long insertRestaurant(String id, String name, String phone, String address)throws SQLException {
    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_RESTAURANT_ID, id);
        initialValues.put(KEY_RESTAURANT_NAME, name);
        initialValues.put(KEY_RESTAURANT_PHONE, phone);
        initialValues.put(KEY_RESTAURANT_ADDRESS, address);
        return mDb.insert(DB_RESTAURANT_TABLE, null, initialValues);
    }
    
    public long insertMenu(String id, String name)throws SQLException {
    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_MENU_ID, id);
        initialValues.put(KEY_MENU_NAME, name);
        return mDb.insert(DB_MENU_TABLE, null, initialValues);
    }
    
    public long insertCategory(String menu_id, String id, String name)throws SQLException {
    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CATEGORY_ID, id);
        initialValues.put(KEY_CATEGORY_MENU_ID, menu_id);
        initialValues.put(KEY_CATEGORY_NAME, name);
        return mDb.insert(DB_CATEGORY_TABLE, null, initialValues);
    }
    
    public long insertItem(String id, String menu_id, String cat_id, String name, 
    		String desc, String price, String number)throws SQLException {
    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ITEM_ID, id);
        initialValues.put(KEY_ITEM_MENU_ID, menu_id);
        initialValues.put(KEY_ITEM_CAT_ID, cat_id);
        initialValues.put(KEY_ITEM_NAME, name);
        initialValues.put(KEY_ITEM_DESC, desc);
        initialValues.put(KEY_ITEM_PRICE, price);
        initialValues.put(KEY_ITEM_NUMBER, number);
        return mDb.insert(DB_ITEM_TABLE, null, initialValues);
    }
    
    /**
     * Delete the restaurant with the given rowId
     * 
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteRestaurant(long rowId) {
        return mDb.delete(DB_RESTAURANT_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    /**
     * Return a Cursor over the list of all restaurants in the database
     * 
     * @return Cursor over all restaurants
     */
    public Cursor fetchAllRestaurants() {
        //return mDb.query(DB_RESTAURANT_TABLE, new String[] {KEY_ROWID, KEY_RESTAURANT_ID,
                //KEY_RESTAURANT_NAME}, null, null, null, null, null);
    	Cursor c =
        	mDb.rawQuery("Select * FROM restaurant;'", null);
       if (c != null) {
           c.moveToFirst();
       }
       return c;
    }
    
    /**
     * Return a Cursor over the list of all restaurants in the database
     * 
     * @return Cursor over all restaurants
     */
    public Cursor fetchAllMenus() {
        //return mDb.query(DB_MENU_TABLE, new String[] {KEY_ROWID, KEY_MENU_ID,
                //KEY_MENU_NAME}, null, null, null, null, null);
    	Cursor c =
        	mDb.rawQuery("Select * FROM menu;'", null);
       if (c != null) {
           c.moveToFirst();
       }
       return c;
    }
    
    /**
     * Return a Cursor over the list of all restaurants in the database
     * 
     * @return Cursor over all restaurants
     */
    public Cursor fetchAllItems() {
        return mDb.query(DB_ITEM_TABLE, new String[] {KEY_ROWID, KEY_ITEM_ID,
                KEY_ITEM_CAT_ID,KEY_ITEM_NAME,KEY_ITEM_DESC,KEY_ITEM_PRICE,
                KEY_ITEM_NUMBER}, null, null, null, null, null);
    }
    
    /**
     * Return a Cursor positioned at the restaurant that matches the given rowId
     * 
     * @param rowId id of note to retrieve
     * @return 
     * @return Cursor positioned to matching restaurant, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchAllCategories(String menu_id){
       Cursor c =
        	mDb.rawQuery("Select * From category where menu_id='" + menu_id +"';", null);
       if (c != null) {
           c.moveToFirst();
       }
       return c;
    }
    
    
    /**
     * Return a Cursor positioned at the restaurant that matches the given rowId
     * 
     * @param rowId id of note to retrieve
     * @return 
     * @return Cursor positioned to matching restaurant, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchCategory(String CategoryName, String m_id) throws SQLException {
       Cursor c =
        	mDb.rawQuery("Select id FROM category where menu_id ='" + m_id + "' AND name='" + CategoryName +"';", null);
       if (c != null) {
           c.moveToFirst();
       }
       return c;
    }
    
    /**
     * Return a Cursor positioned at the restaurant that matches the given rowId
     * 
     * @param rowId id of note to retrieve
     * @return 
     * @return Cursor positioned to matching restaurant, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchItems(String CategoryId, String m_id) throws SQLException {
       Cursor c =
        	mDb.rawQuery("Select * FROM item where menu_id ='" + m_id + "' AND cat_id='" + CategoryId +"';", null);
       if (c != null) {
           c.moveToFirst();
       }
       return c;
    }
    
    /**
     * Return a Cursor positioned at the restaurant that matches the given rowId
     * 
     * @param rowId id of note to retrieve
     * @return 
     * @return Cursor positioned to matching restaurant, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchItem(String ItemId) throws SQLException {
       Cursor c =
        	mDb.rawQuery("Select * FROM item where id='" + ItemId +"';", null);
       if (c != null) {
           c.moveToFirst();
       }
       return c;
    }
    
    
    /**
     * Return a Cursor positioned at the restaurant that matches the given rowId
     * 
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching restaurant, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchRestaurant(long rowId) throws SQLException {
        Cursor mCursor =
            mDb.query(true, DB_RESTAURANT_TABLE, new String[] {KEY_ROWID,
                    KEY_RESTAURANT_ID, KEY_RESTAURANT_NAME}, KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    /**
     * Update the restaurant using the details provided. The restaurant to be updated is
     * specified using the rowId, and it is altered to use the id and name
     * values passed in
     * 
     * @param rowId id of note to update
     * @param id value to set restaurant id to
     * @param name value to set restaurant name to
     * @return true if the restaurant was successfully updated, false otherwise
     */
    public boolean updateRestaurant(long rowId, String id, String name) {
        ContentValues args = new ContentValues();
        args.put(KEY_RESTAURANT_ID, id);
        args.put(KEY_RESTAURANT_NAME, name);

        return mDb.update(DB_RESTAURANT_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*
    public void buildMenuOldforReference() {
    	
		String url = "http://wileynet3.appspot.com/example_xml/1001";
		SAXHelper sh = new SAXHelper();
		
			sh.parseContent(url);
			r = new Restaurant(sh.restaurant_id,
					  						  sh.restaurant_name,
					  						  sh.menu_id,
					  						  sh.menu_name,
					  						  sh.menu_category,
					  						  sh.menu_item);
		
		//System.out.println( r.menu_category );
		
		
        System.out.println( r.menu_id + " - " + r.menu_name );
        System.out.println("===================");
        
        
		
        //category
        for(Map.Entry<String,String> entry : r.getMenu_category().entrySet()) {
        	  String key = entry.getKey();
        	  String value = entry.getValue();
        	  System.out.println("category : " + key + " => " + value);
        	}
        
       
        System.out.println("===================");
        
		
        //item
        for(Map.Entry<String,String> entry : r.getMenu_item().entrySet()) {
      	  String key = entry.getKey();
      	  String value = entry.getValue();

      	  System.out.println("item : " + key + " => " + value);
      	}
	}
	*/
    
    @SuppressWarnings("unused")
	private boolean checkDbExists(String dbname) {
        SQLiteDatabase checkDB = null;
        String dbpath = "DB_PATH" + dbname;
        try {
            checkDB = SQLiteDatabase.openDatabase(dbpath, null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            // no database
        }
        return checkDB != null ? true : false;
    }
}
