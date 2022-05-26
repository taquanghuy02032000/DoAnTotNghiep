package online.javalab.lib.editor.code;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.KeyEvent;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.xiaoyv.editor.android.FreeScrollingTextField;
import com.xiaoyv.editor.android.YoyoNavigationMethod;
import com.xiaoyv.editor.common.ColorScheme;
import com.xiaoyv.editor.common.ColorSchemeDark;
import com.xiaoyv.editor.common.ColorSchemeLight;
import com.xiaoyv.editor.common.Document;
import com.xiaoyv.editor.common.DocumentProvider;
import com.xiaoyv.editor.common.LanguageJava;
import com.xiaoyv.editor.common.Lexer;

import java.io.File;

public class TextEditor extends FreeScrollingTextField {
    private final Context mContext;
    // Currently open text
    private Document currentDocument;
    // Word wrap
    private boolean isWordWrap;
    // Open file
    private File currentFile;
    private int index;

    public TextEditor(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public TextEditor(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mContext = context;
        init();
    }

    private void init() {
        // Set font
//        setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/Consolas.ttf"));
        // Set font size
        setTextSize(ConvertUtils.sp2px(FreeScrollingTextField.BASE_TEXT_SIZE_PIXELS));
        // Show line number
        setShowLineNumbers(true);
        // Set up automatic reminders
        setAutoCompete(true);
        // Current line highlight
        setHighlightCurrentRow(true);
        // Set whether to wrap automatically
        setWordWrap(false);
        // Set auto indent width
        setAutoIndentWidth(2);
        // Set the water drop cursor operation when selecting text
        setNavigationMethod(new YoyoNavigationMethod(this));
        // Set up parsing
        Lexer.setLanguage(LanguageJava.getInstance());
        // Set automatic prompt syntax analysis
        autoCompletePanel.setLanguage(LanguageJava.getInstance());

        reSpan();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (index != 0 && right > 0) {
            moveCaret(index);
            index = 0;
        }
    }

    /**
     * Set whether to night mode
     */
    public void setDark(boolean isDark) {
        if (isDark)
            setColorScheme(new ColorSchemeDark());
        else
            setColorScheme(new ColorSchemeLight());
    }

    /**
     * Set keyword color
     *
     * @param color Keyword color
     */
    public void setKeywordColor(int color) {
        getColorScheme().setColor(ColorScheme.ColorType.KEYWORD, color);
    }

    /**
     * Set comment color
     *
     * @param color Comment color
     */
    public void setCommentColor(int color) {
        getColorScheme().setColor(ColorScheme.ColorType.COMMENT, color);
    }

    /**
     * Set background color
     *
     * @param color Background color
     */
    public void setBackgroundColor(int color) {
        getColorScheme().setColor(ColorScheme.ColorType.BACKGROUND, color);
    }

    /**
     * Set text color
     *
     * @param color Text color
     */
    public void setTextColor(int color) {
        getColorScheme().setColor(ColorScheme.ColorType.FOREGROUND, color);
    }

    /**
     * Set the selected background color
     *
     * @param color Select the background color
     */
    public void setSelectedTextBackgroundColor(int color) {
        getColorScheme().setColor(ColorScheme.ColorType.SELECTION_BACKGROUND, color);
    }

    /**
     * Set the error line color to highlight
     *
     * @param isHighlight Whether to highlight the wrong color
     */
    public void setLineHighlightError(boolean isHighlight) {
        getColorScheme().setColor(ColorScheme.ColorType.LINE_HIGHLIGHT, isHighlight ? 0X50FF0000 : ColorScheme.COLOR_LINE_HIGHLIGHT);
    }

    /**
     * Set start selection text
     *
     * @param index Cursor position
     */
    public void setSelection(int index) {
        selectText(false);
        if (!hasLayout())
            moveCaret(index);
        else
            this.index = index;
    }

    /**
     * Get the selected part of the text
     *
     * @return Selected part of the text
     */
    public String getSelectedText() {
        return documentProvider.subSequence(getSelectionStart(), getSelectionEnd() - getSelectionStart()).toString();
    }

    /**
     * open a file
     *
     * @param fileName file path
     */
    public void open(String fileName) {
        open(new File(fileName));
    }

    /**
     * open a file
     *
     * @param inputFile document
     */
    public void open(File inputFile) {
        currentFile = inputFile;
        // Read file content
        ThreadUtils.getCachedPool().execute(() -> {
            String string = FileIOUtils.readFile2String(inputFile.getAbsolutePath());
            ThreadUtils.runOnUiThread(() -> setText(string));
        });
    }

    /**
     * save document
     */
    public File save() {
        if (currentFile != null)
            FileIOUtils.writeFileFromString(currentFile, getText().toString());
        return currentFile;
    }

    /**
     * Get the current file
     *
     * @return Current file
     */
    public File getFile() {
        return currentFile;
    }

    /**
     * Close file
     */
    public void close() {
        save();
        setText(null);
        currentFile = null;
    }

    /**
     * Determine whether the file opened by the editor is empty
     *
     * @return Is the open file empty
     */
    public boolean isEmpty() {
        return currentFile == null;
    }

    /**
     * Jump to the specified line
     *
     * @param line Designated line
     */
    public void gotoLine(int line) {
        if (line > documentProvider.getRowCount()) {
            line = documentProvider.getRowCount();
        }
        int i = getText().getLineOffset(line - 1);
        setSelection(i);
    }

    /**
     * Shortcut key operation
     */
    @Override
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        final int filteredMetaState = event.getMetaState() & ~KeyEvent.META_CTRL_MASK;
        if (KeyEvent.metaStateHasNoModifiers(filteredMetaState)) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_A:
                    selectAll();
                    return true;
                case KeyEvent.KEYCODE_X:
                    cut();
                    return true;
                case KeyEvent.KEYCODE_C:
                    copy();
                    return true;
                case KeyEvent.KEYCODE_V:
                    paste();
                    return true;
            }
        }
        return super.onKeyShortcut(keyCode, event);
    }


    @Override
    public void setWordWrap(boolean enable) {
        isWordWrap = enable;
        super.setWordWrap(enable);
    }

    /**
     * Set text
     *
     * @param text text
     */
    public void setText(CharSequence text) {
        documentProvider.setWordWrap(isWordWrap);
        documentProvider.setText(text);
        setDocumentProvider(documentProvider);
    }

    /**
     * Get text
     *
     * @return text
     */
    public DocumentProvider getText() {
        return createDocumentProvider();
    }

    /**
     * insert
     *
     * @param idx  Insert position
     * @param text Insert text
     */
    public void insert(int idx, String text) {
        selectText(false);
        moveCaret(idx);
        paste(text);
    }

    /**
     * undo
     */
    public void undo() {
        DocumentProvider doc = createDocumentProvider();
        int newPosition = doc.undo();
        if (newPosition >= 0) {
            setEdited(true);
            reSpan();
            selectText(false);
            moveCaret(newPosition);
            invalidate();
        }
    }

    /**
     * Redo
     */
    public void redo() {
        DocumentProvider doc = createDocumentProvider();
        int newPosition = doc.redo();
        if (newPosition >= 0) {
            setEdited(true);
            reSpan();
            selectText(false);
            moveCaret(newPosition);
            invalidate();
        }
    }

    /**
     * Load default file
     */
    public void loadAppInfoJavaFile() {
        String infoJavaFile = PathUtils.getExternalAppCachePath() + "/JavaStudio.java";
//        ResourceUtils.copyFileFromAssets("JavaStudio.java", infoJavaFile);
        open(infoJavaFile);
    }
}
