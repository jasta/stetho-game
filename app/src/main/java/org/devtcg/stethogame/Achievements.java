package org.devtcg.stethogame;

import android.content.Context;
import android.support.annotation.IntDef;
import android.util.SparseBooleanArray;
import android.widget.Toast;

import java.util.ArrayList;

public class Achievements {
  @IntDef({ QUESTION_1, QUESTION_2, QUESTION_3, QUESTION_4 })
  public @interface Achievement {}

  public static final int QUESTION_1 = 1;
  public static final int QUESTION_2 = 2;
  public static final int QUESTION_3 = 3;
  public static final int QUESTION_4 = 4;

  private static final State sState = new State();

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
    synchronized (sState) {
      sState.set(achievementId, true);
    }
    Toast.makeText(
        context,
        "TODO: unlock "  + achievementId,
        Toast.LENGTH_SHORT)
        .show();
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
