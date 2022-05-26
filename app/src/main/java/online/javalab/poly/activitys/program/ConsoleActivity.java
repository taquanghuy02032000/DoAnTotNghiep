package online.javalab.poly.activitys.program;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.Utils;
import com.xiaoyv.javaengine.JavaEngine;
import com.xiaoyv.javaengine.compile.listener.ExecuteListener;
import com.xiaoyv.javaengine.console.JavaConsole;

import java.util.Objects;

import online.javalab.poly.databinding.ActivityConsoleBinding;

public class ConsoleActivity extends AppCompatActivity {
    public static final String CONSOLE_ACTIVITY_DEX_PATH = "CONSOLE_ACTIVITY_DEX_PATH";
    public static final String CONSOLE_ACTIVITY_CLASS_NAME = "CONSOLE_ACTIVITY_CLASS_NAME";
    private ActivityConsoleBinding binding;
    private String dexPath;
    private String className;

    private JavaConsole javaConsole;
    private final String TAG = "TAG";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConsoleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        initData();
    }

    private void initView() {
        binding.toolbar.setTitle("Output");
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initData() {
        dexPath = getIntent().getStringExtra(CONSOLE_ACTIVITY_DEX_PATH);
        className = getIntent().getStringExtra(CONSOLE_ACTIVITY_CLASS_NAME);

        javaConsole = new JavaConsole(new JavaConsole.AppendStdListener() {
            @Override
            public void printStderr(CharSequence err) {
                showStderr(err);
            }

            @Override
            public void printStdout(CharSequence out) {
                showStdout(out);
            }
        });
        runDexFile(new String[]{""});
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public static void start(String dexPath, String className) {
        Intent intent = new Intent(Utils.getApp().getApplicationContext(), ConsoleActivity.class);
        intent.putExtra(CONSOLE_ACTIVITY_DEX_PATH, dexPath);
        intent.putExtra(CONSOLE_ACTIVITY_CLASS_NAME, className);
        ActivityUtils.startActivity(intent);
    }

    public void showStderr(CharSequence err) {
        Log.e(TAG, "showStderr: " + err);
        binding.consoleOutput.append(Html.fromHtml("<font color=\"#F00\">" + err.toString() + "</font>"));
    }

    public void showStdout(CharSequence out) {
        Log.e("TAG", "showStdout: " + out);
        binding.consoleOutput.append(out);
    }

    public void runDexFile(String[] args) {
        javaConsole.start();
        ExecuteListener executeListener = new ExecuteListener() {
            @Override
            public void onExecuteFinish() {
                javaConsole.stop();
            }

            @Override
            public void onExecuteError(Throwable error) {
                javaConsole.stop();
            }
        };

        if (!StringUtils.isEmpty(className)) {
            JavaEngine.getDexExecutor().exec(dexPath, className, args, executeListener);
        } else {
            JavaEngine.getDexExecutor().exec(dexPath, args, executeListener);
        }
    }

}
