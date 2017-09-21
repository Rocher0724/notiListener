package com.example.visualcamp.notilistener2;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class MyService extends NotificationListenerService implements Observer {

  private Server server;


  public MyService() {
  }

  @Override
  public void onCreate() {
    Log.e("service" , " onCreate");
    super.onCreate();
    initObserver();
  }

  private void initObserver() {
    server = Server.getInstance();
    server.addObserver(this);
  }


  @Override
  public IBinder onBind(Intent intent) {
    Log.e("service" , " onBind");
    // TODO: Return the communication channel to the service.
    return super.onBind(intent);
  }

  @Override
  public void onListenerConnected() {
    super.onListenerConnected();
    Log.e("service" , " onListenerConnected");
  }

  @Override
  public void onNotificationPosted(StatusBarNotification sbn) {
//    super.onNotificationPosted(sbn);
    Log.e("NotificationListener", "[snowdeer] onNotificationPosted() - " + sbn.toString());
    Log.e("NotificationListener", "[snowdeer] PackageName:" + sbn.getPackageName());
    Log.e("NotificationListener", "[snowdeer] PostTime:" + sbn.getPostTime());

    Notification notificatin = sbn.getNotification();
    Bundle extras = notificatin.extras;
    String title = extras.getString(Notification.EXTRA_TITLE);
    int smallIconRes = extras.getInt(Notification.EXTRA_SMALL_ICON);
    Bitmap largeIcon = ((Bitmap) extras.getParcelable(Notification.EXTRA_LARGE_ICON));
    CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT);
    CharSequence subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
//    super.onNotificationPosted(sbn);


    Data data = new Data(sbn.getPackageName(), sbn.getPostTime(), title, text + "");

    Log.e("NotificationListener", "[snowdeer] Title:" + title);
    Log.e("NotificationListener", "[snowdeer] Text:" + text);
    Log.e("NotificationListener", "[snowdeer] Sub Text:" + subText);


    server.notification(data);
  }



  @Override
  public void onNotificationRemoved(StatusBarNotification sbn) {
    Log.i("NotificationListener", "[snowdeer] onNotificationRemoved() - " + sbn.toString());

  }

  @Override
  public void notification(Data data) {

  }

  public static final String VOICE_TRANSCRIPTION_MESSAGE_PATH = "/voice_transcription";


}
