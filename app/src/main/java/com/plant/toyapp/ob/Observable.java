package com.plant.toyapp.ob;

import java.util.ArrayList;

public abstract class Observable {

  private final ArrayList<Observer> mObservers = new ArrayList<>();
  private boolean mChanged = false;

  public boolean registerObserver(Observer observer) {
    checkNull(observer);
    synchronized (mObservers){
      if (hasObserver(observer)) return false;
      mObservers.add(observer);
      return true;
    }
  }

  public void notifyObservers(Object arg){
    synchronized (mObservers){
      if (!hasChanged()) return;
      clearChanged();
      for (Observer observer: mObservers
      ) {
        observer.update(this,arg);
      }
    }
  }

  public boolean unregisterObserver(Observer observer) {
    checkNull(observer);
    synchronized (mObservers){
      int index = mObservers.indexOf(observer);
      if (index == -1) return false;
      mObservers.remove(index);
      return true;
    }
  }

  /**
   * Remove all registered observers.
   */
  public void unregisterAll() {
    synchronized(mObservers) {
      mObservers.clear();
    }
  }

  private void checkNull(Observer observer){
    if (observer == null) {
      throw new IllegalArgumentException("The observer is null.");
    }
  }

  private boolean hasObserver(Observer observer){
    return mObservers.contains(observer);
  }

  private boolean hasChanged(){
    return mChanged;
  }

  private void clearChanged(){
    mChanged = false;
  }
}
