package com.example.ariveralee.pennypincher;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v7.app.AlertDialog;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MyActivity extends AppCompatActivity {
    // private class member
    private AlertDialog mAlertDialog;
    private static DecimalFormat mFormatter = new DecimalFormat("0.00");
    private FrameLayout mLayout;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // layout for the price
        mLayout = (FrameLayout) findViewById(R.id.content);
        mTextView = new TextView(this);
        mTextView.setTextSize(40);
        // instantiate a button for calculating the price
        Button priceButton = (Button) findViewById(R.id.price_button);
        priceButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get input and attempt to calculate price
                getInput(v);
                // if we get here, then we have incorrect input thus a dialog showing already
                if (mAlertDialog != null && mAlertDialog.isShowing()) {
                    return;
                }
            }
        });
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

    /**
     * This method obtains the input from the user once they have pressed the calculate price
     * button. Once the values have been parsed to Strings, the processInput method is called.
     *
     * @param view
     */
    public void getInput(View view) {

        // We need to grab the information from the text input
        EditText itemPrice = (EditText) findViewById(R.id.enter_price);
        EditText discount = (EditText) findViewById(R.id.enter_discount);
        // input is entered to be stored as a string
        String itemPriceString = itemPrice.getText().toString();
        String discountString = discount.getText().toString();
        // processes the strings to make sure the input is correct, then calls calculateDiscount
        processInput(itemPriceString, discountString);
    }

    /**
     * This method takes the resource id passed in from process input and prints an alert dialog
     * message. This allpws for a generic method to create dialog messages, reducing the amount
     * of code needed for different types of messages.
     *
     * @param resourceId
     */
    public void dialogCreator(int resourceId) {
        // Use Builder class for Convenient dialog construction
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MyActivity.this);
        // Resource ID is int, and it doesn't instantiate an object of String at Runtime, so we do it
        alertDialogBuilder.setMessage(this.getString(resourceId));

        mAlertDialog = alertDialogBuilder.create();
        mAlertDialog.show();
        // allows for centering of message in alert dialog
        TextView messageView = (TextView) mAlertDialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }

    /**
     * This method takes in the item price and the discount price strings and determines if the
     * input is correct. This is done by making sure that there is in fact numbers contained within
     * the string, if there isn't then it's incorrect and we pass the resourceId to dialogCreator
     * to print an alertDialog message.
     *
     * @param itemPriceString
     * @param discountString
     */
    public void processInput(String itemPriceString, String discountString) {
        // this checks to make sure that both strings are not empty and do not have a "." with no
        // numbers. If this is true for both of them, we return a messages for both inputs
        // others, we check them individually.
        if ((!itemPriceString.matches(".*\\d+.*")) && ((!discountString.matches(".*\\d+.*")))) {
            dialogCreator(R.string.incorrect_input);
            return;
        } else if (!itemPriceString.matches(".*\\d+.*")) {
            dialogCreator(R.string.incorrect_price);
            return;
        } else if (!discountString.matches(".*\\d+.*")) {
            dialogCreator(R.string.incorrect_discount);
            return;
        }
        // if we reach here, then it appears that we have correct input, let's check a few other things
        if (Double.parseDouble(discountString) > 100.00) {
            dialogCreator(R.string.over_100);
            return;
        }
        // formatting so we don't deal with anything past 2 decimal places
        NumberFormat formatter = new DecimalFormat("#0.00");
        // if we get here, then it's time to calculate dat discount
        double item_price = Double.parseDouble(itemPriceString);
        formatter.format(item_price);
        double discount_amount = Double.parseDouble(discountString);
        formatter.format(discount_amount);
        double new_price = calculateDiscount(item_price, discount_amount);

        // method to set the text view for the price.
        setPriceTextView(new_price);
    }

    /**
     * This function takes in the item price and discount amount and performs the necessary
     * calculations to give the user the discounted price.
     *
     * @param item_price
     * @param discount_amount
     * @return new item price reflecting discount
     */
    public double calculateDiscount(double item_price, double discount_amount) {
        double fullPercentage = 100.00;

        fullPercentage -= discount_amount;

        item_price = (item_price * fullPercentage) / 100.00;

        return item_price;

    }

    /**
     * This function takes the new price and attaches it to the text view object.
     *
     * @param new_price
     */
    public void setPriceTextView(double new_price) {
        // If we have a child count greater than 0 then there's a textview attached so we remove it
        // after, we attach our new view. This ensures that only one text view for the price is
        // attached at a time for this frame layout.
        if (mLayout.getChildCount() > 0) {
            mLayout.removeView(mTextView);
        }
        mTextView.setText("The new price is:\n$" + mFormatter.format(new_price));
        mTextView.setTextSize(24);
        mTextView.setTextColor(Color.parseColor("#00cc00"));
        mLayout.addView(mTextView);
        mTextView.setGravity(Gravity.CENTER);

    }
}
