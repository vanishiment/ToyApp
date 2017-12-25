package com.plant.toyapp.permission.base;

import android.content.Context;
import android.support.v4.app.Fragment;

public class LifecycleFragment extends Fragment {

  private FragmentLifecycle mLifecycle;

  public LifecycleFragment() {
    mLifecycle = getFragmentLifecycle();
  }

  public FragmentLifecycle getFragmentLifecycle(){
    if (mLifecycle == null){
      mLifecycle = new FragmentLifecycle();
    }
    return mLifecycle;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (mLifecycle != null) {
      mLifecycle.onAttach();
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    if (mLifecycle != null) {
      mLifecycle.onStart();
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    if (mLifecycle != null) {
      mLifecycle.onStop();
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (mLifecycle != null) {
      mLifecycle.onDestroy();
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    if (mLifecycle != null) {
      mLifecycle.onDetach();
    }
  }
}
