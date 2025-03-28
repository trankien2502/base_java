package com.livescore.soccerscore.matchlive.ui.livescores.favourite;

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
import com.livescore.soccerscore.matchlive.api_data.ConstantApiData;
import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueModel;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamModel;
import com.livescore.soccerscore.matchlive.base.BaseFragment;
import com.livescore.soccerscore.matchlive.database.league.LeagueDatabase;
import com.livescore.soccerscore.matchlive.database.team.TeamDatabase;
import com.livescore.soccerscore.matchlive.databinding.FragmentFavouriteBinding;
import com.livescore.soccerscore.matchlive.ui.livescores.HomeActivity;
import com.livescore.soccerscore.matchlive.util.SPUtils;

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
    LeagueClickCallBack leagueClickCallBack;
    LeagueAdapter leagueAdapterFavourite, leagueAdapter;
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
                changeFavouriteListTeam(teamModel);
            }
        };
        leagueClickCallBack = new LeagueClickCallBack() {
            @Override
            public void select(LeagueModel leagueModel) {
                Toast.makeText(requireContext(), "" + leagueModel.isFavourite(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void follow(LeagueModel leagueModel) {
                changeFavouriteListLeague(leagueModel);
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
                for (LeagueModel leagueModel1 : listLeagueFavourite) {
                    if (leagueModel1.getId() == leagueModel.getId()) {
                        leagueModel.setFavourite(true);
                        break;
                    }
                }
            }
        }
        checkEmptyTeam();
        checkEmptyLeague();
        teamAdapterFavourite = new TeamAdapter(requireContext(), listTeamFavourite, teamClickCallBack);
        teamAdapter = new TeamAdapter(requireContext(), listAllTeam, teamClickCallBack);
        binding.rcvTeamFavourite.setAdapter(teamAdapterFavourite);
        binding.rcvTeamAll.setAdapter(teamAdapter);
        leagueAdapterFavourite = new LeagueAdapter(requireContext(), listLeagueFavourite, leagueClickCallBack);
        leagueAdapter = new LeagueAdapter(requireContext(), listAllLeague, leagueClickCallBack);
        binding.rcvLeagueAll.setAdapter(leagueAdapter);
        binding.rcvLeagueFavourite.setAdapter(leagueAdapterFavourite);
        changeState();
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    private void changeFavouriteListTeam(TeamModel teamModel) {
        if (teamModel.isFavourite()) {
            for (TeamModel teamModel1 : listTeamFavourite)
                if (teamModel1.getId() == teamModel.getId()) {
                    teamModel = teamModel1;
                    break;
                }
            listTeamFavourite.remove(teamModel);
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

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    private void changeFavouriteListLeague(LeagueModel leagueModel) {
        if (leagueModel.isFavourite()) {
            for (LeagueModel leagueModel1 : listLeagueFavourite)
                if (leagueModel1.getId() == leagueModel.getId()) {
                    leagueModel = leagueModel1;
                    break;
                }
            listLeagueFavourite.remove(leagueModel);
            LeagueDatabase.getInstance(requireContext()).leagueDAO().delete(leagueModel.getId());
            leagueAdapterFavourite.notifyDataSetChanged();
            for (LeagueModel leagueModel1 : listAllLeague)
                if (leagueModel1.getId() == leagueModel.getId()) {
                    leagueModel1.setFavourite(false);
                    break;
                }
            leagueAdapter.notifyDataSetChanged();
        } else {
            leagueModel.setFavourite(true);
            LeagueDatabase.getInstance(requireContext()).leagueDAO().insert(leagueModel);
            listLeagueFavourite.add(leagueModel);
            leagueAdapterFavourite.notifyDataSetChanged();
            for (LeagueModel leagueModel1 : listAllLeague)
                if (leagueModel1.getId() == leagueModel.getId()) {
                    leagueModel1.setFavourite(true);
                    break;
                }
            leagueAdapter.notifyDataSetChanged();
        }
        checkEmptyLeague();
        binding.tvFavouriteLeague.setText(getString(R.string.favourite) + " (" + listLeagueFavourite.size() + ")");
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

    private void checkEmptyLeague() {
        if (listLeagueFavourite.isEmpty()) binding.noFavouriteLeague.setVisibility(VISIBLE);
        else binding.noFavouriteLeague.setVisibility(GONE);

        if (listAllLeague.isEmpty()) {
            binding.noResultLeague.setVisibility(VISIBLE);
            int heightInDp = 136;
            int heightInPx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, heightInDp, binding.clAllLeague.getResources().getDisplayMetrics());

            ViewGroup.LayoutParams params = binding.clAllLeague.getLayoutParams();
            params.height = heightInPx;
            binding.clAllLeague.setLayoutParams(params);
        } else {
            binding.noResultLeague.setVisibility(GONE);
            int heightInDp = 416;
            int heightInPx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, heightInDp, binding.clAllLeague.getResources().getDisplayMetrics());

            ViewGroup.LayoutParams params = binding.clAllLeague.getLayoutParams();
            params.height = heightInPx;
            binding.clAllLeague.setLayoutParams(params);
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
            SPUtils.showKeyboard(requireContext(), binding.edtText);
        });
        binding.ivExitSearch.setOnClickListener(v -> {
            isSearch = false;
            SPUtils.hideKeyboard(requireContext(), binding.edtText);
            binding.edtText.clearFocus();
            binding.clHeader.setVisibility(VISIBLE);
            binding.clSearch.setVisibility(GONE);

        });
        binding.ivHideFavouriteLeague.setOnClickListener(v -> {
            if (binding.clFavouriteLeague.getVisibility() == VISIBLE) {
                binding.ivHideFavouriteLeague.setImageResource(R.drawable.expand_up);
                binding.clFavouriteLeague.setVisibility(GONE);
            } else {
                binding.ivHideFavouriteLeague.setImageResource(R.drawable.expand_down);
                binding.clFavouriteLeague.setVisibility(VISIBLE);
            }
        });
        binding.ivHideFavouriteTeam.setOnClickListener(v -> {
            if (binding.clFavouriteTeam.getVisibility() == VISIBLE) {
                binding.ivHideFavouriteTeam.setImageResource(R.drawable.expand_up);
                binding.clFavouriteTeam.setVisibility(GONE);
            } else {
                binding.ivHideFavouriteTeam.setImageResource(R.drawable.expand_down);
                binding.clFavouriteTeam.setVisibility(VISIBLE);
            }
        });
        binding.ivHideLeagueAll.setOnClickListener(v -> {
            if (binding.clAllLeague.getVisibility() == VISIBLE) {
                binding.ivHideLeagueAll.setImageResource(R.drawable.expand_up);
                binding.clAllLeague.setVisibility(GONE);
            } else {
                binding.clAllLeague.setVisibility(VISIBLE);
                binding.ivHideLeagueAll.setImageResource(R.drawable.expand_down);
            }
        });
        binding.ivHideTeamAll.setOnClickListener(v -> {
            if (binding.clAllTeam.getVisibility() == VISIBLE) {
                binding.clAllTeam.setVisibility(GONE);
                binding.ivHideTeamAll.setImageResource(R.drawable.expand_up);
            } else {
                binding.clAllTeam.setVisibility(VISIBLE);
                binding.ivHideTeamAll.setImageResource(R.drawable.expand_down);
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

            }
        });
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
        if (state == LEAGUE) {
            binding.tvLeague.setBackgroundResource(R.drawable.bg_select_favourite_item);
            binding.tvLeague.setTextColor(Color.parseColor("#0094FD"));
            binding.clLeague.setVisibility(VISIBLE);
        } else if (state == TEAM) {
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