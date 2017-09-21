package com.example.visualcamp.notilistener2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.drive.Drive;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements Observer,
    OnConnectionFailedListener {

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
    init();
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
    // 는 아마 별로 쓸모없을듯 서비스에서 다해먹어야하기 때문.
  }

  public void init () {
    GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
        .enableAutoManage(this /* FragmentActivity */,
            this /* OnConnectionFailedListener */)
        .addApi(Drive.API)
        .addScope(Drive.SCOPE_FILE)
        .build();

  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

  }
}