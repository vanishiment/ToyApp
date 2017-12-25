package com.plant.toyapp.permission.base;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FragmentLifecycle implements Lifecycle<FragmentLifecycleListener> {

  //读写分离，避免遍历的同时add进集合，抛出高并发异常。
  private final CopyOnWriteArrayList<FragmentLifecycleListener> mLifecycleListeners =
      new CopyOnWriteArrayList<FragmentLifecycleListener>();
  private boolean mIsAttach;
  private boolean mIsStarted;
  private boolean mIsDestroyed;

  @Override public void addListener(FragmentLifecycleListener listener) {
    if (mLifecycleListeners.contains(listener)) return;
    mLifecycleListeners.add(listener);
    if (mIsAttach) listener.onAttach();
    if (!mIsAttach) listener.onDetach();
    if (mIsStarted) listener.onStart();
    if (!mIsStarted) listener.onStop();
    if (mIsDestroyed) listener.onDestroy();
  }

  @Override public void removeListener(FragmentLifecycleListener listener) {
    if (mLifecycleListeners.size() > 0 && mLifecycleListeners.contains(listener)) {
      mLifecycleListeners.remove(listener);
    }
  }

  @Override public void removeAllListener() {
    if (mLifecycleListeners.size() >0) mLifecycleListeners.clear();
  }

  @Override public boolean containListener(FragmentLifecycleListener listener) {
    return mLifecycleListeners.size() > 0 && mLifecycleListeners.contains(listener);
  }

  @Override public List<FragmentLifecycleListener> getAllListener() {
    ArrayList<FragmentLifecycleListener> listeners = new ArrayList<>();
    listeners.addAll(mLifecycleListeners);
    return listeners;
  }

  public void onAttach() {
    mIsAttach = true;
    for (FragmentLifecycleListener listener : mLifecycleListeners) {
      listener.onAttach();
    }
  }

  public void onStart() {
    mIsStarted = true;
    for (FragmentLifecycleListener listener : mLifecycleListeners) {
      listener.onStart();
    }
  }

  public void onStop() {
    mIsStarted = false;
    for (FragmentLifecycleListener listener : mLifecycleListeners) {
      listener.onStop();
    }
  }

  public void onDestroy() {
    mIsDestroyed = true;
    for (FragmentLifecycleListener listener : mLifecycleListeners) {
      listener.onDestroy();
    }
  }

  public void onDetach() {
    mIsAttach = false;
    for (FragmentLifecycleListener listener : mLifecycleListeners) {
      listener.onDetach();
    }
  }
}
