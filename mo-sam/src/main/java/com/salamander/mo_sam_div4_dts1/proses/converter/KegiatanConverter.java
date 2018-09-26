package com.salamander.mo_sam_div4_dts1.proses.converter;

import android.content.Context;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.object.Kegiatan;
import com.salamander.mo_sam_div4_dts1.proses.RetrofitResponse;
import com.salamander.mo_sam_div4_dts1.sqlite.KegiatanSQLite;
import com.salamander.mo_sam_div4_dts1.sqlite.SalesHeaderSQLite;
import com.salamander.salamander_base_module.Utils;
import com.salamander.salamander_network.RetroData;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class KegiatanConverter {

    public static class SyncKegiatanConverter implements JsonDeserializer<RetroData> {

        private Context context;
        private ArrayList<Integer> list_id_kegiatan;

        public SyncKegiatanConverter(Context context, ArrayList<Integer> list_id_kegiatan) {
            this.context = context;
            this.list_id_kegiatan = list_id_kegiatan;
        }

        @Override
        public RetroData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            ArrayList<Kegiatan> list_kegiatan = RetrofitResponse.getListKegiatan(context, json.toString());
            KegiatanSQLite kegiatanSQLite = new KegiatanSQLite(context);
            int checked_in_kegiatan = App.getSession(context).getCheckedInKegiatan();
            for (Kegiatan kegiatan : list_kegiatan) {
                list_id_kegiatan.remove(Integer.valueOf(kegiatan.getIDServer()));
                if (checked_in_kegiatan == kegiatan.getIDServer())
                    kegiatan.setCheckedIn(1);

                Kegiatan localKegiatan = kegiatanSQLite.get(kegiatan.getIDServer());
                if (localKegiatan != null && localKegiatan.getSalesHeader() != null && (Utils.isEmpty(kegiatan.getSalesHeader().getOrderNo()) || !localKegiatan.getSalesHeader().getOrderNo().toLowerCase().equals(kegiatan.getSalesHeader().getOrderNo().toLowerCase())))
                    new SalesHeaderSQLite(context).deleteFromKegiatan(kegiatan.getIDServer());

                kegiatanSQLite.Post(kegiatan);
            }
            for (int id_kegiatan : list_id_kegiatan) {
                Kegiatan kegiatan = kegiatanSQLite.get(id_kegiatan);
                if (kegiatan.isCheckedIn()) {
                    kegiatan.setCheckedIn(0);
                    kegiatanSQLite.Post(kegiatan);
                }
                kegiatanSQLite.delete(id_kegiatan);
            }
            RetroData retroData = new RetroData();
            retroData.setResult(json.toString());
            return retroData;
        }
    }
}
