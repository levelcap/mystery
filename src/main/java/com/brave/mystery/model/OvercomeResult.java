package com.brave.mystery.model;

/**
 * Created by dcohen on 1/20/16.
 */
public class OvercomeResult {
    public enum Outcome {
        FAIL, TIE, SUCCESS, SUCCESS_WITH_STYLE;
    }
    private Outcome outcome;
    private int difficulty;
    private int primaryRoll;
    private int successfulHelpers = 0;
    private int failedHelpers = 0;
    private int booster = 0;

    public OvercomeResult(int difficulty, int primaryRoll) {
        this.difficulty = difficulty;
        this.primaryRoll = primaryRoll;
    }

    public Outcome getOutcome() {
        return outcome;
    }

    public void setOutcome(Outcome outcome) {
        this.outcome = outcome;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getPrimaryRoll() {
        return primaryRoll;
    }

    public void setPrimaryRoll(int primaryRoll) {
        this.primaryRoll = primaryRoll;
    }

    public int getSuccessfulHelpers() {
        return successfulHelpers;
    }

    public void setSuccessfulHelpers(int successfulHelpers) {
        this.successfulHelpers = successfulHelpers;
    }

    public int getFailedHelpers() {
        return failedHelpers;
    }

    public void setFailedHelpers(int failedHelpers) {
        this.failedHelpers = failedHelpers;
    }

    public int getBooster() {
        return booster;
    }

    public void setBooster(int booster) {
        this.booster = booster;
    }

    public void addSuccessfulHelper() {
        this.successfulHelpers++;
    }

    public void addFailedHelper() {
        this.failedHelpers++;
    }

    public void calculateOutcome() {
        int totalResult = primaryRoll;
        totalResult += (successfulHelpers*2);
        if (totalResult < difficulty) {
            this.outcome = Outcome.FAIL;
        } else if (totalResult == difficulty) {
            this.outcome = Outcome.TIE;
        } else if (totalResult < (difficulty + 3)) {
            this.outcome = Outcome.SUCCESS;
        } else {
            this.outcome = Outcome.SUCCESS_WITH_STYLE;
        }
    }
}
