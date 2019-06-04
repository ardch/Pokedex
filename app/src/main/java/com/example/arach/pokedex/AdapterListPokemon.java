package com.example.arach.pokedex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterListPokemon extends BaseAdapter {

    Context context;
    ArrayList<String> name;

    public AdapterListPokemon (Context context, ArrayList<String> name){
        this.context = context;
        this.name = name;
    }

    @Override
    public int getCount() {
        return name.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.activity_namelist, parent, false);
        TextView textView1 = view.findViewById(R.id.namelist);
        textView1.setText(name.get(position));
        return view;
    }
}
