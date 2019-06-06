package com.example.arach.pokedex;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Callback {

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    TextView textViewBack,num;
    String next;
    String isend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        editor = sp.edit();

        num = findViewById(R.id.next);
        textViewBack = findViewById(R.id.back);

        if (savedInstanceState == null) {
            editor.clear();
            editor.putString("State","Next");
            editor.putString("Next","https://pokeapi.co/api/v2/pokemon/");
            editor.apply();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, new FragmentListPokemon()).commit();
        }

        next = sp.getString("Next","0");

        if(!next.matches("0")){
            num.setText("Next");
            num.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.putString("State","Next");
                    editor.apply();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contentContainer, new FragmentListPokemon()).commit();
                    textViewBack.setText("Back");
                    isend = sp.getString("End","0");
                    Log.d("sssssssssss",isend);
                    if (!isend.matches("0")){
                        num.setText("");
                    }
                }
            });
            textViewBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.putString("State","Back");
                    editor.apply();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contentContainer, new FragmentListPokemon()).commit();
                }
            });
        }
    }

    @Override
    public void someEvent(Fragment fragment) {
        replaceFragment(fragment);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
