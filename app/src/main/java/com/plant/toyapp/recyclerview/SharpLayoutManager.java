package com.plant.toyapp.recyclerview;

import android.support.v7.widget.RecyclerView;

public class SharpLayoutManager extends RecyclerView.LayoutManager {

  @Override public RecyclerView.LayoutParams generateDefaultLayoutParams() {
    return null;
  }

  /**
   * 对RecyclerView进行布局的入口方法
   * @param recycler
   * @param state
   */
  @Override public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
    super.onLayoutChildren(recycler, state);
  }

  
}
