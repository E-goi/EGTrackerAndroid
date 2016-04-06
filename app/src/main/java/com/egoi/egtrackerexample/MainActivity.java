package com.egoi.egtrackerexample;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.egoi.egtracker.EGTracker;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Track custom events
        Button trackButton = (Button) findViewById(R.id.trackAction);
        trackButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.tfAction);
                String action = editText.getText().toString();

                String message;

                if (action.length() > 0) {
                    EGTracker.sharedInstance().trackEvent(action);
                    message = "Event tracked!";
                } else {
                    message = "Please enter the action to track!";
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("E-Goi Tracker");
                builder.setMessage(message);
                builder.setCancelable(true);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        // Init the framework
        EGTracker.sharedInstance().initEngine(MainActivity.this);
        EGTracker.sharedInstance().trackEvent("APP_OPEN");
    }
}
