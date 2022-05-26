package online.javalab.poly.activitys.program;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.ContentLoadingProgressBar;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.googlejavaformat.FormatterDiagnostic;
import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import com.google.googlejavaformat.java.JavaFormatterOptions;
import com.xiaoyv.editor.common.Lexer;
import com.xiaoyv.javaengine.JavaEngine;
import com.xiaoyv.javaengine.JavaEngineSetting;
import com.xiaoyv.javaengine.compile.listener.CompilerListener;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import online.javalab.lib.JavaStudioSetting;
import online.javalab.lib.editor.code.SymbolView;
import online.javalab.poly.R;
import online.javalab.poly.databinding.ActivityEditorBinding;
import online.javalab.poly.model.ProgramDetail;

public class EditorActivity extends AppCompatActivity {
    private ActivityEditorBinding binding;
    private AlertDialog outputDialog;
    private AppCompatImageView outputCloseView;
    private AppCompatImageView outputEditView;
    private View outputDialogLayout;
    private AppCompatTextView outputConsole;
    private ContentLoadingProgressBar outputProgressBar;
    private ProgramDetail programDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        initData();
        initListener();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_play:
                binding.toolbar.setSubtitle("");
                KeyboardUtils.hideSoftInput(binding.editorLayout);
                if (binding.editorLayout.isEmpty()) return false;
                boolean edited = binding.editorLayout.isEdited();
                if (edited) {
                    binding.editorLayout.save();
                }
                binding.editorLayout.save();
                runFile(binding.editorLayout.getFile());
                //compileCurrentProject(JavaProject.getCurrentSrcDir());
                return false;
            case R.id.menu_undo:
                binding.editorLayout.undo();
                return false;
            case R.id.menu_redo:
                binding.editorLayout.redo();
                return false;
            case R.id.menu_format:
                formatJavaCode(binding.editorLayout.getText().toString(), false);
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initView() {
        binding.toolbar.setTitle(R.string.title_compiler);
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        outputDialogLayout = LayoutInflater.from(EditorActivity.this).inflate(R.layout.view_editor_ouput, null);
        outputConsole = outputDialogLayout.findViewById(R.id.editor_output_view);
        outputProgressBar = outputDialogLayout.findViewById(R.id.editor_output_progress);
        outputEditView = outputDialogLayout.findViewById(R.id.editor_output_edit);
        outputCloseView = outputDialogLayout.findViewById(R.id.editor_output_close);
    }

