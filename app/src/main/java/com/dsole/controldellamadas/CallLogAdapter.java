package com.dsole.controldellamadas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dsole on 29/12/2014.
 */
public class CallLogAdapter extends ArrayAdapter<CallLog> {
    private ArrayList<CallLog> objects;

    public CallLogAdapter(Context context, int textViewResourceId, ArrayList<CallLog> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent){
       View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.listview, null);
        }

        CallLog i = objects.get(position);

        if (i != null) {

            TextView name = (TextView) v.findViewById(R.id.name);
            TextView number = (TextView) v.findViewById(R.id.number);
            TextView type = (TextView) v.findViewById(R.id.type);
            TextView date = (TextView) v.findViewById(R.id.date);
            TextView time = (TextView) v.findViewById(R.id.time);

            name.setText(i.getName());
            number.setText(i.getNumber());
            type.setText(i.getType());
            date.setText(i.getDate());
            time.setText(i.getTime());
        }

        return v;
    }
}
