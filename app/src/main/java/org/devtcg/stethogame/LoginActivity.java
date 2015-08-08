package org.devtcg.stethogame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.example.games.basegameutils.GameHelper;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity
    implements GameHelper.GameHelperListener {
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
      onSignInSucceeded();
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
    MainActivity.show(this);
    finish();
  }
}
