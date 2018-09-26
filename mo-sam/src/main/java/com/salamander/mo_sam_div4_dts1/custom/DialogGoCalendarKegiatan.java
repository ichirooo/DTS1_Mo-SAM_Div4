package com.salamander.mo_sam_div4_dts1.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.datascrip.salamander_calendar.GoCalendarView;
import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.Const;
import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.object.Holiday;
import com.salamander.mo_sam_div4_dts1.object.Kegiatan;
import com.salamander.mo_sam_div4_dts1.sqlite.HolidaySQLite;
import com.salamander.mo_sam_div4_dts1.sqlite.KegiatanSQLite;
import com.salamander.salamander_base_module.DateUtils;
import com.salamander.salamander_base_module.object.Tanggal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DialogGoCalendarKegiatan extends AlertDialog {

    private Context context;
    private GoCalendarView calendarView;
    private Date selectedDate;
    private Calendar currentCalendar;
    private Button bt_ok, bt_cancel;
    private int currentMonthIndex;

    private KegiatanSQLite kegiatanSQLite;
    private HolidaySQLite holidaySQLite;

    private ArrayList<Kegiatan> k_month;
    private ArrayList<Holiday> h_month;

    private OnOKClickListener okClickListener;

    public DialogGoCalendarKegiatan(@NonNull Context context) {
        super(context);
        this.context = context;
        kegiatanSQLite = new KegiatanSQLite(context);
        holidaySQLite = new HolidaySQLite(context);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_go_calendar, null);
        calendarView = view.findViewById(R.id.calendar_go);
        bt_ok = view.findViewById(R.id.bt_ok);
        bt_cancel = view.findViewById(R.id.bt_cancel);
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                okClickListener.onOKClickListener(selectedDate);
                dismiss();
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        setView(view);
    }

    public void setDate(Date date) {
        selectedDate = date;

        calendarView.setGoCalendarListener(new GoCalendarView.GoCalendarListener() {
            @Override
            public void onRightButtonClick() {
                currentMonthIndex++;
                updateCalendar();
            }

            @Override
            public void onLeftButtonClick() {
                currentMonthIndex--;
                updateCalendar();
            }

            @Override
            public void onDateSelected(Date date) {
                selectedDate = date;
                calendarView.markDayAsSelectedDay(date);
            }
        });

        currentMonthIndex = 0;
        selectedDate = null;
        currentCalendar = Calendar.getInstance(App.getLocale());
        calendarView.markDayAsCurrentDay();
        updateCalendar();
    }

    private void updateCalendar() {
        currentCalendar = Calendar.getInstance(App.getLocale());
        currentCalendar.add(Calendar.MONTH, currentMonthIndex);
        calendarView.initializeCalendar(currentCalendar);
        if (currentMonthIndex == 0) {
            calendarView.markDayAsCurrentDay();
            selectedDate = currentCalendar.getTime();
        } else {
            selectedDate = null;
            GoCalendarView.today = null;
        }
        markDatePerMonth();
    }

    private void markDatePerMonth() {
        k_month = new ArrayList<>();
        h_month = new ArrayList<>();
        /*
        if (currentMonthIndex == 0)
            k_month = kegiatanSQLite.Read(ConstSQLite.KEGIATAN_THIS_MONTH);
        else {
            k_month = kegiatanSQLite.Read(currentCalendar, ConstSQLite.KEGIATAN_BY_MONTH);
        }
        */
        k_month = kegiatanSQLite.Read(currentCalendar, Const.KEGIATAN_BY_MONTH);
        h_month = holidaySQLite.getMonthYear(currentCalendar.get(Calendar.MONTH) + 1, currentCalendar.get(Calendar.YEAR));
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<String> dates_holiday = new ArrayList<>();
        for (int i = 0; i < k_month.size(); i++) {
            dates.add(k_month.get(i).getStartDate().getTglString(Tanggal.FORMAT_DATE));
        }
        for (Holiday holiday : h_month) {
            dates_holiday.add(DateUtils.dateToString(Tanggal.FORMAT_DATE, holiday.getTanggal()));
        }

        calendarView.markMultipleDays(dates);
        calendarView.markHolidays(dates_holiday);
    }

    public void setOnOKClickListener(OnOKClickListener onOKClickListener) {
        this.okClickListener = onOKClickListener;
    }

    public interface OnOKClickListener {
        void onOKClickListener(Date selectDate);
    }
}
