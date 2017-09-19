package com.example.visualcamp.notilistener2;

/**
 * Created by visualcamp on 2017. 9. 19..
 */

public class Data {

  String packageName;
  long postTime;
  String name;
  String content;

  public Data(String packageName, long postTime, String name, String content) {
    this.packageName = packageName;
    this.postTime = postTime;
    this.name = name;
    this.content = content;
  }
}
