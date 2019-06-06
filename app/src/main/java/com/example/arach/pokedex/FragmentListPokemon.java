package com.example.arach.pokedex;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FragmentListPokemon extends Fragment {
    Callback mCallback;
    private ArrayList<String> name_pokemon = new ArrayList<>();
    private AdapterListPokemon adapter;
    private View view;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (Callback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement Callback");
        }
    }

    public FragmentListPokemon() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_pokemon, container, false);
        SharedPreferences sp = getContext().getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        if (sp.getString("State","").matches("Back")){
            new JsonTask().execute(sp.getString("Back",""));
        }
        else{
            new JsonTask().execute(sp.getString("Next",""));
        }
        return view;
    }

    private class JsonTask extends AsyncTask<String, String, String> implements AdapterListPokemon.ItemClickListener {

        ProgressDialog loading;
        private JSONObject obj_result;

        protected void onPreExecute() {
            super.onPreExecute();

            loading = new ProgressDialog(getContext());
            loading.setMessage("Please wait");
            loading.setCancelable(false);
            loading.show();
        }

        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);            // GET data from return
            if (loading.isShowing()) {
                loading.dismiss();
            }
            try {
                /* Data to JSON */
                obj_result = new JSONObject(result);
                JSONArray array_result = obj_result.getJSONArray("results");
                String next = obj_result.getString("next");
                String back = obj_result.getString("previous");
                int total = obj_result.getInt("count");

                SharedPreferences sp = getContext().getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();

                if (!next.matches("null")){
                    Integer count = Integer.valueOf(next.substring(next.lastIndexOf("offset=")+7,next.lastIndexOf("&")));
                    if(total/20 == count/20){
                        editor.putString("End","1");
                        editor.putString("Next",next.substring(0,next.lastIndexOf("="))+ "20");
                    }
                    else {
                        editor.putString("Next", next);
                    }
                }
                if (!back.matches("null")){
                    editor.putString("Back",back);
                }
                editor.apply();

                for (int i=0; i<array_result.length(); i++){
                    name_pokemon.add(array_result.getJSONObject(i).getString("name"));
                }
                RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new AdapterListPokemon(getContext(), name_pokemon);
                adapter.setClickListener(this);
                recyclerView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onItemClick(View view, int position) {
            String name = adapter.getName(position);
            Intent intent = new Intent(getContext(), DescriptionPokemon.class);
            intent.putExtra("name", name);
            startActivity(intent);
        }

        @Override
        public void onItemLongClick(View view, final int position) {
            String name = adapter.getName(position);
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            String dialogMsg = getResources().getString(R.string.dialog_message) + " " + name;
            builder.setMessage(dialogMsg).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.removePokemon(position);
                    Toast.makeText(getContext(), "Pokemon Removed", Toast.LENGTH_SHORT).show();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
