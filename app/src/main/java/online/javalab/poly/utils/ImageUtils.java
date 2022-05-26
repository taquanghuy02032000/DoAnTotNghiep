package online.javalab.poly.utils;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import online.javalab.poly.R;

public class ImageUtils {

    public static void loadImage(ImageView imageView, String uri) {
        Picasso.get()
                .load(uri)
                .error(R.drawable.ic_error)
                .into(imageView);
    }
}
