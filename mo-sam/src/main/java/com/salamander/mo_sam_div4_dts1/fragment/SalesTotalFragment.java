package com.salamander.mo_sam_div4_dts1.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.salamander.mo_sam_div4_dts1.Const;
import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.activity.Input_Order_Activity;
import com.salamander.mo_sam_div4_dts1.font.RobotoButton;
import com.salamander.mo_sam_div4_dts1.font.RobotoEditText;
import com.salamander.mo_sam_div4_dts1.function.Hitung;
import com.salamander.mo_sam_div4_dts1.object.Item;
import com.salamander.mo_sam_div4_dts1.proses.Proses_Sales_Header;
import com.salamander.mo_sam_div4_dts1.proses.callback.Callbacks;
import com.salamander.mo_sam_div4_dts1.sqlite.ItemSQLite;
import com.salamander.mo_sam_div4_dts1.sqlite.SalesHeaderSQLite;
import com.salamander.salamander_network.RetroStatus;

import java.util.ArrayList;

/**
 * Created by benny_aziz on 02/16/2015.
 */
public class SalesTotalFragment extends Fragment {

    public static RobotoEditText txSubtotal;
    private RobotoEditText txPotongan, txDPP, txPPN, txTotal, txNotes;
    private RobotoButton btSimpan;

