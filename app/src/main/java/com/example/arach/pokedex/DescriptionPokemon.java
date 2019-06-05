package com.example.arach.pokedex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
import java.util.List;

public class DescriptionPokemon extends AppCompatActivity {

    private String name , baseStat, stat_name;
    private ImageView upleft, upright, downleft, downright;
    private JSONObject obj_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despokemon);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        new JsonTask().execute("https://pokeapi.co/api/v2/pokemon/" + name);

        TextView tName = findViewById(R.id.pokeName);
        tName.setText(name);
    }

    private class JsonTask extends AsyncTask<String, String, String> {

    /*ProgressDialog loading;

    protected void onPreExecute() {
        super.onPreExecute();

        loading = new ProgressDialog(this);
        loading.setMessage("Please wait");
        loading.setCancelable(false);
        loading.show();
    }*/

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
                    if (reader != null) { reader.close(); }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);            // GET data from return
        /*if (loading.isShowing()) {
            loading.dismiss();
        }*/
            try {
                /* Data to JSON */
                String frontPoke, backPoke, sFrontPoke, sBackPoke, txt, speed, spdef, spatk, def, atk;
                TextView tSpeed, tSpdef, tSpatk, tDef, tAtk;

                JSONObject obj_result = new JSONObject(result);
                JSONObject sprites = obj_result.getJSONObject("sprites");

                frontPoke = sprites.getString("front_default");
                backPoke = sprites.getString("back_default");
                sFrontPoke = sprites.getString("front_shiny");
                sBackPoke = sprites.getString("back_shiny");

                upleft = findViewById(R.id.pokefront);
                upright = findViewById(R.id.pokeback);
                downleft = findViewById(R.id.pokesfront);
                downright = findViewById(R.id.pokesback);

                Picasso.get().load(frontPoke).fit().centerCrop().into(upleft);
                Picasso.get().load(backPoke).fit().centerCrop().into(upright);
                Picasso.get().load(sFrontPoke).fit().centerCrop().into(downleft);
                Picasso.get().load(sBackPoke).fit().centerCrop().into(downright);

                tSpeed = findViewById(R.id.tSpeed);
                tSpdef = findViewById(R.id.tSpdef);
                tSpatk = findViewById(R.id.tSpatk);
                tAtk = findViewById(R.id.tAtk);
                tDef = findViewById(R.id.tDef);

                obj_result = new JSONObject(result);
                JSONArray stats = obj_result.getJSONArray("stats");

                for (int i=0; i<stats.length(); i++){
                    String stat = stats.getJSONObject(i).getString("stat");
                    baseStat = stats.getJSONObject(i).getString("base_stat");

                    JSONObject obj_stat = new JSONObject(stat);
                    stat_name = obj_stat.getString("name");

                    txt = stat_name + " : " + baseStat;
                    switch(stat_name){
                        case "speed" :
                            tSpeed.setText(txt); break;
                        case "special-defense" :
                            tSpdef.setText(txt); break;
                        case "special-attack" :
                            tSpatk.setText(txt); break;
                        case "defense" :
                            tAtk.setText(txt); break;
                        case "attack" :
                            tDef.setText(txt); break;
                            default:
                                tSpeed.setText(txt);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}