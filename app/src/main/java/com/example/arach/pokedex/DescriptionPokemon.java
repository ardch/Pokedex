package com.example.arach.pokedex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Collections;

public class DescriptionPokemon extends AppCompatActivity {

    private ImageView upleft, upright, downleft, downright;
    private JSONObject obj_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despokemon);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        new JsonTask().execute("https://pokeapi.co/api/v2/pokemon/" + name);

        TextView tName = findViewById(R.id.pokeName);
        name = name.substring(0,1).toUpperCase() + name.substring(1);
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
                String frontPoke, backPoke, sFrontPoke, sBackPoke, txt, type;
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
                TextView tType = findViewById(R.id.tType);

                obj_result = new JSONObject(result);
                JSONArray stats = obj_result.getJSONArray("stats");
                JSONArray types = obj_result.getJSONArray("types");

                for (int i=0; i<stats.length(); i++){
                    String stat = stats.getJSONObject(i).getString("stat");

                    JSONObject obj_stat = new JSONObject(stat);
                    String stat_name = obj_stat.getString("name");

                    String baseStat = stats.getJSONObject(i).getString("base_stat");

                    txt = stat_name + " : " + baseStat;
                    txt = txt.substring(0,1).toUpperCase() + txt.substring(1);
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

                ArrayList<String> array_type = new ArrayList<>();

                for (int i=0; i<types.length(); i++){
                    type = types.getJSONObject(i).getString("type");
                    JSONObject obj_type = new JSONObject(type);
                    array_type.add(obj_type.getString("name"));
                }
                Collections.sort(array_type);
                String t = "";
                for (Object o : array_type){
                    if (t.matches("")){
                        t = o.toString();
                    }
                    else {
                        t = t + ", " + o.toString();
                    }
                }
                t = "Type : " + t.substring(0,1).toUpperCase() + t.substring(1);
                tType.setText(t);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}