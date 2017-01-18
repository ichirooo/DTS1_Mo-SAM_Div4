package co.id.datascrip.mo_sam_div4_dts1.process;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import co.id.datascrip.mo_sam_div4_dts1.Function;
import co.id.datascrip.mo_sam_div4_dts1.Global;
import co.id.datascrip.mo_sam_div4_dts1.object.BEX;
import co.id.datascrip.mo_sam_div4_dts1.object.Customer;
import co.id.datascrip.mo_sam_div4_dts1.object.Feedback;
import co.id.datascrip.mo_sam_div4_dts1.object.Item;
import co.id.datascrip.mo_sam_div4_dts1.object.Kegiatan;
import co.id.datascrip.mo_sam_div4_dts1.object.Log_Feed;
import co.id.datascrip.mo_sam_div4_dts1.object.SalesHeader;
import co.id.datascrip.mo_sam_div4_dts1.sqlite.LogSQLite;
import okhttp3.ResponseBody;
import retrofit2.Response;

class RetrofitResponse {

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
                customer.setContact(jo.getString("kontak").trim());
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
            if (kegiatan.getID() == 0)
                kegiatan.setID(jo.getInt("id"));
        } catch (Exception e) {
            return kegiatan;
        }
        return kegiatan;
    }

    static ArrayList<Kegiatan> getListKegiatan(String json) {
        ArrayList<Kegiatan> list_kegiatan = new ArrayList<>();
        try {
            JSONArray ja = new JSONArray(json);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                Kegiatan k = new Kegiatan();
                k.setID(jo.getInt("id"));
                k.setCancel(jo.getInt("cancel"));
                if (k.getCancel() == 1) {
                    if (jo.getString("c_reason") == null)
                        k.setReason("-");
                    else {
                        if (jo.getString("c_reason").toLowerCase().equals("null"))
                            k.setReason("-");
                        else
                            k.setReason(jo.getString("c_reason"));
                    }
                }
                k.setSalesHeader(new SalesHeader());
                k.getSalesHeader().setHeaderID(jo.getInt("id_h"));
                k.getSalesHeader().setOrderNo(jo.getString("andro"));
                k.getSalesHeader().setStatus(jo.getInt("status"));
                list_kegiatan.add(k);
            }
        } catch (Exception e) {
            return list_kegiatan;
        }
        return list_kegiatan;
    }

    static String getNoOrder(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return getString(jsonObject, "no_order");
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

    static ArrayList<Item> getListItem(String json) {
        ArrayList<Item> list_item = new ArrayList<>();
        try {
            JSONArray ja_items = new JSONArray(json);
            for (int i = 0; i < ja_items.length(); i++) {
                Item item = new Item();
                JSONObject jo = ja_items.getJSONObject(i);
                item.setCode(jo.getString("No"));
                item.setDesc(jo.getString("Name"));
                item.setUnit(jo.getString("Unit"));
                item.setPrice(jo.getDouble("PL"));
                list_item.add(item);
            }
            return list_item;
        } catch (Exception e) {
            return list_item;
        }
    }

    static Kegiatan PostSalesHeader(Kegiatan kegiatan, String json, String no_order) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            kegiatan.getSalesHeader().setHeaderID(jsonObject.getInt("id"));
            kegiatan.getSalesHeader().setOrderNo(no_order);
            for (int i = 0; i < kegiatan.getSalesHeader().getSalesLine().size(); i++) {
                kegiatan.getSalesHeader().getSalesLine().get(i).setHeaderID(jsonObject.getInt("id"));
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
                feedback.setBex(Global.getBEX(context));
                feedback.setAndroSO(jo_feed.getString("so"));
                feedback.setNote(jo_feed.getString("note"));
                feedback.setLineID(jo_feed.getInt("id"));
                list_feedback.add(feedback);

                Log_Feed log = new Log_Feed();
                log.setFeedback(feedback);
                log.getFeedback().setBex(new BEX());
                log.getFeedback().getBex().setEmpNo(jo_feed.getString("c"));
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

}
