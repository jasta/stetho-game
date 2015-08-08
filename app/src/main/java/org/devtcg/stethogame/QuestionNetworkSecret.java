package org.devtcg.stethogame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuestionNetworkSecret extends Fragment implements QuestionInfoProvider {
  private static final QuestionInfo INFO =
      new QuestionInfo(
          "What's the secret?",
          Achievements.Achievement.QUESTION_NETWORK_SECRET);

  @Bind(R.id.answer_text)
  EditText answerTextView;

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.question_network_password, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @OnClick(R.id.answer_btn)
  public void onAnswerClicked(Button answerBtn) {
    Request request = new Request.Builder()
        .url("http://www.mocky.io/v2/55c556ba88cd97bc1d46dab7")
        .build();

    StethoGameApplication.getClient().newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(Request request, IOException e) {
        Toast.makeText(getActivity(), "Can't connect to server", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onResponse(Response response) throws IOException {
        final Response theResponse = response;
        getActivity().runOnUiThread(new Runnable() {
          @Override
          public void run() {
            try {
              if (!theResponse.isSuccessful()) {
                Toast.makeText(getActivity(), "Unexpected server reply", Toast.LENGTH_SHORT).show();
              }

              if (theResponse.body().string().equalsIgnoreCase(answerTextView.getText().toString())) {
                Achievements.unlock(getActivity(), INFO.achievement);
              } else {
                Toast.makeText(getActivity(), "Invalid password", Toast.LENGTH_SHORT).show();
              }
            } catch (IOException ioe) {
              Toast.makeText(getActivity(), "Can't connect to server", Toast.LENGTH_SHORT).show();
            }
          }
        });
      }
    });
  }

  @Override
  public QuestionInfo getQuestionInfo() {
    return INFO;
  }
}
