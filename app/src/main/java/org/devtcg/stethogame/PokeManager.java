package org.devtcg.stethogame;

public class PokeManager {
  private static PokeManager sInstance;

  private PokeListener mListener;

  public static synchronized PokeManager get() {
    if (sInstance == null) {
      sInstance = new PokeManager();
    }
    return sInstance;
  }

  public void setListener(PokeListener listener) {
    mListener = listener;
  }

  public void poke(String message) {
    if (mListener != null) {
      mListener.onPoke(message);
    }
  }

  public interface PokeListener {
    void onPoke(String message);
  }
}
