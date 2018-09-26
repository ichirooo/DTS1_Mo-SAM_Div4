package com.salamander.mo_sam_div4_dts1.proses;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.Function;
import com.salamander.mo_sam_div4_dts1.object.Customer;
import com.salamander.mo_sam_div4_dts1.object.Feedback;
import com.salamander.mo_sam_div4_dts1.object.Holiday;
import com.salamander.mo_sam_div4_dts1.object.Item;
import com.salamander.mo_sam_div4_dts1.object.Kegiatan;
import com.salamander.mo_sam_div4_dts1.object.Log_Feed;
import com.salamander.mo_sam_div4_dts1.object.Photo;
import com.salamander.mo_sam_div4_dts1.object.SalesHeader;
import com.salamander.mo_sam_div4_dts1.object.User;
import com.salamander.mo_sam_div4_dts1.sqlite.HolidaySQLite;
import com.salamander.mo_sam_div4_dts1.sqlite.LogSQLite;
import com.salamander.mo_sam_div4_dts1.sqlite.PhotoSQLite;
import com.salamander.mo_sam_div4_dts1.sqlite.SalesHeaderSQLite;
import com.salamander.mo_sam_div4_dts1.sqlite.TermsOfPaymentSQLite;
import com.salamander.salamander_network.JSON;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class RetrofitResponse {

    @Nullable
    public static String getString(Context context, Response<ResponseBody> response) {
        try {
            String respon = response.body().string().trim();
            if (response.isSuccessful()) {
                if (!respon.isEmpty())
                    return respon;
                else
                    return null;
            } else {
                new AlertDialog.Builder(context)
                        .setMessage(response.errorBody().string())
                        .setPositiveButton("OK", null)
                        .create()
                        .show();
            }
        } catch (Exception e) {
            new Function().writeToText(e.getClass().getSimpleName(), e.toString());
            return null;
        }
        return null;
    }

    public static ArrayList<Customer> getListCustomer(Context context, String json) {
        ArrayList<Customer> list_customer = new ArrayList<>();
        try {
            JSONArray jsonArray = JSON.getJSONArray(json, "data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                list_customer.add(new Customer().getFromJSON(context, jsonObject));
            }
        } catch (Exception e) {
            Log.e("getListCustomer", e.toString());
        }
        return list_customer;
    }
    /*
    static ArrayList<Customer> getListCustomer(String json) {
        ArrayList<Customer> customers = new ArrayList<>();
        try {
            JSONArray ja_customer = new JSONArray(json);
            int length = ja_customer.length();
            for (int i = 0; i < length; i++) {
                Customer customer = new Customer();
                JSONObject jo = ja_customer.getJSONObject(i);
                customer.setCode(jo.getString("no").trim());
                customer.setName(jo.getString("name").trim());
                customer.setAddress1(jo.getString("address1").trim());
                customer.setAddress2(jo.getString("address2").trim());
                customer.setCity(jo.getString("city").trim());
                customer.setNPWP(jo.getString("npwp").trim());
                customer.setContactPerson(jo.getString("kontak").trim());
                customer.setPhone(jo.getString("phone").trim());
                customer.setLatitude(jo.getDouble("lat"));
                customer.setLongitude(jo.getDouble("lang"));
                customers.add(customer);
            }
        } catch (Exception e) {
            return customers;
        }
        return customers;
    }
    */

    static String getCustomerCode(String json) {
        try {
            JSONObject jo = new JSONObject(json);
            return jo.getString("no").trim();
        } catch (Exception e) {
            return null;
        }
    }

    static Kegiatan getIDKegiatan(Kegiatan kegiatan, String json) {
        try {
            JSONObject jo = new JSONObject(json);
            if (kegiatan.getIDServer() == 0)
                kegiatan.setIDServer(jo.getInt("id"));
        } catch (Exception e) {
            return kegiatan;
        }
        return kegiatan;
    }

    public static ArrayList<Kegiatan> getListKegiatan(Context context, String json) {
        ArrayList<Kegiatan> list_kegiatan = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("data")) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    list_kegiatan.add(new Kegiatan().getFromJSON(context, object));
                }

                if (jsonObject.has(Kegiatan.KEGIATAN_LIST_PHOTO)) {
                    JSONArray jsonArrayPhoto = jsonObject.getJSONArray(Kegiatan.KEGIATAN_LIST_PHOTO);
                    PhotoSQLite photoSQLite = new PhotoSQLite(context);
                    for (int j = 0; j < jsonArrayPhoto.length(); j++) {
                        JSONObject jsonPhoto = jsonArrayPhoto.getJSONObject(j);
                        photoSQLite.Post(new Photo().getFromJSON(jsonPhoto));
                    }
                }
            }
        } catch (Exception e) {
            Log.d(App.TAG, RetrofitResponse.class.getSimpleName() + " => getListKegiatan  => " + e.toString());
        }
        return list_kegiatan;
    }

    /*
        public static ArrayList<Kegiatan> getListKegiatan(Context context, String json) {
            ArrayList<Kegiatan> list_kegiatan = new ArrayList<>();
            try {
                JSONArray ja = new JSONArray(json);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    Kegiatan k = new Kegiatan();
                    k.setIDServer(jo.getInt("id"));
                    k.setCancel(jo.getInt("cancel"));
                    if (k.getCancel() == 1) {
                        if (jo.getString("c_reason") == null)
                            k.setCancelReason("-");
                        else {
                            if (jo.getString("c_reason").toLowerCase().equals("null"))
                                k.setCancelReason("-");
                            else
                                k.setCancelReason(jo.getString("c_reason"));
                        }
                    }
                    k.setSalesHeader(new SalesHeader());
                    k.getSalesHeader().setIDServer(jo.getInt("id_h"));
                    k.getSalesHeader().setOrderNo(jo.getString("andro"));
                    k.getSalesHeader().setStatus(jo.getInt("status"));
                    list_kegiatan.add(k);
                }
            } catch (Exception e) {
                return list_kegiatan;
            }
            return list_kegiatan;
        }
    */
    static String getNoOrder(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return getString(jsonObject, SalesHeader.SALES_HEADER_ORDER_NO);
        } catch (Exception e) {
            return null;
        }
    }

    static int getStatusOrder(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.getInt("sales_status");
        } catch (Exception e) {
            return 0;
        }
    }

    static double getPrice(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.getDouble("PL");
        } catch (Exception e) {
            return 0;
        }
    }

    public static ArrayList<Item> getListItem(String json) {
        ArrayList<Item> list_item = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("data")) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                int size = jsonArray.length();
                for (int i = 0; i < size; i++) {
                    list_item.add(new Item().getFromJSON(jsonArray.getJSONObject(i)));
                }
            }
        } catch (Exception e) {
            Log.d(App.TAG, RetrofitResponse.class.getSimpleName() + " => getListItem  => " + e.toString());
        }
        return list_item;
    }

    public static void getOrderData(Context context, String respon, SalesHeader salesHeader) {
        SalesHeaderSQLite salesHeaderSQLite = new SalesHeaderSQLite(context);
        try {
            JSONObject jsonObject = new JSONObject(respon);
            if (jsonObject.has("data")) {
                jsonObject = jsonObject.getJSONObject("data");

                int id_server = JSON.getInt(jsonObject, SalesHeader.SALES_HEADER_ID_SERVER);
                int status_code = JSON.getInt(jsonObject, SalesHeader.SALES_HEADER_STATUS);
                salesHeader.setIDServer(id_server);
                salesHeader.setStatus(status_code);
                //salesHeader = salesHeaderSQLite.updateID(salesHeader.getIDKegiatan(), salesHeader.getIDServer(), id_server);
                //salesHeader = salesHeaderSQLite.updateStatus(salesHeader.getIDKegiatan(), salesHeader.getIDServer(), status_code);

                ArrayList<Item> list_item = new ArrayList<>(salesHeader.getSalesLine());
                salesHeader.getSalesLine().clear();
                JSONArray jsonArrayLine = jsonObject.getJSONArray(SalesHeader.SALES_HEADER_SALES_LINE);

                for (int i = 0; i < jsonArrayLine.length(); i++) {
                    JSONObject jsonObjectLine = jsonArrayLine.getJSONObject(i);
                    int IDServer = JSON.getInt(jsonObjectLine, Item.ITEM_ID_SERVER);
                    int LineNo = JSON.getInt(jsonObjectLine, Item.ITEM_LINE_NO);
                    for (Item item : list_item) {
                        item.setIDHeader(salesHeader.getIDServer());
                        if (item.getLineNo() == LineNo) {
                            item.setIDServer(IDServer);
                            salesHeader.getSalesLine().add(item);
                        }
                    }
                }
                salesHeaderSQLite.post(salesHeader);
            }
        } catch (Exception e) {
            Log.d(App.TAG, RetrofitResponse.class.getSimpleName() + " => getOrderData  => " + e.toString());
        }
    }

    static Kegiatan PostSalesHeader(Kegiatan kegiatan, String json, String no_order) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            kegiatan.getSalesHeader().setIDServer(jsonObject.getInt("id"));
            kegiatan.getSalesHeader().setOrderNo(no_order);
            for (int i = 0; i < kegiatan.getSalesHeader().getSalesLine().size(); i++) {
                kegiatan.getSalesHeader().getSalesLine().get(i).setIDHeader(jsonObject.getInt("id"));
            }
            return kegiatan;
        } catch (Exception e) {
            return kegiatan;
        }
    }

    static ArrayList<Feedback> getListFeedback(Context context, String json_array) {
        ArrayList<Feedback> list_feedback = new ArrayList<>();
        ArrayList<Log_Feed> logs = new ArrayList<>();
        try {
            JSONArray ja = new JSONArray(json_array);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo_feed = ja.getJSONObject(i);
                Feedback feedback = new Feedback();
                feedback.setUser(App.getUser(context));
                feedback.setOrderNo(jo_feed.getString("so"));
                feedback.setNote(jo_feed.getString("note"));
                feedback.setLineID(jo_feed.getInt("id"));
                list_feedback.add(feedback);

                Log_Feed log = new Log_Feed();
                log.setFeedback(feedback);
                log.getFeedback().setUser(new User());
                log.getFeedback().getUser().setEmpNo(jo_feed.getInt("c"));
                logs.add(log);
            }
        } catch (Exception e) {
            new Function(context).writeToText("feedback", e.toString());
            return list_feedback;
        }
        new LogSQLite(context).Insert(logs);
        return list_feedback;
    }

    static boolean isSuccess(String json) {
        if (json == null)
            return false;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            return jsonObject.has("status") && jsonObject.getString("status").trim().toLowerCase().equals("success");
        } catch (Exception e) {
            return false;
        }
    }

    static String getErrorMessage(String json) {
        String msg = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            msg = jsonObject.getString("msg");
        } catch (Exception e) {
            new Function().writeToText("getErrorMessage", e.toString());
        }
        return msg;
    }

    private static String getString(JSONObject jsonObject, String key) {
        try {
            if (jsonObject.has(key))
                return jsonObject.getString(key);
            else return null;
        } catch (Exception e) {
            new Function().writeToText("getStringJSON", e.toString());
            return null;
        }
    }

    private static boolean isEmpty(String teks) {
        return teks == null || teks.trim().isEmpty();
    }

    public static void getData(Context context, String json) {
        try {
            JSONObject jsonObjects = new JSONObject(json);
            if (jsonObjects.has("data")) {
                JSONObject jsonData = jsonObjects.getJSONObject("data");

                if (jsonData.has("holiday")) {
                    JSONObject jsonHoliday = jsonData.getJSONObject("holiday");
                    if (jsonHoliday.has("data")) {
                        HolidaySQLite holidaySQLite = new HolidaySQLite(context);
                        JSONArray jsonArray = jsonHoliday.getJSONArray("data");
                        if (jsonArray.length() > 0)
                            holidaySQLite.clear(Calendar.getInstance().get(Calendar.YEAR));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            holidaySQLite.Post(new Holiday(jsonObject.getString(Holiday.HOLIDAY_TANGGAL), jsonObject.getString(Holiday.HOLIDAY_DESCRIPTION)));
                        }
                    }
                }

                if (jsonData.has("terms")) {
                    JSONObject jsonTerms = jsonData.getJSONObject("terms");
                    if (jsonTerms.has("data")) {
                        TermsOfPaymentSQLite termsOfPaymentSQLite = new TermsOfPaymentSQLite(context);
                        JSONArray jsonArray = jsonTerms.getJSONArray("data");
                        if (jsonArray.length() > 0)
                            termsOfPaymentSQLite.clear();
                        for (int i = 0; i < jsonArray.length(); i++)
                            termsOfPaymentSQLite.Post(jsonArray.getString(i));
                    }
                }
            }
        } catch (Exception e) {
            Log.e(e.getClass().getSimpleName(), e.toString());
        }
    }
}
