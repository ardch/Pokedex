package com.example.arach.pokedex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterDesPokemon extends BaseAdapter {
    Context context;
    ArrayList<String> type, name;
    ArrayList<Integer> ATK,DEF,speed,SP_ATK,SP_DEF;
    public AdapterDesPokemon(Context context, ArrayList<String> name, ArrayList<String> type, ArrayList<Integer> ATK, ArrayList<Integer> DEF, ArrayList<Integer> speed, ArrayList<Integer> SP_ATK, ArrayList<Integer> SP_DEF){
        context = this.context;
        name = this.name;
        type = this.type;
        ATK = this.ATK;
        DEF = this.DEF;
        speed = this.speed;
        SP_ATK = this.SP_ATK;
        SP_DEF = this.SP_DEF;
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
        //view = mInflater.inflate(R.layout.layoutsend, parent, false);
        return view;
    }
}
