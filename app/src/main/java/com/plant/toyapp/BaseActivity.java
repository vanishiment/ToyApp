package com.plant.toyapp;

import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

  @Override public void onBackPressed() {
    super.onBackPressed();
    moveTaskToBack(true);//将包含此 Activity 的 Task 移到 Activity 任务栈的后面，类似 HOME 键操作。
  }
}
