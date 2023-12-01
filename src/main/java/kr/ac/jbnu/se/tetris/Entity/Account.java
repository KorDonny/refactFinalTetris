package kr.ac.jbnu.se.tetris.Entity;

import java.util.Arrays;

public class Account {
    private static Account clientAccount = null;
    private static Account localMultiAccount = null;
    private final String accountID;
    private final String accountPW;
    public Account(String ID, char[] PW){
        this.accountID = ID;
        this.accountPW = String.valueOf(PW);
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
    public static Account getClientAccount(){ return clientAccount; }
    public static Account getLocalMultiAccount(){ return localMultiAccount; }
    public static void updateClientAccount(Account account){ clientAccount = account; }
    public static void updateLocalMultiAccount(Account account){ localMultiAccount = account; }
}
