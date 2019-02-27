package com.ca1.consumer;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;

import java.io.FileInputStream;
import java.util.concurrent.atomic.AtomicInteger;

public class Database {

    private static final String DATABASE_URL = "https://iotsensornci.firebaseio.com/";
    private static DatabaseReference database;


    /*
            This method is a generic method to allow user to call which sensor to call in firebase and retrieve last child continuously.
     */
    public static void listenToSensor(final String sensorType) {
        database.child("data/" + sensorType ).limitToLast(1).addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println("Checking sensor: " + sensorType);
                System.out.println(dataSnapshot.getValue());
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // NOT REQUIRED
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // NOT REQUIRED
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                // NOT REQUIRED
            }

            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getCode());
            }
        });
    }
    public static void main(String[] args) {
        try {
            FileInputStream serviceAccount =
                    new FileInputStream("C:\\Users\\jalwandy\\IdeaProjects\\SensorConsumer\\src\\main\\java\\com\\ca1\\consumer\\iotsensornci-firebase-adminsdk-fouaq-da0e43822e.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(DATABASE_URL)
                    .build();
            FirebaseApp.initializeApp(options); // Initilize the firebase framework with settings above
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        database = FirebaseDatabase.getInstance().getReference(); // Generic variable to allow database variable object
        while(true) {
            listenToSensor("temperature");
            listenToSensor("distance");
            try {
                Thread.sleep(5000); // Queries database every five seconds
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
