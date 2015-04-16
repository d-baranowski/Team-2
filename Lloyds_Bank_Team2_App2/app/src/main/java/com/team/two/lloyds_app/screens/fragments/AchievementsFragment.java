package com.team.two.lloyds_app.screens.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.team.two.lloyds_app.R;
import com.team.two.lloyds_app.objects.Achievement;
import com.team.two.lloyds_app.screens.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class AchievementsFragment extends android.support.v4.app.Fragment {
    public static final String TITLE = "Achievements";

    private List<Achievement> achievements = new ArrayList<Achievement>();
    private ListView achievementListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout rl = (RelativeLayout)inflater.inflate(R.layout.fragment_achievements, container, false);
        achievementListView = (ListView)rl.findViewById(R.id.listView);
        populateAchievementList();
        populateListView();

        // Inflate the layout for this fragment
        return rl;
    }

    // Test achievement list population
    private void populateAchievementList()
    {
        achievements = ((MainActivity)getActivity()).getAchievements();

        TextView achievementTitle = (TextView) getActivity().findViewById(R.id.achievementTitle);

        //achievements.add(new Achievement(1, "Transaction over £100 a b c d e f g h i h j l m n o p q r s t u v w", "To get this achievement, make a transaction over £100", 451, R.drawable.achievement_1));
        //achievements.add(new Achievement(2, "Transaction over £200", "To get this achievement, make a transaction over £200", 90, R.drawable.achievement_2));
        //achievements.add(new Achievement(3, "Transaction over £400", "To get this achievement, make a transaction over £400", 180, R.drawable.achievement_3));
        //achievements.add(new Achievement(1, "Transaction over £100", "To get this achievement, make a transaction over £100", 45, R.drawable.achievement_1));
        //achievements.add(new Achievement(2, "Transaction over £200", "To get this achievement, make a transaction over £200", 90, R.drawable.achievement_2));
        //achievements.add(new Achievement(3, "Transaction over £400", "To get this achievement, make a transaction over £400", 180, R.drawable.achievement_3));
        //achievements.add(new Achievement(1, "Transaction over £100", "To get this achievement, make a transaction over £100", 45, R.drawable.achievement_1));
        //achievements.add(new Achievement(2, "Transaction over £200", "To get this achievement, make a transaction over £200", 90, R.drawable.achievement_2));
        //achievements.add(new Achievement(3, "Transaction over £400", "To get this achievement, make a transaction over £400", 180, R.drawable.achievement_3));
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
