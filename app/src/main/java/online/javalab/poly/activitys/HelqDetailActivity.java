package online.javalab.poly.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import online.javalab.poly.R;

public class HelqDetailActivity extends AppCompatActivity {
    TextView mDetailBodyText, mTitle;
    ImageView mImageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helq_detail);
        mImageBack = findViewById(R.id.helpdetail_activity_back_img);
        mDetailBodyText = findViewById(R.id.helq_detail_activity_body_text);
        mTitle = findViewById(R.id.helpdetail_activity_title_txt);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        String body = bundle.getString("body");
        String title = bundle.getString("title");
        mDetailBodyText.setText(body);
        mTitle.setText(title);
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}