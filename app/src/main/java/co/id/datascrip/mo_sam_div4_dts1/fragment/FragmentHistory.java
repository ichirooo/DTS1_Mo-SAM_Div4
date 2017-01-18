package co.id.datascrip.mo_sam_div4_dts1.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import co.id.datascrip.mo_sam_div4_dts1.Const;
import co.id.datascrip.mo_sam_div4_dts1.Global;
import co.id.datascrip.mo_sam_div4_dts1.R;
import co.id.datascrip.mo_sam_div4_dts1.activity.Input_Kegiatan_Activity;
import co.id.datascrip.mo_sam_div4_dts1.activity.Input_Order_Activity;
import co.id.datascrip.mo_sam_div4_dts1.activity.Input_Photo_Activity;
import co.id.datascrip.mo_sam_div4_dts1.adapter.Adapter_List_Kegiatan;
import co.id.datascrip.mo_sam_div4_dts1.callback.CallbackKegiatan;
import co.id.datascrip.mo_sam_div4_dts1.callback.CallbackSalesHeader;
import co.id.datascrip.mo_sam_div4_dts1.custom.RobotoCalendarView;
import co.id.datascrip.mo_sam_div4_dts1.custom.RobotoCalendarView.RobotoCalendarListener;
import co.id.datascrip.mo_sam_div4_dts1.littlefluffy.LocationInfo;
import co.id.datascrip.mo_sam_div4_dts1.littlefluffy.LocationLibrary;
import co.id.datascrip.mo_sam_div4_dts1.object.Kegiatan;
import co.id.datascrip.mo_sam_div4_dts1.object.SalesHeader;
import co.id.datascrip.mo_sam_div4_dts1.process.Process_Kegiatan;
import co.id.datascrip.mo_sam_div4_dts1.process.Process_Sales_Header;
import co.id.datascrip.mo_sam_div4_dts1.sqlite.KegiatanSQLite;
import co.id.datascrip.mo_sam_div4_dts1.sqlite.SalesHeaderSQLite;

/**
 * Created by benny_aziz on 02/16/2015.
 */
public class FragmentHistory extends Fragment {

    private static final int KEGIATAN = 100;
    private static final int INPUT_ORDER = 101;
    private static final int INPUT_PHOTO = 102;

    private static final int GPS_SETTING_REQUEST_CHECK_IN = 1111;
    private static final int GPS_SETTING_REQUEST_CHECK_OUT = 1112;
    private static final int GPS_SETTING_REQUEST_PHOTO = 1113;
    private static final int GPS_SETTING_REQUEST_ORDER = 1114;

    private RobotoCalendarView calendarView;
    private SwipeRefreshLayout swipe_layout;
    private ListView list_view;
    private Adapter_List_Kegiatan adapter;
    private Kegiatan kegiatan;

    private int currentMonthIndex;
    private Calendar currentCalendar;
    private Date selectedDate;
    private ArrayList<Kegiatan> k_month, k_day;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        calendarView = (RobotoCalendarView) root.findViewById(R.id.calendar_view);
        swipe_layout = (SwipeRefreshLayout) root.findViewById(R.id.fragment_history_swipe_refresh_listview);
        swipe_layout.setProgressBackgroundColorSchemeColor(Color.BLUE);
        swipe_layout.setColorSchemeColors(Color.RED, Color.GRAY, Color.GREEN, Color.MAGENTA);
        list_view = (ListView) root.findViewById(R.id.fragment_history_listview);

