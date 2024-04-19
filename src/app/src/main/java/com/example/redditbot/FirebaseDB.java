package com.example.redditbot;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirebaseDB {

    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference agentCollection = db.collection("Agents");
    final CollectionReference userCollection = db.collection("Users");
    final String agentsTAG = "Agents";
    final String usersTag = "Users";

    public interface GetBooleanCallBack {
        void onResult(Boolean bool);
    }

    public void userExists(String username, GetBooleanCallBack callBack) {
        userCollection
                .document(username).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        callBack.onResult(documentSnapshot.exists());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(usersTag, "Could not fetch user document" + e);
                    }
                });
    }

    public void agentExists(String id, GetBooleanCallBack callBack) {
        agentCollection
                .document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        callBack.onResult(documentSnapshot.exists());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(usersTag, "Could not fetch user document" + e);
                    }
                });
    }

    public void createUser() {
        CurrentUser user = CurrentUser.getInstance();
        Map<String, String> data = new HashMap<>();
        data.put("username", user.getUsername());
        data.put("pass", user.getPass());
        data.put("agentId", null);

        userCollection
                .document(user.getUsername())
                .set(data)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(usersTag, "Error while adding user document" + e);
                    }
                });
    }

    public void createAgent(UserAgent agent) {
        CurrentUser user = CurrentUser.getInstance();
        agentCollection
                .add(agent)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        user.setAgentId(documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(agentsTAG, "Error while adding agent document", e);
                    }
                });
    }

}
