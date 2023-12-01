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
import java.util.logging.Logger;

public class FirebaseTool {
    public static final String BEST_SCORE = "bestScore";
    private static final Logger logger = Logger.getLogger(FirebaseTool.class.getName());
    private static final String DOMAIN_NAME = "https://jungboproj-default-rtdb.firebaseio.com/";
    private static final String KEY_LOCATION = "./jungboproj-firebase-adminsdk-pco24-58aac1409b.json";
    private static FirebaseTool firebaseTool = null;
    private FirebaseApp firebaseApp;
    private static Firestore db;
    public static FirebaseTool getInstance() throws IOException {
        if (firebaseTool == null) firebaseTool = new FirebaseTool();
        return firebaseTool;
    }
    public FirebaseTool() throws IOException {
        initialize();
    }
    /** Credentials는 json firebase 접근 권한 개인키, option을 통해 도메인 주소에 해당하는 DB를 접근, firebaseApp 초기화 */
    public void initialize() throws IOException{
        FileInputStream serviceAccount = new FileInputStream(KEY_LOCATION);
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .setDatabaseUrl(DOMAIN_NAME)
                .build();
        firebaseApp = FirebaseApp.initializeApp(options);
        initDB();
    }
    /** 원래 static 변수는 클래스 선언과 동시에 초기화 하는게 매우 정석이며, 오류 발생을 줄일 수 있음.
     * 그러나, 해당 방식 적용시 firebase가 아직 초기화가 안된 상태로 오류가 발생함. 이를 방지하고자 static 메소드로 생성자 단계에서
     * 우회하여 접근.
     * */
    public static void initDB(){
        db  = FirestoreClient.getFirestore();
    }
    /** 구형 라이브러리라 getUserByEmailWithPassword를 지원하지 않음. 정보 보안이슈 */
    public Account logIn(Account account){
        UserRecord userRecord;
        try{
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(firebaseApp);
            userRecord = firebaseAuth.getUserByEmail(account.getID());
            if(userRecord.getUid().equals(account.getPW()))
                logger.info("success check");
            else{
                return null;
            }
        }catch (NullPointerException e){
            logger.info("User is null.");
            return null;
        }catch (FirebaseAuthException e){
            logger.info("Error occurs on DB stage");
            return null;
        }
        return new Account(userRecord.getEmail(), userRecord.getUid().toCharArray());
    }
    /** 구형 라이브러리라 EmailWithPassword를 지원하지 않음. 정보 보안이슈 */
    public boolean signUp(Account account) throws NullPointerException, FirebaseAuthException, IllegalArgumentException, ExecutionException, InterruptedException{
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(firebaseApp);
        firebaseAuth.createUser(new UserRecord.CreateRequest()
                .setEmail(account.getID())
                .setUid(account.getPW())
                .setDisplayName(account.getID().split("@")[0]));

        initScore(account.getID().split("@")[0]);

        JOptionPane.showMessageDialog(null, "회원가입에 정상적으로 처리되었습니다.");
        return true;
    }
    /** 상위 10 반환, 그러나 HashMap은 순서를 보장하지 않으므로 이후 정렬함 -> 링크드 해쉬맵을 이용해 볼 계획임 */
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
    /** 해당 컬렉션 하위 문서의 데이터를 덮어쓰지 않고 필요한 부분만 merge */
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
        logger.info("Update time : " + future.get().getUpdateTime());
    }
    /** 유저 점수보다 같거나 낮으면 return으로 미실행 -> firebase 보안규칙을 통해 db단에서 필터링하려 했으나 실패. */
    public void updateUserBestScore(Account account, int score, GameMode mode) throws ExecutionException, InterruptedException {
        Map<String, Object> docData = new HashMap<>();
        docData.put(BEST_SCORE, score);
        // ...
        // future.get() blocks on response
        if(getUserBestScore(account.getID(),mode)<score) {
            ApiFuture<WriteResult> future = db.collection(mode.label()).document(account.getID().split("@")[0]).update(docData);
            logger.info("Update time : " + future.get().getUpdateTime());
        }
    }
    /** 계정 생성시 첫 점수판 초기화 수행 */
    private void initScore(String userId) throws ExecutionException, InterruptedException {
        for(GameMode mode : GameMode.values()){
            setUserBestScore(userId,-1,mode);
        }
    }
}