package co.id.datascrip.mo_sam_div4_dts1.custom;

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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import co.id.datascrip.mo_sam_div4_dts1.R;
import co.id.datascrip.mo_sam_div4_dts1.font.Fonts;

/**
 * The roboto calendar view
 *
 * @author Marco Hernaiz Cao
 */
public class RobotoCalendarView extends LinearLayout {

    // ************************************************************************************************************************************************************************
    // * Attributes
    // ************************************************************************************************************************************************************************

    public static final int RED_CIRCLE = R.drawable.roboto_calendar_red_circle;
    public static final int GREEN_CIRCLE = R.drawable.roboto_calendar_green_circle;
    public static final int BLUE_CIRCLE = R.drawable.roboto_calendar_blue_circle;
    //bny
    public static ViewGroup today, prevSelected;
    // View
    private Context context;
    private TextView dateTitle;
    private ImageView leftButton;
    private ImageView rightButton;
    private View view;
    // Class
    private RobotoCalendarListener robotoCalendarListener;
    private Calendar currentCalendar;
    private Locale locale;
    // Style
    private int monthTitleColor;
    private int monthTitleFont;
    private int dayOfWeekColor;
    private int dayOfWeekFont;
    private int dayOfMonthColor;
    private int dayOfMonthFont;

    // ************************************************************************************************************************************************************************
    // * Initialization methods
    // ************************************************************************************************************************************************************************
    private OnClickListener onDayOfMonthClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            // Extract day selected
            ViewGroup dayOfMonthContainer = (ViewGroup) view;
            String tagId = (String) dayOfMonthContainer.getTag();
            tagId = tagId.substring(19, tagId.length());
            TextView dayOfMonthText = (TextView) view.findViewWithTag("dayOfMonthText" + tagId);

