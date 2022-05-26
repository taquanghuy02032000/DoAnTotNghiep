package online.javalab.poly.activitys.program;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import online.javalab.poly.adapters.ProgramDetailAdapter;
import online.javalab.poly.databinding.ActivityProgramDetailBinding;
import online.javalab.poly.fragments.ProgramFragment;
import online.javalab.poly.model.Program;

public class ProgramDetailActivity extends AppCompatActivity implements ProgramDetailAdapter.ItemClickListener {
    public static final String BUNDLE_PROGRAM_DETAIL = "BUNDLE_PROGRAM_DETAIL";
    private ActivityProgramDetailBinding binding;
    private Program examples;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProgramDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initData();
        initComponent();
        initView();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, EditorActivity.class);
        intent.putExtra(BUNDLE_PROGRAM_DETAIL, examples.getProgramDetails().get(position));
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void initComponent() {
        binding.toolbar.setTitle(examples.getName());
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initData() {
        Intent intent = getIntent();
        examples = (Program) intent.getSerializableExtra(ProgramFragment.BUNDLE_PROGRAM);
    }

    private void initView() {
        ProgramDetailAdapter adapter = new ProgramDetailAdapter(examples.getProgramDetails(), this);
        binding.recyclerExample.setAdapter(adapter);
    }
}
