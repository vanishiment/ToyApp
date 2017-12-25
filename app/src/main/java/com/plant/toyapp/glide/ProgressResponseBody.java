package com.plant.toyapp.glide;

import android.support.annotation.Nullable;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class ProgressResponseBody extends ResponseBody {

  private BufferedSource bufferedSource;
  private ResponseBody responseBody;
  private ProgressListener progressListener;

  public ProgressResponseBody(String url,ResponseBody responseBody) {
    this.responseBody = responseBody;
    progressListener = ProgressIntercept.LISTENER_MAP.get(url);
  }

  @Nullable @Override public MediaType contentType() {
    return responseBody.contentType();
  }

  @Override public long contentLength() {
    return responseBody.contentLength();
  }

  @Override public BufferedSource source() {
    if (bufferedSource == null){
      bufferedSource = Okio.buffer(new ProgressSource(responseBody.source()));
    }
    return bufferedSource;
  }

  private class ProgressSource extends ForwardingSource{

    private long totalBytesRead = 0L;
    private int curProgress;
    private long curTimeMills;

    public ProgressSource(Source delegate) {
      super(delegate);
    }

    @Override public long read(Buffer sink, long byteCount) throws IOException {
      long bytesRead = super.read(sink, byteCount);
      long fullLength = responseBody.contentLength();
      if (bytesRead == -1){
        totalBytesRead = fullLength;
      }else {
        totalBytesRead += bytesRead;
      }
      boolean updateProgress = System.currentTimeMillis() - curTimeMills > 500;
      int progress = (int) ((100L * totalBytesRead) / fullLength);
      if (progressListener != null && progress != curProgress && updateProgress){
        progressListener.onProgress(progress);
      }
      if (progressListener != null && progress == fullLength){
        progressListener = null;
      }
      curProgress = progress;
      curTimeMills = System.currentTimeMillis();
      return bytesRead;
    }
  }
}
