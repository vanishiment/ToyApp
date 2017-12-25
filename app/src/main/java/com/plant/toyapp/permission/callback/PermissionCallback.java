package com.plant.toyapp.permission.callback;

import java.util.List;

public interface PermissionCallback {
  /**
   * 权限允许
   */
  void onGranted();

  /**
   * 权限拒绝
   * @param perms 被拒绝的权限集合
   */
  void onDenied(List<String> perms);
}
