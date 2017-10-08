package com.example.visualcamp.notilistener2.data;

import io.realm.RealmObject;

/**
 * Created by visualcamp on 2017. 9. 19..
 */

public class Data extends RealmObject{

  public String question;
  public String answer;

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }
}
