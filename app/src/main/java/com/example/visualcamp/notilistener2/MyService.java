package com.example.visualcamp.notilistener2;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import com.example.visualcamp.notilistener2.data.Data;
import com.example.visualcamp.notilistener2.data.Post;
import com.example.visualcamp.notilistener2.data.Store;
import io.realm.Realm;
import java.util.Arrays;
import java.util.List;

public class MyService extends NotificationListenerService implements Observer {

  private static final String TAG = "MyService";
  private Server server;
//  Data data;
  Store store;
  Post post;
  Realm realm;

  public MyService() {
  }

  @Override
  public void onCreate() {
    Log.e("service" , " onCreate");
    super.onCreate();
    initObserver();
    realmInit();
  }

  private void realmInit() {
    Realm.init(this);
    realm = Realm.getDefaultInstance();
    Log.e("realmInit","렘시작");
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
    Toast.makeText(this, "연결되었습니다.", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onNotificationPosted(StatusBarNotification sbn) {
//    super.onNotificationPosted(sbn);
    Log.e("NotificationListener", "[snowdeer] onNotificationPosted() - " + sbn.toString());
    Log.e("NotificationListener", "[snowdeer] PackageName:" + sbn.getPackageName());
    Log.e("NotificationListener", "[snowdeer] PostTime:" + sbn.getPostTime());

    Notification notification = sbn.getNotification();

    NotificationCompat.WearableExtender wearableExtender
        = new NotificationCompat.WearableExtender(notification);

    Bundle extras = notification.extras;
    String title = extras.getString(Notification.EXTRA_TITLE);
    int smallIconRes = extras.getInt(Notification.EXTRA_SMALL_ICON);
    Bitmap largeIcon = ((Bitmap) extras.getParcelable(Notification.EXTRA_LARGE_ICON));
    CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT);
    CharSequence subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
//    super.onNotificationPosted(sbn);
    List<Notification> pages = wearableExtender.getPages();


    post = new Post();
    post.packageName = sbn.getPackageName();
    post.postTime = sbn.getPostTime();
    post.name = title;
    post.content = text + "";
    store = new Store();
    store.pIntent = notification.contentIntent;
    store.bundle = notification.extras;
    store.pages.addAll(pages);

    List<NotificationCompat.Action> actions = wearableExtender.getActions();
    for(NotificationCompat.Action act : actions) {
      if(act != null && act.getRemoteInputs() != null) {
        store.remoteInputs.addAll(Arrays.asList(act.getRemoteInputs()));
      }
    }

    Log.e("NotificationListener", "[snowdeer] Title:" + title);
    Log.e("NotificationListener", "[snowdeer] Text:" + text); // 텍스트가 본문임.
    Log.e("NotificationListener", "[snowdeer] Sub Text:" + subText);

//    server.notification(data);

    String message = judgeText(text+"");
    if (message != null) {
      sendMessage(message);
    }
  }

  private String judgeText(String text) {
    if (text.startsWith("::")) { //  입력부
      final String[] qna = text.split("::");
      if(qna.length == 3) {
        Data data1 = realm.where(Data.class)
            .equalTo("question", qna[1])
            .findFirst();
        if (data1 == null) { // 입력값이 db에 없다.
          realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
              Data data = realm.createObject(Data.class);
              data.setQuestion(qna[1]);
              data.setAnswer(qna[2]);
            }
          });
        } else { // 입력값이 db에 있다.
          realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
              Data data = realm.where(Data.class)
                  .equalTo("question", qna[1])
                  .findFirst();
              data.setAnswer(qna[2]);
            }
          });
        }
        return null;

      } else { // 실패한 입력이다.
        return "[충열봇] 입력이 실패하였습니다.";
      }
    } else { // 출력부
      if (text.equals("이충열") ||
          text.equals("이충렬") ||
          text.equals("충렬") ||
          text.equals("충열") ||
          text.equals("충열이형") ||
          text.equals("충형") ||
          text.equals("충렬이형") ||
          text.equals("이충랼") ||
          text.equals("충열님") ||
          text.equals("충렬님") ||
          text.equals("충님")) {
        return " 주인님은 늘 새로워! 짜릿해! 최고야!";
      }

      if (text.equals("충열봇")) {
        return "이시대 최고의 카톡봇";
      }
      Data data1 = realm.where(Data.class)
          .equalTo("question", text)
          .findFirst();
      if (data1 == null) {
        return null;
      } else {
        String answer = data1.getAnswer();
        return answer;
      }
    }
  }

  private void sendMessage(String message) {
    RemoteInput[] remoteInputs = new RemoteInput[store.remoteInputs.size()];

    Intent localIntent = new Intent();
    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    Bundle localBundle = store.bundle;
    int i = 0;
    for(RemoteInput remoteIn : store.remoteInputs){
//            getDetailsOfNotification(remoteIn);
      remoteInputs[i] = remoteIn;
      localBundle.putCharSequence(remoteInputs[i].getResultKey(), "[충열봇]" + message);//This work, apart from Hangouts as probably they need additional parameter (notification_tag?)
      i++;
    }

    RemoteInput.addResultsToIntent(remoteInputs, localIntent, localBundle);
    try {
      store.pIntent.send(this, 0, localIntent);
    } catch (PendingIntent.CanceledException e) {
      Log.e(TAG, "replyToLastNotification error: " + e.getLocalizedMessage());
    }
  }


  @Override
  public void onNotificationRemoved(StatusBarNotification sbn) {
    Log.i("NotificationListener", "[snowdeer] onNotificationRemoved() - " + sbn.toString());

  }

  @Override
  public void notification(Data data) {

  }

  public static final String VOICE_TRANSCRIPTION_MESSAGE_PATH = "/voice_transcription";

  @Override
  public void onDestroy() {
    Log.e(TAG,"onDestroy");
    super.onDestroy();

  }
  public class MyBinder extends Binder {
    public MyService getService(){
      return MyService.this;
    }
  }
}
