package com.plant.toyapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

public class SplashActivity extends AppCompatActivity {

  private long totalMills = 3000;
  private long countInterval = 1000;
  private CountDownTimer mCountDownTimer = new CountDownTimer(totalMills,countInterval) {
    @Override public void onTick(long millisUntilFinished) {

    }

    @Override public void onFinish() {
      next();
    }
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setTheme(R.style.SplashTheme_In);
    setContentView(R.layout.activity_splash);
  }

  @Override protected void onResume() {
    super.onResume();
    mCountDownTimer.start();
  }

  private void next(){
    startActivity(new Intent(this,MainActivity.class));
    finish();
  }

  /**
   * 开屏页防止用户退出
   */
  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
    return keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME || super.onKeyDown(
        keyCode, event);
  }
}
