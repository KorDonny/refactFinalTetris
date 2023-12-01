package kr.ac.jbnu.se.tetris.control;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.cloud.firestore.Query;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import kr.ac.jbnu.se.tetris.entity.Account;
import kr.ac.jbnu.se.tetris.entity.GameMode;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class FirebaseTool {
    public static final String BEST_SCORE = "bestScore";
    private static final String DOMAIN_NAME = "https://jungboproj-default-rtdb.firebaseio.com/";
    private static final String KEY_LOCATION = "./jungboproj-firebase-adminsdk-pco24-58aac1409b.json";
    private static FirebaseTool firebaseTool = null;
    private FirebaseApp firebaseApp;
    private static Firestore db;
    public static FirebaseTool getInstance(){
        if (firebaseTool == null) firebaseTool = new FirebaseTool();
        return firebaseTool;
    }
    public FirebaseTool(){
        initialize();
    }
    public void initialize(){
        try{
            FileInputStream serviceAccount = new FileInputStream(KEY_LOCATION);
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .setDatabaseUrl(DOMAIN_NAME)
                    .build();
            firebaseApp = FirebaseApp.initializeApp(options);
            initStore();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    private static void initStore(){ db = FirestoreClient.getFirestore(); }
    public Account logIn(Account account){
        UserRecord userRecord;
        try{
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(firebaseApp);
            userRecord = firebaseAuth.getUserByEmail(account.getID());
            if(userRecord.getUid().equals(account.getPW()))
                System.out.println("success check");
            else{
                return null;
            }
        }catch (NullPointerException | FirebaseAuthException e){
            if(e instanceof NullPointerException) JOptionPane.showMessageDialog(null, "User is null.");
            else if(e instanceof FirebaseAuthException) JOptionPane.showMessageDialog(null, "Error occurs on DB stage");
            return null;
        }
        return new Account(userRecord.getEmail(), userRecord.getUid().toCharArray());
    }
    public boolean signUp(Account account) {
        try {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(firebaseApp);
            firebaseAuth.createUser(new UserRecord.CreateRequest()
                    .setEmail(account.getID())
                    .setUid(account.getPW())
                    .setDisplayName(account.getID().split("@")[0]));

            initScore(account.getID().split("@")[0]);

            JOptionPane.showMessageDialog(null, "회원가입에 정상적으로 처리되었습니다.");
            return true;
        }catch (FirebaseAuthException | IllegalArgumentException | ExecutionException | InterruptedException | NullPointerException e) {
            e.printStackTrace();
            /* Clean up whatever needs to be handled before interrupting  */
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
    public static Map<String,Integer> getModeBestScoreChart(GameMode mode) throws ExecutionException, InterruptedException {
        CollectionReference singleScore = db.collection(mode.label());
        Query query = singleScore.orderBy(BEST_SCORE, Query.Direction.DESCENDING).limit(10);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        Map<String,Integer> result = new LinkedHashMap<>();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            result.put(document.getId(),Integer.parseInt(document.getData().get(BEST_SCORE).toString()));
        }
        return result;
    }
    /**
     * p.s) future.get().getData()단이 기존 아이디가 존재하지 않을 때,
     * null값을 반환함. 그것때문에 정상 시퀀스 Integer.parseInt부분에서 Nullpointer exception 발생.
     * 이를 막고자 null일시 -1을 반환하고 ,setUser에서 사용시 신규 등록 상황에서 오류 없이 초기화시킬 수 있음.
     * */
    public static int getUserBestScore(String userID, GameMode mode) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection(mode.label()).document(userID.split("@")[0]);
        // asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = docRef.get();
        if(future.get().getData()==null)return -1;
        return Integer.parseInt(future.get().getData().get(BEST_SCORE).toString());
    }
    private static void setUserBestScore(String userID, int score, GameMode mode) throws ExecutionException, InterruptedException {
        // Create a Map to store the data we want to set
        Map<String, Object> docData = new HashMap<>();
        docData.put(BEST_SCORE, score);
        // ...
        // future.get() blocks on response
        // Add a new document (asynchronously) in collection "cities" with id "LA"
        ApiFuture<WriteResult> future = db.collection(mode.label()).document(userID).set(docData,SetOptions.merge());
        // ...
        // future.get() blocks on response
        System.out.println("Update time : " + future.get().getUpdateTime());
    }
    public void updateUserBestScore(Account account, int score, GameMode mode) throws ExecutionException, InterruptedException {
        Map<String, Object> docData = new HashMap<>();
        docData.put(BEST_SCORE, score);

        // future.get() blocks on response
        if(getUserBestScore(account.getID(),mode)<score){
            ApiFuture<WriteResult> future = db.collection(mode.label()).document(account.getID().split("@")[0]).update(docData);
            System.out.println("Update time : " + future.get().getUpdateTime());
        }
    }
    private void initScore(String userId) throws ExecutionException, InterruptedException {
        for(GameMode mode : GameMode.values()){
            setUserBestScore(userId,-1,mode);
        }
    }
}