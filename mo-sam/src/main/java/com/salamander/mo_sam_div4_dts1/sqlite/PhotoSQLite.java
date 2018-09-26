package com.salamander.mo_sam_div4_dts1.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.salamander.mo_sam_div4_dts1.object.Photo;
import com.salamander.mo_sam_div4_dts1.util.FileUtil;
import com.salamander.salamander_base_module.object.Tanggal;

import java.util.ArrayList;

public class PhotoSQLite {

    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public PhotoSQLite(Context context) {
        this.dbHelper = new DatabaseHelper(context);
        this.context = context;
    }

    public Photo Insert(Photo photo) {
        try {
            db = dbHelper.getWritableDatabase();
            long result = db.insert(ConstSQLite.TABLE_PHOTO, null, photoToContentValues(photo));
            db.close();

            return getLast();
        } catch (Exception e) {
            FileUtil.writeExceptionLog(context, PhotoSQLite.class.getSimpleName() + " => Insert => ", e);
            db.close();
            return null;
        }
    }

    public Photo Update(Photo photo) {
        try {
            db = dbHelper.getWritableDatabase();

            long result = db.update(ConstSQLite.TABLE_PHOTO, photoToContentValues(photo), Photo.PHOTO_ID_SERVER + " = ?",
                    new String[]{String.valueOf(photo.getIDServer())});

            db.close();

            return get(photo.getIDServer());
        } catch (Exception e) {
            FileUtil.writeExceptionLog(context, PhotoSQLite.class.getSimpleName() + " => Update => ", e);
            db.close();
            return null;
        }
    }

    public Photo Post(Photo photo) {
        if (isExist(photo))
            return Update(photo);
        else
            return Insert(photo);
    }

    private boolean isExist(Photo photo) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_PHOTO + " WHERE " +
                Photo.PHOTO_ID_SERVER + " = " + String.valueOf(photo.getIDServer());
        Cursor c = db.rawQuery(query, null);
        boolean exist = c.getCount() > 0;
        db.close();
        c.close();
        return exist;
    }

    public void Delete(int id) {
        db = dbHelper.getReadableDatabase();
        String query = "DELETE FROM " + ConstSQLite.TABLE_PHOTO + " WHERE " +
                Photo.PHOTO_ID_SERVER + " = " + String.valueOf(id);
        db.execSQL(query);
        db.close();
    }

    public Photo get(int id) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_PHOTO + " WHERE " +
                Photo.PHOTO_ID_SERVER + " = '" + String.valueOf(id) + "'";

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {

            Photo photo = cursorToPhoto(c);

            db.close();
            c.close();
            return photo;
        } else {
            db.close();
            c.close();
            return null;
        }
    }

    public Photo getLast() {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_PHOTO +
                " ORDER BY " + Photo.PHOTO_ID_SERVER + " DESC LIMIT 1";

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {

            Photo photo = cursorToPhoto(c);

            db.close();
            c.close();
            return photo;
        } else {
            db.close();
            c.close();
            return null;
        }
    }

    public ArrayList<Photo> getAll(int id_kegiatan) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_PHOTO + " WHERE " +
                Photo.PHOTO_ID_KEGIATAN + " = " + String.valueOf(id_kegiatan) +
                " ORDER BY " + Photo.PHOTO_TANGGAL + " DESC";

        ArrayList<Photo> list_photo = new ArrayList<>();

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                list_photo.add(cursorToPhoto(c));
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return list_photo;
    }

    private ContentValues photoToContentValues(Photo photo) {
        ContentValues values = new ContentValues();
        values.put(Photo.PHOTO_ID_SERVER, photo.getIDServer());
        values.put(Photo.PHOTO_ID_KEGIATAN, photo.getIDKegiatan());
        values.put(Photo.PHOTO_PATH, photo.getPath());
        values.put(Photo.PHOTO_NAME, photo.getName());
        values.put(Photo.PHOTO_URL, photo.getURL());
        values.put(Photo.PHOTO_DESCRIPTION, photo.getDescription());
        values.put(Photo.PHOTO_TANGGAL, photo.getTanggal().getTglString());
        return values;
    }

    private Photo cursorToPhoto(Cursor c) {
        Photo photo = new Photo();
        photo.setIDLocal(ConstSQLite.getInt(c, Photo.PHOTO_ID_LOCAL));
        photo.setIDServer(ConstSQLite.getInt(c, Photo.PHOTO_ID_SERVER));
        photo.setIDKegiatan(ConstSQLite.getInt(c, Photo.PHOTO_ID_KEGIATAN));
        photo.setPath(ConstSQLite.getString(c, Photo.PHOTO_PATH));
        photo.setName(ConstSQLite.getString(c, Photo.PHOTO_NAME));
        photo.setURL(ConstSQLite.getString(c, Photo.PHOTO_URL));
        photo.setDescription(ConstSQLite.getString(c, Photo.PHOTO_DESCRIPTION));
        photo.getTanggal().set(Tanggal.FORMAT_DATETIME_FULL, ConstSQLite.getString(c, Photo.PHOTO_TANGGAL));
        return photo;
    }
}
