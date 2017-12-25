package com.plant.toyapp.permission.base;

import java.util.List;

public interface Lifecycle<T extends LifecycleListener> {

  /**
   * 添加生命周期回调监听器
   *
   * @param listener
   */
  void addListener(T listener);

  /**
   * 移除生命周期回调监听器
   *
   * @param listener
   */
  void removeListener(T listener);

  /**
   * 移除所有生命周期回调监听器
   */
  void removeAllListener();

  /**
   * 是否包含了该监听器
   *
   * @param listener
   */
  boolean containListener(T listener);

  /**
   * 获取所有的监听器
   *
   * @return
   */
  List<T> getAllListener();

}
