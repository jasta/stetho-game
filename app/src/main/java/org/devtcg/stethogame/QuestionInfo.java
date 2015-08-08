package org.devtcg.stethogame;

public class QuestionInfo {
  public final String displayName;
  public final Achievements.Achievement achievement;

  public QuestionInfo(String displayName, Achievements.Achievement achievement) {
    this.displayName = displayName;
    this.achievement = achievement;
  }
}
