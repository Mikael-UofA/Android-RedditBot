package com.example.redditbot;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FirebaseDB {

    private static final FirebaseDB instance = new FirebaseDB();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference agentCollection = db.collection("Agents");
    private final CollectionReference userCollection = db.collection("Users");
    private final CollectionReference subredditCollection = db.collection("Subreddits");
    final String agentsTAG = "Agents";
    final String usersTAG = "Users";
    final String subredditsTAG = "Subreddits";

    private FirebaseDB() {
    }
    public static FirebaseDB getInstance() { return instance; }

    public interface GetBooleanCallBack { void onResult(Boolean bool); }

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
                        Log.w(usersTAG, "Could not fetch user document: " + e);
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
                        Log.w(agentsTAG, "Could not fetch user document: " + e);
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
                        Log.w(usersTAG, "Error while adding user document: " + e);
                    }
                });
    }
    public void createAgent(UserAgent agent) {
        agentCollection
                .add(agent)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(agentsTAG, "Error while adding agent document: ", e);
                    }
                });
    }
    public void getAgent() {
        CurrentUser user = CurrentUser.getInstance();
        agentCollection
                .whereEqualTo("agentAuthorName", user.getUsername())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            UserAgent agent = documentSnapshot.toObject(UserAgent.class);
                            user.setAgent(agent);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(agentsTAG, "Error while retrieving agent document: ", e);
                    }
                });
    }
    public void loginUser(String username, String pass, GetBooleanCallBack callBack) {
        CurrentUser user = CurrentUser.getInstance();
        userCollection
                .document(username)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (Objects.equals(pass, documentSnapshot.getString("pass"))) {
                            user.setUsername(username);
                            user.setPass(pass);

                            getAgent();
                            callBack.onResult(true);
                        } else {
                            callBack.onResult(false);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(usersTAG, "Error while logging in: ", e);
                    }
                });
    }
    public void deleteAgent(String agentId) {
        CurrentUser user = CurrentUser.getInstance();
        agentCollection
                .document(agentId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        userCollection.document(user.getUsername()).update("agentId", null)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        user.setAgentId(null);
                                        user.setAgent(null);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(usersTAG, "Error while updating user: ", e);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(agentsTAG, "Error while deleting agent document: ", e);
                    }
                });
    }
    public void deleteAccount() {
        CurrentUser user = CurrentUser.getInstance();
        if (user.getAgentId() != null) {
            deleteAgent(user.getAgentId());
        }
        userCollection.document(user.getUsername()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        user.setUsername(null);
                        user.setPass(null);
                        System.exit(0);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(usersTAG, "Error while deleting user document: ", e);
                    }
                });
    }
    public void updatePass(String newPass) {
        CurrentUser user = CurrentUser.getInstance();
        userCollection.document(user.getUsername()).update("pass", newPass)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(usersTAG, "Error while updating password: ", e);
                    }
                });
    }
    public void addSubreddit(Subreddit subreddit) {
        subredditCollection
                .add(subreddit)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(subredditsTAG, "Error while adding subreddit: ", e);
                    }
                });
    }
}
