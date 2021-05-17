package com.streamliners.intentsplayground;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.streamliners.intentsplayground.databinding.ActivityIntentsPlaygroundBinding;

public class IntentsPlayground extends AppCompatActivity {

    private static final int REQUEST_COUNT = 0;
    ActivityIntentsPlaygroundBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityIntentsPlaygroundBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        setTitle("Implicit Intents");
        setUpHideErrorForEdiText();
    }

    //Initial setup
    private void setUpHideErrorForEdiText() {
        TextWatcher myTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hideError();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };


        b.outlinedTextField.getEditText().addTextChangedListener(myTextWatcher);
            b.SendData.getEditText().addTextChangedListener(myTextWatcher);

    }

    //Event Handler
    public void OpenMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void SendImplicitIntent(View view) {
        //validate input
        String input = b.outlinedTextField.getEditText().getText().toString().trim();
        if (input.isEmpty()){
            b.outlinedTextField.setError("Please enter something!");
            return;
        }

        //validate implicit type
        int type = b.RadioGrp.getCheckedRadioButtonId();

        //handle implicit intent
        if (type == R.id.OpenWebPage)
            openWebPage(input);
        else if (type ==  R.id.dialNum)
            dialNumber(input);
        else if (type == R.id.shareText)
            shareText(input);
        else
            Toast.makeText(this, "Please choose a intent type", Toast.LENGTH_SHORT).show();
    }

    public void sendData(View view) {
        //validate input
        String input = b.SendData.getEditText().getText().toString().trim();
        if (input.isEmpty()){
            b.SendData.setError("Please enter something!");
            return;
        }

        //get count
        int initialCount = Integer.parseInt(input);

        //create intent
        Intent intent = new Intent(this, MainActivity.class);

       //Create bundle to pass
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.INITIAL_COUNT, initialCount);
        bundle.putInt(Constants.MIN_VALUE, -100);
        bundle.putInt(Constants.MAX_VALUE, 100);

        //pass Bundle
        intent.putExtras(bundle);

        startActivityForResult(intent, REQUEST_COUNT);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_COUNT && resultCode == RESULT_OK){

            //get data
            int count = data.getIntExtra(Constants.FINAL_VALUE, Integer.MIN_VALUE);

            //show data
            b.result.setText("Final count received: " + count);
            b.result.setVisibility(View.VISIBLE);
        }

        //show data

    }

    //Implicit intent sender
    private void shareText(String text) {
        Intent intent = new Intent(); intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text );
        startActivity(Intent.createChooser(intent, "Share text via"));
    }

    private void dialNumber(String number) {
        //check if input is Phone number
        if (!number.matches("^\\d{10}$")) {
            b.outlinedTextField.setError("Invalid NUMBER!");
            return;
        }

        Uri uri = Uri.parse("tel:" + number );
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        startActivity(intent);

        hideError();
    }

    private void openWebPage(String url) {
        //check if input is URL
        if (!url.matches("^(http?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~|!:,.;]*[-a-zA-Z0-9+&@#/%?=~_|]")) {
            b.outlinedTextField.setError("Invalid URL!");
            return;
        }

        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

        hideError();
    }

    //Utility function
    private void hideError(){
        b.outlinedTextField.setError(null);
    }


}

