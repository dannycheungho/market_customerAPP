package com.example.danny.customerapp;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SuccessActivity extends AppCompatActivity {
    /** Called when the activity is first created. */
    private Button Logout;
    private TextView textView;
    private String username;
    private int enterno = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);
        Logout = (Button)findViewById(R.id.Logout);
            Intent it = getIntent();
            username = it.getStringExtra("username");



        GridView gridview = (GridView) findViewById(R.id.GridView);

        ArrayList<HashMap<String, Object>> meumList = new ArrayList<HashMap<String, Object>>();
        int[] itemimage = {R.drawable.balance,R.drawable.profile,R.drawable.payment,R.drawable.trade,R.drawable.promotion,R.drawable.card};
        for(int i = 0;i < 6;i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", itemimage[i]);
            //  map.put("ItemText", ""+i);
            meumList.add(map);
        }
        SimpleAdapter saItem = new SimpleAdapter(this,
                meumList, //数据源
                R.layout.test, //xml实现
                new String[]{"ItemImage","ItemText"}, //对应map的Key
                new int[]{R.id.ItemImage});  //对应R的Id

        //添加Item到网格中
        gridview.setAdapter(saItem);
        //添加点击事件
        gridview.setOnItemClickListener(
                new OnItemClickListener()
                {
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3)
                    {
                        Intent intent;
                        int index=arg2+1;//id是从0开始的，所以需要+1
                        switch(index) {
                            case 1:
                                intent = new Intent( SuccessActivity.this, BalanceActivity.class );
                                intent.putExtra("username", username );
                                startActivity(intent);
                                break;
                            case 2:
                                intent = new Intent( SuccessActivity.this, ProfileActivity.class );
                                intent.putExtra("username", username );
                                startActivity(intent);
                                break;
                            case 3:
                                intent = new Intent( SuccessActivity.this, VaildRecordActivity.class );
                                intent.putExtra("username", username );
                                startActivity(intent);
                                break;
                            case 4:
                                intent = new Intent( SuccessActivity.this, TradeActivity.class );
                                intent.putExtra("username", username );
                                startActivity(intent);
                                break;
                            case 5:
                                intent = new Intent( SuccessActivity.this, Promotion.class );
                                startActivity(intent);
                                break;
                            case 6:
                                intent = new Intent( SuccessActivity.this, MemberShipCard.class );
                                intent.putExtra("username", username );
                                startActivity(intent);
                                break;

                        }
                        //     Toast.makeText(getApplicationContext(), "你按下了选项："+index, 1000).show();
                        //Toast用于向用户显示一些帮助/提示
                    }
                }



        );



    }


    public void Logout(View view) {
        Intent intent = new Intent( SuccessActivity.this, MainActivity.class );

        startActivity(intent);
    }



}