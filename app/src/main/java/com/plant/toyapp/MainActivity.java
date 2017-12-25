package com.plant.toyapp;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.plant.toyapp.recyclerview.EmptyRecyclerView;
import com.plant.toyapp.recyclerview.QuickAdapter;
import com.plant.toyapp.recyclerview.RVAdapterWrapper;
import com.plant.toyapp.recyclerview.SimpleItemTouchCallback;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  String url = "http://ws4.sinaimg.cn/mw690/795c59cbgy1fmm5q5zv5jj23v92kyb2g.jpg";

  private EmptyRecyclerView rv;
  private RecyclerView.Adapter adapter;
  private List<String> dataList = new ArrayList<>();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    initData();

    rv = findViewById(R.id.rv);
    rv.setLayoutManager(new LinearLayoutManager(this));
    adapter = new QuickAdapter<String>(dataList){

      @Override public int getLayoutId(int viewType) {
        return R.layout.rv_item;
      }

      @Override public void bindVH(QuickAdapter.VH holder, String data, int position) {
        TextView tv = holder.getView(R.id.rv_item_tv);
        tv.setText(data);
      }
    };
    RVAdapterWrapper wrapper = new RVAdapterWrapper(adapter);
    wrapper.addHeaderView(LayoutInflater.from(rv.getContext()).inflate(R.layout.rv_header,null));
    wrapper.addFooter(LayoutInflater.from(rv.getContext()).inflate(R.layout.rv_footer,null));
    rv.setEmptyView(LayoutInflater.from(rv.getContext()).inflate(R.layout.rv_empty,null));
    rv.setAdapter(wrapper);

    ItemTouchHelper helper = new ItemTouchHelper(new SimpleItemTouchCallback<String>(wrapper,dataList));
    helper.attachToRecyclerView(rv);
    ImageView imageView = findViewById(R.id.iv);

    RequestOptions options = RequestOptions.centerCropTransform();
    Glide.with(this).load(url).apply(options).into(imageView);
  }

  private void initData(){
    for (int i = 0; i < 30; i++) {
      dataList.add("data " + i);
    }
  }
}
