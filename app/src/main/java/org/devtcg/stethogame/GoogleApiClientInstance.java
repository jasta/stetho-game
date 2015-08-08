package org.devtcg.stethogame;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;

public class GoogleApiClientInstance {
  private static final String TAG = "GoogleApiClientInstance";

  private static GoogleApiClient sInstance;

  public static synchronized void set(GoogleApiClient client) {
    sInstance = client;
  }

  public static synchronized GoogleApiClient get(Context context) {
    return sInstance;
  }
}
