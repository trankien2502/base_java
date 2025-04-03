package com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.timeline;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.livescore.soccerscore.matchlive.R;
import com.livescore.soccerscore.matchlive.api_data.model.ScoreModel;
import com.livescore.soccerscore.matchlive.base.BaseFragment;
import com.livescore.soccerscore.matchlive.databinding.FragmentTimelineBinding;
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.EventDetail;
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.FixtureDetailModel;
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.MatchDetailActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class TimelineFragment extends BaseFragment<FragmentTimelineBinding> {

    EventAdapter adapter1, adapter2;
    FixtureDetailModel fixtureDetailModel;
    List<EventDetail> listFirstHalf = new ArrayList<>();
    List<EventDetail> listSecondHalf = new ArrayList<>();

    @Override
    public FragmentTimelineBinding setBinding(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        return FragmentTimelineBinding.inflate(getLayoutInflater());
    }

    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    @Override
    public void initView() {
        if (MatchDetailActivity.instance != null) {
            if (MatchDetailActivity.instance.fixtureDetailModel != null) {
                fixtureDetailModel = MatchDetailActivity.instance.fixtureDetailModel;
                if (fixtureDetailModel.participants.get(0).getMeta().location.equals("home")) {
                    Glide.with(this).load(fixtureDetailModel.participants.get(0).getImage_path()).into(binding.ivGuessHome);
                    Glide.with(this).load(fixtureDetailModel.participants.get(1).getImage_path()).into(binding.ivGuessAway);
                    binding.tvHome.setText(fixtureDetailModel.participants.get(0).getName());
                    binding.tvAway.setText(fixtureDetailModel.participants.get(1).getName());
                } else {
                    Glide.with(this).load(fixtureDetailModel.participants.get(1).getImage_path()).into(binding.ivGuessHome);
                    Glide.with(this).load(fixtureDetailModel.participants.get(0).getImage_path()).into(binding.ivGuessAway);
                    binding.tvHome.setText(fixtureDetailModel.participants.get(1).getName());
                    binding.tvAway.setText(fixtureDetailModel.participants.get(0).getName());
                }
                Log.e("check_api_null", "fixtureDetail: " + fixtureDetailModel);
                String result = fixtureDetailModel.result_info != null ? fixtureDetailModel.result_info : "";
                if (result.equals("null") || result.isEmpty()) {
                    if (fixtureDetailModel.odds != null) {
                        if (!fixtureDetailModel.odds.isEmpty()) {
                            binding.llGuess.setVisibility(VISIBLE);
                        } else {
                            binding.llGuess.setVisibility(GONE);
                        }
                    } else {
                        binding.llGuess.setVisibility(GONE);
                    }
                } else {
                    binding.llGuess.setVisibility(GONE);
                }

                getEventFirstAndHalf();
                adapter1 = new EventAdapter(requireContext(), listFirstHalf, MatchDetailActivity.instance.homeId);
                binding.rcvEventFirst.setAdapter(adapter1);
                adapter2 = new EventAdapter(requireContext(), listSecondHalf, MatchDetailActivity.instance.homeId);
                binding.rcvEventSecond.setAdapter(adapter2);
                binding.tvVenue.setText(fixtureDetailModel.getVenue().address);
                binding.tvCompetition.setText(fixtureDetailModel.getLeague().name);
                if (!fixtureDetailModel.scores.isEmpty()) {
                    int scoreHome1 = 0, scoreAway1 = 0;
                    int scoreHome2 = 0, scoreAway2 = 0;
                    for (ScoreModel scoreModel : fixtureDetailModel.scores) {
                        if (scoreModel.description.equals("2ND_HALF")) {
                            if (scoreModel.getScore().participant.equals("home")) {
                                scoreHome2 = scoreModel.getScore().goals;
                            }
                            if (scoreModel.getScore().participant.equals("away")) {
                                scoreAway2 = scoreModel.getScore().goals;
                            }
                        } else if (scoreModel.description.equals("1ST_HALF")) {
                            if (scoreModel.getScore().participant.equals("home")) {
                                scoreHome1 = scoreModel.getScore().goals;
                            }
                            if (scoreModel.getScore().participant.equals("away")) {
                                scoreAway1 = scoreModel.getScore().goals;
                            }
                        }
                    }
                    binding.tvHalfTime.setText(getString(R.string.half_time) + " (" + scoreHome1 + " - " + scoreAway1 + ")");
                    binding.tvFullTime.setText(getString(R.string.full_time) + " (" + scoreHome2 + " - " + scoreAway2 + ")");
                }
                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = inputFormat.parse(fixtureDetailModel.starting_at);
                    assert date != null;
                    String timeText = timeFormat.format(date);
                    String dateText = dateFormat.format(date);
                    binding.tvTimeKickOff.setText(dateText + " - " + timeText);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }

    private void getEventFirstAndHalf() {
        if (fixtureDetailModel != null) {
            if (!fixtureDetailModel.events.isEmpty()) {
                for (EventDetail eventDetail : fixtureDetailModel.events) {
                    if (eventDetail.getPeriod().sort_order == 1) {
                        listFirstHalf.add(eventDetail);
                    } else {
                        listSecondHalf.add(eventDetail);
                    }
                }
                Collections.sort(listFirstHalf, new Comparator<EventDetail>() {
                    @Override
                    public int compare(EventDetail o1, EventDetail o2) {
                        return Integer.compare(o2.minute, o1.minute);
                    }
                });
                Collections.sort(listSecondHalf, new Comparator<EventDetail>() {
                    @Override
                    public int compare(EventDetail o1, EventDetail o2) {
                        return Integer.compare(o2.minute, o1.minute);
                    }
                });
            }
        }
    }

    @Override
    public void bindView() {
        binding.tvDrawBtn.setOnClickListener(v -> {
            binding.llButton.setVisibility(GONE);
            binding.llResultGuess.setVisibility(VISIBLE);
            showGuess();
        });
        binding.tvAway.setOnClickListener(v -> {
            binding.llButton.setVisibility(GONE);
            binding.llResultGuess.setVisibility(VISIBLE);
            showGuess();
        });
        binding.tvHome.setOnClickListener(v -> {
            binding.llButton.setVisibility(GONE);
            binding.llResultGuess.setVisibility(VISIBLE);
            showGuess();
        });
    }

    private void showGuess() {
        for (OddDetail oddDetail : fixtureDetailModel.odds) {
            if (oddDetail.label.equals("Home")) {
                LinearLayout.LayoutParams paramsHome = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        Float.parseFloat(oddDetail.probability.replace("%", ""))

                );
                binding.viewHomeWin.setLayoutParams(paramsHome);
                binding.tvHomeWin.setLayoutParams(paramsHome);
                binding.tvHomeWin.setText(oddDetail.probability);
            } else if (oddDetail.label.equals("Draw")) {
                LinearLayout.LayoutParams paramsDraw = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        Float.parseFloat(oddDetail.probability.replace("%", ""))

                );
                binding.viewDraw.setLayoutParams(paramsDraw);
                binding.tvDraw.setLayoutParams(paramsDraw);
                binding.tvDraw.setText(oddDetail.probability);
            } else if (oddDetail.label.equals("Away")) {
                LinearLayout.LayoutParams paramsAway = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        Float.parseFloat(oddDetail.probability.replace("%", ""))

                );
                binding.viewAwayWin.setLayoutParams(paramsAway);
                binding.tvAwayWin.setLayoutParams(paramsAway);
                binding.tvAwayWin.setText(oddDetail.probability);
            }
        }
    }
}