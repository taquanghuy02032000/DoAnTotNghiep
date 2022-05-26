package online.javalab.poly.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import online.javalab.poly.MainActivity;
import online.javalab.poly.databinding.ActivityGetStartedBinding;

public class GetStartedActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private ActivityGetStartedBinding binding;
    private static final String AUTH_KEY = "AAAA1B7zZfc:APA91bGq3hOJ-QZmrMtnf5_63KsQvE75_E2HglUoiC3ITPqGrsV8O7IU3o9TOXhS4SGU4xfdmQ-mZcZ_7kxS3NGHVH9AJiz1tzRgZl9vmnk-OvTVh7_51dCEDrZ-VMvIuTvloTFMLngw";
    private TextView mTextView;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetStartedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkFirstRunAndLogin();
        notifi();
    }

    private void checkFirstRunAndLogin() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            binding.progressStarted.setVisibility(View.GONE);
            binding.buttonStartNow.setOnClickListener(v -> {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            });
        } else {
            binding.buttonStartNow.setVisibility(View.GONE);
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }, 500);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void notifi() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String tmp = "";
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                tmp += key + ": " + value + "\n\n";
            }
        }
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    token = task.getException().getMessage();
                } else {
                    token = task.getResult().getToken();
                }
            }
        });

    }


}
