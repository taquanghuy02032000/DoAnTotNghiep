package online.javalab.poly.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public  class CommonExt {
    public static int progressCalculator(int complete, int total) {
        return (int) ((float) complete / (float) total * 100);
    }

    public static void toast(Context context, String content) {
         Toast.makeText(context, content, Toast.LENGTH_LONG).show();
    }
    public static void toast(Context context, String content,int time) {
        Toast.makeText(context, content, time).show();
    }

    public static RecyclerView.OnItemTouchListener onRecyclerViewItemTouchListener(int type){
        return  new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                int action = e.getAction();
                if (type == 1){
                    if (action == MotionEvent.ACTION_SCROLL) {
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }else if (type==2){
                    if (action == MotionEvent.ACTION_MOVE) {
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }

                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        };
    }

    private static String getHtmlString(String bodyContent){
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


