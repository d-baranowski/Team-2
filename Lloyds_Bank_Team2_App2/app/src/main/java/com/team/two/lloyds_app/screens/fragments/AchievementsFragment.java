package com.team.two.lloyds_app.screens.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.team.two.lloyds_app.R;
import com.team.two.lloyds_app.objects.Achievement;
import com.team.two.lloyds_app.screens.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Deniz
 * Date: 12/04/2015
 * Purpose: Code for achievements
 */

public class AchievementsFragment extends android.support.v4.app.Fragment {
    public static final String TITLE = "Achievements";

    private List<Achievement> achievements = new ArrayList<Achievement>();
    private ListView achievementListView;
    private Button redeemRewards;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout rl = (RelativeLayout)inflater.inflate
		//Builds the horizontal aspect of the table
		(R.layout.fragment_achievements, container, false);
        achievementListView = (ListView)rl.findViewById(R.id.listView);
        redeemRewards = (Button) rl.findViewById(R.id.redeemRewardsButton);

        populateAchievementList();
        populateListView();

        achievementListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Achievement achievement = (Achievement) parent.getAdapter().getItem(position);
                showAchievementDialog(achievement);
            }
        });

        redeemRewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).openOffers();
            }
        });

        // Inflate the layout for this fragment
        return rl;
    }

    public void showAchievementDialog(Achievement achievement){
        // Create custom dialog object
        final Dialog dialog = new Dialog(getActivity());

        // Include dialog.xml file
        dialog.setContentView(R.layout.achievement_information_dialog);

        // Set dialog title
        dialog.setTitle(achievement.getTitle());

        // Print achievement instructions
        TextView achievementDescription = (TextView) dialog.findViewById(R.id.achievementInformation);
        achievementDescription.setText(achievement.getDescription());

        // Check if achievement is an incremental achievement (requires mutliple steps to be accomplished)
        if(achievement.getIncremental() == 1)
        {
            ProgressBar achievementProgress = (ProgressBar) dialog.findViewById(R.id.achievementProgressBar);
            achievementProgress.setVisibility(View.VISIBLE);
        }


        Button okButton = (Button) dialog.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    // Test achievement list population
    private void populateAchievementList()
    {
        achievements = ((MainActivity)getActivity()).getAchievements();
        TextView achievementTitle = (TextView) getActivity().findViewById(R.id.achievementTitle);
    }

    private void populateListView()
    {
        ArrayAdapter<Achievement> adapter = new AchievementListAdapter();
        //ListView list = (ListView) getActivity().findViewById(R.id.listView);
        achievementListView.setAdapter(adapter);
    }

    private class AchievementListAdapter extends ArrayAdapter<Achievement> {
        public AchievementListAdapter() {
            super(getActivity(), R.layout.achievement_view, achievements);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View achievementView = convertView;
            if (achievementView == null) {
                achievementView = getActivity().getLayoutInflater().inflate(R.layout.achievement_view, parent, false);
            }

            // Get achievement to fill and show in the view
            Achievement achievement = achievements.get(position);

            // Show image
            ImageView achievementIcon = (ImageView) achievementView.findViewById(R.id.achievementIcon);
            //int iconResourceId = getResources().getIdentifier("achievement_1", "drawable", getActivity().getPackageName());
            achievementIcon.setImageResource(achievement.getIconId());

            // Show title
            TextView achievementTitle = (TextView) achievementView.findViewById(R.id.achievementTitle);
            achievementTitle.setText(achievement.getTitle());

            // Show points
            TextView achievementPoints = (TextView) achievementView.findViewById(R.id.achievementPoints);
            int pointsInt = achievement.getPointsAchieved();
            String pointsString = Integer.toString(pointsInt);
            achievementPoints.setText(pointsString);

            return achievementView;
        }
    }


}
