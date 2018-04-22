import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;

import java.io.FileInputStream;

public class DialyWorker {
    private static DatabaseReference database;

    public static void main(String[] args) throws Exception{

        FileInputStream serviceAccount =
                new FileInputStream("path/to/serviceAccountKey.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://shooplists.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);

        database = FirebaseDatabase.getInstance().getReference();
        while (true) {
            try {
                Thread.sleep(1000*60);
            } catch (InterruptedException e) {
            }
            getUsers();
        }
    }

    private static void getUsers(){
        database.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    System.out.println(data.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
