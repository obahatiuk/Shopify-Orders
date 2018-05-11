package com.example.user.tutorialjson;
import android.content.Context;
import android.os.AsyncTask;
import android.text.InputType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


public class FetchData extends  AsyncTask<Void, Void, Void> {
    String data = "";
    String dataParsed = "";
    String singleParsed = "";
    List<Order> ordersList = new ArrayList<>();
    ArrayList<String> provinces = new ArrayList<String>();
    ArrayList<String> years = new ArrayList<String>();
    HashMap<String, List<Order>> ordersProvinceMap = new HashMap<String, List<Order>>();
    HashMap<String, List<Order>> ordersYearMap = new HashMap<String, List<Order>>();
    ListView listView;
    Button yearsButton;
    Button provincesButton;
    Context context;


    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("https://shopicruit.myshopify.com/admin/orders.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6");

            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    data += line;
                }

                try {

                    JSONObject jsonObj = new JSONObject(data);

                    JSONArray orders = jsonObj.getJSONArray("orders");

                    for (int i = 0; i < orders.length(); i++) {
                        Order order = new Order();
                        User user = new User();
                        Address billingAddress = new Address();


                        JSONObject jsonObject = orders.getJSONObject(i);

                        if (jsonObject.has("id")) {

                            order.id = jsonObject.getInt("id");
                        }

                        if (jsonObject.has("created_at")) {
                            order.created_at = jsonObject.getString("created_at");
                        }

                        if (jsonObject.has("user_id") && !jsonObject.isNull("user_id")) {

                            user.user_id = jsonObject.getInt("user_id");

                        }

                        if (jsonObject.has("email")) {
                            user.email = jsonObject.getString("email");
                        }


                        if (jsonObject.has("billing_address")) {
                            JSONObject jsonObjBilling_address = jsonObject.getJSONObject("billing_address");

                            if (jsonObjBilling_address.has("province")) {

                                billingAddress.province = jsonObjBilling_address.getString("province");
                                String province = jsonObjBilling_address.getString("province") + "\n";//jsonObjBilling_address.getString("jsonObjBilling_address");
                                dataParsed += billingAddress.province;

                            }

                            if (jsonObjBilling_address.has("name")) {

                                user.name = jsonObjBilling_address.getString("name");

                            }


                        }

                        order.user = user;
                        order.billingAddress = billingAddress;

                        ordersList.add(order);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);


        for (Order order : ordersList) {
            // Check if ordersMap contains key for this order
            if (ordersProvinceMap.containsKey(order.billingAddress.province)) {
                List<Order> list = ordersProvinceMap.get(order.billingAddress.province);
                list.add(order);
            } else {
                List<Order> newList = new ArrayList<>();
                newList.add(order);
                ordersProvinceMap.put(order.billingAddress.province, newList);
            }

        }

        for (Order order : ordersList) {
            // Check if ordersMap contains key for this order
            if (ordersYearMap.containsKey(order.created_at.substring(0, 4))) {
                List<Order> list = ordersYearMap.get(order.created_at.substring(0, 4));
                list.add(order);
            } else {
                List<Order> newList = new ArrayList<>();
                newList.add(order);
                ordersYearMap.put(order.created_at.substring(0, 4), newList);
            }

        }


        for (String key : ordersProvinceMap.keySet()) {
            if (null != key) {
                provinces.add(key + " : " + ordersProvinceMap.get(key).size());
            }
        }

        for (String key : ordersYearMap.keySet()) {
            if (null != key) {
                years.add(key + " : " + ordersYearMap.get(key).size());
            }
        }
        Collections.sort(provinces);
        Collections.sort(years);
        ArrayAdapter adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, years);
        listView.setAdapter(adapter);
        ((ArrayAdapter) listView.getAdapter()).notifyDataSetChanged();

        yearsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayAdapter adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, years);
                listView.setAdapter(adapter);
                ((ArrayAdapter) listView.getAdapter()).notifyDataSetChanged();

            }


        });

        provincesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayAdapter adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, provinces);
                listView.setAdapter(adapter);
                ((ArrayAdapter) listView.getAdapter()).notifyDataSetChanged();


            }


        });
    }
}
