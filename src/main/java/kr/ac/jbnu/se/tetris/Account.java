package kr.ac.jbnu.se.tetris;

public class Account {
    private static Account clientAccount = null;
    private static Account localMultiAccount = null;
    private final String accountID;
    private final String accountPW;
    public Account(String id, char[] pw){
        this.accountID = id;
        this.accountPW = String.valueOf(pw);
    }
    public String getID(){ return accountID; }
    public String getPW(){ return accountPW; }
    public static Account getClientAccount(){ return clientAccount; }
    public static Account getLocalMultiAccount(){ return localMultiAccount; }
    public static void updateClientAccount(Account account){ clientAccount = account; }
    public static void updateLocalMultiAccount(Account account){ localMultiAccount = account; }
    public String getNickName(){
        return getID().split("@")[0];
    }
}