    private void initData() {
        Intent intent = getIntent();
        programDetail = (ProgramDetail) intent.getSerializableExtra(ProgramDetailActivity.BUNDLE_PROGRAM_DETAIL);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            try {
                Path path = Paths.get(PathUtils.getExternalAppCachePath() + "/JavaStudio.java");
                writeFile(path, setInputSample());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        binding.editorLayout.loadAppInfoJavaFile();
        // Set the syntax prompt data source
        ThreadUtils.getCachedPool().execute(() ->
                Lexer.getLanguage().addRtIdentifier(JavaEngineSetting.getRtPath()));
    }

    private void initListener() {
        // Symbol column monitoring
        binding.editorTabSymbol.setOnSymbolViewClick((view, text) -> {
            if (text.equals(SymbolView.TAB)) {
                // TAB key, two spaces
                binding.editorLayout.insert(binding.editorLayout.getCaretPosition(), "  ");
            } else {
                binding.editorLayout.insert(binding.editorLayout.getCaretPosition(), text);
            }
        });

        // Editor edit monitoring
        binding.editorLayout.setOnEditListener(() -> {
            if (binding.editorLayout.isEdited()) {
                binding.toolbar.setSubtitle("(Not saved yet)");
            }
        });

        // Soft keyboard monitor
        KeyboardUtils.registerSoftInputChangedListener(this.getWindow(), height -> {
            RelativeLayout.LayoutParams params =
                    (RelativeLayout.LayoutParams) binding.editorTabSymbol.getLayoutParams();
            // To subtract the height of the homepage BottomView
//            params.bottomMargin = height - ConvertUtils.dp2px(56);
            binding.editorTabSymbol.setLayoutParams(params);
            binding.editorTabSymbol.setVisible(height > 0);
        });

        // Query guide package
        binding.editorLayout.setImportBtnClickListener(this::findPackage);
    }

    /**
     * Presenter
     */
    public void findPackage(String selectedText, boolean isHide) {
        // If the selected non-word, skip
        if (selectedText.trim().contains(" ")) {
            return;
        }

        HashMap<String, String> identifier = Lexer.getLanguage().getIdentifier();
        String className;
        if (identifier.containsKey(selectedText)) {
            className = identifier.get(selectedText);
        } else {
            if (!isHide)
                ToastUtils.showShort("Class not found in JDK:" + selectedText);
            return;
        }

        String importText = "import " + className + ";";
        sendImportText(importText);
    }

    public void runFile(File javaFile) {
        showLog();
        showNormalInfo("Tips: First time compiling need to download environment\n\n>>> Compilation begins");

        File saveClassDir = new File(PathUtils.getExternalAppCachePath() + "/class");
        File saveDexFile = new File(PathUtils.getExternalAppCachePath() + "/dex/temp.dex");

        /*
         * Compile xxx.java into xxx.class file
         */
        JavaEngine.getClassCompiler().compile(javaFile, saveClassDir, new CompilerListener() {
            @Override
            public void onSuccess(String path) {
                ThreadUtils.runOnUiThread(() ->
                        showNormalInfo(String.format(">>> Compilation has ended\n>>> Output line：%s\n>>> Start converting",
                                path.replace(PathUtils.getExternalAppDataPath(), ""))));

                /*
                 * Convert xxx.class to xxx.dex file
                 */
                JavaEngine.getDexCompiler().compile(path, saveDexFile.getAbsolutePath(), new CompilerListener() {
                    @Override
                    public void onSuccess(String path) {
                        ThreadUtils.runOnUiThread(() -> {
                            showNormalInfo(String.format(">>> Conversion is complete\n>>> Output line：%s\nTips: Please close the log box to run the program",
                                    path.replace(PathUtils.getExternalAppDataPath(), "")));
                            showLogDismissListener(path);
                        });
                    }

                    @Override
                    public void onError(Throwable error) {
                        ThreadUtils.runOnUiThread(() -> showErrorInfo(error.getMessage()));
                    }

                    @Override
                    public void onProgress(String task, int progress) {
                        showProgress(task.replace(PathUtils.getExternalAppDataPath(), ""), progress);
                    }
                });
            }

            @Override
            public void onError(Throwable error) {
                ThreadUtils.runOnUiThread(() -> showErrorInfo(error.getMessage()));
            }

            @Override
            public void onProgress(String task, int progress) {
                showProgress(task.replace(PathUtils.getExternalAppDataPath(), ""), progress);
            }
        });
    }

    public void formatJavaCode(String sourceCode, boolean ignoreError) {
        ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<Object>() {
            @Override
            public Object doInBackground() throws Throwable {
                JavaFormatterOptions javaFormatterOptions = JavaStudioSetting.getJavaFormatterOptions();
                Formatter formatter = new Formatter(javaFormatterOptions);
                return formatter.formatSource(sourceCode);
            }

            @Override
            public void onFail(Throwable t) {
                if (ignoreError) return;
                if (t instanceof FormatterException) {
                    FormatterException exception = (FormatterException) t;
                    formatCodeFail(exception);
                }
            }

            @Override
            public void onSuccess(Object result) {
                formatCodeSuccess(String.valueOf(result));
            }
        });
    }

    /**
     * View
     */
    public void sendImportText(String importText) {
        int oldCaretPosition = binding.editorLayout.getCaretPosition();
        String code = binding.editorLayout.getText().toString();

        if (code.contains(importText)) {
            return;
        }

        if (code.trim().contains("package")) {
            String tempPackage = code.substring(0, code.indexOf(";") + 1);
            binding.editorLayout.insert(tempPackage.length() + 1, "\n" + importText);
            binding.editorLayout.moveCaret(oldCaretPosition);
            binding.editorLayout.moveCaretDown();
            return;
        }
        binding.editorLayout.insert(0, importText + "\n");
        binding.editorLayout.moveCaret(oldCaretPosition);
        binding.editorLayout.moveCaretDown();
    }

