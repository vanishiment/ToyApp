package com.plant.toyapp.permission.entity;

import com.plant.toyapp.permission.callback.PermissionCallback;

public class RequestEntry {
  private PermissionCallback mCallback;
  private Runnable mRunnable;

  private RequestEntry() {
  }

  public RequestEntry newInstance(Builder builder) {
    this.mCallback = builder.callback;
    this.mRunnable = builder.runnable;
    return this;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public PermissionCallback getCallback() {
    return mCallback;
  }

  public Runnable getRunnable() {
    return mRunnable;
  }

  public static class Builder {
    private PermissionCallback callback;
    private Runnable runnable;

    public Builder withCallback(PermissionCallback callback) {
      this.callback = callback;
      return this;
    }

    public Builder withRunnable(Runnable runnable) {
      this.runnable = runnable;
      return this;
    }

    public RequestEntry build() {
      RequestEntry entry = new RequestEntry();
      return entry.newInstance(this);
    }
  }
}
