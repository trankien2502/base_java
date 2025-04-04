package com.livescore.soccerscore.matchlive.ui.livescores.favourite;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.livescore.soccerscore.matchlive.R;
import com.livescore.soccerscore.matchlive.ads.IsNetWork;
import com.livescore.soccerscore.matchlive.api_data.ApiDataService;
import com.livescore.soccerscore.matchlive.api_data.ConstantApiData;
import com.livescore.soccerscore.matchlive.api_data.model.PaginationModel;
import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueDetail;
import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueModel;
import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueResponse;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamInMatch;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamModel;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamResponse;
import com.livescore.soccerscore.matchlive.base.BaseFragment;
import com.livescore.soccerscore.matchlive.database.league.LeagueDatabase;
import com.livescore.soccerscore.matchlive.database.team.TeamDatabase;
import com.livescore.soccerscore.matchlive.databinding.FragmentFavouriteBinding;
import com.livescore.soccerscore.matchlive.dialog.LoadingDialog;
import com.livescore.soccerscore.matchlive.ui.livescores.HomeActivity;
import com.livescore.soccerscore.matchlive.ui.livescores.league_detail.LeagueDetailActivity;
import com.livescore.soccerscore.matchlive.ui.livescores.team_detail.TeamDetailActivity;
import com.livescore.soccerscore.matchlive.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteFragment extends BaseFragment<FragmentFavouriteBinding> {

    private int state = 1;
    private final static int TEAM = 1;
    private final static int LEAGUE = 2;
    LoadingDialog loadingDialog;
    List<TeamModel> listTeamFavourite = new ArrayList<>();
    List<LeagueModel> listLeagueFavourite = new ArrayList<>();

    List<TeamModel> listAllTeam = new ArrayList<>();
    List<LeagueModel> listAllLeague = new ArrayList<>();


    LeagueClickCallBack leagueClickCallBack;
    LeagueAdapter leagueAdapterFavourite, leagueAdapter;
    TeamClickCallBack teamClickCallBack;
    TeamAdapter teamAdapterFavourite, teamAdapter;
    boolean isSearch = false;
    String str = "";
    private boolean isHasMoreTeam = true;
    private boolean isHasMoreLeague = true;
    int currentPageTeam = 1;
    int currentPageLeague = 1;

    @Override
    public FragmentFavouriteBinding setBinding(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        return FragmentFavouriteBinding.inflate(getLayoutInflater());
    }


    @Override
    public void initView() {
        loadingDialog = new LoadingDialog(requireContext(), false);
        listTeamFavourite = TeamDatabase.getInstance(requireContext()).teamDAO().getAllTeamFavourite();
        listLeagueFavourite = LeagueDatabase.getInstance(requireContext()).leagueDAO().getAllLeagueFavourite();
        initAdapter();
        changeState();
    }


    private void setFavouriteTeamLoad() {
        if (!listTeamFavourite.isEmpty() && !listAllTeam.isEmpty()) {
            for (int i = 0; i < listAllTeam.size(); i++) {
                for (TeamModel teamModel1 : listTeamFavourite) {
                    if (teamModel1.getId() == listAllTeam.get(i).getId()) {
                        listAllTeam.get(i).setFavourite(true);
                        teamAdapter.notifyItemChanged(i);
                        break;
                    }
                }
            }

        }
    }

    private void setFavouriteLeagueLoad() {
        if (!listLeagueFavourite.isEmpty() && !listAllLeague.isEmpty()) {
            for (int i = 0; i < listAllLeague.size(); i++) {
                for (LeagueModel leagueModel1 : listLeagueFavourite) {
                    if (leagueModel1.getId() == listAllLeague.get(i).getId()) {
                        listAllLeague.get(i).setFavourite(true);
                        leagueAdapter.notifyItemChanged(i);
                        break;
                    }
                }
            }
        }
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    private void changeFavouriteListTeam(int position, TeamModel teamModel) {
        if (teamModel.isFavourite()) {
            for (TeamModel teamModel1 : listTeamFavourite)
                if (teamModel1.getId() == teamModel.getId()) {
                    teamModel = teamModel1;
                    break;
                }
            listTeamFavourite.remove(teamModel);
            TeamDatabase.getInstance(requireContext()).teamDAO().delete(teamModel.getId());
            teamAdapterFavourite.notifyItemRemoved(position);
            for (int i = 0; i < listAllTeam.size(); i++)
                if (listAllTeam.get(i).getId() == teamModel.getId()) {
                    listAllTeam.get(i).setFavourite(false);
                    teamAdapter.notifyItemChanged(i);
                    break;
                }

        } else {
            teamModel.setFavourite(true);
            TeamDatabase.getInstance(requireContext()).teamDAO().insert(teamModel);
            listTeamFavourite.add(teamModel);
            teamAdapterFavourite.notifyDataSetChanged();
            for (int i = 0; i < listAllTeam.size(); i++)
                if (listAllTeam.get(i).getId() == teamModel.getId()) {
                    listAllTeam.get(i).setFavourite(true);
                    teamAdapter.notifyItemChanged(i);
                    break;
                }
        }
        checkEmptyTeam();
        binding.tvFavouriteTeam.setText(getString(R.string.favourite) + " (" + listTeamFavourite.size() + ")");
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    private void changeFavouriteListLeague(int position, LeagueModel leagueModel) {
        if (leagueModel.isFavourite()) {
            for (LeagueModel leagueModel1 : listLeagueFavourite)
                if (leagueModel1.getId() == leagueModel.getId()) {
                    leagueModel = leagueModel1;
                    break;
                }
            listLeagueFavourite.remove(leagueModel);
            LeagueDatabase.getInstance(requireContext()).leagueDAO().delete(leagueModel.getId());
            leagueAdapterFavourite.notifyItemRemoved(position);
            for (int i = 0; i < listAllLeague.size(); i++)
                if (listAllLeague.get(i).getId() == leagueModel.getId()) {
                    listAllLeague.get(i).setFavourite(false);
                    leagueAdapter.notifyItemChanged(i);
                    break;
                }

        } else {
            leagueModel.setFavourite(true);
            LeagueDatabase.getInstance(requireContext()).leagueDAO().insert(leagueModel);
            listLeagueFavourite.add(leagueModel);
            leagueAdapterFavourite.notifyDataSetChanged();
            for (int i = 0; i < listAllLeague.size(); i++)
                if (listAllLeague.get(i).getId() == leagueModel.getId()) {
                    listAllLeague.get(i).setFavourite(true);
                    leagueAdapter.notifyItemChanged(i);
                    break;
                }
        }
        checkEmptyLeague();
        binding.tvFavouriteLeague.setText(getString(R.string.favourite) + " (" + listLeagueFavourite.size() + ")");
    }

    private void checkEmptyTeam() {
        if (listTeamFavourite.isEmpty()) {
            if (isSearch) {
                binding.noFavouriteTeam.setVisibility(GONE);
                binding.noResultFavouriteTeam.setVisibility(VISIBLE);
            } else {
                binding.noFavouriteTeam.setVisibility(VISIBLE);
                binding.noResultFavouriteTeam.setVisibility(GONE);
            }
        } else {
            binding.noFavouriteTeam.setVisibility(GONE);
            binding.noResultFavouriteTeam.setVisibility(GONE);
        }

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
        if (listLeagueFavourite.isEmpty()) {
            if (isSearch) {
                binding.noResultFavouriteLeague.setVisibility(VISIBLE);
                binding.noFavouriteLeague.setVisibility(GONE);
            } else {
                binding.noResultFavouriteLeague.setVisibility(GONE);
                binding.noFavouriteLeague.setVisibility(VISIBLE);
            }
        } else {
            binding.noResultFavouriteLeague.setVisibility(GONE);
            binding.noFavouriteLeague.setVisibility(GONE);
        }

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
            str = "";
            isSearch = false;
            SPUtils.hideKeyboard(requireContext(), binding.edtText);
            binding.edtText.clearFocus();
            binding.clHeader.setVisibility(VISIBLE);
            binding.clSearch.setVisibility(GONE);
            search();
        });
        binding.edtText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    String text = binding.edtText.getText().toString().trim();
                    if (!text.isEmpty()) {
                        search();

                    }
                    binding.edtText.clearFocus();
                    SPUtils.hideKeyboard(requireContext(), binding.edtText);
                    return true;
                }
                return false;
            }
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
                str = s.toString().trim();
            }
        });
        binding.edtText.setOnClickListener(v -> {
            SPUtils.showKeyboard(requireContext(), binding.edtText);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void search() {
        if (str.isEmpty()) {
            if (state == TEAM) {
                listAllTeam.clear();
                currentPageTeam = 1;
                loadingDialog.show();
                fetchTeamPage(currentPageTeam);
                listTeamFavourite.clear();
                listTeamFavourite.addAll(TeamDatabase.getInstance(requireContext()).teamDAO().getAllTeamFavourite());
                teamAdapterFavourite.notifyDataSetChanged();
                checkEmptyTeam();
            } else {
                listAllLeague.clear();
                currentPageLeague = 1;
                loadingDialog.show();
                fetchLeaguePage(currentPageLeague);
                listLeagueFavourite.clear();
                listLeagueFavourite.addAll(LeagueDatabase.getInstance(requireContext()).leagueDAO().getAllLeagueFavourite());
                leagueAdapterFavourite.notifyDataSetChanged();
                checkEmptyLeague();
            }
        } else {
            if (state == TEAM) {
                listAllTeam.clear();
                currentPageTeam = 1;
                loadingDialog.show();
                fetchTeamPageSearch(str, currentPageTeam);
                listTeamFavourite.clear();
                for (TeamModel teamModel : TeamDatabase.getInstance(requireContext()).teamDAO().getAllTeamFavourite()) {
                    if (teamModel.getName().toLowerCase().contains(str.toLowerCase()))
                        listTeamFavourite.add(teamModel);
                }
                teamAdapterFavourite.notifyDataSetChanged();
                checkEmptyTeam();
            } else {
                listAllLeague.clear();
                currentPageLeague = 1;
                loadingDialog.show();
                fetchLeaguePageSearch(str, currentPageLeague);
                listLeagueFavourite.clear();
                for (LeagueModel leagueModel : LeagueDatabase.getInstance(requireContext()).leagueDAO().getAllLeagueFavourite()) {
                    if (leagueModel.getName().toLowerCase().contains(str.toLowerCase()))
                        listLeagueFavourite.add(leagueModel);
                }
                leagueAdapterFavourite.notifyDataSetChanged();
                checkEmptyLeague();
            }
        }
    }

    private void fetchTeamPageSearch(String str, int page) {
        try {
            ApiDataService.apiService.callTeamSearch(str, ConstantApiData.KEY, page, "country").enqueue(new Callback<TeamResponse>() {
                @Override
                public void onResponse(@NonNull Call<TeamResponse> call, @NonNull Response<TeamResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e("API_RESPONSE", "Raw JSON: " + new Gson().toJson(response.body()));
                        Log.e("API_RESPONSE", "page: " + page);
                        TeamResponse teamResponse = response.body();
                        if (teamResponse.data != null) {
                            int oldPos = listAllTeam.size();
                            for (TeamInMatch team : teamResponse.data) {
                                team.countryName = team.getCountry().name;
                            }
                            listAllTeam.addAll(teamResponse.data);
                            Log.e("call_api_data", "call true:");
                            Gson gson = new Gson();
                            PaginationModel pagination = gson.fromJson(new Gson().toJson(teamResponse.pagination), PaginationModel.class);
                            if (pagination == null) {
                                isHasMoreTeam = false;
                            } else isHasMoreTeam = pagination.has_more;
                            setFavouriteTeamLoad();
                            if (oldPos != 0) teamAdapter.notifyItemChanged(oldPos - 1);
                            teamAdapter.notifyItemRangeChanged(oldPos, teamResponse.data.size());
                            binding.rcvTeamAll.post(() -> {
                                loadingDialog.dismiss();
                                checkEmptyTeam();
                            });
                        } else {
                            Log.e("call_api_data", "call false: Code: " + response.code());
                            loadingDialog.dismiss();
                            setFavouriteTeamLoad();
                            checkEmptyTeam();
                        }

                    } else {
                        Log.e("call_api_data", "call false: Code: " + response.code());
                        loadingDialog.dismiss();
                        setFavouriteTeamLoad();
                        checkEmptyTeam();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<TeamResponse> call, @NonNull Throwable t) {
                    Log.e("call_api_data", "onfailure" + t);
                    loadingDialog.dismiss();
                    setFavouriteTeamLoad();
                    checkEmptyTeam();
                }
            });

        } catch (Exception e) {
            Log.e("call_api_data", "catch: ", e);
            loadingDialog.dismiss();
            setFavouriteTeamLoad();
            checkEmptyTeam();
        }
    }

    private void fetchTeamPage(int page) {
        try {
            ApiDataService.apiService.callTeam(ConstantApiData.KEY, page, "country").enqueue(new Callback<TeamResponse>() {
                @Override
                public void onResponse(@NonNull Call<TeamResponse> call, @NonNull Response<TeamResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e("API_RESPONSE", "Raw JSON: " + new Gson().toJson(response.body()));
                        Log.e("API_RESPONSE", "page: " + page);
                        TeamResponse teamResponse = response.body();
                        if (teamResponse.data != null) {
                            int oldPos = listAllTeam.size();
                            for (TeamInMatch team : teamResponse.data) {
                                team.countryName = team.getCountry().name;
                            }
                            listAllTeam.addAll(teamResponse.data);
                            Log.e("call_api_data", "call true:");
                            Gson gson = new Gson();
                            PaginationModel pagination = gson.fromJson(new Gson().toJson(teamResponse.pagination), PaginationModel.class);
                            if (pagination == null) {
                                isHasMoreTeam = false;
                            } else isHasMoreTeam = pagination.has_more;
                            setFavouriteTeamLoad();
                            if (oldPos != 0) teamAdapter.notifyItemChanged(oldPos - 1);
                            teamAdapter.notifyItemRangeChanged(oldPos, teamResponse.data.size());
                            binding.rcvTeamAll.post(() -> {
                                loadingDialog.dismiss();
                                checkEmptyTeam();
                            });
                        } else {
                            Log.e("call_api_data", "call false: Code: " + response.code());
                            loadingDialog.dismiss();
                            setFavouriteTeamLoad();
                            checkEmptyTeam();
                        }

                    } else {
                        Log.e("call_api_data", "call false: Code: " + response.code());
                        loadingDialog.dismiss();
                        setFavouriteTeamLoad();
                        checkEmptyTeam();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<TeamResponse> call, @NonNull Throwable t) {
                    Log.e("call_api_data", "onfailure" + t);
                    loadingDialog.dismiss();
                    setFavouriteTeamLoad();
                    checkEmptyTeam();
                }
            });

        } catch (Exception e) {
            Log.e("call_api_data", "catch: ", e);
            loadingDialog.dismiss();
            setFavouriteTeamLoad();
            checkEmptyTeam();
        }
    }

    private void fetchLeaguePageSearch(String str, int page) {
        try {
            ApiDataService.apiService.callLeagueSearch(str, ConstantApiData.KEY, page, "country").enqueue(new Callback<LeagueResponse>() {
                @Override
                public void onResponse(@NonNull Call<LeagueResponse> call, @NonNull Response<LeagueResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e("API_RESPONSE", "Raw JSON: " + new Gson().toJson(response.body()));
                        Log.e("API_RESPONSE", "page: " + page);
                        LeagueResponse leagueResponse = response.body();
                        if (leagueResponse.data != null) {
                            int oldPos = listAllLeague.size();
                            for (LeagueDetail leagueDetail : leagueResponse.data) {
                                leagueDetail.countryName = leagueDetail.getCountry().name;
                            }
                            listAllLeague.addAll(leagueResponse.data);
                            Log.e("call_api_data", "call true:");
                            Gson gson = new Gson();
                            PaginationModel pagination = gson.fromJson(new Gson().toJson(leagueResponse.pagination), PaginationModel.class);
                            if (pagination == null) {
                                isHasMoreLeague = false;
                            } else isHasMoreLeague = pagination.has_more;
                            setFavouriteLeagueLoad();
                            if (oldPos != 0) leagueAdapter.notifyItemChanged(oldPos - 1);
                            leagueAdapter.notifyItemRangeChanged(oldPos, leagueResponse.data.size());
                            binding.rcvLeagueAll.post(() -> {
                                loadingDialog.dismiss();
                                checkEmptyLeague();
                            });
                        } else {
                            Log.e("call_api_data", "call false: Code: " + response.code());
                            loadingDialog.dismiss();
                            setFavouriteLeagueLoad();
                            checkEmptyLeague();
                        }

                    } else {
                        Log.e("call_api_data", "call false: Code: " + response.code());
                        loadingDialog.dismiss();
                        setFavouriteLeagueLoad();
                        checkEmptyLeague();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LeagueResponse> call, @NonNull Throwable t) {
                    Log.e("call_api_data", "onfailure" + t);
                    loadingDialog.dismiss();
                    setFavouriteLeagueLoad();
                    checkEmptyLeague();
                }
            });

        } catch (Exception e) {
            Log.e("call_api_data", "catch: ", e);
            loadingDialog.dismiss();
            setFavouriteLeagueLoad();
            checkEmptyLeague();
        }
    }

    private void fetchLeaguePage(int page) {
        try {
            ApiDataService.apiService.callLeague(ConstantApiData.KEY, page, "country").enqueue(new Callback<LeagueResponse>() {
                @Override
                public void onResponse(@NonNull Call<LeagueResponse> call, @NonNull Response<LeagueResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e("API_RESPONSE", "Raw JSON: " + new Gson().toJson(response.body()));
                        Log.e("API_RESPONSE", "page: " + page);
                        LeagueResponse leagueResponse = response.body();
                        if (leagueResponse.data != null) {
                            int oldPos = listAllLeague.size();
                            for (LeagueDetail leagueDetail : leagueResponse.data) {
                                leagueDetail.countryName = leagueDetail.getCountry().name;
                            }
                            listAllLeague.addAll(leagueResponse.data);
                            Log.e("call_api_data", "call true:");
                            Gson gson = new Gson();
                            PaginationModel pagination = gson.fromJson(new Gson().toJson(leagueResponse.pagination), PaginationModel.class);
                            if (pagination == null) {
                                isHasMoreLeague = false;
                            } else isHasMoreLeague = pagination.has_more;
                            if (oldPos != 0) leagueAdapter.notifyItemChanged(oldPos - 1);
                            setFavouriteLeagueLoad();
                            leagueAdapter.notifyItemRangeChanged(oldPos, leagueResponse.data.size());
                            binding.rcvLeagueAll.post(() -> {
                                loadingDialog.dismiss();
                                checkEmptyLeague();
                            });
                        } else {
                            Log.e("call_api_data", "call false: Code: " + response.code());
                            loadingDialog.dismiss();
                            setFavouriteLeagueLoad();
                            checkEmptyLeague();
                        }

                    } else {
                        Log.e("call_api_data", "call false: Code: " + response.code());
                        loadingDialog.dismiss();
                        checkEmptyLeague();
                        setFavouriteLeagueLoad();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LeagueResponse> call, @NonNull Throwable t) {
                    Log.e("call_api_data", "onfailure" + t);
                    loadingDialog.dismiss();
                    setFavouriteLeagueLoad();
                    checkEmptyLeague();
                }
            });

        } catch (Exception e) {
            Log.e("call_api_data", "catch: ", e);
            loadingDialog.dismiss();
            setFavouriteLeagueLoad();
            checkEmptyLeague();
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
        if (state == LEAGUE) {
            binding.tvLeague.setBackgroundResource(R.drawable.bg_select_favourite_item);
            binding.tvLeague.setTextColor(Color.parseColor("#0094FD"));
            binding.clLeague.setVisibility(VISIBLE);
            loadingDialog.show();
            if (IsNetWork.haveNetworkConnection(requireContext())) {
                currentPageLeague = 1;
                if (isSearch)
                    fetchLeaguePageSearch(str, currentPageLeague);
                else fetchLeaguePage(currentPageLeague);
            } else {
                Log.e("call_api_data", "No internet to call api");
                new Handler().postDelayed(() -> loadingDialog.dismiss(), 500);
            }
        } else if (state == TEAM) {
            binding.tvTeam.setBackgroundResource(R.drawable.bg_select_favourite_item);
            binding.tvTeam.setTextColor(Color.parseColor("#0094FD"));
            binding.clTeam.setVisibility(VISIBLE);
            loadingDialog.show();
            if (IsNetWork.haveNetworkConnection(requireContext())) {
                currentPageTeam = 1;
                if (isSearch)
                    fetchTeamPageSearch(str, currentPageTeam);
                else fetchTeamPage(currentPageTeam);
            } else {
                Log.e("call_api_data", "No internet to call api");
                new Handler().postDelayed(() -> loadingDialog.dismiss(), 500);
            }
        }
    }

    public void startArc(Intent intent) {
        if (getContext() instanceof HomeActivity) {
            HomeActivity main = (HomeActivity) getContext();
            main.resultLauncher.launch(intent);
        }
    }

    @SuppressLint("SetTextI18n")
    private void initAdapter() {
        teamClickCallBack = new TeamClickCallBack() {
            @Override
            public void select(TeamModel teamModel) {
                TeamModel teamModel1 = TeamDatabase.getInstance(requireContext()).teamDAO().getTeamById(teamModel.getId());
                if (teamModel1 != null) {
                    Intent intent = new Intent(requireContext(), TeamDetailActivity.class);
                    intent.putExtra(SPUtils.INTENT_TEAM, teamModel1);
                    startArc(intent);
                } else {
                    for (TeamModel team : listAllTeam) {
                        if (team.getId() == teamModel.getId()) {
                            Intent intent = new Intent(requireContext(), TeamDetailActivity.class);
                            intent.putExtra(SPUtils.INTENT_TEAM, team);
                            startArc(intent);
                            break;
                        }
                    }
                }
            }

            @Override
            public void follow(int position, TeamModel teamModel) {
                changeFavouriteListTeam(position, teamModel);
            }

            @Override
            public void load() {
                loadingDialog.show();
                if (isHasMoreTeam) {
                    if (IsNetWork.haveNetworkConnection(requireContext())) {
                        currentPageTeam++;
                        if (isSearch)
                            fetchTeamPageSearch(str, currentPageTeam);
                        else fetchTeamPage(currentPageTeam);
                    } else {
                        Log.e("call_api_data", "No internet to call api");
                        new Handler().postDelayed(() -> loadingDialog.dismiss(), 500);
                    }
                } else {
                    new Handler().postDelayed(() -> loadingDialog.dismiss(), 500);
                }
            }
        };
        leagueClickCallBack = new LeagueClickCallBack() {
            @Override
            public void select(LeagueModel leagueModel) {
                LeagueModel leagueModel1 = LeagueDatabase.getInstance(requireContext()).leagueDAO().getLeagueById(leagueModel.id);
                if (leagueModel1 != null) {
                    Intent intent = new Intent(requireContext(), LeagueDetailActivity.class);
                    intent.putExtra(SPUtils.INTENT_LEAGUE, leagueModel1);
                    startArc(intent);
                } else {
                    for (LeagueModel leagueDetail : listAllLeague) {
                        if (leagueModel.getId() == leagueDetail.getId()) {
                            Intent intent = new Intent(requireContext(), LeagueDetailActivity.class);
                            intent.putExtra(SPUtils.INTENT_LEAGUE, leagueDetail);
                            startArc(intent);
                            break;
                        }
                    }
                }

            }

            @Override
            public void follow(int position, LeagueModel leagueModel) {
                changeFavouriteListLeague(position, leagueModel);
            }

            @Override
            public void load() {
                loadingDialog.show();
                if (isHasMoreLeague) {
                    if (IsNetWork.haveNetworkConnection(requireContext())) {
                        currentPageLeague++;
                        if (isSearch)
                            fetchLeaguePageSearch(str, currentPageLeague);
                        else fetchLeaguePage(currentPageLeague);
                    } else {
                        Log.e("call_api_data", "No internet to call api");
                        new Handler().postDelayed(() -> loadingDialog.dismiss(), 500);
                    }
                } else {
                    new Handler().postDelayed(() -> loadingDialog.dismiss(), 500);
                }
            }
        };
        binding.tvFavouriteTeam.setText(getString(R.string.favourite) + " (" + listTeamFavourite.size() + ")");
        binding.tvAllTeam.setText(getString(R.string.all_teams) + " (" + 58875 + ")");
        binding.tvFavouriteLeague.setText(getString(R.string.favourite) + " (" + listLeagueFavourite.size() + ")");
        binding.tvAllLeague.setText(getString(R.string.all_leagues) + " (" + 2275 + ")");
        teamAdapterFavourite = new TeamAdapter(requireContext(), listTeamFavourite, true, teamClickCallBack);
        teamAdapter = new TeamAdapter(requireContext(), listAllTeam, false, teamClickCallBack);
        binding.rcvTeamFavourite.setAdapter(teamAdapterFavourite);
        binding.rcvTeamAll.setAdapter(teamAdapter);
        leagueAdapterFavourite = new LeagueAdapter(requireContext(), listLeagueFavourite, true, leagueClickCallBack);
        leagueAdapter = new LeagueAdapter(requireContext(), listAllLeague, false, leagueClickCallBack);
        binding.rcvLeagueAll.setAdapter(leagueAdapter);
        binding.rcvLeagueFavourite.setAdapter(leagueAdapterFavourite);
    }

}