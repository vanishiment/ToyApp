package com.plant.toyapp.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import java.util.Collections;
import java.util.List;

/**
 * 使用方法：
 * 1. 为指定的 RecyclerView 设置
 *
 * ItemTouchHelper helper = new ItemTouchHelper(new SimpleItemTouchCallback(adapter, data));
 * helper.attachToRecyclerView(recyclerview);
 *
 * 2.内部 View 单独支持拖拽 核心方法 helper.startDrag(holder)
 *
 * 定义接口
 * interface OnStartDragListener{
 * void startDrag(RecyclerView.ViewHolder holder);
 * }
 *
 * Activity 实现该接口
 * public MainActivity extends Activity implements OnStartDragListener{
 * ...
 * public void startDrag(RecyclerView.ViewHolder holder) {
 * mHelper.startDrag(holder);
 * }
 *}
 *
 * 支持拖拽
 * 在 Adapter 的 onBindView 中使用 mListener.startDrag
 * @param <T>
 */
public class SimpleItemTouchCallback<T> extends ItemTouchHelper.Callback {

  private RecyclerView.Adapter mAdapter;
  private List<T> mDataList;

  public SimpleItemTouchCallback(RecyclerView.Adapter adapter, List<T> dataList) {
    this.mAdapter = adapter;
    this.mDataList = dataList;
  }

  @Override
  public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
    int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
    int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
    return makeMovementFlags(dragFlags,swipeFlags);
  }

  @Override public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
      RecyclerView.ViewHolder target) {
    int from = viewHolder.getAdapterPosition();
    int to = target.getAdapterPosition();
    Collections.swap(mDataList,from,to);
    mAdapter.notifyItemMoved(from,to);
    return true;
  }

  @Override public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    int position = viewHolder.getAdapterPosition();
    mDataList.remove(position);
    mAdapter.notifyItemRemoved(position);
  }

  @Override public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
    super.onSelectedChanged(viewHolder, actionState);
    if (actionState != ItemTouchHelper.ACTION_STATE_IDLE){
      //viewHolder.itemView.setBackgroundColor(0xffbcbcbc);// 设置滑动和拖拽时的背景色
    }
  }

  @Override public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
    super.clearView(recyclerView, viewHolder);
    //viewHolder.itemView.setBackgroundColor(0xffeeeeee);// 设置滑动和拖拽时的背景色
  }

  @Override public boolean isLongPressDragEnabled() {
    return false;
  }

  @Override public boolean isItemViewSwipeEnabled() {
    return true;
  }
}
