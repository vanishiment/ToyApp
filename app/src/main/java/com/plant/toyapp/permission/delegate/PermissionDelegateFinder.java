package com.plant.toyapp.permission.delegate;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class PermissionDelegateFinder {
  private static final String DELEGATE_FRAGMENT_TAG = PermissionDelegateFragment.class.getSimpleName() + "Tag";

  private static class Singleton {
    private static final PermissionDelegateFinder instance = new PermissionDelegateFinder();
  }

  public static PermissionDelegateFinder getInstance() {
    return Singleton.instance;
  }

  /**
   * 添加隐藏权限fragment
   */
  public PermissionDelegateFragment find(FragmentActivity activity) {
    PermissionDelegateFragment fragment = null;
    if (activity != null && !activity.isFinishing()) {
      FragmentManager fm = activity.getSupportFragmentManager();
      fragment = (PermissionDelegateFragment) fm.findFragmentByTag(DELEGATE_FRAGMENT_TAG);
      if (fragment == null) {
        fragment = PermissionDelegateFragment.get();
        fm.beginTransaction()
            .add(fragment, DELEGATE_FRAGMENT_TAG)
            .commitAllowingStateLoss();
      }
    }
    return fragment;
  }
}
