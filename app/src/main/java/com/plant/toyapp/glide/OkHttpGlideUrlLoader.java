package com.plant.toyapp.glide;

import android.support.annotation.Nullable;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import java.io.InputStream;
import okhttp3.OkHttpClient;

public class OkHttpGlideUrlLoader implements ModelLoader<GlideUrl,InputStream> {

  private final ModelCache<GlideUrl,GlideUrl> modelCache;
  private OkHttpClient client;

  public OkHttpGlideUrlLoader(OkHttpClient client) {
    this(null,client);
  }

  public OkHttpGlideUrlLoader(ModelCache<GlideUrl, GlideUrl> modelCache,OkHttpClient client) {
    this.modelCache = modelCache;
    this.client = client;
  }

  @Nullable @Override
  public LoadData<InputStream> buildLoadData(GlideUrl glideUrl, int width, int height,
      Options options) {
    GlideUrl url = glideUrl;
    if (modelCache != null){
      url = modelCache.get(glideUrl,0,0);
      if (url == null){
        modelCache.put(glideUrl,0,0,glideUrl);
        url = glideUrl;
      }
    }
    return new LoadData<InputStream>(url,new OkHttpFetcher(client,url));
  }

  @Override public boolean handles(GlideUrl glideUrl) {
    return false;
  }

  public static class Factory implements ModelLoaderFactory<GlideUrl,InputStream>{

    private final ModelCache<GlideUrl,GlideUrl> modelCache = new ModelCache<>(500);
    private OkHttpClient client;

    public Factory(OkHttpClient client) {
      this.client = client;
    }

    private synchronized OkHttpClient getOkHttpClient(){
      if (client == null){
        client = new OkHttpClient();
      }
      return client;
    }

    @Override
    public ModelLoader<GlideUrl, InputStream> build(MultiModelLoaderFactory multiFactory) {
      return new OkHttpGlideUrlLoader(modelCache,getOkHttpClient());
    }

    @Override public void teardown() {

    }
  }
}
