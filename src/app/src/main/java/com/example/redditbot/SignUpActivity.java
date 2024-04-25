package com.example.redditbot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.redditbot.databinding.ActivityMainBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    TextInputEditText usernameEdit;
    Button signUpButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        usernameEdit = findViewById(R.id.name_edit);
        signUpButton = findViewById(R.id.button);

        CurrentUser user = CurrentUser.getInstance();
        FirebaseDB firebaseDBInstance = FirebaseDB.getInstance();

        @SuppressLint("HardwareIds") String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        user.setDeviceId(deviceId);
        firebaseDBInstance.userExists(new FirebaseDB.GetBooleanCallBack() {
            @Override
            public void onResult(Boolean bool) {
               if (bool) {
                   firebaseDBInstance.loginUser();
                   signUpComplete();
               } else {
                   usernameEdit.setVisibility(View.VISIBLE);
               }
            }
        });
        usernameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text.length() < 4) {
                    usernameEdit.setError("Username must be at least 4 characters");
                    signUpButton.setEnabled(false);
                } else if (text.length() > 20) {
                    usernameEdit.setError("Username must be less than 20 characters");
                    signUpButton.setEnabled(false);
                } else if (text.contains(" ")) {
                    usernameEdit.setError("Username cannot have whitespace");
                    signUpButton.setEnabled(false);
                } else {
                    firebaseDBInstance.usernameExists(text, new FirebaseDB.GetBooleanCallBack() {
                        @Override
                        public void onResult(Boolean bool) {
                            if (bool) {
                                usernameEdit.setError("Username already taken");
                                signUpButton.setEnabled(false);
                            } else {
                                usernameEdit.setError(null);
                                signUpButton.setEnabled(true);
                            }
                        }
                    });
                }
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUsername(Objects.requireNonNull(usernameEdit.getText()).toString().toLowerCase());
                firebaseDBInstance.loginUser();
                signUpComplete();
            }
        });

    }

    public void signUpComplete() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }
}