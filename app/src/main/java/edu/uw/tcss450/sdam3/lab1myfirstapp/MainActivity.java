package edu.uw.tcss450.sdam3.lab1myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    //Adding Comment to test push/pull by lmc56.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MAINACTIVITY", "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("MAINACTIVITY", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MAINACTIVITY", "onResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("MAINACTIVITY", "onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("MAINACTIVITY", "onStop");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("MAINACTIVITY", "onRestart");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MAINACTIVITY", "onDestroy");

    }

}