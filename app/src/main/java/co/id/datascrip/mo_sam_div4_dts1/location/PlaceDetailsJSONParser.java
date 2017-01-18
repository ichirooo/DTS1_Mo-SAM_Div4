package co.id.datascrip.mo_sam_div4_dts1.location;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PlaceDetailsJSONParser {

    /**
     * Receives a JSONObject and returns a list
     *
     * @throws JSONException
     */
    public HashMap<String, String> parse(JSONObject jObject) {

        Double lat = Double.valueOf(0);
        Double lng = Double.valueOf(0);
        String formattedAddress = "";

        HashMap<String, String> hm = new HashMap<String, String>();

        try {
            lat = (Double) jObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lat");
            lng = (Double) jObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lng");
            formattedAddress = (String) jObject.getJSONArray("results").getJSONObject(0).getString("formatted_address");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        hm.put("lat", Double.toString(lat));
        hm.put("lng", Double.toString(lng));
        hm.put("formatted_address", formattedAddress);

        return hm;
    }
}