    public void formatCodeSuccess(String sourceCode) {
        binding.editorLayout.setText(sourceCode);
        binding.editorLayout.save();
    }

    public void formatCodeFail(FormatterException exception) {
        // Error message
        List<FormatterDiagnostic> diagnostics = exception.diagnostics();
        if (ObjectUtils.isNotEmpty(diagnostics)) {
            FormatterDiagnostic diagnostic = diagnostics.get(0);
            int line = diagnostic.line();
            int column = diagnostic.column();
            String message = diagnostic.message();
            message = "line:" + line + "\ncolumn:" + column + "\nerror:" + message;
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Alignment failed")
                    .setMessage(message)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        binding.editorLayout.gotoLine(line);
                        int caretPosition = binding.editorLayout.getCaretPosition();
                        binding.editorLayout.moveCaret(caretPosition + column);
                    })
                    .create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }

    public void showLog() {
        if (outputDialog == null) {
            outputCloseView.setOnClickListener(v ->
                    outputDialog.dismiss());
            outputEditView.setOnClickListener(v -> {
                outputConsole.setTextIsSelectable(true);
                outputConsole.requestFocus();
                Toast.makeText(this, "Log copy mode is on", Toast.LENGTH_SHORT).show();
            });
            outputDialog = new AlertDialog.Builder(this, R.style.console_dialog)
                    .setView(outputDialogLayout)
                    .create();
            outputDialog.setCanceledOnTouchOutside(false);
        }
        outputDialog.show();
        outputConsole.setText(null);
        outputConsole.setTextIsSelectable(false);
        outputCloseView.setEnabled(false);
        outputProgressBar.setProgress(0);
        outputProgressBar.setIndeterminate(true);
        outputDialog.setCancelable(false);

        Window window = outputDialog.getWindow();
        if (window != null) {
            window.setGravity(Gravity.BOTTOM);
            window.getDecorView().setPadding(0, 0, 0, 0);
            Display display = this.getWindowManager().getDefaultDisplay();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = display.getWidth(); // Set full screen width
            lp.height = display.getHeight() / 2;// Set half screen width
            window.setAttributes(lp);
        }
    }

    public void showProgress(String task, int progress) {
        if (!outputDialog.isShowing()) outputDialog.show();
        outputProgressBar.setIndeterminate(false);
        outputProgressBar.setProgress(progress);
        outputConsole.append(String.format(">>> Compilation progress：%s\n", FileUtils.getFileName(task)));
    }

    public void showErrorInfo(String err) {
        String error = err;
        if (err.equalsIgnoreCase("编译代码源不存在")) {
            error = err.replace("编译代码源不存在", "The compiled code source does not exist");
        } else {
            error = err.replace("字节码编译错误", "Bytecode compilation error");
        }
        if (!outputDialog.isShowing()) outputDialog.show();
        outputCloseView.setEnabled(true);
        outputDialog.setOnDismissListener(null);
        outputConsole.append(Html.fromHtml("<br><font color=\"#FF0000\">" + error.replace("\n", "<br>") + "</font>"));
    }

    public void showNormalInfo(String out) {
        if (!outputDialog.isShowing()) outputDialog.show();
        outputConsole.append(out);
        outputConsole.append("\n");
    }

    public void showLogDismissListener(String dexPath) {
        outputCloseView.setEnabled(true);
        outputDialog.setOnDismissListener(dialog -> {
            // Run Dex file
            ConsoleActivity.start(dexPath, null);
        });
    }

    @SuppressLint("SetTextI18n")
    private String setInputSample() {
        return "public class JavaStudio {\n" +
                "    public static void main(String[] args) {\n" +
                programDetail.getContent() +
                "    }\n" +
                "}";
    }

    private static void writeFile(Path path, String content)
            throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Files.write(path, Collections.singleton(content));
        }
    }
}
