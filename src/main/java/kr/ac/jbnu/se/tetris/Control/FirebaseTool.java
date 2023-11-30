package kr.ac.jbnu.se.tetris.Control;

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
import com.google.firebase.database.*;
import kr.ac.jbnu.se.tetris.Entity.Account;
import kr.ac.jbnu.se.tetris.Entity.GameMode;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class FirebaseTool {
    private static final String DOMAIN_NAME = "https://jungboproj-default-rtdb.firebaseio.com/";
    private static final String KEY_LOCATION = "./jungboproj-firebase-adminsdk-pco24-58aac1409b.json";
    private static final String MEMBER = "user";
    private static FirebaseTool firebaseTool = null;
    private FirebaseApp firebaseApp;
    private DatabaseReference databaseReference;
    private static Firestore db;
    public final static String BEST_SCORE = "bestScore";
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
            if (firebaseApp != null){
                databaseReference = FirebaseDatabase.getInstance(firebaseApp).getReference();
            }
            db = FirestoreClient.getFirestore();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        //test for collection firestore
//        try {
//            forTestingUpdateScoreBoard();
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }
    public boolean logIn(String id, String password){
        try{
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(firebaseApp);
            UserRecord userRecord = firebaseAuth.getUser(id);
            if(userRecord!=null){
                if(userRecord.getUid().equals(id)){
                    return true;
                }
            }
        }catch (NullPointerException e){
            return false;
        }catch (FirebaseAuthException e){
            return false;
        }
        return false;
    }
    public boolean signUp(String id, String password) {
        try {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(firebaseApp);
            firebaseAuth.createUser(new UserRecord.CreateRequest()
                    .setUid(id)
                    .setPassword(password)
                    .setDisplayName(id));

            DatabaseReference initReference = FirebaseDatabase.getInstance(firebaseApp).getReference();

            for (Account.scoreType scores : Account.scoreType.values()){
                initReference.child(MEMBER).child(id.split("@")[0]).child(scores.getTag()).setValue("0", null);
            }

            JOptionPane.showMessageDialog(null, "회원가입에 정상적으로 처리되었습니다.");
            return true;

        } catch (NullPointerException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "회원가입에 문제가 생겼습니다.");
            return  false;
        } catch (FirebaseAuthException e) {
            JOptionPane.showMessageDialog(null, "회원가입에 문제가 생겼습니다.");
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
        DocumentReference docRef = db.collection(mode.label()).document(userID);
        // asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = docRef.get();
        if(future.get().getData()==null)return -1;
        return Integer.parseInt(future.get().getData().get(BEST_SCORE).toString());
    }
    public static void setUserBestScore(String userID, int score, GameMode mode) throws ExecutionException, InterruptedException {
        // Create a Map to store the data we want to set
        Map<String, Object> docData = new HashMap<>();
        docData.put(BEST_SCORE, score);
        // ...
        // future.get() blocks on response
        if(getUserBestScore(userID,mode)>=score) return;
        // Add a new document (asynchronously) in collection "cities" with id "LA"
        ApiFuture<WriteResult> future = db.collection(mode.label()).document(userID).set(docData,SetOptions.merge());
        // ...
        // future.get() blocks on response
        System.out.println("Update time : " + future.get().getUpdateTime());
    }
    public void forTestingUpdateScoreBoard() throws ExecutionException, InterruptedException {
        final String test = "test";
        for (GameMode mode : GameMode.values()){
            for(int i = 0; i < 10; i++){
                setUserBestScore(test+(i+1),i+1, mode);
            }
        }
        //getModeBestScoreChart(GameMode.NORMAL);
    }
}
