package com.example.visualcamp.notilistener2;

import io.realm.RealmObject;

/**
 * Created by visualcamp on 2017. 10. 4..
 */

public class Qna extends RealmObject {

  public String question;

  public String answer;

  public Qna() {

  }

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
