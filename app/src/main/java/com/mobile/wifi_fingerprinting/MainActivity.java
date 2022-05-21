package com.mobile.wifi_fingerprinting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.HttpResponse;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button scanButton;
    private Button sendButton;

    private WifiManager wifiManager;
    private ListView listView;
    private List<ScanResult> scanResultList;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter adapter;

    private String URL = "server.chromato99.com/test";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanButton = findViewById(R.id.scanBtn);
        sendButton = findViewById(R.id.sendBtn);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanWifi();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result;
                Post post = new Post();
                result = post.POST(URL);
                Log.d("test12",result);
            }
        });

        listView = findViewById(R.id.wifiList);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);
    }

    private void scanWifi() {
        arrayList.clear();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
    }

    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            scanResultList = wifiManager.getScanResults();
            unregisterReceiver(this);

            for (ScanResult scanResult : scanResultList) {
                arrayList.add(scanResult.SSID + "\n" + scanResult.BSSID + "\n" + scanResult.level);
                adapter.notifyDataSetChanged(); // listview로 보낼 정보 들습득 + adapter로 연결

            }
        }
    };}