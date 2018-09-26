package com.salamander.mo_sam_div4_dts1.function;

import android.text.Editable;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.util.Locale;

public class Hitung {

    public double CalcSubTotal(EditText quantity, EditText price, EditText discount) {
        // Gets the two EditText controls' Editable values
        String a = quantity.getText().toString().replaceAll(",", ""),
                b = price.getText().toString().replaceAll(",", ""),
                c = discount.getText().toString().replaceAll(",", "");

        // Initializes the double values and result
        double value1 = 0.0, value2 = 0.0, value3 = 0.0;

        // If the Editable values are not null, obtains their double values by parsing
        if (a.equals("") || a.equals("0."))
            value1 = 0.0;
        else
            value1 = Double.parseDouble(a);

        if (b.equals("") || b.equals("0."))
            value2 = 0.0;
        else
            value2 = Double.parseDouble(b);

        if (c.equals("") || c.equals("0."))
            value3 = 0.0;
        else
            value3 = Double.parseDouble(c);

        return ((value2 - value3) * value1);
    }

    public void FormatNumberDecimal(Editable s, EditText e) {
        if (!s.toString().equals("")) {

            int start, end, cp, sel;
            String price;

            if (s.toString().contains("0."))
                price = s.toString();
            else
                price = formatNumber(Double.valueOf(s.toString().replaceAll(",", "")));

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

    public String formatNumber(double d) {
        DecimalFormat numberFormat = new DecimalFormat("###,###,###.##");
        Locale.setDefault(Locale.US);
        String result = numberFormat.format(d);
        return result;
    }

    public double getDouble(EditText e) {
        double value;
        String txt = e.getText().toString().replaceAll(",", "").trim();
        if (txt.equals(""))
            value = 0.0;
        else
            value = Double.parseDouble(txt);

        return value;
    }

    public int getInt(EditText e) {
        int value;
        String txt = e.getText().toString().replaceAll(",", "");
        if (txt.equals(""))
            value = 0;
        else
            value = Integer.parseInt(txt);

        return value;
    }

    public double countDiscOfPct(EditText pct, EditText price) {
        String a = pct.getText().toString().replaceAll(",", ""),
                b = price.getText().toString().replaceAll(",", "");

        double pct1 = 0.0,
                price1 = 0.0,
                val = 0.0;

        if (!a.trim().equals(""))
            pct1 = Double.parseDouble(a);

        if (!b.trim().equals(""))
            price1 = Double.parseDouble(b);

        val = (pct1 / 100 * price1);

        return val;
    }

    public double countDiscOfVal(EditText disc, EditText price) {
        String a = disc.getText().toString().replaceAll(",", ""),
                b = price.getText().toString().replaceAll(",", "");

        double disc1 = 0.0,
                price1 = 0.0,
                pct = 0.0;

        if (a.endsWith(".")) {
            a = a.replace(".", "");
        }

        if (!a.trim().equals(""))
            disc1 = Double.parseDouble(a);

        if (!b.trim().equals(""))
            price1 = Double.parseDouble(b);

        if (price1 == 0.0) {
            return 0.0;
        } else {
            pct = (disc1 / price1 * 100);
            return pct;
        }
    }
}
