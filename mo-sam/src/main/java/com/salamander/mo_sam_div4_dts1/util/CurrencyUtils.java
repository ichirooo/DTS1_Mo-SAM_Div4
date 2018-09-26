package com.salamander.mo_sam_div4_dts1.util;

import android.text.Editable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.salamander.mo_sam_div4_dts1.App;

import java.text.DecimalFormat;
import java.util.Locale;

public class CurrencyUtils {

    public static double CalcTotal(EditText... editText) {
        String[] datas = new String[editText.length];
        double total = 0.0;
        for (int i = 0; i < editText.length; i++) {
            datas[i] = editText[i].getText().toString().replaceAll(",", "");

            if (TextUtils.isEmpty(datas[i]) || datas[i].equals("0."))
                total += 0.0;
            else total += Double.parseDouble(datas[i]);
        }
        return total;
    }

    public static double CalcTotal(int jmlHari, EditText... editText) {
        String[] datas = new String[editText.length];
        double total = 0.0;
        for (int i = 0; i < editText.length; i++) {
            datas[i] = editText[i].getText().toString().replaceAll("[^0-9.]", "");
            if (TextUtils.isEmpty(datas[i]) || datas[i].equals("0."))
                total += 0.0;
            else total += Double.parseDouble(datas[i]) * jmlHari;
        }
        return total;
    }

    public static void FormatNumberDecimal(Editable s, EditText e) {
        if (!s.toString().equals("")) {

            int start, end, cp, sel;
            String price;

            if (s.toString().startsWith("0."))
                price = s.toString();
            else price = formatNumber(Double.valueOf(s.toString().replaceAll(",", "")));

            start = e.getText().length();
            cp = e.getSelectionStart();
            if (!s.toString().trim().equals(price))
                e.setText(price);
            end = e.getText().length();
            sel = cp + (end - start);
            if (sel > 0 && sel <= e.getText().length()) {
                e.setSelection(sel);
            } else {
                // place cursor at the end?
                e.setSelection(e.getText().length());
            }
        }
    }

    public static String formatNumber(double d) {
        DecimalFormat numberFormat = new DecimalFormat("###,###,###.##");
        Locale.setDefault(Locale.US);
        return numberFormat.format(d);
    }

    public static String formatCurrency(double d) {
        DecimalFormat numberFormat = new DecimalFormat("###,###,###.##");
        Locale.setDefault(Locale.US);
        return App.CURRENCY_SYMBOL + numberFormat.format(d);
    }

    public static String formatCurrencyWithSign(double d) {
        DecimalFormat numberFormat = new DecimalFormat("###,###,###.##");
        Locale.setDefault(Locale.US);
        if (d < 0)
            return " - " + App.CURRENCY_SYMBOL + numberFormat.format(d * -1);
        else return " + " + App.CURRENCY_SYMBOL + numberFormat.format(d);
    }

    public static double getDouble(EditText e) {
        double value;
        String txt = e.getText().toString().replaceAll(",", "").trim();
        if (txt.endsWith("."))
            txt = txt + "0";
        if (txt.equals(""))
            value = 0.0;
        else
            value = Double.parseDouble(txt);

        return value;
    }

    public static double getDouble(TextView e) {
        double value;
        String txt = e.getText().toString().replace("Rp.", "").replaceAll("[^0-9.]", "").replaceAll(",", "").trim();
        if (txt.equals(""))
            value = 0.0;
        else
            value = Double.parseDouble(txt);

        return value;
    }

    public static int getInt(EditText e) {
        int value;
        String txt = e.getText().toString().replaceAll(",", "");
        if (txt.equals(""))
            value = 0;
        else
            value = Integer.parseInt(txt);

        return value;
    }
}
