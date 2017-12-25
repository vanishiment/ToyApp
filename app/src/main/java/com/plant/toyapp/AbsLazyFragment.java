package com.plant.toyapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class AbsLazyFragment extends Fragment {

  private boolean mIsUIPrepared;
  private boolean mIsDataInited;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View mRootView = inflater.inflate(getLayoutId(), container, false);
      mIsUIPrepared = true;
      initView(mRootView);
      lazyLoad();
    return mRootView;
  }

  @Override public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    if (isVisibleToUser) lazyLoad();
  }

  private void lazyLoad(){
    if (getUserVisibleHint() && mIsUIPrepared && !mIsDataInited) loadData();
  }

  public abstract int getLayoutId();
  public abstract void initView(View view);

  /**
   * 数据加载完成调用 loadDataFinished
   */
  public abstract void loadData();
  public void refreshData(){
    mIsDataInited = false;
    loadData();
  }
  public void loadDataFinish(){
    mIsDataInited = true;
  }
}
