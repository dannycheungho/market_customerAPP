package com.example.danny.customerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class VaildRecordActivity extends AppCompatActivity {
    private TextView vaild;
    private String username,p_id;
    private double balance;
    private List<String> unindexedVectors = new ArrayList<String>();
    private List<String> unindexedVectors2 = new ArrayList<String>();
    private int vvv = 0;
    private int zzz = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaildrecord);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        vaild = (TextView)findViewById(R.id.vaild);
        final String name = username;
        // Initialize  AsyncLogin() class with email and password
        new VaildRecordActivity.AsyncLogin().execute(name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




    }

    @Override
    public Intent getSupportParentActivityIntent() {
        finish();
        return null;
    }


    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(VaildRecordActivity.this);
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
                url = new URL("http://192.168.25.124/test/vaild.php");

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
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

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

                vaild.setText(result);

                VaildRecordActivity.this.finish();

            } else if (result.equalsIgnoreCase("false")) {

                // If username and password does not match display a error message
                Toast.makeText(VaildRecordActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(VaildRecordActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            } else {
                String[] tokens = result.split("&");


       //         int count = Integer.valueOf( unindexedVectors.get( unindexedVectors.size()-1 )   );
                LinearLayout linearLayout = new LinearLayout(VaildRecordActivity.this);
                setContentView(linearLayout);
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(14, 18, 0, 0);

                for (String token : tokens) {
                    zzz++;
                    unindexedVectors.add(token);
                    //   unindexedVectors2.add(token);
                    vvv++;
           /*         TextView textView = new TextView(VaildRecordActivity.this);
                    textView.setText( token );
                    textView.setWidth(1500);
                    textView.setHeight(80);
                    linearLayout.addView(textView, params);
*/
                }

                int count = unindexedVectors.size()/ 7;

               for ( int num = 0 ; num <  count ; num++ ) {
                   double qun = Double.parseDouble( unindexedVectors.get( num*7+5 ) ) ;
                   double pri = Double.parseDouble( unindexedVectors.get( num*7+6 ) ) ;
                   p_id = unindexedVectors.get( num*7+4 );
                   balance = qun*pri;

                  //  price =  Integer.valueOf(unindexedVectors.get( num*7+5 )) *  Integer.valueOf(unindexedVectors.get( num*7+6 )) ;
                    TextView textView = new TextView(VaildRecordActivity.this);
                    textView.setText("付款   " +  "產品號碼" + unindexedVectors.get( num*7+4 ) +  "數量" + unindexedVectors.get( num*7+5 ) );
                    textView.setWidth(1500);
                    textView.setHeight(80);
                    linearLayout.addView(textView, params);
                    TextView textView2 = new TextView(VaildRecordActivity.this);
                    textView2.setBackgroundResource(R.color.transparent_background);
                    textView2.setWidth(9999);
                    textView2.setHeight(4);
                    linearLayout.addView(textView2);
                    TextView textView3 = new TextView(VaildRecordActivity.this);
                    textView3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.logo123, 0, 0, 0);
                    textView3.setText("  總價錢  :    " +  balance +  "      認證狀況  " +  unindexedVectors.get(num*7+7)  );
                    textView3.setWidth(1099990);
                    textView3.setHeight(115);
                    linearLayout.addView(textView3, params);

                    Button button1 = new Button(VaildRecordActivity.this);
                    button1.setGravity(30);
                    button1.setBackgroundColor(R.drawable.button_rounde3);
                    button1.setWidth(30);
                    button1.setText("線上付款");
                    button1.setHeight(60);
                    button1.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v)
                        {
                            //Toast.makeText(VaildRecordActivity.this, "Payment Fail, Please check your balance", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent( VaildRecordActivity.this, Test.class );
                            intent.putExtra("price", balance );
                            intent.putExtra("pid", p_id );
                            intent.putExtra("username", username );

                            startActivity(intent);
                        }
                    });
                    linearLayout.addView(button1);
                }

                Button button2 = new Button(VaildRecordActivity.this);
                button2.setGravity(30);
                button2.setWidth(30);
                button2.setText("查看交易記錄");
                button2.setHeight(60);
                button2.setBackgroundColor(R.drawable.button_rounde3);
                button2.setOnClickListener(new View.OnClickListener() {
                         public void onClick(View v)
                         {
                             Intent intent = new Intent( VaildRecordActivity.this, PaymentRecordActivity.class );
                             intent.putExtra("username", username );
                             startActivity(intent);
                         }
     });

                linearLayout.addView(button2);


            }
            }

        public Intent getSupportParentActivityIntent() {
            finish();
            return null;
        }
        }



}
