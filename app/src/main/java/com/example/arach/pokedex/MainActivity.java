package com.example.arach.pokedex;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.textclassifier.TextSelection;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

    ArrayList<String> name_pokemon = new ArrayList();

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

    private class JsonTask extends AsyncTask<String, String, String> {

        ProgressDialog pd;
        //TextView textView1 = findViewById(R.id.textview1);
        AdapterListPokemon adapterListPokemon;

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
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
                    //Log.d("Response: ", "> " + line);  // Get RESPONSE
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
            if (pd.isShowing()) {
                pd.dismiss();
            }
            try {
                JSONObject obj_result = new JSONObject(result);
                JSONArray array_result = obj_result.getJSONArray("results");
                for (int i=0; i<array_result.length(); i++){
                    name_pokemon.add(array_result.getJSONObject(i).getString("name"));
                    adapterListPokemon = new AdapterListPokemon(getApplicationContext(), name_pokemon);
                    ListView list = (ListView) findViewById(R.id.list);
                    list.setAdapter(adapterListPokemon);
                }
                //textView1.setText();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
