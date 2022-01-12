package com.example.myimag_sql;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView text_time;
    Button btn_settime,btn_settimea,btn_sopta;
    MaterialTimePicker picker;
    Calendar calendar;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );


        createNotificationChannle();
        text_time=findViewById( R.id.text_time);
        btn_settime=findViewById( R.id.btn_settime );
        btn_settimea=findViewById( R.id.btn_settimea );
        btn_sopta=findViewById( R.id.btn_sopta );
        btn_settime.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    showtime();
            }
        } );
        btn_settimea.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setalarm();

            }
        } );
        btn_sopta.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cancelAlarm();
            }
        } );

    }

    private void cancelAlarm() {
        Intent intent=new Intent(this,AlarmReceiver.class);

        pendingIntent=PendingIntent.getBroadcast( this,0,intent,0 );
        if(alarmManager==null){

            alarmManager=(AlarmManager) getSystemService( Context.ALARM_SERVICE );
        }
        alarmManager.cancel(pendingIntent);
        Toast.makeText( MainActivity.this, "Alarm Cancelled", Toast.LENGTH_SHORT ).show();
    }

    private void setalarm() {
        alarmManager=(AlarmManager) getSystemService( Context.ALARM_SERVICE );

        Intent intent=new Intent(this,AlarmReceiver.class);

        pendingIntent=PendingIntent.getBroadcast( this,0,intent,0 );
        alarmManager.setInexactRepeating( AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        Toast.makeText( MainActivity.this, "Alarm set Successfully", Toast.LENGTH_SHORT ).show();
    }

    private void showtime() {
        picker =new MaterialTimePicker.Builder()
                .setTimeFormat( TimeFormat.CLOCK_12H)
                .setHour( 12 )
                .setMinute( 0 )
                .setTitleText( "Select Alarm Time" )
                .build();

        picker.show( getSupportFragmentManager(),"foxAndroid" );
        picker.addOnPositiveButtonClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (picker.getHour()>12){
                    text_time.setText( String.format( "%02d",(picker.getHour()-12)+":"+String.format("%02d",picker.getMinute())+" PM" ));

                }else {
                    text_time.setText(picker.getHour()+":"+picker.getMinute()+" AM" );
                }
                calendar=Calendar.getInstance();
                calendar.set( Calendar.HOUR_OF_DAY,picker.getHour() );
                calendar.set( Calendar.MINUTE,picker.getMinute() );
                calendar.set( Calendar.SECOND,0 );
                calendar.set( Calendar.MILLISECOND,0 );
            }
        } );
    }

    private void createNotificationChannle() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            CharSequence name="foxAndroidRemiderChannle";
            String description="Channle for Alarm m";
            int importance= NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel=new NotificationChannel("foxAndroid", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager=getSystemService( NotificationManager.class );
            notificationManager.createNotificationChannel(channel);
        }
    }

}