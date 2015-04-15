package com.team.two.lloyds_app.screens.fragments;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.team.two.lloyds_app.R;
import com.team.two.lloyds_app.objects.Offer;
import com.team.two.lloyds_app.screens.activities.MainActivity;

import java.util.ArrayList;


/**
 * Created by danielbaranowski on 02/04/15.
 */
public class OffersFragment extends android.support.v4.app.Fragment {
    public static final String TITLE = "Offers and Deals";
    View root;

    TextView availablePoints;
    ArrayList<Offer> availableOfferData;
    ListView availableOffersListView;

    public OffersFragment() {
        // Required empty public constructor
    }

    @SuppressWarnings("EmptyMethod")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActivity().setTitle(TITLE);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        root = inflater.inflate(R.layout.fragment_offers, container, false);

        availablePoints = (TextView) root.findViewById(R.id.customer_points);
        availableOffersListView = (ListView) root.findViewById(R.id.available_deals_list);

        availablePoints.setText("Available Points: " + ((MainActivity) getActivity()).getCustomer().getOfferPoints());
        availableOfferData = ((MainActivity) getActivity()).getAvailableOffers();
        availableOffersListView.setAdapter(new AvailableOffersAdapter(getActivity(),availableOfferData));
        availableOffersListView.setClickable(true);
        availableOffersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity) getActivity()).activateOffer((Offer)parent.getAdapter().getItem(position));
                ((MainActivity) getActivity()).openOffers();
            }
        });

        return root;
    }

    class AvailableOffersAdapter extends BaseAdapter {

        ArrayList<Offer> list = new ArrayList<>();

        public AvailableOffersAdapter(Activity activity, ArrayList<Offer> list){
            super();
            this.list = list;

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View row = inflater.inflate(R.layout.avaliable_offer_row,parent,false);

            ImageView icon = (ImageView) row.findViewById(R.id.available_offer_icon);
            TextView name = (TextView) row.findViewById(R.id.available_offer_name);
            TextView description = (TextView) row.findViewById(R.id.available_offer_description);
            TextView price = (TextView) row.findViewById(R.id.available_offer_price);

            icon.setImageResource(list.get(position).getIcon());
            name.setText(list.get(position).getName());
            description.setText(list.get(position).getDescription());
            price.setText(list.get(position).getPrice()+" Points");



            return row;
        }
    }

}
