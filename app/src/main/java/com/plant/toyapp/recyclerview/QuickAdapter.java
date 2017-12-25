package com.plant.toyapp.recyclerview;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * 简单的万能适配器
 * @param <T> 数据类型
 */
public abstract class QuickAdapter<T> extends RecyclerView.Adapter<QuickAdapter.VH> {

  private List<T> mDataList;

  public QuickAdapter(List<T> dataList) {
    this.mDataList = dataList;
  }

  public abstract @LayoutRes int getLayoutId(int viewType);

  public abstract void bindVH(VH holder, T data, int position);

  @Override public VH onCreateViewHolder(ViewGroup parent, int viewType) {
    return VH.get(parent, getLayoutId(viewType));
  }

  @Override public void onBindViewHolder(VH holder, int position) {
    bindVH(holder, mDataList.get(position), position);
  }

  @Override public int getItemCount() {
    return mDataList == null ? 0 : mDataList.size();
  }

  public static class VH extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;
    private View mContentView;

    public VH(View itemView) {
      super(itemView);
      mContentView = itemView;
      mViews = new SparseArray<>();
    }

    public static VH get(ViewGroup parent, @LayoutRes int layoutId) {
      View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
      return new VH(view);
    }

    public <T extends View> T getView(@IdRes int id) {
      View v = mViews.get(id);
      if (v == null) {
        v = mContentView.findViewById(id);
        mViews.put(id, v);
      }
      return (T) v;
    }
  }
}
