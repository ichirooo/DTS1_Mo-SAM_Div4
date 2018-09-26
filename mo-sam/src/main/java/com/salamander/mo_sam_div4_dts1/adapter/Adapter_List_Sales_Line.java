package com.salamander.mo_sam_div4_dts1.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.activity.order.Input_Sales_Order_Activity;
import com.salamander.mo_sam_div4_dts1.object.Item;
import com.salamander.mo_sam_div4_dts1.util.CurrencyUtils;

import java.util.ArrayList;

/**
 * Created by benny_aziz on 04/26/2017.
 */

public class Adapter_List_Sales_Line extends RecyclerView.Adapter<Adapter_List_Sales_Line.ItemViewHolder> {

    private Input_Sales_Order_Activity context;
    private ArrayList<Item> list_item;
    private OnItemClick onItemClick;
    private OnDelete onDelete;

    public Adapter_List_Sales_Line(Input_Sales_Order_Activity context, ArrayList<Item> list_item) {
        this.context = context;
        this.list_item = list_item;
    }

    @Override
    public @NonNull
    ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_sales_line, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {
        final Item item = list_item.get(position);

        holder.tx_row_number.setText(String.valueOf(holder.getAdapterPosition() + 1) + ".");
        holder.tx_item_code.setText(item.getCode());
        holder.tx_item_description.setText(item.getDescription());
        holder.tx_item_unit.setText(item.getUnit());
        holder.tx_item_subtotal.setText(CurrencyUtils.formatNumber(item.getSubtotal()));
        holder.tx_item_quantity.setText(CurrencyUtils.formatNumber(item.getQuantity()));

        holder.bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDelete != null)
                    onDelete.onDelete(item, holder.getAdapterPosition());
            }
        });

        holder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClick != null)
                    onItemClick.onItemClick(item, holder.getAdapterPosition());
                /*
                Intent intent = new Intent(context, Input_Item_Activity.class);
                intent.putExtra("item", item);
                intent.putExtra("position", position);
                context.startActivityForResult(intent, Fragment_Sales_Line.REQ_EDIT_ITEM);
                */
            }
        });

        if (context.getSalesHeader().isEditable())
            holder.bt_delete.setVisibility(View.VISIBLE);
        else holder.bt_delete.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return list_item.size();
    }

    public void setOnItemDeletedListener(OnDelete onDeleteListener) {
        this.onDelete = onDeleteListener;
    }

    public OnDelete getOnDeleteListener() {
        return this.onDelete;
    }

    public void setOnItemClickListener(OnItemClick onItemClickListener) {
        this.onItemClick = onItemClickListener;
    }

    public interface OnDelete {
        void onDelete(Item item, int position);
    }

    public interface OnItemClick {
        void onItemClick(Item item, int position);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        View rootView;
        LinearLayout ll_item;
        ImageView bt_delete;
        TextView tx_row_number, tx_item_code, tx_item_description, tx_currency_symbol, tx_item_subtotal, tx_item_quantity, tx_item_unit;

        public ItemViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            this.tx_row_number = rootView.findViewById(R.id.tx_row_number);
            this.ll_item = rootView.findViewById(R.id.ll_item);
            this.tx_item_code = rootView.findViewById(R.id.tx_item_code);
            this.tx_item_description = rootView.findViewById(R.id.tx_item_description);
            this.tx_currency_symbol = rootView.findViewById(R.id.tx_currency_symbol);
            this.tx_item_subtotal = rootView.findViewById(R.id.tx_item_subtotal);
            this.tx_item_quantity = rootView.findViewById(R.id.tx_item_quantity);
            this.tx_item_unit = rootView.findViewById(R.id.tx_item_unit);
            this.bt_delete = rootView.findViewById(R.id.bt_delete);
            tx_currency_symbol.setText(App.CURRENCY_SYMBOL);
        }
    }
}
