package com.plant.toyapp;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class CountDownView extends View {

  public CountDownView(Context context) {
    super(context);
  }

  public CountDownView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public CountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  @Override protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
  }
}