            // Fire event
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentCalendar.getTime());
            calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(dayOfMonthText.getText().toString()));

            if (robotoCalendarListener == null) {
                throw new IllegalStateException("You must assing a valid RobotoCalendarListener first!");
            } else {
                robotoCalendarListener.onDateSelected(calendar.getTime());
            }
        }
    };

    public RobotoCalendarView(Context context) {
        super(context);
        this.context = context;
        onCreateView();
    }

    public RobotoCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        if (isInEditMode()) {
            return;
        }
        getAttributes(context, attrs);
        onCreateView();
    }

    private void getAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RobotoCalendarView, 0, 0);
        monthTitleColor = typedArray.getColor(R.styleable.RobotoCalendarView_monthTitleColor, R.color.monthTitleColor);
        monthTitleFont = typedArray.getInt(R.styleable.RobotoCalendarView_monthTitleFont, R.string.monthTitleFont);
        dayOfWeekColor = typedArray.getColor(R.styleable.RobotoCalendarView_dayOfWeekColor, R.color.dayOfWeekColor);
        dayOfWeekFont = typedArray.getInt(R.styleable.RobotoCalendarView_dayOfWeekFont, R.string.dayOfWeekFont);
        dayOfMonthColor = typedArray.getColor(R.styleable.RobotoCalendarView_dayOfMonthColor, R.color.dayOfMonthColor);
        dayOfMonthFont = typedArray.getInt(R.styleable.RobotoCalendarView_dayOfMonthFont, R.string.dayOfMonthFont);
        typedArray.recycle();
    }

    public View onCreateView() {

        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflate.inflate(R.layout.roboto_calendar_picker_layout, this, true);

        findViewsById(view);

        initializeEventListeners();

        initializeComponentBehavior();

        return view;
    }

    private void findViewsById(View view) {
        leftButton = (ImageView) view.findViewById(R.id.leftButton);
        rightButton = (ImageView) view.findViewById(R.id.rightButton);
        dateTitle = (TextView) view.findViewWithTag("dateTitle");
    }

    private void initializeEventListeners() {

        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (robotoCalendarListener == null) {
                    throw new IllegalStateException("You must assing a valid RobotoCalendarListener first!");
                }
                robotoCalendarListener.onLeftButtonClick();
            }
        });

        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (robotoCalendarListener == null) {
                    throw new IllegalStateException("You must assing a valid RobotoCalendarListener first!");
                }
                robotoCalendarListener.onRightButtonClick();
            }
        });
    }

    // ************************************************************************************************************************************************************************
    // * Private auxiliary methods
    // ************************************************************************************************************************************************************************

    private void initializeComponentBehavior() {
        // Initialize calendar for current month
        Locale locale = context.getResources().getConfiguration().locale;
        Calendar currentCalendar = Calendar.getInstance(locale);
        initializeCalendar(currentCalendar);
    }

    @SuppressLint("DefaultLocale")
    private void initializeTitleLayout() {
        // Apply styles
        String font = getResources().getString(monthTitleFont);
        Typeface robotoTypeface = Fonts.obtaintTypefaceFromString(context, font);
        int color = getResources().getColor(monthTitleColor);
        dateTitle.setTypeface(robotoTypeface);
        dateTitle.setTextColor(color);

        String dateText = new DateFormatSymbols(locale).getMonths()[currentCalendar.get(Calendar.MONTH)].toString();
        dateText = dateText.substring(0, 1).toUpperCase() + dateText.subSequence(1, dateText.length());
        dateTitle.setText(dateText + " " + currentCalendar.get(Calendar.YEAR));
    }

    @SuppressLint("DefaultLocale")
    private void initializeWeekDaysLayout() {

        // Apply styles
        String font = getResources().getString(dayOfWeekFont);
        Typeface robotoTypeface = Fonts.obtaintTypefaceFromString(context, font);
        int color = getResources().getColor(dayOfWeekColor);

        TextView dayOfWeek;
        String dayOfTheWeekString;
        String[] weekDaysArray = new DateFormatSymbols(locale).getShortWeekdays();
        for (int i = 1; i < weekDaysArray.length; i++) {
            dayOfWeek = (TextView) view.findViewWithTag("dayOfWeek" + getWeekIndex(i, currentCalendar));
            dayOfTheWeekString = weekDaysArray[i];

            // Check it for languages with only one week day lenght
            if (dayOfTheWeekString.length() > 1) {
                dayOfTheWeekString = dayOfTheWeekString.substring(0, 1).toUpperCase() + dayOfTheWeekString.subSequence(1, 2);
            }

            dayOfWeek.setText(dayOfTheWeekString);

            // Apply styles
            dayOfWeek.setTypeface(robotoTypeface);
            dayOfWeek.setTextColor(color);
        }
    }

    private void initializeDaysOfMonthLayout() {

        // Apply styles
        String font = getResources().getString(dayOfMonthFont);
        Typeface robotoTypeface = Fonts.obtaintTypefaceFromString(context, font);
        int color = getResources().getColor(dayOfMonthColor);
        TextView dayOfMonthText, dayOfMonthJmlOrder;
        ImageView dayOfMonthImage;
        ViewGroup dayOfMonthContainer;

        for (int i = 1; i < 43; i++) {

            dayOfMonthContainer = (ViewGroup) view.findViewWithTag("dayOfMonthContainer" + i);
            dayOfMonthText = (TextView) view.findViewWithTag("dayOfMonthText" + i);
            dayOfMonthImage = (ImageView) view.findViewWithTag("dayOfMonthImage" + i);
            dayOfMonthJmlOrder = (TextView) view.findViewWithTag("dayOfMonthJmlOrder" + i);

            dayOfMonthText.setVisibility(View.INVISIBLE);
            dayOfMonthImage.setVisibility(View.INVISIBLE);
            //layoutDayOfMonthImage.setVisibility(View.INVISIBLE);
            dayOfMonthJmlOrder.setVisibility(View.INVISIBLE);

            // Apply styles
            dayOfMonthText.setTypeface(robotoTypeface);
//			dayOfMonthText.setTextColor(color);
            dayOfMonthText.setBackgroundResource(android.R.color.transparent);

            dayOfMonthContainer.setBackgroundResource(android.R.color.transparent);
            dayOfMonthContainer.setOnClickListener(null);
        }
    }

    private void setDaysInCalendar() {
        Calendar auxCalendar = Calendar.getInstance(locale);
        auxCalendar.setTime(currentCalendar.getTime());
        auxCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfMonth = auxCalendar.get(Calendar.DAY_OF_WEEK);
        TextView dayOfMonthText;
        ViewGroup dayOfMonthContainer;

        // Calculate dayOfMonthIndex
        int dayOfMonthIndex = getWeekIndex(firstDayOfMonth, auxCalendar);

        for (int i = 1; i <= auxCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++, dayOfMonthIndex++) {
            dayOfMonthContainer = (ViewGroup) view.findViewWithTag("dayOfMonthContainer" + dayOfMonthIndex);
            dayOfMonthText = (TextView) view.findViewWithTag("dayOfMonthText" + dayOfMonthIndex);
            if (dayOfMonthText == null) {
                break;
            }
            dayOfMonthContainer.setOnClickListener(onDayOfMonthClickListener);
            dayOfMonthText.setVisibility(View.VISIBLE);
            dayOfMonthText.setText(String.valueOf(i));
        }

        // If the last week row has no visible days, hide it or show it in case
        ViewGroup weekRow = (ViewGroup) view.findViewWithTag("weekRow6");
        dayOfMonthText = (TextView) view.findViewWithTag("dayOfMonthText36");
        if (dayOfMonthText.getVisibility() == INVISIBLE) {
            weekRow.setVisibility(GONE);
        } else {
            weekRow.setVisibility(VISIBLE);
        }
    }

    private void clearDayOfMonthContainerBackground() {
        ViewGroup dayOfMonthContainer;
//		for (int i = 1; i < 43; i++) {
//			dayOfMonthContainer = (ViewGroup) view.findViewWithTag("dayOfMonthContainer" + i);
//			if (dayOfMonthContainer!=today)
//				dayOfMonthContainer.setBackgroundResource(android.R.color.transparent);
//		}
        if (prevSelected != today)
            prevSelected.setBackgroundResource(android.R.color.transparent);
    }

    private ViewGroup getDayOfMonthContainer(Calendar currentCalendar) {
        int monthOffset = getMonthOffset(currentCalendar);
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        ViewGroup dayOfMonthContainer = (ViewGroup) view.findViewWithTag("dayOfMonthContainer" + (currentDay + monthOffset));
        return dayOfMonthContainer;
    }

    private TextView getDayOfMonthText(Calendar currentCalendar) {
        int monthOffset = getMonthOffset(currentCalendar);
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        TextView dayOfMonth = (TextView) view.findViewWithTag("dayOfMonthText" + (currentDay + monthOffset));
        return dayOfMonth;
    }

    private ImageView getDayOfMonthImage(Calendar currentCalendar) {
        int monthOffset = getMonthOffset(currentCalendar);
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        ImageView dayOfMonth = (ImageView) view.findViewWithTag("dayOfMonthImage" + (currentDay + monthOffset));
        return dayOfMonth;
    }

    private TextView getDayOfMonthJmlOrder(Calendar currentCalendar) {
        int monthOffset = getMonthOffset(currentCalendar);
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        TextView dayOfMonth = (TextView) view.findViewWithTag("dayOfMonthJmlOrder" + (currentDay + monthOffset));
        return dayOfMonth;
    }

    private int getMonthOffset(Calendar currentCalendar) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentCalendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayWeekPosition = calendar.getFirstDayOfWeek();
        int dayPosition = calendar.get(Calendar.DAY_OF_WEEK);

        if (firstDayWeekPosition == 1) {
            return dayPosition - 1;
        } else {

            if (dayPosition == 1) {
                return 6;
            } else {
                return dayPosition - 2;
            }
        }
    }

    // ************************************************************************************************************************************************************************
    // * Event handler methods
    // ************************************************************************************************************************************************************************

    private int getWeekIndex(int weekIndex, Calendar currentCalendar) {
        int firstDayWeekPosition = currentCalendar.getFirstDayOfWeek();

        if (firstDayWeekPosition == 1) {
            return weekIndex;
        } else {

            if (weekIndex == 1) {
                return 7;
            } else {
                return weekIndex - 1;
            }
        }
    }

    // ************************************************************************************************************************************************************************
    // * Public calendar methods
    // ************************************************************************************************************************************************************************

    public void setRobotoCalendarListener(RobotoCalendarListener robotoCalendarListener) {
        this.robotoCalendarListener = robotoCalendarListener;
    }

    @SuppressLint("DefaultLocale")
    public void initializeCalendar(Calendar currentCalendar) {

        this.currentCalendar = currentCalendar;
        locale = context.getResources().getConfiguration().locale;

        // Set date title
        initializeTitleLayout();

        // Set weeks days titles
        initializeWeekDaysLayout();

        // Initialize days of the month
        initializeDaysOfMonthLayout();

        // Set days in calendar
        setDaysInCalendar();
    }

    public void markDayAsCurrentDay() {
        Locale locale = context.getResources().getConfiguration().locale;
        Calendar currentCalendar = Calendar.getInstance(locale);
        currentCalendar.setTime(currentCalendar.getTime());
        TextView dayOfMonth = getDayOfMonthText(currentCalendar);
        Typeface robotoTypeface = Fonts.obtaintTypefaceFromString(context, getResources().getString(R.string.currentDayOfMonthFont));
//		dayOfMonth.setTypeface(robotoTypeface, Typeface.BOLD);
//		dayOfMonth.setTextColor(context.getResources().getColor(R.color.currentDayOfMonthColor));

        ViewGroup dayOfMonthContainer = getDayOfMonthContainer(currentCalendar);
        today = dayOfMonthContainer;
        dayOfMonthContainer.setBackgroundResource(R.drawable.roboto_calendar_blue_fill);
    }

    public void markDayAsSelectedDay(Date currentDate) {

        // Clear previous marks
        if (prevSelected != null)
            clearDayOfMonthContainerBackground();

        Locale locale = context.getResources().getConfiguration().locale;
        Calendar currentCalendar = Calendar.getInstance(locale);
        currentCalendar.setTime(currentDate);
        ViewGroup dayOfMonthContainer = getDayOfMonthContainer(currentCalendar);
        if (dayOfMonthContainer != today) {
            dayOfMonthContainer.setBackgroundResource(R.drawable.roboto_calendar_blue_ring);
        }
        prevSelected = dayOfMonthContainer;
    }

    public void markDayWithStyle(int style, Date currentDate, int count) {
        Locale locale = context.getResources().getConfiguration().locale;
        Calendar currentCalendar = Calendar.getInstance(locale);
        currentCalendar.setTime(currentDate);
        ImageView dayOfMonthImage = getDayOfMonthImage(currentCalendar);
        TextView dayOfMonthText = getDayOfMonthText(currentCalendar);
        TextView dayOfMonthCount = getDayOfMonthJmlOrder(currentCalendar);

        // Draw day with style
        dayOfMonthImage.setVisibility(View.VISIBLE);
        dayOfMonthCount.setVisibility(View.VISIBLE);
        dayOfMonthCount.setText(String.valueOf(count));
        dayOfMonthImage.setImageDrawable(null);
        dayOfMonthImage.setBackgroundResource(style);
        dayOfMonthText.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/treb_bold.ttf"));
    }

    public void markMultipleDays(ArrayList<String> dates) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        HashMap<String, Integer> maps = new HashMap<String, Integer>();
        for (String temp : dates) {
            Integer count = maps.get(temp);
            maps.put(temp, (count == null) ? 1 : count + 1);
        }
        for (HashMap.Entry<String, Integer> entry : maps.entrySet()) {
            try {
                String date = entry.getKey();
                int count = entry.getValue();
                Date d = format.parse(date.toString());
                markDayWithStyle(BLUE_CIRCLE, d, count);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public interface RobotoCalendarListener {

        public void onDateSelected(Date date);

        public void onRightButtonClick();

        public void onLeftButtonClick();
    }

}
