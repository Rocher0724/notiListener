package com.example.visualcamp.notilistener2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.visualcamp.notilistener2.data.Data;
import io.realm.Realm;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements Observer {

  private static final String TAG = "MainActivity";
  private Server server;
  Button btn, btn2;
  Data data;
  Realm realm;
  EditText et1, et2, et3;
  private boolean serviceBound;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    boolean isPermissionAllowed = isNotiPermissionAllowed();

    if (!isPermissionAllowed) {
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

  public void init() {
    et1 = (EditText) findViewById(R.id.et1);
    et2 = (EditText) findViewById(R.id.et2);
    et3 = (EditText) findViewById(R.id.et3);
    btn = (Button) findViewById(R.id.button);
    btn2 = (Button) findViewById(R.id.button2);
    btn.setOnClickListener(clickListener);
    btn2.setOnClickListener(clickListener);

    Realm.init(this);
    realm = Realm.getDefaultInstance();
  }

  // 알림설정 권한을 내앱이 갖고있는지 아닌지 체크하는 메소드
  private boolean isNotiPermissionAllowed() {
    Set<String> notiListenerSet = NotificationManagerCompat.getEnabledListenerPackages(this);
    String myPackageName = getPackageName();

    for (String packageName : notiListenerSet) {
      if (packageName == null) {
        continue;
      }
      if (packageName.equals(myPackageName)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public void notification(Data data) {
//    this.data = data;
//    Toast.makeText(this, "data is updated", Toast.LENGTH_SHORT).show();

  }

  View.OnClickListener clickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      Data data;
//      Realm realm = Realm.getDefaultInstance();
//      Realm.init(MainActivity.this);
      switch (view.getId()) {
        case R.id.button:

          // 기존에 있는 데이터인지 확인
          data = realm.where(Data.class)
              .equalTo("question", et1.getText().toString())
              .findFirst();

          if (data == null) {
            realm.executeTransaction(new Realm.Transaction() {
              @Override
              public void execute(Realm realm) {
                Data data = realm.createObject(Data.class);
                data.setQuestion(et1.getText().toString());
                data.setAnswer(et2.getText().toString());
              }
            });
            Toast.makeText(MainActivity.this, "저.장.", Toast.LENGTH_SHORT).show();
          } else {
            realm.executeTransaction(new Realm.Transaction() {
              @Override
              public void execute(Realm realm) {
                Data data = realm.where(Data.class)
                    .equalTo("question", et1.getText().toString())
                    .findFirst();
                data.setAnswer(et2.getText().toString());
              }
            });
            Toast.makeText(MainActivity.this, "변.경.저.장.", Toast.LENGTH_SHORT).show();
          }

          break;
        case R.id.button2:
//          RealmResults<Data> data11 = realm.where(Data.class).contains("question","").findAll();
          data = realm.where(Data.class)
              .equalTo("question", et3.getText().toString())
              .findFirst();

          if (data == null) {
            Toast.makeText(MainActivity.this, "해당하는 답변이 없습니다.", Toast.LENGTH_SHORT).show();
          } else {
            String answer = data.getAnswer();
            Toast.makeText(MainActivity.this, "답변 : " + answer, Toast.LENGTH_SHORT).show();

          }
          break;
      }
    }
  };
}

