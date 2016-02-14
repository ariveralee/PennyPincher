package com.example.ariveralee.pennypincher;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.EditText;

public class MyActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.ariveralee.pennypincher.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void calculatePrice(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
       /* We need to grab the information from the text input */
        EditText itemPrice = (EditText) findViewById(R.id.enter_price);
        EditText discount = (EditText) findViewById(R.id.enter_discount);

        /* input is entered to be stored as a string */
        String itemPriceString = itemPrice.getText().toString();
        String discountString = discount.getText().toString();
        /* parse the string into a double. */
        double item_price = Double.parseDouble(itemPriceString);
        double discount_amount = Double.parseDouble(discountString);

        double new_price = calculateDiscount(item_price, discount_amount);
        Bundle b = new Bundle();
        b.putDouble("NEW_PRICE", new_price);
        intent.putExtras(b);
        startActivity(intent);


    }

    public double calculateDiscount(double item_price, double discount_amount) {
        double fullPercentage = 100;

        fullPercentage -= discount_amount;

        item_price = (item_price * fullPercentage) / 100;

        return item_price;

    }
}
