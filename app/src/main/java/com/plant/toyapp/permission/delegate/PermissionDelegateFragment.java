package com.plant.toyapp.permission.delegate;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.SparseArrayCompat;
import com.plant.toyapp.permission.base.LifecycleFragment;
import com.plant.toyapp.permission.base.SimpleFragmentLifecycleListenerAdapter;
import com.plant.toyapp.permission.callback.PermissionCallback;
import com.plant.toyapp.permission.entity.RequestEntry;
import com.plant.toyapp.permission.util.Util;
import java.util.ArrayList;
import java.util.List;

public class PermissionDelegateFragment extends LifecycleFragment {

  private static final int REQUEST_CODE = 0x0122;
  private SparseArrayCompat<RequestEntry> mCallbacks = new SparseArrayCompat<>();

  public static PermissionDelegateFragment get() {
    return new PermissionDelegateFragment();
  }

  @Override public void onDetach() {
    super.onDetach();
    popAll();
    getFragmentLifecycle().removeAllListener();
  }

  private void push(final RequestEntry entry) {
    mCallbacks.put(entry.hashCode(), entry);
    getFragmentLifecycle().addListener(new SimpleFragmentLifecycleListenerAdapter() {
      @Override public void onAttach() {
        super.onAttach();
        mCallbacks.get(entry.hashCode()).getRunnable().run();
        getFragmentLifecycle().removeListener(this);
      }
    });
  }

  private void pop(RequestEntry entry) {
    mCallbacks.remove(entry.hashCode());
  }

  private void popAll() {
    if (mCallbacks != null && mCallbacks.size() > 0) mCallbacks.clear();
  }

  public void requestPermission(final FragmentActivity context, final String[] permissions,
      final PermissionCallback callback) {
    if (!Util.isNeedCheck(context)) {
      callback.onGranted();
      return;
    }
    push(RequestEntry.newBuilder().withCallback(callback).withRunnable(new Runnable() {
      @Override public void run() {
        //只申请用户未授权的权限
        ArrayList<String> unGrantedList = new ArrayList<>();
        for (String p : permissions) {
          if (ContextCompat.checkSelfPermission(context, p) != PackageManager.PERMISSION_GRANTED) {
            unGrantedList.add(p);
          }
        }
        if (unGrantedList.size() > 0) {
          PermissionDelegateFragment.this.requestPermissions(unGrantedList.toArray(new String[] {}),
              REQUEST_CODE);
        } else {
          callback.onGranted();
        }
      }
    }).build());
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode) {
      case REQUEST_CODE:
        if (grantResults.length > 0 && mCallbacks.size() > 0) {
          for (int i = 0; i < mCallbacks.size(); i++) {
            RequestEntry entry = mCallbacks.valueAt(i);
            PermissionCallback callback = entry.getCallback();
            List<String> deniedList = new ArrayList<>();
            for (int j = 0; j < grantResults.length; j++) {
              int grant = grantResults[j];
              String p = permissions[j];
              if (grant != PackageManager.PERMISSION_GRANTED) {
                deniedList.add(p);
              }
            }
            if (deniedList.isEmpty()) {
              callback.onGranted();
            } else {
              callback.onDenied(deniedList);
            }
          }
        }
        break;
      default:
        break;
    }
  }
}
