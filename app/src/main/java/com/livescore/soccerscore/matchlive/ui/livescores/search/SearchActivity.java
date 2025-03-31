package com.livescore.soccerscore.matchlive.ui.livescores.search;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.livescore.soccerscore.matchlive.R;
import com.livescore.soccerscore.matchlive.api_data.ConstantApiData;
import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueModel;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamModel;
import com.livescore.soccerscore.matchlive.base.BaseActivity;
import com.livescore.soccerscore.matchlive.databinding.ActivitySearchBinding;
import com.livescore.soccerscore.matchlive.ui.livescores.league_detail.LeagueDetailActivity;
import com.livescore.soccerscore.matchlive.ui.livescores.search.league.LeagueSearchAdapter;
import com.livescore.soccerscore.matchlive.ui.livescores.search.league.LeagueSearchClickCallBack;
import com.livescore.soccerscore.matchlive.ui.livescores.search.recent.ItemRecentClickCallBack;
import com.livescore.soccerscore.matchlive.ui.livescores.search.recent.SearchAdapter;
import com.livescore.soccerscore.matchlive.ui.livescores.search.team.TeamSearchAdapter;
import com.livescore.soccerscore.matchlive.ui.livescores.search.team.TeamSearchClickCallBack;
import com.livescore.soccerscore.matchlive.ui.livescores.search.top_search.TopSearchAdapter;
import com.livescore.soccerscore.matchlive.ui.livescores.search.top_search.TopSearchClickCallBack;
import com.livescore.soccerscore.matchlive.ui.livescores.search.top_search.TopSearchModel;
import com.livescore.soccerscore.matchlive.ui.livescores.team_detail.TeamDetailActivity;
import com.livescore.soccerscore.matchlive.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity<ActivitySearchBinding> {

    ArrayList<String> list = new ArrayList<>();
    ArrayList<TopSearchModel> listTopSearch = new ArrayList<>();
    SearchAdapter adapter;
    TopSearchAdapter topSearchAdapter;
    TeamSearchAdapter teamAdapter;
    LeagueSearchAdapter leagueAdapter;
    List<TeamModel> listTeamModel = new ArrayList<>();
    List<LeagueModel> listLeagueModel = new ArrayList<>();

    @Override
    public ActivitySearchBinding getBinding() {
        return ActivitySearchBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        initDataTopSearch();
        list = SPUtils.getList(this, SPUtils.LIST_RECENT);
        initAdapter();

        if (list.isEmpty()) {
            binding.clRecent.setVisibility(GONE);
        } else {
            binding.clRecent.setVisibility(VISIBLE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void bindView() {
        binding.tvTeam.setOnClickListener(v -> {
            search(1, binding.edtText.getText().toString().trim());
        });
        binding.tvLeague.setOnClickListener(v -> {
            search(2, binding.edtText.getText().toString().trim());
        });
        binding.ivExitSearch.setOnClickListener(v -> {
            if (binding.edtText.getText().toString().trim().isEmpty())
                onBack();
            else {
                binding.edtText.setText("");
                binding.clRecentTopSearch.setVisibility(VISIBLE);
                binding.clResult.setVisibility(GONE);
                binding.llOption.setVisibility(GONE);
            }
        });
        binding.tvDeleteAll.setOnClickListener(v -> {
            list.clear();
            SPUtils.removeList(this, SPUtils.LIST_RECENT);
            adapter.notifyDataSetChanged();
            binding.clRecent.setVisibility(GONE);
        });
        binding.edtText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    String text = binding.edtText.getText().toString().trim();
                    if (!text.isEmpty()) {
                        search(1, text);
                        list.add(0, text);
                        SPUtils.setList(getBaseContext(), SPUtils.LIST_RECENT, list);
                        adapter.notifyDataSetChanged();
                        if (list.isEmpty()) {
                            binding.clRecent.setVisibility(GONE);
                        } else {
                            binding.clRecent.setVisibility(VISIBLE);
                        }
                    }
                    binding.edtText.clearFocus();
                    SPUtils.hideKeyboard(getBaseContext(), binding.edtText);
                    return true;
                }
                return false;
            }
        });
        binding.edtText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty()) {
                    binding.clRecentTopSearch.setVisibility(VISIBLE);
                    binding.clResult.setVisibility(GONE);
                    binding.llOption.setVisibility(GONE);
                }
            }
        });
        binding.edtText.setOnClickListener(v -> {
            SPUtils.showKeyboard(this, binding.edtText);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void search(int state, String str) {
        binding.edtText.setText(str);
        binding.clRecentTopSearch.setVisibility(GONE);
        binding.clResult.setVisibility(VISIBLE);
        binding.llOption.setVisibility(VISIBLE);
        Log.e("check_search", "str: " + str);
        changeState(state);
        if (state == 1) {
            listTeamModel.clear();
            Log.e("check_search", "listteam: " + ConstantApiData.listTeam);
            for (TeamModel teamModel : ConstantApiData.listTeam) {
                if (teamModel.getName().toLowerCase().contains(str.toLowerCase()))
                    listTeamModel.add(teamModel);
            }
            teamAdapter.notifyDataSetChanged();
            binding.rcvResult.setAdapter(teamAdapter);
            if (listTeamModel.isEmpty()) binding.noResult.setVisibility(VISIBLE);
            else binding.noResult.setVisibility(GONE);
        } else {
            listLeagueModel.clear();
            Log.e("check_search", "listteam: " + ConstantApiData.listLeague);
            for (LeagueModel leagueModel : ConstantApiData.listLeague) {
                if (leagueModel.getName().toLowerCase().contains(str.toLowerCase()))
                    listLeagueModel.add(leagueModel);
            }
            leagueAdapter.notifyDataSetChanged();
            binding.rcvResult.setAdapter(leagueAdapter);
            if (listLeagueModel.isEmpty()) binding.noResult.setVisibility(VISIBLE);
            else binding.noResult.setVisibility(GONE);
        }
    }

    private void resetChange() {
        binding.tvLeague.setBackgroundResource(0);
        binding.tvTeam.setBackgroundResource(0);
        binding.tvLeague.setTextColor(Color.parseColor("#a3a3a3"));
        binding.tvTeam.setTextColor(Color.parseColor("#a3a3a3"));
    }

    private void changeState(int state) {
        resetChange();
        if (state == 2) {
            binding.tvLeague.setBackgroundResource(R.drawable.bg_select_favourite_item);
            binding.tvLeague.setTextColor(Color.parseColor("#0094FD"));
        } else if (state == 1) {
            binding.tvTeam.setBackgroundResource(R.drawable.bg_select_favourite_item);
            binding.tvTeam.setTextColor(Color.parseColor("#0094FD"));
        }
    }

    @Override
    public void onBack() {
        setResult(RESULT_OK);
        finish();
    }

    private void initDataTopSearch() {
        listTopSearch.add(new TopSearchModel(R.drawable.img_logo, "Arsenal", true));
        listTopSearch.add(new TopSearchModel(R.drawable.img_logo, "Champions League", false));
        listTopSearch.add(new TopSearchModel(R.drawable.img_logo, "La Liga", false));
        listTopSearch.add(new TopSearchModel(R.drawable.img_logo, "West Ham United", true));
    }

    private void initAdapter() {
        teamAdapter = new TeamSearchAdapter(this, listTeamModel, new TeamSearchClickCallBack() {
            @Override
            public void select(TeamModel teamModel) {
                Intent intent = new Intent(getBaseContext(), TeamDetailActivity.class);
                intent.putExtra(SPUtils.INTENT_TEAM, teamModel);
                resultLauncher.launch(intent);
            }
        });
        leagueAdapter = new LeagueSearchAdapter(this, listLeagueModel, new LeagueSearchClickCallBack() {
            @Override
            public void select(LeagueModel leagueModel) {
                Intent intent = new Intent(getBaseContext(), LeagueDetailActivity.class);
                intent.putExtra(SPUtils.INTENT_LEAGUE, leagueModel);
                resultLauncher.launch(intent);
            }
        });
        topSearchAdapter = new TopSearchAdapter(this, listTopSearch, new TopSearchClickCallBack() {
            @Override
            public void select(TopSearchModel topSearchModel) {
                if (topSearchModel.isTeam)
                    search(1, topSearchModel.name);
                else search(2, topSearchModel.name);
            }
        });
        adapter = new SearchAdapter(this, list, new ItemRecentClickCallBack() {
            @Override
            public void select(String s) {
                search(1, s);
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void delete(int position) {
                list.remove(position);
                SPUtils.setList(getBaseContext(), SPUtils.LIST_RECENT, list);
                adapter.notifyDataSetChanged();
                if (list.isEmpty()) {
                    binding.clRecent.setVisibility(GONE);
                } else {
                    binding.clRecent.setVisibility(VISIBLE);
                }
            }
        });
        binding.rcvResult.setAdapter(teamAdapter);
        binding.rcvRecent.setAdapter(adapter);
        binding.rcvTopSearch.setAdapter(topSearchAdapter);
    }

    public ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            //ads
            Log.d("activity_check", "home");
        }
    });
}