package com.salamander.mo_sam_div4_dts1.custom;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.adapter.Adapter_List_Log;
import com.salamander.mo_sam_div4_dts1.font.RobotoButton;
import com.salamander.mo_sam_div4_dts1.font.RobotoEditText;
import com.salamander.mo_sam_div4_dts1.object.Feedback;
import com.salamander.mo_sam_div4_dts1.object.Item;
import com.salamander.mo_sam_div4_dts1.object.Log_Feed;
import com.salamander.mo_sam_div4_dts1.proses.Proses_Feedback;
import com.salamander.mo_sam_div4_dts1.proses.callback.CallbackFeedback;
import com.salamander.mo_sam_div4_dts1.sqlite.LogSQLite;

import java.util.ArrayList;

/**
 * Created by benny_aziz on 03/24/2015.
 */
public class PopUpComment extends PopupWindows implements PopupWindow.OnDismissListener {

    private Context context;
    private Animation mTrackAnim;
    private LayoutInflater inflater;
    private OnDismissListener mDismissListener;

    private boolean mDidAction;

    private Item item;
    private SwipeRefreshLayout swipe;
    private RobotoEditText txReply;
    private RobotoButton btReply, btClose;
    private ListView listView;
    private Adapter_List_Log adapter;
    private ArrayList<Log_Feed> logs;

    /**
     * Constructor.
     *
     * @param context Context
     */
    public PopUpComment(Context context, Item item) {
        super(context);
        this.context = context;
        this.item = item;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mTrackAnim = AnimationUtils.loadAnimation(context, R.anim.rail);

        mTrackAnim.setInterpolator(new Interpolator() {
            public float getInterpolation(float t) {
                // Pushes past the target area, then snaps back into place.
                // Equation for graphing: 1.2-((x*1.6)-1.1)^2
                final float inner = (t * 1.55f) - 1.1f;

                return 1.2f - inner * inner;
            }
        });

        setRootViewId(R.layout.custom_dialog_comment);

    }

    /**
     * Set root view.
     *
     * @param id Layout resource id
     */
    public void setRootViewId(int id) {
        mRootView = (ViewGroup) inflater.inflate(id, null);

        //This was previously defined on show() method, moved here to prevent force close that occured
        //when tapping fastly on a view to show quickaction dialog.
        //Thanx to zammbi (github.com/zammbi)
        mRootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        setContentView(mRootView);
    }

    /**
     * Show popup mWindow
     */
    public void show(View anchor) {
        preShow();

        inisiasi();

        int[] location = new int[2];

        mDidAction = false;

        anchor.getLocationOnScreen(location);

        int screenWidth = context.getApplicationContext().getResources().getDisplayMetrics().widthPixels;
        int screenHeight = context.getApplicationContext().getResources().getDisplayMetrics().heightPixels;

        mRootView.measure(screenWidth, screenHeight);

        setAnimationStyle();

        mWindow.showAtLocation(anchor, Gravity.CENTER, 0, 0);
    }

    private void inisiasi() {
        swipe = (SwipeRefreshLayout) mRootView.findViewById(R.id.iia_swipe_container);
        listView = (ListView) mRootView.findViewById(R.id.iia_listview_log);
        txReply = (RobotoEditText) mRootView.findViewById(R.id.iia_reply);
        btReply = (RobotoButton) mRootView.findViewById(R.id.iia_btReply);
        btClose = (RobotoButton) mRootView.findViewById(R.id.iia_btClose);

        refresh();

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                swipe.setRefreshing(false);
            }
        });

        swipe.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        btReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!txReply.getText().toString().trim().isEmpty()) {
                    Feedback f = new Feedback();
                    f.setUser(App.getUser(context));
                    f.setLineID(item.getIDServer());
                    f.setNote(txReply.getText().toString());
                    /*
                    new Proses_Feedback.Send_Reply(context, new Callback.CBReply() {
                        @Override
                        public void onCB(String feed_notes) {
                            if (feed_notes != null) {
                                refresh();
                                txReply.setText("");
                            }
                        }
                    }).execute(f);
                    */
                    new Proses_Feedback(context, "Loading...").Send_Reply(f, new CallbackFeedback.CBPost() {
                        @Override
                        public void onCB(String feedback_note) {
                            if (feedback_note != null) {
                                refresh();
                                txReply.setText("");
                            }
                        }
                    });
                }
            }
        });

        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    /**
     * Set animation style
     */
    private void setAnimationStyle() {
        boolean onTop = false;
        mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);
    }

    /**
     * Set listener for window dismissed. This listener will only be fired if the quicakction dialog is dismissed
     * by clicking outside the dialog or clicking on sticky item.
     */
    public void setOnDismissListener(PopUpComment.OnDismissListener listener) {
        setOnDismissListener(this);

        mDismissListener = listener;
    }

    @Override
    public void onDismiss() {
        if (!mDidAction && mDismissListener != null) {
            mDismissListener.onDismiss();
        }
    }

    private void refresh() {
        logs = new LogSQLite(context).get(item.getIDServer());
        adapter = new Adapter_List_Log(context, R.layout.adapter_list_log, logs);
        listView.setAdapter(adapter);
    }

    /**
     * Listener for window dismiss
     */
    public interface OnDismissListener {
        public abstract void onDismiss();
    }


}
