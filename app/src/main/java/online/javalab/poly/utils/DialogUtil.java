package online.javalab.poly.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import online.javalab.poly.R;
import online.javalab.poly.databinding.DialogLoadingBinding;

public class DialogUtil {
    private static boolean shown = false;

    private AlertDialog dialog = null;

    private DialogLoadingBinding binding;

    @SuppressLint("StaticFieldLeak")
    private static DialogUtil instance = null;

    private Context context;

    public static void init(Context context){
        destroyCurrentInstance();
        instance = new DialogUtil(context);
    }

    public static DialogUtil getInstance(Context context) {
        if (instance == null) {
            instance = new DialogUtil(context);
        }
        return instance;
    }

    @SuppressLint("ResourceAsColor")
    private DialogUtil(Context context) {
        this.context = context;
        if (context != null && !DialogUtil.isShown()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            binding = DataBindingUtil.inflate(layoutInflater, R.layout.dialog_loading, null, false);
            View dialogView = binding.getRoot();
            binding.pbLoading.getIndeterminateDrawable().setColorFilter(R.color.rainbow_8, android.graphics.PorterDuff.Mode.SRC_ATOP);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setCancelable(false);
            dialog = dialogBuilder.create();
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnKeyListener((dialog, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    // we cannot close dialog when we press back button
                }
                return false;
            });
        }
    }

    public void show() {
        if (!((Activity) context).isFinishing()) {
            if (!DialogUtil.isShown() && dialog != null) {
                forceShown();
                dialog.show();
            }
        }
    }

    public void hidden() {
        if (DialogUtil.isShown() && dialog != null && dialog.isShowing()) {
            initialize();
            dialog.dismiss();
        }
    }

    private static boolean isShown() {
        return shown;
    }

    private static void forceShown() {
        shown = true;
    }

    private static void initialize() {
        shown = false;
    }

    public static void destroyCurrentInstance() {
        if (instance != null)
            instance = null;
    }


}
