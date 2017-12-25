package com.plant.toyapp.glide;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import java.io.InputStream;
import okhttp3.OkHttpClient;

@GlideModule
public class ToyAppGlideModule extends AppGlideModule {

  @Override public void applyOptions(Context context, GlideBuilder builder) {

  }

  @Override public void registerComponents(Context context, Glide glide, Registry registry) {
    OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
    okHttpBuilder.addInterceptor(new ProgressIntercept());
    OkHttpClient client = okHttpBuilder.build();
    registry.append(GlideUrl.class, InputStream.class,new OkHttpGlideUrlLoader.Factory(client));
  }
}
