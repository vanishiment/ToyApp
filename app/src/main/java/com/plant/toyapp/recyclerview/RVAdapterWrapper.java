package com.plant.toyapp.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class RVAdapterWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private RecyclerView.Adapter mAdapter;
  private View mHeader;
  private View mFooter;

  enum ITEM_TYPE{
    HEADER,
    BODY,
    FOOTER
  }

  public RVAdapterWrapper(RecyclerView.Adapter adapter) {
    this.mAdapter = adapter;
  }

  @Override public int getItemViewType(int position) {
    if (position == 0){
      return ITEM_TYPE.HEADER.ordinal();
    }else if (position == mAdapter.getItemCount() + 1){
      return ITEM_TYPE.FOOTER.ordinal();
    }else {
      return ITEM_TYPE.BODY.ordinal();
    }
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == ITEM_TYPE.HEADER.ordinal()){
      return new RecyclerView.ViewHolder(mHeader){};
    }else if (viewType == ITEM_TYPE.FOOTER.ordinal()){
      return new RecyclerView.ViewHolder(mFooter) {};
    }else {
      return mAdapter.onCreateViewHolder(parent, viewType);
    }
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (position == 0 || position == mAdapter.getItemCount() + 1){
      return;
    }else {
      mAdapter.onBindViewHolder(holder, position - 1);
    }
  }

  @Override public int getItemCount() {
    return mAdapter.getItemCount() + 2;
  }

  public void addHeaderView(View view){
    this.mHeader = view;
  }

  public void addFooter(View view){
    this.mFooter = view;
  }
}
