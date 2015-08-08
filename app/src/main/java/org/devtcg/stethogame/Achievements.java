package org.devtcg.stethogame;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.IntDef;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.widget.Toast;

import com.google.android.gms.games.Games;

import java.util.ArrayList;
import java.util.Map;

public class Achievements {
  private static final String TAG = "Achievements";

  @IntDef({ QUESTION_1, QUESTION_2, QUESTION_3, QUESTION_IMAGE_TYPE, QUESTION_TOP_SCORE, QUESTION_NETWORK_PASSWORD, QUESTION_MODIFY_SETTING })
  public @interface Achievement {}

  public static final int QUESTION_1 = R.string.achievement_test_achivement;
  public static final int QUESTION_2 = 2;
  public static final int QUESTION_3 = 3;
  public static final int QUESTION_IMAGE_TYPE = 4;
  public static final int QUESTION_TOP_SCORE = 5;
  public static final int QUESTION_NETWORK_PASSWORD = 6;
  public static final int QUESTION_MODIFY_SETTING = 7;

  private static final String PREFS_TAG = "Achievements";

  private static final State sState = new State();

  public static void syncStateFromDisk(Context context) {
    synchronized (sState) {
      SharedPreferences prefs = getPrefs(context);
      try {
        for (Map.Entry<String, ?> entry : prefs.getAll().entrySet()) {
          @Achievement int achievementId = Integer.parseInt(entry.getKey());
          boolean result = (Boolean)entry.getValue();
          sState.set(achievementId, result);
        }
      } catch (NumberFormatException | ClassCastException e) {
        Log.e(TAG, "Error syncing state from disk", e);
        prefs.edit().clear().apply();
      }
    }
  }

  private static SharedPreferences getPrefs(Context context) {
    return context.getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE);
  }

  public static boolean isUnlocked(@Achievement int achievementId) {
    synchronized (sState) {
      return sState.get(achievementId);
    }
  }

  public static void registerListener(AchievementListener listener) {
    synchronized (sState) {
      sState.registerListener(listener);
    }
  }

  public static void unregisterListener(AchievementListener listener) {
    synchronized (sState) {
      sState.unregisterListener(listener);
    }
  }

  public static void unlock(Context context, @Achievement int achievementId) {
    if (StethoGameApplication.USE_GOOGLE_PLAY) {
      Games.Achievements.unlock(
          GoogleApiClientInstance.get(context),
          context.getResources().getString(achievementId));
    }
    synchronized (sState) {
      sState.set(achievementId, true);
    }
    getPrefs(context).edit().putBoolean(String.valueOf(achievementId), true).apply();
  }

  private static class State {
    private final SparseBooleanArray mCached = new SparseBooleanArray();
    private final ArrayList<AchievementListener> mListeners = new ArrayList<>();

    public synchronized boolean get(@Achievement int achievementId) {
      return mCached.get(achievementId);
    }

    public synchronized void set(@Achievement int achievementId, boolean value) {
      mCached.put(achievementId, value);
      for (int i = 0, N = mListeners.size(); i < N; i++) {
        mListeners.get(i).onChange(achievementId, value);
      }
    }

    public synchronized void registerListener(AchievementListener listener) {
      mListeners.add(listener);
    }

    public synchronized void unregisterListener(AchievementListener listener) {
      mListeners.remove(listener);
    }
  }

  public interface AchievementListener {
    void onChange(@Achievement int achievementId, boolean state);
  }
}
