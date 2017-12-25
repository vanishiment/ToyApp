package com.plant.toyapp.permission;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import com.plant.toyapp.permission.callback.PermissionCallback;
import com.plant.toyapp.permission.delegate.PermissionDelegateFinder;
import com.plant.toyapp.permission.delegate.PermissionDelegateFragment;
import com.plant.toyapp.permission.util.Util;

public class MPermission {

  private MPermission() {
  }

  private static class Singleton {
    private static final MPermission instance = new MPermission();
  }

  public static MPermission getInstance() {
    return Singleton.instance;
  }

  /**
   * 检查指定权限是否已经获取
   */
  public boolean isAccept(Context context, String permission) {
    return Util.isAccept(context, permission);
  }

  /**
   * 使用Activity申请权限
   *
   * @param activity 要注入权限申请代理的FragmentActivity
   * @param callback 权限申请 成功、失败回调
   * @param perms 权限列表数组
   */
  public void request(FragmentActivity activity, PermissionCallback callback, String[] perms) {
    PermissionDelegateFragment delegate = findDelegate(activity);
    if (delegate != null) {
      delegate.requestPermission(activity, perms, callback);
    }
  }

  /**
   * 使用Fragment申请权限
   *
   * @param fragment 使用的Fragment
   * @param callback 权限申请 成功、失败回调
   * @param perms 权限列表数组
   */
  public void request(Fragment fragment, PermissionCallback callback, String[] perms) {
    FragmentActivity activity = fragment.getActivity();
    if (activity != null && !activity.isFinishing()) {
      PermissionDelegateFragment delegate = findDelegate(activity);
      if (delegate != null) {
        delegate.requestPermission(activity, perms, callback);
      }
    }
  }

  /**
   * 构建申请权限用的隐藏的fragment
   */
  private PermissionDelegateFragment findDelegate(FragmentActivity activity) {
    return PermissionDelegateFinder.getInstance().find(activity);
  }
}
