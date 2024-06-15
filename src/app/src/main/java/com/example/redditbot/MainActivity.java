package com.example.redditbot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.redditbot.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    CurrentUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        user = CurrentUser.getInstance();
        SubredditList list = loadSubreddits(getApplicationContext());
        AgentInfo agentInfo = loadAgentInfo(getApplicationContext());

        user.setSubreddits(list);
        user.setAgent(agentInfo);

        ChangeFragment(new HomeNavHost());

        binding.BottomNavView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                ChangeFragment(new HomeNavHost());
            } else if (id == R.id.nav_settings) {
                ChangeFragment(new SettingsFragment());
            }

            return true;
        });
    }

    @Override
    protected void onDestroy() {
        saveSubreddits(user.getSubreddits(), getApplicationContext());
        saveAgentInfo(user.getAgent(), getApplicationContext());
        super.onDestroy();
    }

    /**
     * Use this method to display the fragment that is passed
     * as an argument
     *
     * @param fragment The fragment we want to display.
     */
    private void ChangeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
    public void saveAgentInfo(AgentInfo agentInfo, Context context) {
        try (FileOutputStream fos = context.openFileOutput("agent-info.ser", Context.MODE_PRIVATE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(agentInfo);
        } catch (IOException e) {
            Log.w("FileSaving", "Error: " + e);
        }
    }
    public AgentInfo loadAgentInfo(Context context) {
        try (FileInputStream fis = context.openFileInput("agent-info.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (AgentInfo) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Log.w("FileLoading", "Error: " + e);;
        }
        return null;
    }

    public void saveSubreddits(SubredditList list, Context context) {
        try (FileOutputStream fos = context.openFileOutput("subreddits.ser", Context.MODE_PRIVATE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(list);
        } catch (IOException e) {
            Log.w("FileSaving", "Error: " + e);
        }
    }
    public SubredditList loadSubreddits(Context context) {
        try (FileInputStream fis = context.openFileInput("subreddits.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (SubredditList) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Log.w("FileLoading", "Error: " + e);;
        }
        return null;
    }
}