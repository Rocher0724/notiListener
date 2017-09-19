package com.example.visualcamp.notilistener2;

import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements Observer{

  private Server server;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    boolean isPermissionAllowed = isNotiPermissionAllowed();

    if(!isPermissionAllowed) {
      Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
      startActivity(intent);
    }

    initObserver();
//    startService(new Intent(this, NotificationCollectorMonitorService.class));

//    Intent serviceIntent = new Intent(this, MyService.class);
//    startService(serviceIntent);
  }

  private void initObserver() {
    server = Server.getInstance();
    server.addObserver(this);
  }

  // 알림설정 권한을 내앱이 갖고있는지 아닌지 체크하는 메소드
  private boolean isNotiPermissionAllowed() {
    Set<String> notiListenerSet = NotificationManagerCompat.getEnabledListenerPackages(this);
    String myPackageName = getPackageName();

    for(String packageName : notiListenerSet) {
      if(packageName == null) {
        continue;
      }
      if(packageName.equals(myPackageName)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public void notification(Data data) {
    // 여기서 데이터를 이용해먹자
  }
}