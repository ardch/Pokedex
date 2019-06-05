package com.example.arach.pokedex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> name_pokemon = new ArrayList<>();
    private AdapterListPokemon adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JsonTask().execute("https://pokeapi.co/api/v2/pokemon/");
            }
        });
    }

    private class JsonTask extends AsyncTask<String, String, String> implements AdapterListPokemon.ItemClickListener {

        ProgressDialog loading;
        private JSONObject obj_result;

        protected void onPreExecute() {
            super.onPreExecute();

            loading = new ProgressDialog(MainActivity.this);
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
                for (int i=0; i<array_result.length(); i++){
                    name_pokemon.add(array_result.getJSONObject(i).getString("name"));
                }
                RecyclerView recyclerView = findViewById(R.id.recyclerview);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new AdapterListPokemon(getApplicationContext(), name_pokemon);
                adapter.setClickListener(this);
                recyclerView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onItemClick(View view, int position) {
            String name = adapter.getName(position);
            Intent intent = new Intent(getApplicationContext(), DescriptionPokemon.class);
            intent.putExtra("name", name);
            startActivity(intent);
        }

        @Override
        public void onItemLongClick(View view, int position) {
            Toast.makeText(MainActivity.this, "Long Click", Toast.LENGTH_SHORT).show();
        }
    }
}
