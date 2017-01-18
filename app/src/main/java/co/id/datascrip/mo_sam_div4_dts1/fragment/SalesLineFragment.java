package co.id.datascrip.mo_sam_div4_dts1.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import co.id.datascrip.mo_sam_div4_dts1.R;
import co.id.datascrip.mo_sam_div4_dts1.activity.Input_Item_Activity;
import co.id.datascrip.mo_sam_div4_dts1.activity.Input_Order_Activity;
import co.id.datascrip.mo_sam_div4_dts1.adapter.Adapter_List_Item;
import co.id.datascrip.mo_sam_div4_dts1.font.RobotoButton;
import co.id.datascrip.mo_sam_div4_dts1.font.RobotoEditText;
import co.id.datascrip.mo_sam_div4_dts1.font.RobotoTextView;
import co.id.datascrip.mo_sam_div4_dts1.function.Hitung;
import co.id.datascrip.mo_sam_div4_dts1.object.Item;

/**
 * Created by benny_aziz on 02/16/2015.
 */
public class SalesLineFragment extends Fragment {

    private static final int NEW_ITEM_REQUEST = 1;
    private static final int EDIT_ITEM_REQUEST = 2;

    private ListView lvListItem;
    private RobotoButton btaddItem;
    private RobotoTextView tvSubTotal;

    private Adapter_List_Item adapter;

    private Hitung h = new Hitung();

    private int edit_position;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sales_line_fragment, container, false);

        lvListItem = (ListView) root.findViewById(R.id.lvListItem);
        lvListItem.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        btaddItem = (RobotoButton) root.findViewById(R.id.btAddItem);
        tvSubTotal = (RobotoTextView) root.findViewById(R.id.tvSubTotal);

        setData();

        btaddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAddItem = new Intent(getActivity(), Input_Item_Activity.class);
                intentAddItem.putExtra("currency", Input_Order_Activity.kegiatan.getSalesHeader().getCurrency());
                startActivityForResult(intentAddItem, NEW_ITEM_REQUEST);
            }
        });

        lvListItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intentEditItem = new Intent(getActivity(), Input_Item_Activity.class);
                intentEditItem.putExtra("item", adapter.getItem(i));
                intentEditItem.putExtra("currency", Input_Order_Activity.kegiatan.getSalesHeader().getCurrency());
                edit_position = i;
                startActivityForResult(intentEditItem, EDIT_ITEM_REQUEST);
            }
        });

        lvListItem.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            private int selectedCount;

            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                selectedCount = lvListItem.getCheckedItemCount();
                actionMode.setTitle(selectedCount + " selected");
                adapter.ToggleSelection(i);
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                actionMode.getMenuInflater().inflate(R.menu.menu_sales_line_fragment, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.delete:
                        SparseBooleanArray selected = adapter.getSelectedId();
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                Item selectedItem = adapter.getItem(selected.keyAt(i));
                                Input_Order_Activity.kegiatan.getSalesHeader().getSalesLine().remove(selectedItem);
                            }
                        }
                        actionMode.finish();
                        Toast.makeText(getActivity(), String.valueOf(selectedCount) + " item deleted.", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                setData();
            }
        });

        setEnabled();

        return root;
    }

    private void setData() {
        adapter = new Adapter_List_Item(getActivity(), R.layout.adapter_list_item,
                Input_Order_Activity.kegiatan.getSalesHeader().getSalesLine());
        lvListItem.setAdapter(adapter);
        tvSubTotal.setText(Input_Order_Activity.kegiatan.getSalesHeader().getCurrency() + "  " + h.formatNumber(setTotal()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case NEW_ITEM_REQUEST:
                if (resultCode == getActivity().RESULT_OK && data != null)
                    Input_Order_Activity.kegiatan.getSalesHeader().getSalesLine().add((Item) data.getParcelableExtra("item"));
                break;
            case EDIT_ITEM_REQUEST:
                if (resultCode == getActivity().RESULT_OK && data != null) {
                    Input_Order_Activity.kegiatan.getSalesHeader().getSalesLine().remove(edit_position);
                    Input_Order_Activity.kegiatan.getSalesHeader().getSalesLine().add(edit_position, (Item) data.getParcelableExtra("item"));
                }
                break;
        }
        setData();
        RobotoEditText txSubtotal = SalesTotalFragment.txSubtotal;
        txSubtotal.setText(h.formatNumber(setTotal()));
    }

    private double setTotal() {
        double total = 0.0;
        for (int i = 0; i < Input_Order_Activity.kegiatan.getSalesHeader().getSalesLine().size(); i++) {
            total = total + Input_Order_Activity.kegiatan.getSalesHeader().getSalesLine().get(i).getSubtotal();
        }
        Input_Order_Activity.SubTotal = total;
        return total;
    }

    private void setEnabled() {
        if (!Input_Order_Activity.canEdit) {
            lvListItem.setOnItemSelectedListener(null);
            lvListItem.setOnItemLongClickListener(null);
            btaddItem.setVisibility(View.GONE);
        }
    }
}
