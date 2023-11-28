package kr.ac.jbnu.se.tetris.Control;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.database.*;
import kr.ac.jbnu.se.tetris.Entity.Account;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Consumer;

public class FirebaseTool {

    private static final String DOMAIN_NAME = "https://jungboproj-default-rtdb.firebaseio.com/";
    private static final String KEY_LOCATION = "./jungboproj-firebase-adminsdk-pco24-58aac1409b.json";
    private static final String MEMBER = "user";
    private static FirebaseTool firebaseTool = null;
    private FirebaseApp firebaseApp;
    private DatabaseReference databaseReference;
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
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(DOMAIN_NAME)
                    .build();

            firebaseApp = FirebaseApp.initializeApp(options,MEMBER);
            if (firebaseApp != null){
                databaseReference = FirebaseDatabase.getInstance(firebaseApp).getReference();
            }

        }catch (IOException e){
            throw new RuntimeException(e);
        }
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
    public void getAllUserBestScore(Consumer<ArrayList<HashMap<String, String>>> callback) {
        DatabaseReference userAllScoreDatabase = FirebaseDatabase.getInstance(firebaseApp).getReference();

        userAllScoreDatabase.child("user")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<HashMap<String, String>> userAllBestScore = new ArrayList<HashMap<String, String>>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String id = ds.getKey();
                            String bestscore = ds.child("bestscore").getValue(String.class);

                            HashMap<String, String> userScore = new HashMap<String, String>();
                            userScore.put("id", id);
                            userScore.put("bestscore", bestscore);
                            userAllBestScore.add(userScore);
                        }
                        // 데이터 가져오기 완료 후 내림차순으로 정렬
                        Collections.sort(userAllBestScore, new Comparator<HashMap<String, String>>() {
                            @Override
                            public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
                                return Integer.compare(Integer.parseInt(o2.get("bestscore")), Integer.parseInt(o1.get("bestscore")));
                            }
                        });
                        callback.accept(userAllBestScore);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("Data Error " + databaseError);
                    }
                });
    }

    public void setUserBestScore(String id, String bestscore) {
        try {
            DatabaseReference userScoreDatabase = FirebaseDatabase.getInstance(firebaseApp).getReference();

            userScoreDatabase.child("user").child(id.split("@")[0]).child("bestscore").setValue(bestscore, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                }
            });

        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }
}
