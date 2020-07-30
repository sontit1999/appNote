package com.example.notesmasteer;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.notesmasteer.base.BaseActivity;
import com.example.notesmasteer.callback.CircleMenuCallback;
import com.example.notesmasteer.databinding.ActivityMainBinding;
import com.example.notesmasteer.service.AlarmReciever;
import com.example.notesmasteer.service.ForegroundService;
import com.example.notesmasteer.service.SoundService;
import com.hanks.passcodeview.PasscodeView;
import com.ramotion.circlemenu.CircleMenuView;

import java.util.Calendar;
import java.util.Random;

import me.adawoud.bottomsheettimepicker.BottomSheetTimeRangePicker;
import me.adawoud.bottomsheettimepicker.OnTimeRangeSelectedListener;


public class MainActivity extends BaseActivity<ActivityMainBinding,MainViewModel> implements CircleMenuCallback, OnTimeRangeSelectedListener {
    private static final String CHANNEL_ID = "id";
    NotificationCompat.Builder builder;
    private static final String CHANNLE_NAME = "Sondz";
    private static final String CHANNEL_DESCRIPTION = "hihihi" ;
    SharedPreferences preferences;
    NavController controller;
    private int notificationId = 123;

    @Override
    public Class<MainViewModel> getViewmodel() {
        return MainViewModel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public void setBindingViewmodel() {
        preferences = getSharedPreferences("sontit",MODE_PRIVATE);
        controller = Navigation.findNavController(MainActivity.this,R.id.nav_host_fragment);
       binding.setViewmodel(viewmodel);
       checkLogin();
       event();
    }

    private void createNotification() {

         builder = new NotificationCompat.Builder(this,"hicc")
                .setSmallIcon(R.drawable.ic_home_black_24dp)
                .setContentTitle("Demo notification")
                .setContentText("Đây là 1 thông báo đemo của sơn tít")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
         Toast.makeText(this, "Create notification", Toast.LENGTH_SHORT).show();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNLE_NAME;
            String description = CHANNEL_DESCRIPTION;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("idchannel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            Toast.makeText(this, "Create channel", Toast.LENGTH_SHORT).show();
        }
    }
    private void checkLogin() {
        Boolean isfirst = preferences.getBoolean("first",true);
        if(isfirst){
            preferences.edit().putBoolean("first",false).apply();
            binding.passcodeView.setPasscodeType(PasscodeView.PasscodeViewType.TYPE_SET_PASSCODE);
        }else{
            String localpass = preferences.getString("pass","9999");
            binding.passcodeView.setLocalPasscode(localpass);
            binding.passcodeView.setPasscodeType(PasscodeView.PasscodeViewType.TYPE_CHECK_PASSCODE);
        }
    }

    private void event() {

        binding.circleMenu.setEventListener(new CircleMenuView.EventListener() {
            @Override
            public void onMenuOpenAnimationStart(@NonNull CircleMenuView view) {
                Log.d("D", "onMenuOpenAnimationStart");
            }

            @Override
            public void onMenuOpenAnimationEnd(@NonNull CircleMenuView view) {
                Log.d("D", "onMenuOpenAnimationEnd");
            }

            @Override
            public void onMenuCloseAnimationStart(@NonNull CircleMenuView view) {
                Log.d("D", "onMenuCloseAnimationStart");
            }

            @Override
            public void onMenuCloseAnimationEnd(@NonNull CircleMenuView view) {
                Log.d("D", "onMenuCloseAnimationEnd");
            }

            @Override
            public void onButtonClickAnimationStart(@NonNull CircleMenuView view, int index) {

            }

            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuView view, int index) {
                Log.d("D", "onButtonClickAnimationStart| index: " + index);
                switch (index){
                    case 0 :
                        controller.navigate(R.id.AddFragment);
                        break;
                    case 1 :
                        Toast.makeText(MainActivity.this, "Voice search", Toast.LENGTH_SHORT).show();
                        alarm();
                        break;
                    case 2 :
                        controller.navigate(R.id.SettingFragment);
                        break;
                    case 3 :
                        //controller.popBackStack(R.id.HomeFragment,true);
                          BottomSheetTimeRangePicker bottomSheetTimeRangePicker = BottomSheetTimeRangePicker.Companion.newInstance(MainActivity.this, DateFormat.is24HourFormat(MainActivity.this));
                          bottomSheetTimeRangePicker.show(getSupportFragmentManager(),bottomSheetTimeRangePicker.getTag());
                          //showNotification(MainActivity.this,"Dậy thôi",new Random().nextInt() + " hic",intent);
                        break;
                    case 4 :
                        MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.alarm);
                        mp.start();
                        Toast.makeText(MainActivity.this, "PlaySound", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        binding.passcodeView.setVisibility(View.VISIBLE);
                        binding.passcodeView.setPasscodeType(PasscodeView.PasscodeViewType.TYPE_SET_PASSCODE);
                        break;
                }
            }
        });
        binding.passcodeView.setListener(new PasscodeView.PasscodeViewListener() {
            @Override
            public void onFail() {

            }

            @Override
            public void onSuccess(String number) {
                preferences.edit().putString("pass",number).apply();
                binding.passcodeView.setVisibility(View.GONE);
            }
        });
    }

    private void alarm() {
        Toast.makeText(this, "Repeat after 20s", Toast.LENGTH_SHORT).show();
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, AlarmReciever.class);
        alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

      // Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 16);
        calendar.set(Calendar.MINUTE, 53);

// setRepeating() lets you specify a precise custom interval--in this case,
// 20 minutes.
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 20, alarmIntent);
    }

    @Override
    public void hiddenMenuCircle(Boolean isHident) {
        if(isHident){
            binding.circleMenu.setVisibility(View.GONE);
        }else{
            binding.circleMenu.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if(controller.getCurrentDestination().getId()==R.id.HomeFragment){
            super.onBackPressed();
        }
    }
    public void showNotification(Context context, String title, String body, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_add_alarm_black_24dp)
                .setContentTitle(title)
                .setContentText(body).setOngoing(true).setPriority(NotificationCompat.PRIORITY_DEFAULT);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
        //startService(new Intent(MainActivity.this, SoundService.class));
    }

    @Override
    public void onTimeRangeSelected(int i, int i1, int i2, int i3) {
        Toast.makeText(this, i +" " + i1 + " " + i2 + " " + i3, Toast.LENGTH_SHORT).show();
        Calendar cal= Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, i);
        cal.set(Calendar.MINUTE, i1);
        cal.set(Calendar.SECOND, 0);

        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, AlarmReciever.class);
        alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,i);
        calendar.set(Calendar.MINUTE,i1);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            alarmMgr.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), alarmIntent);
        else
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), alarmIntent);
        Toast.makeText(this, "Đã đặt báo thức vào  " + i + ":" + i1 , Toast.LENGTH_SHORT).show();
    }
}
