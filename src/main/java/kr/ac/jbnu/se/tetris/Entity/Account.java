package kr.ac.jbnu.se.tetris.Entity;

import java.util.Arrays;

public class Account {
    public enum scoreType{
        BEST_SCORE_NORMAL("bestScoreNormal"),BEST_SCORE_ITEM("bestScoreItem"),BEST_SCORE_SPRINT("bestScoreSprint"),
        BEST_SCORE_SURVIVAL("bestScoreSurvival"),BEST_SCORE_AI("bestScoreAi"),BEST_SCORE_MULTI("bestScoreMulti");
        private String tag;
        scoreType(String tag){
            this.tag = tag;
        }
        public String getTag(){ return tag; }
    }
    private int bestScoreNormal, bestScoreItem, bestScoreSprint,
            bestScoreSurvival, bestScoreAi, bestScoreMulti;
    private final String accountID;
    private final String accountPW;
    public Account(String ID, char[] PW){
        this.accountID = ID;
        this.accountPW = Arrays.toString(PW);
        this.bestScoreNormal=0;
        this.bestScoreItem=0;
        this.bestScoreSprint=0;
        this.bestScoreSurvival=0;
        this.bestScoreAi=0;
        this.bestScoreMulti=0;
    }
    public void checkBestScore(GameMode mode, int score){
        switch (mode){
            case NORMAL:
                //if()
                break;
            case ITEM:
                break;
            case SPRINT:
                break;
            case SURVIVAL:
                break;
            case MULTI:
                break;
            case AI:
                break;
        }
        //return this.bestScore
    }
    public String getID(){ return accountID; }
    public String getPW(){ return accountPW; }
}