        refreshAdapter();

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                if (position >= 0) {
                    kegiatan = adapter.getItem(position);
                    registerForContextMenu(list_view);
                    list_view.showContextMenuForChild(view);
                    unregisterForContextMenu(list_view);
                }
            }
        });

        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_layout.setRefreshing(true);
                swipe_layout.setEnabled(false);
                /*
                new Proses_Kegiatan.Refresh(getActivity(), new CallbackKegiatan.CBRefresh() {
                    @Override
                    public void onCB(ArrayList<Kegiatan> kegiatans) {
                        refreshAdapter();
                        swipe_layout.setRefreshing(false);
                        swipe_layout.setEnabled(true);
                    }
                }, Const.KEGIATAN_BY_DATE).execute(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(selectedDate));
                */
                new Process_Kegiatan(getActivity()).Refresh(Process_Kegiatan.ACTION_REFRESH_DATE,
                        new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(selectedDate), new CallbackKegiatan.CBRefresh() {
                            @Override
                            public void onCB(ArrayList<Kegiatan> list_kegiatan) {
                                refreshAdapter();
                                swipe_layout.setRefreshing(false);
                                swipe_layout.setEnabled(true);
                            }
                        });
            }
        });

        calendarView.setRobotoCalendarListener(new RobotoCalendarListener() {

            @Override
            public void onRightButtonClick() {
                // TODO Auto-generated method stub
                currentMonthIndex++;
                updateCalendar();
            }

            @Override
            public void onLeftButtonClick() {
                // TODO Auto-generated method stub
                currentMonthIndex--;
                updateCalendar();
            }

            @Override
            public void onDateSelected(Date date) {
                // TODO Auto-generated method stub
                // Mark calendar day
                selectedDate = date;
                calendarView.markDayAsSelectedDay(date);
                refreshAdapter();
            }
        });

        // Initialize the RobotoCalendarPicker with the current index and date
        currentMonthIndex = 0;
        selectedDate = null;
        currentCalendar = Calendar.getInstance(Locale.getDefault());
        // Mark current day
        calendarView.markDayAsCurrentDay();
        // Mark some random days. These days are not fixed, if you change the
        // month they will be cleaned
        updateCalendar();
        return root;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        MenuInflater inflater = getActivity().getMenuInflater();
        if (new KegiatanSQLite(getActivity()).isClosed(kegiatan.getID())) {
            inflater.inflate(R.menu.menu_kegiatan_cancel, menu);
        } else {
            if (kegiatan.getCheckIn() == Const.CHECK_OUT) {
                inflater.inflate(R.menu.menu_kegiatan, menu);
            } else {
                inflater.inflate(R.menu.menu_kegiatan_checkin, menu);
            }
        }
        super.onCreateContextMenu(menu, view, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        KegiatanSQLite ks = new KegiatanSQLite(getActivity());
        SalesHeaderSQLite ss = new SalesHeaderSQLite(getActivity());
        boolean closed = ks.isClosed(kegiatan.getID());
        SalesHeader sh = ss.get(kegiatan.getID());

        final EditText input = new EditText(getActivity());
        input.setLines(3);

        switch (item.getItemId()) {

            case R.id.photo:
                if (checkGPS(GPS_SETTING_REQUEST_PHOTO)) {
                    Intent intentPhoto = new Intent(getActivity(), Input_Photo_Activity.class);
                    intentPhoto.putExtra("id_kegiatan", kegiatan.getID());
                    intentPhoto.putExtra("canEdit", kegiatan.getSalesHeader().getStatus() == 0);
                    startActivityForResult(intentPhoto, INPUT_PHOTO);
                }
                break;

            case R.id.edit:
                Intent intentEditKegiatan = new Intent(getActivity(), Input_Kegiatan_Activity.class);
                intentEditKegiatan.putExtra("id_kegiatan", kegiatan.getID());
                if (kegiatan.getCancel() != 0)
                    intentEditKegiatan.putExtra("canEdit", false);
                else
                    intentEditKegiatan.putExtra("canEdit", kegiatan.getSalesHeader().getStatus() == 0);
                startActivityForResult(intentEditKegiatan, KEGIATAN);
                break;

            case R.id.order:
                if (!closed) {
                    if (kegiatan.getSalesHeader().getCustomer().getCode().contains("CUST")) {
                        Toast.makeText(getActivity(), "Customer lead tidak diperbolehkan input SO",
                                Toast.LENGTH_LONG).show();
                    } else {
                        if (checkGPS(GPS_SETTING_REQUEST_ORDER)) {
                            OpenSalesOrder(kegiatan);
                        }
                    }
                } else {
                    if (sh.getOrderNo() == null) {
                        Toast.makeText(getActivity(), "Order not found",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intentOrder = new Intent(getActivity(), Input_Order_Activity.class);
                        intentOrder.putExtra("id_kegiatan", kegiatan.getID());
                        intentOrder.putExtra("canEdit", false);
                        startActivityForResult(intentOrder, INPUT_ORDER);
                    }
                }
                break;

            case R.id.checkin:
                if (checkGPS(GPS_SETTING_REQUEST_CHECK_IN)) {
                    kegiatan.setCheckIn(Const.CHECK_IN);
                    prosesCheckIn(kegiatan);
                }
                LocationLibrary.startAlarmAndListener(getActivity());
                break;

            case R.id.checkout:
                if (checkGPS(GPS_SETTING_REQUEST_CHECK_OUT)) {
                    kegiatan.setCheckIn(Const.CHECK_OUT);
                    prosesCheckOut(kegiatan, true);
                }
                LocationLibrary.stopAlarmAndListener(getActivity());
                break;

            case R.id.batal:
                input.setHint("Alasan");
                if (kegiatan.getReason() != null) {
                    input.setText(kegiatan.getReason());
                }
                new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT)
                        .setTitle("Batal").setView(input)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                String reason = input.getText().toString().trim();
                                if (!reason.equals("")) {
                                    kegiatan.setCancel(Integer.parseInt(kegiatan.getBEX().getEmpNo()));
                                    kegiatan.setReason(reason);
                                    Action(Process_Kegiatan.ACTION_CANCEL, kegiatan);
                                }
                            }
                        }).setNegativeButton("Cancel", null).show();
                break;

            case R.id.keterangan:
                input.setHint("Keterangan");
                if (kegiatan.getKeterangan() != null) {
                    input.setText(kegiatan.getKeterangan());
                }
                new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT)
                        .setTitle("Keterangan").setView(input)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                String ket = input.getText().toString().trim();
                                if (!ket.equals(""))
                                    kegiatan.setKeterangan(ket);
                                else
                                    kegiatan.setKeterangan(null);
                                Action(Process_Kegiatan.ACTION_KETERANGAN, kegiatan);
                            }
                        }).setNegativeButton("Cancel", null).show();
                break;

            case R.id.result:
                input.setHint("Hasil Kegiatan");
                if (kegiatan.getResult() != null) {
                    input.setText(kegiatan.getResult());
                }
                new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT)
                        .setTitle("Result").setView(input)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                String res = input.getText().toString().trim();
                                if (!res.equals(""))
                                    kegiatan.setResult(res);
                                else
                                    kegiatan.setResult(null);
                                Action(Process_Kegiatan.ACTION_RESULT, kegiatan);
                            }
                        }).setNegativeButton("Cancel", null).show();
                break;

            case R.id.hasil:
                int layout = R.layout.keg_result_info;
                View customView = getActivity().getLayoutInflater().inflate(layout, null);
                TextView status = (TextView) customView
                        .findViewById(R.id.keg_result_status);
                TextView caption = (TextView) customView
                        .findViewById(R.id.keg_result_caption);
                TextView reason = (TextView) customView
                        .findViewById(R.id.hasil_reason);

                status.setText("Open");
                caption.setVisibility(View.GONE);
                reason.setVisibility(View.GONE);

                if (kegiatan.getCancel() != 0) { // -cancel
                    status.setText("Cancel");
                    caption.setText("Reason");
                    reason.setText(kegiatan.getReason());
                    caption.setVisibility(View.VISIBLE);
                    reason.setVisibility(View.VISIBLE);
                } else if (sh != null) {
                    if (sh.getStatus() == 1) { // -cos
                        status.setText("COS");
                    }
                }
                new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT)
                        .setTitle("Result").setView(input)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setView(customView)
                        .setPositiveButton("OK", null)
                        .show();
                break;
        }

        refreshAdapter();
        return super.onContextItemSelected(item);
    }

    private void OpenSalesOrder(Kegiatan k) {
        // TODO Auto-generated method stub
        final Intent intentOrder = new Intent(getActivity(), Input_Order_Activity.class);
        intentOrder.putExtra("id_kegiatan", k.getID());

        if (kegiatan.getSalesHeader().getOrderNo() == null) {
            /*
            Proses_Sales_Header.Generate_No_Order proses = new Proses_Sales_Header.Generate_No_Order(getActivity(),
                    new CallbackSalesHeader.CBNoOrder() {
                        @Override
                        public void onCB(String no_order) {
                            intentOrder.putExtra("no_order", no_order);
                            intentOrder.putExtra("canEdit", true);
                            startActivityForResult(intentOrder, INPUT_ORDER);
                        }
                    }, k.getID());
            proses.execute(Global.getBEX());
            */
            new Process_Sales_Header(getActivity()).Generate_No_Order(k.getID(), new CallbackSalesHeader.CBNoOrder() {
                @Override
                public void onCB(String no_order) {
                    intentOrder.putExtra("no_order", no_order);
                    intentOrder.putExtra("canEdit", true);
                    startActivityForResult(intentOrder, INPUT_ORDER);
                }
            });
        } else if (kegiatan.getSalesHeader().getOrderNo().contains("XXX")) {
            /*
            Proses_Sales_Header.Generate_No_Order proses = new Proses_Sales_Header.Generate_No_Order(getActivity(),
                    new CallbackSalesHeader.CBNoOrder() {
                        @Override
                        public void onCB(String no_order) {
                            intentOrder.putExtra("no_order", no_order);
                            intentOrder.putExtra("canEdit", true);
                            startActivityForResult(intentOrder, INPUT_ORDER);
                        }
                    }, k.getID());
            proses.execute(Global.getBEX());
            */
            new Process_Sales_Header(getActivity()).Generate_No_Order(k.getID(), new CallbackSalesHeader.CBNoOrder() {
                @Override
                public void onCB(String no_order) {
                    intentOrder.putExtra("no_order", no_order);
                    intentOrder.putExtra("canEdit", true);
                    startActivityForResult(intentOrder, INPUT_ORDER);
                }
            });
        } else {
            /*
            Proses_Sales_Header.CheckStatus proses = new Proses_Sales_Header.CheckStatus(getActivity(),
                    new CallbackSalesHeader.CBCheckStatus() {
                        @Override
                        public void onCB(int sales_status) {
                            switch (sales_status) {
                                case 0:
                                    intentOrder.putExtra("canEdit", true);
                                    startActivityForResult(intentOrder, INPUT_ORDER);
                                    break;
                                case 1:
                                    kegiatan.getSalesHeader().setStatus(1);
                                    new SalesHeaderSQLite(getActivity()).Update(kegiatan);
                                    Toast.makeText(getActivity(), "Order telah diproses menjadi COS", Toast.LENGTH_LONG).show();
                                    intentOrder.putExtra("canEdit", false);
                                    startActivityForResult(intentOrder, INPUT_ORDER);
                                    break;
                                case 2:
                                    intentOrder.putExtra("canEdit", true);
                                    startActivityForResult(intentOrder, INPUT_ORDER);
                                    break;
                                case 3:
                                    Toast.makeText(getActivity(), "Order sedang diproses oleh admin", Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    intentOrder.putExtra("canEdit", true);
                                    startActivityForResult(intentOrder, INPUT_ORDER);
                                    break;
                            }
                        }
                    });
            proses.execute(kegiatan.getSalesHeader().getHeaderID());
            */
            new Process_Sales_Header(getActivity()).CheckStatus(kegiatan.getSalesHeader().getHeaderID(), new CallbackSalesHeader.CBCheckStatus() {
                @Override
                public void onCB(int sales_status) {
                    switch (sales_status) {
                        case 0:
                            intentOrder.putExtra("canEdit", true);
                            startActivityForResult(intentOrder, INPUT_ORDER);
                            break;
                        case 1:
                            kegiatan.getSalesHeader().setStatus(1);
                            new SalesHeaderSQLite(getActivity()).Update(kegiatan);
                            Toast.makeText(getActivity(), "Order telah diproses menjadi COS", Toast.LENGTH_LONG).show();
                            intentOrder.putExtra("canEdit", false);
                            startActivityForResult(intentOrder, INPUT_ORDER);
                            break;
                        case 2:
                            intentOrder.putExtra("canEdit", true);
                            startActivityForResult(intentOrder, INPUT_ORDER);
                            break;
                        case 3:
                            Toast.makeText(getActivity(), "Order sedang diproses oleh admin", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            intentOrder.putExtra("canEdit", true);
                            startActivityForResult(intentOrder, INPUT_ORDER);
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        Intent i = new Intent(getActivity(), Input_Kegiatan_Activity.class);
        startActivityForResult(i, KEGIATAN);
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        LocationManager locationManager = (LocationManager) (getActivity().getSystemService(Context.LOCATION_SERVICE));
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            LocationLibrary.forceLocationUpdate(getActivity());
            new LocationInfo(getActivity()).refresh(getActivity());
            switch (requestCode) {
                case GPS_SETTING_REQUEST_CHECK_IN:
                    kegiatan.setCheckIn(Const.CHECK_IN);
                    prosesCheckIn(kegiatan);
                    break;
                case GPS_SETTING_REQUEST_CHECK_OUT:
                    kegiatan.setCheckIn(Const.CHECK_OUT);
                    prosesCheckOut(kegiatan, true);
                    break;
                case GPS_SETTING_REQUEST_PHOTO:
                    Intent intentPhoto = new Intent(getActivity(), Input_Photo_Activity.class);
                    intentPhoto.putExtra("id_kegiatan", kegiatan.getID());
                    intentPhoto.putExtra("canEdit", kegiatan.getSalesHeader().getStatus() == 0);
                    startActivityForResult(intentPhoto, INPUT_PHOTO);
                    break;
                case GPS_SETTING_REQUEST_ORDER:
                    OpenSalesOrder(kegiatan);
                    break;
            }
        }
        if (data != null) {
            switch (requestCode) {
                case KEGIATAN:
                    if (resultCode == Activity.RESULT_OK)
                        Toast.makeText(getActivity(), "Kegiatan berhasil disimpan", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        refreshAdapter();
    }

    private void updateCalendar() {
        currentCalendar = Calendar.getInstance(Locale.getDefault());
        currentCalendar.add(Calendar.MONTH, currentMonthIndex);
        calendarView.initializeCalendar(currentCalendar);
        if (currentMonthIndex == 0) {
            calendarView.markDayAsCurrentDay();
            selectedDate = currentCalendar.getTime();
        } else {
            selectedDate = null;
            RobotoCalendarView.today = null;
        }
        markDatePerMonth();
        refreshAdapter();
    }

    private void markDatePerMonth() {
        KegiatanSQLite s = new KegiatanSQLite(getActivity());
        k_month = new ArrayList<Kegiatan>();
        if (currentMonthIndex == 0)
            k_month = s.Read(Const.KEGIATAN_THIS_MONTH);
        else {
            k_month = s.Read(currentCalendar, Const.KEGIATAN_BY_MONTH);
        }
        ArrayList<String> dates = new ArrayList<String>();
        for (int i = 0; i < k_month.size(); i++) {
            dates.add(k_month.get(i).getStartDate().substring(0, 10));
        }
        calendarView.markMultipleDays(dates);
    }

    private void refreshAdapter() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        k_day = new ArrayList<>();
        if (selectedDate != null) {
            calendar.setTime(selectedDate);
            KegiatanSQLite s = new KegiatanSQLite(getActivity());
            k_day = s.Read(calendar, Const.KEGIATAN_BY_DATE);
        }
        adapter = new Adapter_List_Kegiatan(getActivity(), R.layout.adapter_list_kegiatan, k_day);
        list_view.setAdapter(adapter);
    }

    private void Action(final String action, Kegiatan k) {
        /*
        Proses_Kegiatan.ProsesAction proses = new Proses_Kegiatan.ProsesAction(getActivity(), new CallbackKegiatan.CBAction() {
            @Override
            public void onCB(Kegiatan k) {
                // TODO Auto-generated method stub
                if (action == Const.ACTION_CANCEL) {
                    Toast.makeText(getActivity(), "Kegiatan dibatalkan.",
                            Toast.LENGTH_SHORT).show();
                    kegiatan.setCheckIn(Const.CHECK_OUT);
                    prosesCheckOut(kegiatan, false);
                }
                refreshAdapter();
            }
        }, action);
        proses.execute(k);
        */
        new Process_Kegiatan(getActivity()).Action(k, action, new CallbackKegiatan.CBAction() {
            @Override
            public void onCB(Kegiatan k) {
                if (action.equals(Process_Kegiatan.ACTION_CANCEL)) {
                    Toast.makeText(getActivity(), "Kegiatan dibatalkan.",
                            Toast.LENGTH_SHORT).show();
                    kegiatan.setCheckIn(Const.CHECK_OUT);
                    prosesCheckOut(kegiatan, false);
                }
                refreshAdapter();
            }
        });
    }

    private void prosesCheckIn(Kegiatan k) {
        new KegiatanSQLite(getActivity()).CheckOutAll();
        /*
        Proses_Kegiatan.CheckInCheckOut check = new Proses_Kegiatan.CheckInCheckOut(getActivity(),
                new CallbackKegiatan.CBCheckInCheckOut() {
                    @Override
                    public void onCB(Kegiatan k) {
                        Global.registerReceiver(getActivity());
                        refreshAdapter();
                        Toast.makeText(getActivity(), "Check In Successfully", Toast.LENGTH_LONG).show();
                    }
                }, Const.ACTION_CHECK_IN);
        check.execute(k);
        */
        new Process_Kegiatan(getActivity()).CheckInCheckOut(k, Process_Kegiatan.ACTION_CHECK_IN, new CallbackKegiatan.CBCheckInCheckOut() {
            @Override
            public void onCB(Kegiatan k) {
                Global.registerReceiver(getActivity());
                refreshAdapter();
                Toast.makeText(getActivity(), "Check In Successfully", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void prosesCheckOut(Kegiatan k, final boolean showToast) {
        /*
        Proses_Kegiatan.CheckInCheckOut check = new Proses_Kegiatan.CheckInCheckOut(getActivity(),
                new CallbackKegiatan.CBCheckInCheckOut() {
                    @Override
                    public void onCB(Kegiatan k) {
                        Global.unregisterReceiver(getActivity());
                        refreshAdapter();
                        if (showToast)
                            Toast.makeText(getActivity(), "Check Out Successfully", Toast.LENGTH_SHORT).show();
                    }
                }, Const.ACTION_CHECK_OUT);
        check.execute(k);
        */
        new Process_Kegiatan(getActivity()).CheckInCheckOut(k, Process_Kegiatan.ACTION_CHECK_OUT, new CallbackKegiatan.CBCheckInCheckOut() {
            @Override
            public void onCB(Kegiatan k) {
                Global.unregisterReceiver(getActivity());
                refreshAdapter();
                if (showToast)
                    Toast.makeText(getActivity(), "Check Out Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkGPS(int requestCode) {
        LocationManager locationManager = (LocationManager) (getActivity().getSystemService(Context.LOCATION_SERVICE));
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            LocationLibrary.forceLocationUpdate(getActivity());
            new LocationInfo(getActivity()).refresh(getActivity());
            return true;
        } else {
            Intent intentSettingGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intentSettingGPS, requestCode);
            return false;
        }
    }
}
