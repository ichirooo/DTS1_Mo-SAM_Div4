/*
 * Copyright (C) 2014 Marco Hernaiz Cao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package co.id.datascrip.mo_sam_div4_dts1.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.util.SparseArray;

import java.util.HashMap;
import java.util.Map;

/**
 * The manager of roboto typefaces.
 *
 * @author Marco Hernaiz Cao
 */
public class Fonts {

    /*
    * Available fonts
    */
    public final static int TREBUCHET_REGULAR = 0;
    public final static int ROBOTO_THIN = 1;
    public final static int ROBOTO_THIN_ITALIC = 2;
    public final static int ROBOTO_LIGHT = 3;
    public final static int ROBOTO_LIGHT_ITALIC = 4;
    public final static int ROBOTO_REGULAR = 5;
    public final static int ROBOTO_ITALIC = 6;
    public final static int ROBOTO_MEDIUM = 7;
    public final static int ROBOTO_MEDIUM_ITALIC = 8;
    public final static int ROBOTO_BOLD = 9;
    public final static int ROBOTO_BOLD_ITALIC = 10;
    public final static int ROBOTO_BLACK = 11;
    public final static int ROBOTO_BLACK_ITALIC = 12;
    public final static int ROBOTO_CONDENSED_LIGHT = 13;
    public final static int ROBOTO_CONDENSED_LIGHT_ITALIC = 14;
    public final static int ROBOTO_CONDENSED_REGULAR = 15;
    public final static int ROBOTO_CONDENSED_ITALIC = 16;
    public final static int ROBOTO_CONDENSED_BOLD = 17;
    public final static int ROBOTO_CONDENSED_BOLD_ITALIC = 18;
    public final static int ROBOTO_SLAB_THIN = 19;
    public final static int ROBOTO_SLAB_LIGHT = 20;
    public final static int ROBOTO_SLAB_REGULAR = 21;
    public final static int ROBOTO_SLAB_BOLD = 22;
    public final static int TREBUCHET_BOLD = 23;
    public final static int TREBUCHET_ITALIC = 24;
    public final static int TREBUCHET_BOLD_ITALIC = 25;

    private final static Map<String, Integer> typeFacesMap;
    private final static SparseArray<Typeface> mTypefaces = new SparseArray<Typeface>(20);

    static {
        typeFacesMap = new HashMap<String, Integer>();
        typeFacesMap.put("trebuchet_regular", 0);
        typeFacesMap.put("roboto_thin", 1);
        typeFacesMap.put("roboto_thin_italic", 2);
        typeFacesMap.put("roboto_light", 3);
        typeFacesMap.put("roboto_light_italic", 4);
        typeFacesMap.put("roboto_regular", 5);
        typeFacesMap.put("roboto_italic", 6);
        typeFacesMap.put("roboto_medium", 7);
        typeFacesMap.put("roboto_medium_italic", 8);
        typeFacesMap.put("roboto_bold", 9);
        typeFacesMap.put("roboto_bold_italic", 10);
        typeFacesMap.put("roboto_black", 11);
        typeFacesMap.put("roboto_black_italic", 12);
        typeFacesMap.put("roboto_condensed_light", 13);
        typeFacesMap.put("roboto_condensed_light_italic", 14);
        typeFacesMap.put("roboto_condensed_regular", 15);
        typeFacesMap.put("roboto_condensed_italic", 16);
        typeFacesMap.put("roboto_condensed_bold", 17);
        typeFacesMap.put("roboto_condensed_bold_italic", 18);
        typeFacesMap.put("roboto_slab_thin", 19);
        typeFacesMap.put("roboto_slab_light", 20);
        typeFacesMap.put("roboto_slab_regular", 21);
        typeFacesMap.put("roboto_slab_bold", 22);
        typeFacesMap.put("trebuchet_bold", 23);
        typeFacesMap.put("trebuchet_italic", 24);
        typeFacesMap.put("trebuchet_bold_italic", 25);
    }

    /**
     * Obtain typeface.
     *
     * @param context       The Context the widget is running in, through which it can access the current theme, resources, etc.
     * @param typefaceValue The value of "typeface" attribute
     * @return specify {@link Typeface}
     * @throws IllegalArgumentException if unknown `typeface` attribute value.
     */
    public static Typeface obtaintTypeface(Context context, int typefaceValue) throws IllegalArgumentException {
        Typeface typeface = mTypefaces.get(typefaceValue);
        if (typeface == null) {
            typeface = createTypeface(context, typefaceValue);
            mTypefaces.put(typefaceValue, typeface);
        }
        return typeface;
    }

