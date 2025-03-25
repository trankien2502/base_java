package com.livescore.soccerscore.matchlive.ui.home.favourite;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.livescore.soccerscore.matchlive.R;
import com.livescore.soccerscore.matchlive.api_data.CallApiUtils;
import com.livescore.soccerscore.matchlive.api_data.ConstantApiData;
import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueModel;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamModel;
import com.livescore.soccerscore.matchlive.base.BaseFragment;
import com.livescore.soccerscore.matchlive.database.league.LeagueDatabase;
import com.livescore.soccerscore.matchlive.database.team.TeamDatabase;
import com.livescore.soccerscore.matchlive.databinding.FragmentFavouriteBinding;
import com.livescore.soccerscore.matchlive.databinding.FragmentHomeBinding;
import com.livescore.soccerscore.matchlive.ui.home.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends BaseFragment<FragmentFavouriteBinding> {

    private int state = 1;
    private final static int TEAM = 1;
    private final static int LEAGUE = 2;
    List<TeamModel> listTeamFavourite = new ArrayList<>();
    List<LeagueModel> listLeagueFavourite = new ArrayList<>();

    List<TeamModel> listAllTeam = new ArrayList<>();
    List<LeagueModel> listAllLeague = new ArrayList<>();
    TeamClickCallBack teamClickCallBack;
    TeamAdapter teamAdapterFavourite, teamAdapter;
    boolean isSearch = false;

    @Override
    public FragmentFavouriteBinding setBinding(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        return FragmentFavouriteBinding.inflate(getLayoutInflater());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initView() {
        teamClickCallBack = new TeamClickCallBack() {
            @Override
            public void select(TeamModel teamModel) {
                Toast.makeText(requireContext(), "" + teamModel.isFavourite(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void follow(TeamModel teamModel) {
                changeFavouriteList(teamModel);
            }
        };
        listAllLeague.addAll(ConstantApiData.listLeague);
        listAllTeam.addAll(ConstantApiData.listTeam);
        listTeamFavourite = TeamDatabase.getInstance(requireContext()).teamDAO().getAllTeamFavourite();
        listLeagueFavourite = LeagueDatabase.getInstance(requireContext()).leagueDAO().getAllLeagueFavourite();
        binding.tvFavouriteTeam.setText(getString(R.string.favourite) + " (" + listTeamFavourite.size() + ")");
        binding.tvAllTeam.setText(getString(R.string.all_teams) + " (" + listAllTeam.size() + ")");
        if (!listTeamFavourite.isEmpty()) {
            for (TeamModel teamModel : listAllTeam) {
                for (TeamModel teamModel1 : listTeamFavourite) {
                    if (teamModel1.getId() == teamModel.getId()) {
                        teamModel.setFavourite(true);
                        break;
                    }
                }
            }

        }
        if (!listLeagueFavourite.isEmpty()) {
            for (LeagueModel leagueModel : listAllLeague) {
                leagueModel.setFavourite(listLeagueFavourite.contains(leagueModel));
            }
            for (LeagueModel leagueModel : listLeagueFavourite) {
                leagueModel.setFavourite(true);
            }
        }

        checkEmptyTeam();
        teamAdapterFavourite = new TeamAdapter(requireContext(), listTeamFavourite, teamClickCallBack);
        teamAdapter = new TeamAdapter(requireContext(), listAllTeam, teamClickCallBack);
        binding.rcvTeamFavourite.setAdapter(teamAdapterFavourite);
        binding.rcvTeamAll.setAdapter(teamAdapter);
        changeState();
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    private void changeFavouriteList(TeamModel teamModel) {
        if (teamModel.isFavourite()) {
            listTeamFavourite.remove(teamModel);
            teamModel.setFavourite(false);
            TeamDatabase.getInstance(requireContext()).teamDAO().delete(teamModel.getId());

            teamAdapterFavourite.notifyDataSetChanged();
            for (TeamModel teamModel1 : listAllTeam)
                if (teamModel1.getId() == teamModel.getId()) {
                    teamModel1.setFavourite(false);
                    break;
                }
            teamAdapter.notifyDataSetChanged();
        } else {
            teamModel.setFavourite(true);
            TeamDatabase.getInstance(requireContext()).teamDAO().insert(teamModel);
            listTeamFavourite.add(teamModel);
            teamAdapterFavourite.notifyDataSetChanged();
            for (TeamModel teamModel1 : listAllTeam)
                if (teamModel1.getId() == teamModel.getId()) {
                    teamModel1.setFavourite(true);
                    break;
                }
            teamAdapter.notifyDataSetChanged();
        }
        checkEmptyTeam();
        binding.tvFavouriteTeam.setText(getString(R.string.favourite) + " (" + listTeamFavourite.size() + ")");
    }

    private void checkEmptyTeam() {
        if (listTeamFavourite.isEmpty()) binding.noFavouriteTeam.setVisibility(VISIBLE);
        else binding.noFavouriteTeam.setVisibility(GONE);

        if (listAllTeam.isEmpty()) {
            binding.noResultTeam.setVisibility(VISIBLE);
            int heightInDp = 136;
            int heightInPx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, heightInDp, binding.clAllTeam.getResources().getDisplayMetrics());

            ViewGroup.LayoutParams params = binding.clAllTeam.getLayoutParams();
            params.height = heightInPx;
            binding.clAllTeam.setLayoutParams(params);
        } else {
            binding.noResultTeam.setVisibility(GONE);
            int heightInDp = 416;
            int heightInPx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, heightInDp, binding.clAllTeam.getResources().getDisplayMetrics());

            ViewGroup.LayoutParams params = binding.clAllTeam.getLayoutParams();
            params.height = heightInPx;
            binding.clAllTeam.setLayoutParams(params);
        }
    }

    @Override
    public void bindView() {
        binding.tvLeague.setOnClickListener(v -> {
            state = 2;
            changeState();
        });
        binding.tvTeam.setOnClickListener(v -> {
            state = 1;
            changeState();
        });
        binding.ivSearch.setOnClickListener(v -> {
            isSearch = true;
            binding.clHeader.setVisibility(INVISIBLE);
            binding.clSearch.setVisibility(VISIBLE);
            binding.edtText.setText("");
            binding.edtText.requestFocus();
            showKeyboard(requireContext(), binding.edtText);
        });
        binding.ivExitSearch.setOnClickListener(v -> {
            isSearch = false;
            hideKeyboard(requireContext(), binding.edtText);
            binding.edtText.clearFocus();
            binding.clHeader.setVisibility(VISIBLE);
            binding.clSearch.setVisibility(GONE);

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

            }
        });
    }

    public void showKeyboard(Context context, View view) {
        if (view == null) return;

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();

        view.post(() -> {
            if (imm != null) {
                imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            }
        });
    }

    public void hideKeyboard(Context context, View view) {
        if (view == null) return;

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void resetChange() {
        binding.tvLeague.setBackgroundResource(0);
        binding.tvTeam.setBackgroundResource(0);
        binding.tvLeague.setTextColor(Color.parseColor("#a3a3a3"));
        binding.tvTeam.setTextColor(Color.parseColor("#a3a3a3"));
        binding.clLeague.setVisibility(GONE);
        binding.clTeam.setVisibility(GONE);
    }

    private void changeState() {
        resetChange();
        if (state == 2) {
            binding.tvLeague.setBackgroundResource(R.drawable.bg_select_favourite_item);
            binding.tvLeague.setTextColor(Color.parseColor("#0094FD"));
            binding.clLeague.setVisibility(VISIBLE);
        } else {
            binding.tvTeam.setBackgroundResource(R.drawable.bg_select_favourite_item);
            binding.tvTeam.setTextColor(Color.parseColor("#0094FD"));
            binding.clTeam.setVisibility(VISIBLE);
        }
    }

    public void startArc(Intent intent) {
        if (getContext() instanceof HomeActivity) {
            HomeActivity main = (HomeActivity) getContext();
            main.resultLauncher.launch(intent);
        }
    }

}