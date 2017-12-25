package com.plant.toyapp.permission.base;

public interface FragmentLifecycleListener extends LifecycleListener {
  /**
   * Fragment onAttach对应方法
   */
  void onAttach();

  /**
   * Fragment onStart对应方法
   */
  void onStart();

  /**
   * Fragment onStop对应方法
   */
  void onStop();

  /**
   * Fragment onDestroy对应方法
   */
  void onDestroy();

  /**
   * Fragment onDetach对应方法
   */
  void onDetach();
}