    /**
     * Obtain typeface.
     *
     * @param context             The Context the widget is running in, through which it can access the current theme, resources, etc.
     * @param typefaceValueString The value of "typeface" attribute
     * @return specify {@link Typeface}
     * @throws IllegalArgumentException if unknown `typeface` attribute value.
     */
    public static Typeface obtaintTypefaceFromString(Context context, String typefaceValueString) throws IllegalArgumentException {
        int typefaceValue = typeFacesMap.get(typefaceValueString);
        return obtaintTypeface(context, typefaceValue);
    }

    /**
     * Create typeface from assets.
     *
     * @param context       The Context the widget is running in, through which it can
     *                      access the current theme, resources, etc.
     * @param typefaceValue The value of "typeface" attribute
     * @return Roboto {@link Typeface}
     * @throws IllegalArgumentException if unknown `typeface` attribute value.
     */
    private static Typeface createTypeface(Context context, int typefaceValue)
            throws IllegalArgumentException {

        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "fonts/treb_regular.ttf");
        try {

            switch (typefaceValue) {
                case ROBOTO_THIN:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Thin.ttf");
                    break;
                case ROBOTO_THIN_ITALIC:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-ThinItalic.ttf");
                    break;
                case ROBOTO_LIGHT:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
                    break;
                case ROBOTO_LIGHT_ITALIC:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-LightItalic.ttf");
                    break;
                case ROBOTO_REGULAR:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
                    break;
                case ROBOTO_ITALIC:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Italic.ttf");
                    break;
                case ROBOTO_MEDIUM:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
                    break;
                case ROBOTO_MEDIUM_ITALIC:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-MediumItalic.ttf");
                    break;
                case ROBOTO_BOLD:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");
                    break;
                case ROBOTO_BOLD_ITALIC:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-BoldItalic.ttf");
                    break;
                case ROBOTO_BLACK:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Black.ttf");
                    break;
                case ROBOTO_BLACK_ITALIC:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-BlackItalic.ttf");
                    break;
                case ROBOTO_CONDENSED_LIGHT:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Light.ttf");
                    break;
                case ROBOTO_CONDENSED_LIGHT_ITALIC:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-LightItalic.ttf");
                    break;
                case ROBOTO_CONDENSED_REGULAR:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Regular.ttf");
                    break;
                case ROBOTO_CONDENSED_ITALIC:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Italic.ttf");
                    break;
                case ROBOTO_CONDENSED_BOLD:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Bold.ttf");
                    break;
                case ROBOTO_CONDENSED_BOLD_ITALIC:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-BoldItalic.ttf");
                    break;
                case ROBOTO_SLAB_THIN:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoSlab-Thin.ttf");
                    break;
                case ROBOTO_SLAB_LIGHT:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoSlab-Light.ttf");
                    break;
                case ROBOTO_SLAB_REGULAR:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoSlab-Regular.ttf");
                    break;
                case ROBOTO_SLAB_BOLD:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoSlab-Bold.ttf");
                    break;
                case TREBUCHET_REGULAR:
                    typeface = Typeface.createFromAsset(context.getAssets(),
                            "fonts/treb_regular.ttf");
                    break;
                case TREBUCHET_BOLD:
                    typeface = Typeface.createFromAsset(context.getAssets(),
                            "fonts/treb_bold.ttf");
                    break;
                case TREBUCHET_ITALIC:
                    typeface = Typeface.createFromAsset(context.getAssets(),
                            "fonts/treb_italic.ttf");
                    break;
                case TREBUCHET_BOLD_ITALIC:
                    typeface = Typeface.createFromAsset(context.getAssets(),
                            "fonts/treb_bold_italic.ttf");
                    break;
                default:
                    typeface = Typeface.createFromAsset(context.getAssets(),
                            "fonts/treb_regular.ttf");
                    // throw new
                    // IllegalArgumentException("Unknown `typeface` attribute value "
                    // + typefaceValue);
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("font", e.toString());
        }
        return typeface;
    }

}