    private Hitung h = new Hitung();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.sales_total_fragment, container, false);

        txSubtotal = root.findViewById(R.id.stf_subtotal);
        txPotongan = root.findViewById(R.id.stf_potongan);
        txDPP = root.findViewById(R.id.stf_dpp);
        txPPN = root.findViewById(R.id.stf_ppn);
        txTotal = root.findViewById(R.id.stf_total);
        txNotes = root.findViewById(R.id.stf_notes);
        btSimpan = root.findViewById(R.id.btSimpan);

        setData();
        setEnabled();

        txSubtotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                h.FormatNumberDecimal(editable, txTotal);
                HitungTotal();
            }
        });

        txPotongan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                h.FormatNumberDecimal(editable, txPotongan);
                HitungTotal();
            }
        });

        txNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Input_Order_Activity.kegiatan.getSalesHeader().setNotes(txNotes.getText().toString());
            }
        });

        btSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {
                    setHeader();
                    ArrayList<Item> items = Input_Order_Activity.kegiatan.getSalesHeader().getSalesLine();
                    Input_Order_Activity.kegiatan.getSalesHeader().setSalesLine(new ItemSQLite(getActivity()).Post(items));
                    /*
                    Proses_Sales_Header.Posting posting = new Proses_Sales_Header.Posting(getActivity(),
                            new CallbackSalesHeader.CBPosting() {
                                @Override
                                public void onCB(Kegiatan kegiatan) {
                                    if (kegiatan != null) {
                                        Toast.makeText(getActivity(), "Order berhasil disimpan.", Toast.LENGTH_LONG).show();
                                        getActivity().finish();
                                    }
                                }
                            });
                    posting.execute(Input_Order_Activity.kegiatan);
                    */
                    new Proses_Sales_Header(getActivity()).post(Input_Order_Activity.kegiatan.getSalesHeader(), new Callbacks.OnCB() {
                        @Override
                        public void onCB(RetroStatus retroStatus) {
                            if (retroStatus.isSuccess()) {
                                Toast.makeText(getActivity(), "Order berhasil disimpan.", Toast.LENGTH_LONG).show();
                                getActivity().finish();
                            }
                        }
                    });
                }
            }
        });

        return root;
    }

    private boolean isValid() {
        if (Input_Order_Activity.kegiatan.getSalesHeader().getSyarat() == null) {
            Toast.makeText(getActivity(), "Syarat Pembayaran tidak boleh kosong!",
                    Toast.LENGTH_LONG).show();
            Input_Order_Activity.viewPager.setCurrentItem(0, true);
            SalesHeaderFragment.tx_syarat.requestFocus();
            return false;
        } else if (Input_Order_Activity.kegiatan.getSalesHeader().getSyarat().equals("")) {
            Toast.makeText(getActivity(), "Syarat Pembayaran tidak boleh kosong!",
                    Toast.LENGTH_LONG).show();
            Input_Order_Activity.viewPager.setCurrentItem(0, true);
            SalesHeaderFragment.tx_syarat.requestFocus();
            return false;
        } else if (Input_Order_Activity.kegiatan.getSalesHeader().getSalesLine().size() == 0) {
            Toast.makeText(getActivity(), "Sales Lines tidak boleh kosong!",
                    Toast.LENGTH_LONG).show();
            Input_Order_Activity.viewPager.setCurrentItem(1, true);
            return false;
        }
        return true;
    }

    private void setData() {
        txSubtotal.setText(h.formatNumber(Input_Order_Activity.SubTotal));
        txSubtotal.setText(h.formatNumber(Input_Order_Activity.kegiatan.getSalesHeader().getSubtotal()));
        txPotongan.setText(h.formatNumber(Input_Order_Activity.kegiatan.getSalesHeader().getPotongan()));
        txNotes.setText(Input_Order_Activity.kegiatan.getSalesHeader().getNotes());
        HitungTotal();
    }

    private void setEnabled() {
        if (!Input_Order_Activity.canEdit) btSimpan.setVisibility(View.GONE);
        txPotongan.setFocusable(Input_Order_Activity.canEdit);
        txNotes.setFocusable(Input_Order_Activity.canEdit);
    }

    private void HitungTotal() {
        double t = h.getDouble(txSubtotal),
                po = h.getDouble(txPotongan),
                d = t - po,
                pp = 0.1 * d,
                g = d + pp;

        txDPP.setText(h.formatNumber(d));
        txPPN.setText(h.formatNumber(pp));
        txTotal.setText(h.formatNumber(g));

        h.FormatNumberDecimal(txDPP.getText(), txDPP);
        h.FormatNumberDecimal(txPPN.getText(), txPPN);
        h.FormatNumberDecimal(txTotal.getText(), txTotal);

        setHeader();
    }

    private void setHeader() {
        Input_Order_Activity.kegiatan.getSalesHeader().setOrderNo(Input_Order_Activity.no_order);
        Input_Order_Activity.kegiatan.getSalesHeader().setSubtotal(h.getDouble(txSubtotal));
        Input_Order_Activity.kegiatan.getSalesHeader().setPotongan(h.getDouble(txPotongan));
        Input_Order_Activity.kegiatan.getSalesHeader().setDPP(h.getDouble(txDPP));
        Input_Order_Activity.kegiatan.getSalesHeader().setPPnDPP(h.getDouble(txPPN));
        Input_Order_Activity.kegiatan.getSalesHeader().setTotal(h.getDouble(txTotal));
        Input_Order_Activity.kegiatan.getSalesHeader().setNotes(txNotes.getText().toString());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        String no = new SalesHeaderSQLite(getActivity()).getFromKegiatan(Input_Order_Activity.kegiatan.getIDServer()).getOrderNo();
        if (no == null)
            no = "";
        if (no.contains("XXX") || no.equals(""))
            inflater.inflate(R.menu.menu_sales_header, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if (isValid()) {
                    setHeader();
                    if (Input_Order_Activity.kegiatan.getSalesHeader().getIDServer() >= Const.DEFAULT_HEADER_ID || Input_Order_Activity.kegiatan.getSalesHeader().getIDServer() == 0) {
                        if (!Input_Order_Activity.kegiatan.getSalesHeader().getOrderNo().contains("XXX"))
                            Input_Order_Activity.kegiatan.getSalesHeader().setOrderNo(new SalesHeaderSQLite(getActivity()).getTempOrderNo());
                        new SalesHeaderSQLite(getActivity()).post(Input_Order_Activity.kegiatan.getSalesHeader());
                        getActivity().finish();
                    } else
                        Toast.makeText(getActivity(), "Order ini sudah diposting.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
