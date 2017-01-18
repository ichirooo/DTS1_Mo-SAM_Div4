package co.id.datascrip.mo_sam_div4_dts1.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import co.id.datascrip.mo_sam_div4_dts1.object.Photo;

/**
 * Created by benny_aziz on 02/23/2015.
 */
public class PhotoSQLite {

    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public PhotoSQLite(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public int Insert(Photo photo) {
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(ConstSQLite.KEY_PHOTO_ID_KEGIATAN, photo.getIdKegiatan());
            values.put(ConstSQLite.KEY_PHOTO_PATH, photo.getPath());
            values.put(ConstSQLite.KEY_PHOTO_INFO, photo.getInfo());
            values.put(ConstSQLite.KEY_PHOTO_POSTED, photo.getPosted());

            long id = db.insert(ConstSQLite.TABLE_PHOTO, null, values);
            db.close();
            return (int) id;
        } catch (Exception e) {
            // TODO: handle exception
            Log.i("insert photo", e.toString());
            return 0;
        }
    }

    public int Update(Photo photo) {
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(ConstSQLite.KEY_PHOTO_ID, photo.getID());
            values.put(ConstSQLite.KEY_PHOTO_ID_KEGIATAN, photo.getIdKegiatan());
            values.put(ConstSQLite.KEY_PHOTO_PATH, photo.getPath());
            values.put(ConstSQLite.KEY_PHOTO_INFO, photo.getInfo());
            values.put(ConstSQLite.KEY_PHOTO_POSTED, photo.getPosted());

            long id = db.update(ConstSQLite.TABLE_PHOTO, values, ConstSQLite.KEY_PHOTO_ID + " = ?",
                    new String[]{String.valueOf(photo.getID())});
            db.close();

            return (int) id;

        } catch (Exception e) {
            // TODO: handle exception
            Log.i("update photo", e.toString());
            return 0;
        }
    }

    public Photo Post(Photo photo) {
        if (photo.getID() == 0)
            photo.setID(Insert(photo));
        else
            photo.setID(Update(photo));
        return photo;
    }

    public void Delete(Photo photo) {
        db = dbHelper.getWritableDatabase();
        String query = "DELETE FROM " + ConstSQLite.TABLE_PHOTO
                + " WHERE " + ConstSQLite.KEY_PHOTO_ID + " = " + String.valueOf(photo.getID());
        db.execSQL(query);
    }

    public ArrayList<Photo> Post(ArrayList<Photo> photos) {
        for (int i = 0; i < photos.size(); i++) {
            if (photos.get(i).getID() == 0)
                photos.get(i).setID(Insert(photos.get(i)));
            else
                photos.get(i).setID(Update(photos.get(i)));
        }
        return photos;
    }

    public ArrayList<Photo> gets(int id_kegiatan) {
        ArrayList<Photo> photos = new ArrayList<Photo>();
        try {
            db = dbHelper.getReadableDatabase();
            String query = "SELECT * FROM " + ConstSQLite.TABLE_PHOTO + " WHERE " + ConstSQLite.KEY_PHOTO_ID_KEGIATAN + " = " + String.valueOf(id_kegiatan);
            Cursor c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                do {
                    Photo photo = new Photo();
                    photo.setIdKegiatan(c.getInt(c.getColumnIndex(ConstSQLite.KEY_PHOTO_ID_KEGIATAN)));
                    photo.setID(c.getInt(c.getColumnIndex(ConstSQLite.KEY_PHOTO_ID)));
                    photo.setInfo(c.getString(c.getColumnIndex(ConstSQLite.KEY_PHOTO_INFO)));
                    photo.setPath(c.getString(c.getColumnIndex(ConstSQLite.KEY_PHOTO_PATH)));
                    photo.setPosted(c.getInt(c.getColumnIndex(ConstSQLite.KEY_PHOTO_POSTED)));
                    photos.add(photo);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            Log.i("gets photo", e.toString());
        } finally {
            db.close();
        }
        return photos;
    }
}
