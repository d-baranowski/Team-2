package com.team.two.lloyds_app.screens.drawer;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.team.two.lloyds_app.Constants;
import com.team.two.lloyds_app.R;
import com.team.two.lloyds_app.screens.activities.MainActivity;

import java.util.ArrayList;

public class NavigationDrawerFragment extends Fragment {
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private boolean userLearnedDrawer;
    private boolean fromSavedInstanceState;
    private Toolbar toolbar;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), Constants.KEY_USER_LEARNED_DRAWER,"false"));
        if (savedInstanceState == null){
            fromSavedInstanceState = true;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        RecyclerView drawerList = (RecyclerView) layout.findViewById(R.id.drawer_recycler);
        CustomDrawerAdapter adapter = new CustomDrawerAdapter(getActivity(), getDrawerData());
        drawerList.setAdapter(adapter);
        drawerList.setLayoutManager(new LinearLayoutManager(getActivity()));

        drawerList.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), drawerList, new ClickListener() {
            @Override
            public void onClick(final View view, int position) {
                Integer colorFrom = Color.WHITE;
                Integer colorTo = Color.parseColor("#add8e6");

                ValueAnimator thereAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                thereAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        view.setBackgroundColor((Integer) animator.getAnimatedValue());
                    }

                });

                thereAnimation.start();
                thereAnimation.reverse();
                toolbar.setTitle(Constants.SCREEN_NAMES[position]);

                switch (position) {
                    case 0:
                        ((MainActivity) getActivity()).openHome();
                        break;
                    case 1:
                        ((MainActivity) getActivity()).openStatement();
                        break;
                    case 2:
                        ((MainActivity) getActivity()).openTransfers();
                        break;
                    case 3:
                        ((MainActivity) getActivity()).openFinder();
                        break;
                    case 4:
                        ((MainActivity) getActivity()).openPlanner();
                        break;
                    case 5:
                        ((MainActivity) getActivity()).openOffers();
                        break;
                    case 6:
                        ((MainActivity) getActivity()).openAchievements();
                        break;
                    case 7:
                        ((MainActivity) getActivity()).openOptions();
                        break;
                    case 8:
                        ((MainActivity) getActivity()).openHelp();
                        break;
                    case 9:
                        ((MainActivity) getActivity()).logOut();
                        break;

                }

                drawerLayout.closeDrawers();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return layout;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        this.drawerLayout = drawerLayout;
        this.toolbar = toolbar;
        View containerView = getActivity().findViewById(fragmentId);

        //using brackets to Override methods of ActionBarDrawerToggle
        drawerToggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                if (!userLearnedDrawer){
                    userLearnedDrawer = true;
                    saveToPreferences(getActivity(), Constants.KEY_USER_LEARNED_DRAWER,userLearnedDrawer+"");
                }
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView){
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView,float slideOffset){
                if (slideOffset < 0.6){
                    //toolbar.setAlpha(1 + slideOffset);
                }
            }
        };

        if (!userLearnedDrawer && !fromSavedInstanceState){
            drawerLayout.openDrawer(containerView);
        }

        drawerLayout.setDrawerListener(drawerToggle);
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                drawerToggle.syncState();
            }
        });

    }

    public static void saveToPreferences(Context context,String preferenceName,String preverenceValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName,preverenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context,String preferenceName,String defaultValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName,defaultValue);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(final Context context, final RecyclerView recyclerView, final ClickListener clickListener ){
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                public boolean onSingleTapUp(MotionEvent e){
                    return true;
                }

                public void onLongPress(MotionEvent e){
                    View child = recyclerView.findChildViewUnder(e.getX(),e.getY());

                    if (child != null && clickListener != null){
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(),e.getY());

            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)){
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }
    }

    public static interface ClickListener{
        public void onClick(View view, int position);
        public void onLongClick(View view, int position);
    }

    public ArrayList<DrawerItem> getDrawerData(){
        ArrayList<DrawerItem> data = new ArrayList<>();
        data.add(new DrawerItem(Constants.SCREEN_NAMES[0],R.drawable.homeicon));
        data.add(new DrawerItem(Constants.SCREEN_NAMES[1],R.drawable.ic_statement_icon));
        data.add(new DrawerItem(Constants.SCREEN_NAMES[2],R.drawable.ic_transfers_icon));
        data.add(new DrawerItem(Constants.SCREEN_NAMES[3],R.drawable.ic_branch_finder_icon));
        data.add(new DrawerItem(Constants.SCREEN_NAMES[4],R.drawable.ic_money_planner_icon));
        data.add(new DrawerItem(Constants.SCREEN_NAMES[5],R.drawable.ic_offers_icon));
        data.add(new DrawerItem(Constants.SCREEN_NAMES[6],R.drawable.ic_achievements_icon));
        data.add(new DrawerItem(Constants.SCREEN_NAMES[7],R.drawable.optionsicon));
        data.add(new DrawerItem(Constants.SCREEN_NAMES[8],R.drawable.helpicon));
        data.add(new DrawerItem(Constants.SCREEN_NAMES[9],R.drawable.signouticon));
        return data;
    }

}
