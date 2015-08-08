package org.devtcg.stethogame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuestionLoginDumpapp extends Fragment implements QuestionInfoProvider {
  private static final QuestionInfo INFO =
      new QuestionInfo(
          "Login via Command Line",
          Achievements.Achievement.QUESTION_DUMPAPP_LOGIN);


@Bind(R.id.dumpapp_login_text)
    TextView dumpAppLoginText;
  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.question_dumpapp_login, container, false);
    ButterKnife.bind(this, view);
      StethoGameApplication.registerLoginFragment(this);
    return view;
  }

  @Override
  public QuestionInfo getQuestionInfo() {
    return INFO;
  }

    public void login(final String yourName) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dumpAppLoginText.setText("Welcome "+yourName+"! You're logged in.");
                Achievements.unlock(getActivity(), INFO.achievement);
            }
        });
    }
}
