package com.streamliners.intentsplayground;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.streamliners.intentsplayground.Constants;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;

import com.streamliners.intentsplayground.databinding.ActivityMainBinding
        ;

public class MainActivity extends AppCompatActivity {

    private int qty = 0;
    private ActivityMainBinding b;
    private int minValue;
    private int maxValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding .inflate(getLayoutInflater());
        setContentView(b.getRoot());
        setupEventHandlers();

        getInitialCount();

        Toast.makeText(this, "Hello World!", Toast.LENGTH_SHORT).show();
    }

    private void getInitialCount() {

        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            return;
        }

        //get data from intent
        qty = bundle.getInt(Constants.INITIAL_COUNT, 0);
        minValue = bundle.getInt(Constants.MIN_VALUE, Integer.MIN_VALUE);
        maxValue = bundle.getInt(Constants.MAX_VALUE,Integer.MAX_VALUE);
        b.qty.setText(String.valueOf(qty));


        if (qty != 0){
            b.sendBack.setVisibility(View.VISIBLE);
        }
    }

    private void setupEventHandlers(){
        b.incBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incQty();
            }
        });
    }

    public void decQty(View view) {
        b.qty.setText("" + --qty);
    }

    public void incQty() {
        //TextView qtyTv = findViewById(R.id.qty);
        b.qty.setText("" + ++qty);

    }

    public void sendCount(View view) {
        //validate count
        if (qty >= minValue && qty <= maxValue){

            //send back data
            Intent intent = new Intent();
            intent.putExtra(Constants.FINAL_VALUE, qty);
            setResult(RESULT_OK, intent);

            //close the activity
            finish();
        }
    }
}