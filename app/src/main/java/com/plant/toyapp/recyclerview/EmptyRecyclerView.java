package com.plant.toyapp.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * https://github.com/wasabeef/recyclerview-animators
 */
public class EmptyRecyclerView extends RecyclerView {

  private View mEmptyView;
  private AdapterDataObserver mDataObserver = new AdapterDataObserver() {
    @Override public void onChanged() {
      Adapter adapter = getAdapter();
      if (adapter.getItemCount() == 0){
        mEmptyView.setVisibility(VISIBLE);
        EmptyRecyclerView.this.setVisibility(GONE);
      }else {
        mEmptyView.setVisibility(GONE);
        EmptyRecyclerView.this.setVisibility(VISIBLE);
      }
    }

    @Override public void onItemRangeChanged(int positionStart, int itemCount) {
      onChanged();
    }

    @Override public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
      onChanged();
    }

    @Override public void onItemRangeInserted(int positionStart, int itemCount) {
      onChanged();
    }

    @Override public void onItemRangeRemoved(int positionStart, int itemCount) {
      onChanged();
    }

    @Override public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
      onChanged();
    }
  };

  public EmptyRecyclerView(Context context) {
    super(context);
  }

  public EmptyRecyclerView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public EmptyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void setEmptyView(View view){
    this.mEmptyView = view;
    ((ViewGroup)getRootView()).addView(mEmptyView);
  };

  public void setAdapter(RecyclerView.Adapter adapter){
    super.setAdapter(adapter);
    adapter.registerAdapterDataObserver(mDataObserver);
    mDataObserver.onChanged();
  }
}
