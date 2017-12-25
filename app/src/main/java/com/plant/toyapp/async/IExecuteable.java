package com.plant.toyapp.async;

public interface IExecuteable {

  /**
   * 执行一个任务
   * @param r 任务
   */
  void execute(AbsHandlerTask r);

  /**
   * 执行一个任务，同步或异步返回结果
   * @param r 要执行的任务
   * @param isAsync true 在 UI 线程返回，false 在该线程返回
   */
  void execute(AbsHandlerTask r, boolean isAsync);

  /**
   * 延时执行一个任务
   * @param r 要执行的任务
   * @param delayMills 延时时间，单位毫秒
   */
  void executeDelay(AbsHandlerTask r, long delayMills);

  /**
   * 延时执行一个任务，同步或异步返回结果
   * @param r 要执行的任务
   * @param delayMills 延时时间，单位毫秒
   * @param isAsync true 在 UI 线程返回，false 在该线程返回
   */
  void executeDelay(AbsHandlerTask r, long delayMills, boolean isAsync);

  /**
   * 如果任务未执行，直接取消任务且不会回调结果。如果已执行或执行完毕，则不回调结果
   * @param token 任务的 token
   */
  void cancel(Object token);

  /**
   * 取消所有任务，如果任务未执行，直接取消任务且不会回调结果。如果已执行或执行完毕，则不回调结果
   */
  void cancelAll();
}
