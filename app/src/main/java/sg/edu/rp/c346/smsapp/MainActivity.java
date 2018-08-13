package sg.edu.rp.c346.smsapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText etNum;
    EditText etContent;
    Button btnSend;
    Button btnSendMsg;
    ArrayList<String> alNum;

    BroadcastReceiver br = new MessageReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        etNum = findViewById(R.id.editTextTo);
        etContent = findViewById(R.id.editTextContext);
        btnSend = findViewById(R.id.buttonSend);
        btnSendMsg = findViewById(R.id.buttonSendMsg);
        alNum = new ArrayList<>();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(br, filter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] numList = etNum.getText().toString().split(",");

                //String num = etNum.getText().toString();
                String content = etContent.getText().toString();
                SmsManager smsManager = SmsManager.getDefault();
                for (String num : numList){
                    smsManager.sendTextMessage(num, null, content, null, null);
                }


            }
        });
        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] numList = etNum.getText().toString().split(",");
                //String num = etNum.getText().toString();
                String content = etContent.getText().toString();
                for (String num : numList) {
                    Intent messageIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", num , content));
                    startActivity(messageIntent);
                }

            }
        });
    }
    private void checkPermission() {
        int permissionSendSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int permissionRecvSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);
        if (permissionSendSMS != PackageManager.PERMISSION_GRANTED &&
                permissionRecvSMS != PackageManager.PERMISSION_GRANTED) {
            String[] permissionNeeded = new String[]{Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS};
            ActivityCompat.requestPermissions(this, permissionNeeded, 1);
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        this.unregisterReceiver(br);
    }
}
