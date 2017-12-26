package com.plant.toyapp.async;

public interface ICallback<T> {

  void callback(Object token, T t);

  void timeOut(Object token, T t);
}
