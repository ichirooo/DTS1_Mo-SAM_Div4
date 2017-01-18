package co.id.datascrip.mo_sam_div4_dts1.callback;

import java.util.ArrayList;

import co.id.datascrip.mo_sam_div4_dts1.adapter.Adapter_List_Photo;
import co.id.datascrip.mo_sam_div4_dts1.object.Photo;

/**
 * Created by benny_aziz on 02/24/2015.
 */
public class CallbackPhoto {

    public interface CBCamera {
        void onCB(Photo photo);
    }

    public interface CBLoadSQLitePhoto {
        void onCB(ArrayList<Photo> list_photos, Adapter_List_Photo adapter_list_photo);
    }

    public interface CBUploadPhoto {
        void onCB(boolean success);
    }
}
