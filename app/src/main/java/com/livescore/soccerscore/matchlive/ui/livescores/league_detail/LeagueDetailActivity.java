package com.livescore.soccerscore.matchlive.ui.livescores.league_detail;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.livescore.soccerscore.matchlive.R;
import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueDetail;
import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueModel;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamInMatch;
import com.livescore.soccerscore.matchlive.base.BaseActivity;
import com.livescore.soccerscore.matchlive.database.league.LeagueDatabase;
import com.livescore.soccerscore.matchlive.database.team.TeamDatabase;
import com.livescore.soccerscore.matchlive.databinding.ActivityLeagueDetailBinding;
import com.livescore.soccerscore.matchlive.databinding.ActivityTeamDetailBinding;
import com.livescore.soccerscore.matchlive.ui.livescores.team_detail.TeamDetailActivity;
import com.livescore.soccerscore.matchlive.ui.livescores.team_detail.TeamDetailAdapter;
import com.livescore.soccerscore.matchlive.util.SPUtils;

public class LeagueDetailActivity extends BaseActivity<ActivityLeagueDetailBinding> {


    LeagueDetailAdapter adapter = new LeagueDetailAdapter(this);
    public LeagueModel leagueDetail;
    public static LeagueDetailActivity instance;

    @Override
    public ActivityLeagueDetailBinding getBinding() {
        return ActivityLeagueDetailBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        leagueDetail = (LeagueModel) getIntent().getSerializableExtra(SPUtils.INTENT_LEAGUE);
        if (leagueDetail == null) {
            Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
            onBack();
        } else {
            binding.tvLeagueNational.setText(leagueDetail.countryName);
            binding.tvLeagueName.setText(leagueDetail.getName());
            Glide.with(this).load(leagueDetail.getImage_path()).into(binding.ivLeague);
            if (LeagueDatabase.getInstance(this).leagueDAO().getLeagueById(leagueDetail.getId()) != null) {
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
            if (LeagueDatabase.getInstance(this).leagueDAO().getLeagueById(leagueDetail.getId()) != null) {
                LeagueDatabase.getInstance(this).leagueDAO().delete(leagueDetail.getId());
                binding.ivFavourite.setImageResource(R.drawable.star_sn);
            } else {
                leagueDetail.setFavourite(true);
                LeagueDatabase.getInstance(this).leagueDAO().insert(leagueDetail);
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
        binding.tvFixtures.setOnClickListener(v -> {
            binding.viewPager.setCurrentItem(0);
        });
        binding.tvTable.setOnClickListener(v -> {
            binding.viewPager.setCurrentItem(1);
        });
    }

    private void resetChange() {
        binding.tvFixtures.setBackgroundResource(0);
        binding.tvTable.setBackgroundResource(0);
        binding.tvFixtures.setTextColor(Color.parseColor("#a3a3a3"));
        binding.tvTable.setTextColor(Color.parseColor("#a3a3a3"));
    }

    private void changeState(int state) {
        resetChange();
        if (state == 1) {
            binding.tvTable.setBackgroundResource(R.drawable.bg_select_favourite_item);
            binding.tvTable.setTextColor(Color.parseColor("#0094FD"));
        } else if (state == 0) {
            binding.tvFixtures.setBackgroundResource(R.drawable.bg_select_favourite_item);
            binding.tvFixtures.setTextColor(Color.parseColor("#0094FD"));
        }
    }

    @Override
    public void onBack() {
        setResult(RESULT_OK);
        finish();
    }
}