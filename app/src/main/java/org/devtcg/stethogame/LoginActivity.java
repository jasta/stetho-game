package org.devtcg.stethogame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.achievement.Achievements;
import com.google.example.games.basegameutils.GameHelper;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity
    implements GameHelper.GameHelperListener {
  private static final String TAG = "LoginActivity";

  private GameHelper mGameHelper;

  public static void show(Activity context) {
    context.startActivity(new Intent(context, LoginActivity.class));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    ButterKnife.bind(this);

    if (StethoGameApplication.USE_GOOGLE_PLAY) {
      mGameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
      mGameHelper.enableDebugLog(true);

      mGameHelper.setup(this);
      GoogleApiClientInstance.set(mGameHelper.getApiClient());
    } else {
      proceed();
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
//    mGameHelper.onStart(this);
  }

  @Override
  protected void onStop() {
    super.onStop();
//    mGameHelper.onStop();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (StethoGameApplication.USE_GOOGLE_PLAY) {
      mGameHelper.onActivityResult(requestCode, resultCode, data);
    }
  }

  @OnClick(R.id.sign_in)
  public void onSignInClicked(View v) {
    if (StethoGameApplication.USE_GOOGLE_PLAY) {
      mGameHelper.beginUserInitiatedSignIn();
    }
  }

  @Override
  public void onSignInFailed() {
    Toast.makeText(
        this,
        "Google services fail :(",
        Toast.LENGTH_SHORT)
        .show();
  }

  @Override
  public void onSignInSucceeded() {
    PendingResult<Achievements.LoadAchievementsResult> result = Games.Achievements.load(
            mGameHelper.getApiClient(),
            false /* forceReload */);
    result.setResultCallback(new ResultCallback<Achievements.LoadAchievementsResult>() {
      @Override
      public void onResult(Achievements.LoadAchievementsResult result) {
        Log.i(TAG, "Syncing state from Google Play...");
        org.devtcg.stethogame.Achievements.syncStateFromGooglePlay(
                LoginActivity.this,
                result.getAchievements());
      }
    });
    proceed();
  }

  private void proceed() {
    MainActivity.show(this);
    finish();
  }
}
