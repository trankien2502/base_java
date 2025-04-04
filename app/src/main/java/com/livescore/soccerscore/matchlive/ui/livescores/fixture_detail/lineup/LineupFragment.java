package com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.lineup;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.livescore.soccerscore.matchlive.base.BaseFragment;
import com.livescore.soccerscore.matchlive.databinding.FragmentLineupBinding;
import com.livescore.soccerscore.matchlive.databinding.FragmentTimelineBinding;
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.EventDetail;
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.FixtureDetailModel;
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.LineupDetail;
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.MatchDetailActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LineupFragment extends BaseFragment<FragmentLineupBinding> {

    FixtureDetailModel fixtureDetailModel;
    List<LineupDetail> listHome = new ArrayList<>();
    List<LineupDetail> listAway = new ArrayList<>();
    List<EventDetail> listEventHome = new ArrayList<>();
    List<EventDetail> listEventAway = new ArrayList<>();
    LineupAwayAdapter awayAdapter;
    LineupHomeAdapter homeAdapter;
    SubstitutionAdapter substitutionHomeAdapter, substitutionAwayAdapter;
    long homeId = 0, awayId = 0;


    @Override
    public FragmentLineupBinding setBinding(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        return FragmentLineupBinding.inflate(getLayoutInflater());
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void initView() {
        awayAdapter = new LineupAwayAdapter(requireContext(), listAway);
        homeAdapter = new LineupHomeAdapter(requireContext(), listHome);
        binding.rcvLineupHome.setAdapter(homeAdapter);
        binding.rcvLineupAway.setAdapter(awayAdapter);
        substitutionAwayAdapter = new SubstitutionAdapter(requireContext(), listEventAway);
        substitutionHomeAdapter = new SubstitutionAdapter(requireContext(), listEventHome);
        binding.rcvSubstitutionAway.setAdapter(substitutionAwayAdapter);
        binding.rcvSubstitutionHome.setAdapter(substitutionHomeAdapter);
        if (MatchDetailActivity.instance != null) {
            homeId = MatchDetailActivity.instance.homeId;
            awayId = MatchDetailActivity.instance.awayId;
            if (MatchDetailActivity.instance.fixtureDetailModel != null) {
                fixtureDetailModel = MatchDetailActivity.instance.fixtureDetailModel;
                if (fixtureDetailModel.participants.get(0).getMeta().location.equals("home")) {
                    Glide.with(this).load(fixtureDetailModel.participants.get(0).getImage_path()).into(binding.ivHome);
                    Glide.with(this).load(fixtureDetailModel.participants.get(1).getImage_path()).into(binding.ivAway);
                    binding.tvHome.setText(fixtureDetailModel.participants.get(0).getName());
                    binding.tvAway.setText(fixtureDetailModel.participants.get(1).getName());
                } else {
                    Glide.with(this).load(fixtureDetailModel.participants.get(1).getImage_path()).into(binding.ivHome);
                    Glide.with(this).load(fixtureDetailModel.participants.get(0).getImage_path()).into(binding.ivAway);
                    binding.tvHome.setText(fixtureDetailModel.participants.get(1).getName());
                    binding.tvAway.setText(fixtureDetailModel.participants.get(0).getName());
                }
                if (!fixtureDetailModel.lineups.isEmpty()) {
                    Collections.sort(fixtureDetailModel.lineups, new Comparator<LineupDetail>() {
                        @Override
                        public int compare(LineupDetail o1, LineupDetail o2) {
                            return Integer.compare(o2.formation_position, o1.formation_position);
                        }
                    });
                    for (LineupDetail lineupDetail : fixtureDetailModel.lineups) {
                        if (lineupDetail.team_id == homeId && lineupDetail.formation_position != 0) {
                            listHome.add(lineupDetail);
                        } else if (lineupDetail.team_id == awayId && lineupDetail.formation_position != 0) {
                            listAway.add(lineupDetail);
                        }
                    }
                    awayAdapter.notifyDataSetChanged();
                    homeAdapter.notifyDataSetChanged();
                    binding.tvLineupHome.setText(getFormationField(listHome));
                    binding.tvLineupAway.setText(getFormationField(listAway));
                }
                if (!fixtureDetailModel.events.isEmpty()) {
                    Collections.sort(fixtureDetailModel.events, new Comparator<EventDetail>() {
                        @Override
                        public int compare(EventDetail o1, EventDetail o2) {
                            return Integer.compare(o1.minute, o2.minute);
                        }
                    });
                    for (EventDetail eventDetail : fixtureDetailModel.events) {
                        if (eventDetail.participant_id == homeId && eventDetail.getType().developer_name.equals("SUBSTITUTION")) {
                            listEventHome.add(eventDetail);
                        } else if (eventDetail.participant_id == awayId && eventDetail.getType().developer_name.equals("SUBSTITUTION")) {
                            listEventAway.add(eventDetail);
                        }
                    }
                    substitutionHomeAdapter.notifyDataSetChanged();
                    substitutionAwayAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void bindView() {

    }

    private String getFormationField(List<LineupDetail> listPlayer) {
        if (listPlayer.isEmpty()) return "";
        int countRow2 = 0, countRow3 = 0, countRow4 = 0, countRow5 = 0;
        for (LineupDetail player : listPlayer) {
            if (player.formation_field != null) {
                String[] parts = player.formation_field.split(":");
                String row = parts[0]; // hàng
                switch (row) {
                    case "2":
                        countRow2++;
                        break;
                    case "3":
                        countRow3++;
                        break;
                    case "4":
                        countRow4++;
                        break;
                    case "5":
                        countRow5++;
                        break;
                    default:
                        break;
                }
            }
        }
        String str = "";
        if (countRow2 != 0) str += countRow2;
        if (countRow3 != 0) str += "-" + countRow3;
        if (countRow4 != 0) str += "-" + countRow4;
        if (countRow5 != 0) str += "-" + countRow5;
        return str;
    }
}