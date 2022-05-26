package online.javalab.poly.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import online.javalab.poly.R;

public class HelpFAQActivity extends AppCompatActivity {
    private TextView mText1title, mText2title, mText3title, mText5title;
    private LinearLayout mLinearLayout1, mLinearLayout2, mLinearLayout3, mLinearLayout5;
    private ImageView mImageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_faqactivity);

        mText1title = findViewById(R.id.activity_helpfaq_title1_text);
        mText2title = findViewById(R.id.activity_helpfaq_title2_text);
        mText3title = findViewById(R.id.activity_helpfaq_title3_text);
        mText5title = findViewById(R.id.activity_helpfaq_title5_text);
        mLinearLayout1 = findViewById(R.id.activity_helpfaq_title1_linearlayout);
        mLinearLayout2 = findViewById(R.id.activity_helpfaq_title2_linearlayout);
        mLinearLayout3 = findViewById(R.id.activity_helpfaq_title3_linearlayout);
        mLinearLayout5 = findViewById(R.id.activity_helpfaq_title5_linearlayout);
        mImageBack = findViewById(R.id.activity_helpfaq_back_img);

        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mLinearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpFAQActivity.this, HelqDetailActivity.class);
                String title = String.valueOf("That way I can see the last 7 days.\n");
                String body = String.valueOf("That way I can see the last 7 days.\n" +
                        "Step 1 click on the three lines icon in the upper left corner\n" +
                        "Step 2 click on the avatar icon\n" +
                        "Step 3 you will see your latest 7 days information");
                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putString("body", body);
                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });
        mLinearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpFAQActivity.this, HelqDetailActivity.class);
                String title = String.valueOf("How do I get to the top?\n");
                String body = String.valueOf("How do I get to the top?\n" +
                        "Step 1 login to the app\n" +
                        "step 2 take quiz and try to get high score\n" +
                        "Step 3 you will get the system to accumulate points and increase the rank");
                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putString("body", body);
                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });
        mLinearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpFAQActivity.this, HelqDetailActivity.class);
                String title = String.valueOf("How to report wrong answer?");
                String body = String.valueOf("How to report wrong answer?\n" +
                        "step 1 on quiz\n" +
                        "Step 2 choose the question with the wrong answer\n" +
                        "Step 3 choose the answer\n" +
                        "Step 4 will appear dialog box\n" +
                        "Step 5 into the app bar on the dialog box will have a warning icon\n" +
                        "Step 6 Click on the warning and a dialog box will appear");
                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putString("body", body);
                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });
        mLinearLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpFAQActivity.this, HelqDetailActivity.class);
                String title = String.valueOf("How to comment on questions?");
                String body = String.valueOf("How to comment on questions?\n" +
                        "step 1 on quiz\n" +
                        "Step 2 choose the question with the wrong answer\n" +
                        "Step 3 choose the answer\n" +
                        "Step 4 will appear dialog box\n" +
                        "Step 5 into the app bar on the dialog box will have a chat icon\n" +
                        "Step 6 will then appear a comment box about the question");
                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putString("body", body);
                intent.putExtra("data", bundle);
                startActivity(intent);
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