package com.example.visualcamp.notilistener2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by visualcamp on 2017. 9. 1..
 */

public class Server {

  private static Server instance;
  List<Observer> observers;

  private Server() {
    observers = new ArrayList<>();
  }

  public static Server getInstance() {
    if (instance == null) {
      instance = new Server();
    }
    return instance;
  }

  public void addObserver(Observer observer) {
    observers.add(observer);
  }

  public void notification(Data data) {
    for (Observer observer : observers) {
      observer.notification(data);
    }
  }


}
