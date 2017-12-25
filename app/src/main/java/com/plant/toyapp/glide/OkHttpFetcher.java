package com.plant.toyapp.glide;

import android.support.annotation.NonNull;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.util.ContentLengthInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OkHttpFetcher implements DataFetcher<InputStream> {

  private final OkHttpClient client;
  private final GlideUrl url;
  private InputStream stream;
  private ResponseBody responseBody;
  private volatile boolean isCancelled;

  public OkHttpFetcher(OkHttpClient client, GlideUrl url) {
    this.client = client;
    this.url = url;
  }

  @Override public void loadData(Priority priority, DataCallback<? super InputStream> callback) {
    Request.Builder builder = new Request.Builder().url(this.url.toString());
    for (Map.Entry<String, String> header : url.getHeaders().entrySet()) {
      String key = header.getKey();
      String value = header.getValue();
      builder.addHeader(key,value);
    }
    Request request = builder.build();
    if (isCancelled){
      callback.onDataReady(null);
      return;
    }
    try {
      Response response = client.newCall(request).execute();
      responseBody = response.body();
      if (!response.isSuccessful() || responseBody == null){
        throw new IOException("Request failed with code: " + response.code());
      }
      stream = ContentLengthInputStream.obtain(responseBody.byteStream(),responseBody.contentLength());
      callback.onDataReady(stream);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override public void cleanup() {
    try {
      if (null != stream){
        stream.close();
      }
      if (responseBody != null){
        responseBody.close();
      }
    }catch (IOException ignored){

    }

  }

  @Override public void cancel() {
    isCancelled = true;
  }

  @NonNull @Override public Class<InputStream> getDataClass() {
    return InputStream.class;
  }

  @NonNull @Override public DataSource getDataSource() {
    return DataSource.REMOTE;
  }
}
