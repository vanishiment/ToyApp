package com.plant.toyapp.lego;

/**
 * lego 渲染流程 init--->getData--->processData--->render--->bindEvent--->report
 */
public interface ILego {

  /**
   * 事件绑定器，设置一切 setOnXXXListener
   */
  void getEvent();

  /**
   * 数据拉取
   */
  void getData();

  /**
   * View 的初始化，只执行一次
   */
  void initView();

  /**
   * 该 lego 唯一渲染接口
   */
  void render();

  /**
   * loading
   */
  void showLoading();

  /**
   * success
   */
  void showSuccess();

  /**
   * error
   */
  void showError();

}
