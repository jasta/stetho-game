package org.devtcg.stethogame;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
  @SuppressWarnings("unchecked")
  private static final Class<? extends Fragment>[] QUESTIONS_FRAGMENTS =
      new Class[] {
          Question1.class,
          Question2.class,
      };

  @Bind(R.id.pager) ViewPager mPager;
  QuestionsAdapter mQuestionsAdapter;

  @Bind(R.id.question_title) TextView mQuestionTitle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ButterKnife.bind(this);

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

  private final Achievements.AchievementListener mAchievementListener =
      new Achievements.AchievementListener() {
    @Override
    public void onChange(@Achievements.Achievement int achievementId, boolean state) {
      QuestionInfo info = mQuestionsAdapter.getQuestionInfo(mPager.getCurrentItem());
      if (achievementId == info.achievementId) {
        updateQuestionTitle(info.displayName, state);
      }
    }
  };

  private final ViewPager.SimpleOnPageChangeListener mPageChanged =
      new ViewPager.SimpleOnPageChangeListener() {
    @Override
    public void onPageSelected(int position) {
      QuestionInfo info = mQuestionsAdapter.getQuestionInfo(position);
      updateQuestionTitle(
          info.displayName,
          Achievements.isUnlocked(info.achievementId));
    }
  };

  private void updateQuestionTitle(String displayName, boolean state) {
    mQuestionTitle.setText("[" + state + "] " + displayName);
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
