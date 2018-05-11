package com.example.user.tutorialjson;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button yearsButton;
    Button provincesButton;
    public static TextView data;
    public static ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        yearsButton = findViewById(R.id.yearsButton);
        provincesButton = findViewById(R.id.provincesButton);
        listView = findViewById(R.id.z);

        FetchData process = new FetchData();
        process.listView = listView;
        process.provincesButton = provincesButton;
        process.yearsButton = yearsButton;
        process.context = MainActivity.this;
        process.execute();
    }
}
