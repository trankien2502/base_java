package com.livescore.soccerscore.matchlive.ui.livescores.team_detail;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.livescore.soccerscore.matchlive.R;
import com.livescore.soccerscore.matchlive.api_data.ConstantApiData;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamInMatch;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamModel;
import com.livescore.soccerscore.matchlive.base.BaseActivity;
import com.livescore.soccerscore.matchlive.database.team.TeamDatabase;
import com.livescore.soccerscore.matchlive.databinding.ActivityTeamDetailBinding;
import com.livescore.soccerscore.matchlive.util.SPUtils;

import java.util.List;

public class TeamDetailActivity extends BaseActivity<ActivityTeamDetailBinding> {

    TeamDetailAdapter adapter;
    public TeamInMatch teamModel;
    public static TeamDetailActivity instance;

    @Override
    public ActivityTeamDetailBinding getBinding() {
        return ActivityTeamDetailBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        adapter = new TeamDetailAdapter(this);
        teamModel = (TeamInMatch) getIntent().getSerializableExtra(SPUtils.INTENT_TEAM);
        if (teamModel == null) {
            Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
            onBack();
        } else {
            binding.tvTeamNational.setText(teamModel.getCountry().name);
            binding.tvTeamName.setText(teamModel.getName());
            Glide.with(this).load(teamModel.getImage_path()).into(binding.ivTeam);
            if (TeamDatabase.getInstance(this).teamDAO().getTeamById(teamModel.getId()) != null) {
                binding.ivFavourite.setImageResource(R.drawable.star_s);
            } else {
                binding.ivFavourite.setImageResource(R.drawable.star_sn);
            }
            binding.viewPager.setAdapter(adapter);
            binding.viewPager.setCurrentItem(0);
        }


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    @Override
    public void bindView() {
        binding.ivBack.setOnClickListener(v -> onBack());
        binding.ivFavourite.setOnClickListener(v -> {
            if (TeamDatabase.getInstance(this).teamDAO().getTeamById(teamModel.getId()) != null) {
                TeamDatabase.getInstance(this).teamDAO().delete(teamModel.getId());
                binding.ivFavourite.setImageResource(R.drawable.star_sn);
            } else {
                teamModel.setFavourite(true);
                TeamDatabase.getInstance(this).teamDAO().insert(teamModel);
                binding.ivFavourite.setImageResource(R.drawable.star_s);
            }
        });
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                changeState(position);
            }
        });
        binding.tvStats.setOnClickListener(v -> {
            binding.viewPager.setCurrentItem(0);
        });
        binding.tvFixtures.setOnClickListener(v -> {
            binding.viewPager.setCurrentItem(1);
        });
        binding.tvSquad.setOnClickListener(v -> {
            binding.viewPager.setCurrentItem(2);
        });
    }

    private void resetChange() {
        binding.tvFixtures.setBackgroundResource(0);
        binding.tvSquad.setBackgroundResource(0);
        binding.tvStats.setBackgroundResource(0);
        binding.tvFixtures.setTextColor(Color.parseColor("#a3a3a3"));
        binding.tvStats.setTextColor(Color.parseColor("#a3a3a3"));
        binding.tvSquad.setTextColor(Color.parseColor("#a3a3a3"));
    }

    private void changeState(int state) {
        resetChange();
        if (state == 1) {
            binding.tvFixtures.setBackgroundResource(R.drawable.bg_select_favourite_item);
            binding.tvFixtures.setTextColor(Color.parseColor("#0094FD"));
        } else if (state == 0) {
            binding.tvStats.setBackgroundResource(R.drawable.bg_select_favourite_item);
            binding.tvStats.setTextColor(Color.parseColor("#0094FD"));
        } else if (state == 2) {
            binding.tvSquad.setBackgroundResource(R.drawable.bg_select_favourite_item);
            binding.tvSquad.setTextColor(Color.parseColor("#0094FD"));
        }
    }

    @Override
    public void onBack() {
        setResult(RESULT_OK);
        finish();
    }
    public ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            //ads
            Log.d("activity_check", "home");
        }
    });
}