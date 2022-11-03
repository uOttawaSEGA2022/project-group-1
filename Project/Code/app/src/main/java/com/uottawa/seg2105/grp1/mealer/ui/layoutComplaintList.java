package com.uottawa.seg2105.grp1.mealer.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

import com.uottawa.seg2105.grp1.mealer.R;
import com.uottawa.seg2105.grp1.mealer.model.Complaint;

public class layoutComplaintList extends ArrayAdapter<Complaint> {

    private Activity context;
    List<Complaint> products;

    public layoutComplaintList(Activity context, List<Complaint> products) {
        super(context, R.layout.activity_layout_complaint_list, products);
        this.context = context;
        this.products = products;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_layout_complaint_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewPrice = (TextView) listViewItem.findViewById(R.id.textViewComplaint);

        Complaint product = products.get(position);
        textViewName.setText(product.getProductName());
        textViewPrice.setText(String.valueOf(product.getPrice()));
        return listViewItem;
    }
}