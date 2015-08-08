package org.devtcg.stethogame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
  private static final String TAG = "MainActivity";

  private static final int REQUEST_ACHIEVEMENTS = 1;

  @SuppressWarnings("unchecked")
  private static final Class<? extends Fragment>[] QUESTIONS_FRAGMENTS =
      new Class[] {
          QuestionListView.class,
          QuestionImageType.class,
          QuestionTopScore.class,
          QuestionNetworkPassword.class,
          QuestionModifySetting.class,
          QuestionPoke.class
      };

  @Bind(R.id.pager) ViewPager mPager;
  QuestionsAdapter mQuestionsAdapter;

  public static void show(Activity context) {
    context.startActivity(new Intent(context, MainActivity.class));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (StethoGameApplication.USE_GOOGLE_PLAY) {
      GoogleApiClient client = GoogleApiClientInstance.get(this);
      if (client == null) {
        LoginActivity.show(this);
        finish();
        return;
      }
    }

    setupUI();
  }

  private void setupUI() {
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    if (StethoGameApplication.USE_GOOGLE_PLAY) {
      Games.setViewForPopups(
          GoogleApiClientInstance.get(this),
          findViewById(android.R.id.content));
    }

    mQuestionsAdapter = new QuestionsAdapter(
        this,
        getSupportFragmentManager(),
        QUESTIONS_FRAGMENTS);
    mPager.setAdapter(mQuestionsAdapter);
    mPager.addOnPageChangeListener(mPageChanged);
    mPageChanged.onPageSelected(0); // hack :(

    Achievements.registerListener(mAchievementListener);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Achievements.unregisterListener(mAchievementListener);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_main, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_achievements:
        startActivityForResult(
            Games.Achievements.getAchievementsIntent(
                GoogleApiClientInstance.get(this)),
            REQUEST_ACHIEVEMENTS);
        return true;
      case R.id.action_settings:
        UserSettingActivity.show(this);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private final Achievements.AchievementListener mAchievementListener =
      new Achievements.AchievementListener() {
    @Override
    public void onChange(Achievements.Achievement achievement, boolean state) {
      QuestionInfo info = mQuestionsAdapter.getQuestionInfo(mPager.getCurrentItem());
      if (achievement == info.achievement) {
        updateQuestionInfo(info.displayName, state);
      }
    }
  };

  private final ViewPager.SimpleOnPageChangeListener mPageChanged =
      new ViewPager.SimpleOnPageChangeListener() {
    @Override
    public void onPageSelected(int position) {
      Fragment fragment = mQuestionsAdapter.getItem(position);
      if (fragment instanceof RevealListener) {
        ((RevealListener)fragment).onRevealed();
      }
      QuestionInfo info = mQuestionsAdapter.getQuestionInfo(position);
      updateQuestionInfo(
          info.displayName,
          Achievements.isUnlocked(info.achievement));
    }
  };

  private void updateQuestionInfo(String displayName, boolean state) {
    if (state) {
      displayName = displayName + " ✓";
    }
    setTitle(displayName);
  }

  private static class QuestionsAdapter extends FragmentStatePagerAdapter {
    private final Context mContext;
    private final Class<? extends Fragment>[] mQuestions;
    private final Fragment[] mFragments;

    public QuestionsAdapter(
        Context context,
        FragmentManager fragmentManager,
        Class<? extends Fragment>[] questions) {
      super(fragmentManager);
      mContext = context;
      mQuestions = questions;
      mFragments = new Fragment[questions.length];
    }

    @Override
    public Fragment getItem(int position) {
      Fragment fragment = mFragments[position];
      if (fragment == null) {
        fragment = createFragment(mQuestions[position]);
        mFragments[position] = fragment;
      }
      return fragment;
    }

    public QuestionInfo getQuestionInfo(int position) {
      Fragment fragment = getItem(position);
      return ((QuestionInfoProvider)fragment).getQuestionInfo();
    }

    private static Fragment createFragment(Class<? extends Fragment> clazz) {
      try {
        return clazz.newInstance();
      } catch (InstantiationException e) {
        throw new RuntimeException(e);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public int getCount() {
      return mQuestions.length;
    }
  }
}
