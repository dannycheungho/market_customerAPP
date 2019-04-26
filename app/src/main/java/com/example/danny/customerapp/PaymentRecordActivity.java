package com.example.danny.customerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Danny on 13/3/2018.
 */

public class PaymentRecordActivity  extends AppCompatActivity {
    private TextView record;
    private String username;
    private String[] dataarray = new String[99];
    private List<String> unindexedVectors = new ArrayList<String>();
    private int vvv = 0;
    private int zzz = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        record = (TextView)findViewById(R.id.record);
        final String name = username;
        // Initialize  AsyncLogin() class with email and password
        new PaymentRecordActivity.AsyncLogin().execute(name);




    }

    @Override
    public Intent getSupportParentActivityIntent() {
        finish();
        return null;
    }


    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(PaymentRecordActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://192.168.25.142/test/payrecord.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0])
                        ;

                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {

                           result.append(line + " \n ");


                    }
                  //  result.append ( data[0] + " " + data[1]);
                    // Pass data to onPostExecute method
                    return (result.toString() ) ;

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();

            if (result.equalsIgnoreCase("true")) {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                record.setText(result);

                PaymentRecordActivity.this.finish();

            } else if (result.equalsIgnoreCase("false")) {

                // If username and password does not match display a error message
                Toast.makeText(PaymentRecordActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(PaymentRecordActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            } else {
                String[] tokens = result.split("%");

                for (String token : tokens) {
                    if ( unindexedVectors.size() % 8 == 0 )
                        zzz++;
                    String[] regex = {"id : ", "customer_ ", "time : ", "product_ ", "quantity : ", "price : ", "state : ", "true"};
                    for (int v = 0; v < regex.length; v++) {

                        token = replaceChar(token, regex[v]);
                    }

                    unindexedVectors.add(token);
                    vvv++;
                    String zv2 = Integer.toString(zzz);

                }

                LinearLayout linearLayout = new LinearLayout(PaymentRecordActivity.this);
                setContentView(linearLayout);
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(14, 18, 0, 0);

                for ( int num = 0 ; num < zzz ; num++) {
                    TextView textView = new TextView(PaymentRecordActivity.this);
                    textView.setText( unindexedVectors.get(3+num*7) );
                    textView.setWidth(1500);
                    textView.setHeight(80);
                    linearLayout.addView(textView, params);
                    TextView textView2 = new TextView(PaymentRecordActivity.this);
                    textView2.setBackgroundResource(R.color.transparent_background);
                    textView2.setWidth(9999);
                    textView2.setHeight(4);
                    linearLayout.addView(textView2);
                    TextView textView3 = new TextView(PaymentRecordActivity.this);
                    textView3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.logo123, 0, 0, 0);
                    textView3.setText("      價錢   " + unindexedVectors.get(6+num*7)+ "     數量  " + unindexedVectors.get(5+num*7));
                    textView3.setWidth(1099990);
                    textView3.setHeight(115);
                    linearLayout.addView(textView3, params);
                    TextView textView4 = new TextView(PaymentRecordActivity.this);
                    textView4.setBackgroundResource(R.color.transparent_background);
                    textView4.setWidth(9999);
                    textView4.setHeight(4);
                    linearLayout.addView(textView4);

                }



            }
        }

        public String replaceChar(String str,String target){
            String result = str.replaceAll(target, " ");
            return result;
        }



    }


}
