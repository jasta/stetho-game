package org.devtcg.stethogame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.MalformedInputException;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageTypeQuestion extends Fragment implements QuestionInfoProvider {
  private static final QuestionInfo INFO =
      new QuestionInfo(
          "4th question",
          Achievements.QUESTION_4);

  @Bind(R.id.imageView)
  ImageView mImageView;

  @Bind(R.id.answer_text)
  EditText mAnswer;

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.question_4, container, false);
    ButterKnife.bind(this, view);

    try {
      mImageView.setImageBitmap(new SetImageTask().execute().get());
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }

    return view;
  }

  @Override
  public QuestionInfo getQuestionInfo() {
    return INFO;
  }

  @OnClick(R.id.answer_btn)
  public void onAnswerClicked(Button answerBtn) {
    if (mAnswer.getText().toString().equals("webp")) {
      Achievements.unlock(getActivity(), INFO.achievementId);
    } else {
      Toast.makeText(getActivity(), "Incorrect answer.", Toast.LENGTH_SHORT).show();
    }
  }

  private class SetImageTask extends AsyncTask<Void, Integer, Bitmap> {
    @Override
    protected Bitmap doInBackground(Void... params) {
      try {
        return (BitmapFactory.decodeStream(new URL("https://www.gstatic.com/webp/gallery/5.sm.webp").openConnection().getInputStream()));
      } catch (IOException e) {
        e.printStackTrace();
      }

      return null;
    }
  }
}
