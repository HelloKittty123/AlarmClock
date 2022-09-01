package com.samsung.alarmteam6.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.samsung.alarmteam6.R;
import com.samsung.alarmteam6.models.Product;

import java.util.ArrayList;

public class AdapterStopWatch extends BaseAdapter {
    final ArrayList<Product> listview;
    private LayoutInflater inflater;
    TextView view_ID, view_buff, view_timer;

    public AdapterStopWatch(Context context, ArrayList<Product> list) {
        this.listview = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return listview.size();
    }

    @Override
    public Object getItem(int i) {
        return listview.get(i);
    }

    @Override
    public long getItemId(int i) {
        return listview.get(i).ProductID;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.product_view, null);
        }
        Product product = listview.get(i);
        System.out.println(product.ProductID);
        System.out.println(product.buff);
        System.out.println(product.timer);
        view_ID = view.findViewById(R.id.product_id);
        view_buff = view.findViewById(R.id.product_buff);
        view_timer = view.findViewById(R.id.product_timer);
        view_ID.setText(Integer.toString(product.getProductID()) + "  ");
        view_buff.setText(product.getBuff() + "  ");
        view_timer.setText(product.getTimer());

        return view;
    }
}
