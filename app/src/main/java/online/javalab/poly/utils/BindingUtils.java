package online.javalab.poly.utils;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

import online.javalab.poly.R;

public class BindingUtils {

    @BindingAdapter({"app:totalTopics", "app:completed"})
    public static void setProgress(ProgressBar progressBar, int total, int completed) {
        int value = CommonExt.progressCalculator(completed, total);
        progressBar.setProgress(value);
    }

    @BindingAdapter("app:imageRes")
    public static void setExpandCollapseIcon(ImageView img, boolean isExpand) {
        if (isExpand)
            img.setImageResource(R.drawable.ic_arrow_up_gray_24);
        else
            img.setImageResource(R.drawable.ic_arrow_down_gray_24);
    }

    @BindingAdapter("app:setImage")
    public static void setImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .circleCrop()
                .centerCrop()
                .placeholder(R.drawable.coder)
                .into(imageView);
    }

    @BindingAdapter("app:setImageRes")
    public static void setImageRes(ImageView imageView, boolean isCompleted) {
        if (!isCompleted) {
            imageView.setImageResource(R.drawable.ic_arrow_right_gray_24);
        } else {
            imageView.setImageResource(R.drawable.ic_done_green_24);
        }
    }

    @BindingAdapter({"app:isEnable", "app:isCompleted"})
    public static void setItemBackground(View view, boolean enabled, boolean isCompleted) {
        if (enabled) {
            if (isCompleted)
                view.setBackgroundResource(R.drawable.custom_background_item_primary_enabled_1);
            else view.setBackgroundResource(R.drawable.custom_background_item_primary_enabled_2);

        } else {
            view.setBackgroundResource(R.drawable.custom_background_item_primary_disabled);
        }
    }


    @SuppressLint("ResourceAsColor")
    @BindingAdapter("app:itemTextColor")
    public static void setItemTextColor(TextView view, boolean enabled) {
        if (enabled) {
            view.setTextColor(R.color.material_500);
        } else {
            view.setTextColor(R.color.m_gray_200);
        }
    }

    @BindingAdapter("app:formatContent")
    public static void setContent(WebView webView, String contentHtml) {
        if (contentHtml != null) {
            webView.setWebViewClient(new WebViewClient());
            webView.loadData(getHtmlString(contentHtml), "text/html", "UTF-8");
        }
    }


    private static String getHtmlString(String bodyContent) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title></title>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n"
                +
                bodyContent
                +
                "</body>\n" +
                "\n" +
                "</html>";
    }
}
