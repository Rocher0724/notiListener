package com.example.visualcamp.notilistener2.data;

import android.app.Notification;
import android.app.PendingIntent;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;
import java.util.ArrayList;

/**
 * Created by visualcamp on 2017. 10. 7..
 */

public class Store {
  public PendingIntent pIntent;
  public Bundle bundle;
  public ArrayList<Notification> pages = new ArrayList<>();
  public ArrayList<RemoteInput> remoteInputs = new ArrayList<>();
}
