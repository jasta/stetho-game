package org.devtcg.stethogame;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.DumperPluginsProvider;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumperPlugin;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;

public class StethoGameApplication extends Application {

  public static final boolean USE_GOOGLE_PLAY = false;

  private static OkHttpClient client;

  private static QuestionLoginDumpapp questionLoginDumpappFragment;

  static {
    client = new OkHttpClient();
    client.networkInterceptors().add(new StethoInterceptor());
  }

  public static OkHttpClient getClient() {
    return client;
  }

  public static void registerLoginFragment(QuestionLoginDumpapp fragment) {
    questionLoginDumpappFragment = fragment;
  }

  public static void loginToFragment(String yourName) {
    questionLoginDumpappFragment.login(yourName);
  }

  @Override
  public void onCreate() {
    super.onCreate();

    // This blocks, *sigh*...
    Achievements.syncStateFromDisk(this);

    // ...let the fun begin :)
    Context context = this;
    Stetho.initialize(Stetho.newInitializerBuilder(context)
        .enableDumpapp(new GameDumperPluginProvider(context))
        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context))
        .build());
  }

  private static class GameDumperPluginProvider implements DumperPluginsProvider {
    private final Context mContext;

    public GameDumperPluginProvider(Context context) {
      mContext = context;
    }

    @Override
    public Iterable<DumperPlugin> get() {
      ArrayList<DumperPlugin> plugins = new ArrayList<>();
      for (DumperPlugin defaultPlugin : Stetho.defaultDumperPluginsProvider(mContext).get()) {
        plugins.add(defaultPlugin);
      }
      plugins.add(new GoogleApiDumperPlugin(mContext));
      plugins.add(new LoginDumperPlugin(mContext));
      return plugins;
    }
  }
}
