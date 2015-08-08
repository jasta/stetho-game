package org.devtcg.stethogame;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.IntDef;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.widget.Toast;

import com.google.android.gms.games.Games;
import com.google.android.gms.games.achievement.Achievement;
import com.google.android.gms.games.achievement.AchievementBuffer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Achievements {
  private static final String TAG = "Achievements";

  public enum Achievement {
    QUESTION_LIST_ITEM_HEIGHT(R.string.achievement_list_item_height),
    QUESTION_IMAGE_TYPE(R.string.achievement_image_type),
    QUESTION_TOP_SCORE(R.string.achievement_top_gun),
    QUESTION_NETWORK_PASSWORD(R.string.achievement_steal_the_beacon),
    QUESTION_MODIFY_SETTING(R.string.achievement_modify_settings),
    QUESTION_DUMPAPP_LOGIN(R.string.achievement_login);

    private final int mResId;

    Achievement(int resId) {
      mResId = resId;
    }

    public int getResId() {
      return mResId;
    }

    public static Achievement fromResId(int resId) {
      for (Achievement achievement : values()) {
        if (achievement.getResId() == resId) {
          return achievement;
        }
      }
      throw new IllegalArgumentException("Unknown resId=" + resId);
    }
  }

  private static final String PREFS_TAG = "Achievements";

  private static final State sState = new State();

  public static void syncStateFromDisk(Context context) {
    synchronized (sState) {
      SharedPreferences prefs = getPrefs(context);
      try {
        for (Map.Entry<String, ?> entry : prefs.getAll().entrySet()) {
          int achievementId = Integer.parseInt(entry.getKey());
          boolean result = (Boolean)entry.getValue();
          sState.set(Achievement.fromResId(achievementId), result);
        }
      } catch (NumberFormatException | ClassCastException e) {
        Log.e(TAG, "Error syncing state from disk", e);
        prefs.edit().clear().apply();
      }
    }
  }

  public static void syncStateFromGooglePlay(
      Context context,
      AchievementBuffer buffer) {
    Resources res = context.getResources();
    Map<String, Integer> playIdsToResIds = new HashMap<>();
    for (Achievement achievement : Achievement.values()) {
      int resId = achievement.getResId();
      playIdsToResIds.put(res.getString(resId), resId);
    }
    SharedPreferences prefs = getPrefs(context);
    SharedPreferences.Editor editor = prefs.edit();
    for (com.google.android.gms.games.achievement.Achievement achievement : buffer) {
      int resId = playIdsToResIds.get(achievement.getAchievementId());
      boolean unlocked =
          achievement.getState() ==
              com.google.android.gms.games.achievement.Achievement.STATE_UNLOCKED;
      sState.set(Achievement.fromResId(resId), unlocked);
      editor.putBoolean(String.valueOf(resId), unlocked);
    }
    editor.apply();
  }

  private static SharedPreferences getPrefs(Context context) {
    return context.getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE);
  }

  public static boolean isUnlocked(Achievement achievement) {
    synchronized (sState) {
      return sState.get(achievement);
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

  public static void unlock(Context context, Achievement achievement) {
    if (StethoGameApplication.USE_GOOGLE_PLAY) {
      Games.Achievements.unlock(
          GoogleApiClientInstance.get(context),
          context.getResources().getString(achievement.getResId()));
    }
    synchronized (sState) {
      sState.set(achievement, true);
    }
    getPrefs(context).edit().putBoolean(String.valueOf(achievement.getResId()), true).apply();
  }

  private static class State {
    private final SparseBooleanArray mCached = new SparseBooleanArray();
    private final ArrayList<AchievementListener> mListeners = new ArrayList<>();

    public synchronized boolean get(Achievement achievementId) {
      return mCached.get(achievementId.getResId());
    }

    public synchronized void set(Achievement achievement, boolean value) {
      mCached.put(achievement.getResId(), value);
      for (int i = 0, N = mListeners.size(); i < N; i++) {
        mListeners.get(i).onChange(achievement, value);
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
    void onChange(Achievement achievement, boolean state);
  }
}
