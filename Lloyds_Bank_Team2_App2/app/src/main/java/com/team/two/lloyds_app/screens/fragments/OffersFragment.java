package com.team.two.lloyds_app.screens.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.team.two.lloyds_app.R;
import com.team.two.lloyds_app.objects.Offer;
import com.team.two.lloyds_app.screens.activities.MainActivity;

import java.util.ArrayList;

/**
 * Author: Daniel Baranowski
 * Date: 02/04/2015
 * Purpose: Code for offers
 */

public class OffersFragment extends android.support.v4.app.Fragment{
	public static final String TITLE = "Offers and Deals";
	View root;

	TextView availablePoints;
	ArrayList<Offer> availableOfferData;
	ListView availableOffersListView;
	ArrayList<Offer> activeOfferData;
	ListView activeOffersListView;

	int points;

	public OffersFragment(){
		// Required empty public constructor
	}

	@SuppressWarnings("EmptyMethod")
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getActivity().setTitle(TITLE);


	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		root = inflater.inflate(R.layout.fragment_offers, container, false);

		points = ((MainActivity) getActivity()).getCustomer().getOfferPoints();
		availablePoints = (TextView) root.findViewById(R.id.customer_points);
		availableOffersListView = (ListView) root.findViewById(R.id.available_deals_list);

		availablePoints.setText("Available Points: " + points);
		availableOfferData = ((MainActivity) getActivity()).getAvailableOffers();
		availableOffersListView.setAdapter(new AvailableOffersAdapter(getActivity(), availableOfferData));

		availableOffersListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				Offer offer = (Offer) parent.getAdapter().getItem(position);

				if(offer.getPrice() <= points){
					((MainActivity) getActivity()).spendOffersPoints(offer.getPrice());
					((MainActivity) getActivity()).activateOffer(offer);
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(getActivity(), "Successfully activated " + offer.getName(), duration);
					toast.show();
					((MainActivity) getActivity()).openOffers();
				}else{
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(getActivity(), "Not enough points", duration);
					toast.show();
				}

			}
		});

		activeOffersListView = (ListView) root.findViewById(R.id.active_deals_list);
		activeOfferData = ((MainActivity) getActivity()).getActiveOffers();
		activeOffersListView.setAdapter(new ActiveOffersAdapter(getActivity(), activeOfferData));
		activeOffersListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				String offerName = ((Offer) parent.getAdapter().getItem(position)).getName();
				int offerBarCode = ((Offer) parent.getAdapter().getItem(position)).getBarcode();
				activeOfferDialog(offerName, offerBarCode);
			}
		});

		return root;
	}

	public void activeOfferDialog(String offerName, int barCode){
		// Create custom dialog object
		final Dialog dialog = new Dialog(getActivity());
		// Include dialog.xml file
		dialog.setContentView(R.layout.active_offer_dialog);
		// Set dialog title
		dialog.setTitle(offerName);
		//UI References
		ImageView barcode = (ImageView) dialog.findViewById(R.id.deal_barcode);
		Button dismiss = (Button) dialog.findViewById(R.id.dismiss_button);

		barcode.setImageResource(barCode);
		dismiss.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	class AvailableOffersAdapter extends BaseAdapter{

		ArrayList<Offer> list = new ArrayList<>();

		public AvailableOffersAdapter(Activity activity, ArrayList<Offer> list){
			super();
			this.list = list;

		}

		@Override
		public int getCount(){
			return list.size();
		}

		@Override
		public Object getItem(int position){
			return list.get(position);
		}

		@Override
		public long getItemId(int position){
			return position;
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			LayoutInflater inflater = getActivity().getLayoutInflater();
			View row = inflater.inflate(R.layout.avaliable_offer_row, parent, false);

			ImageView icon = (ImageView) row.findViewById(R.id.available_offer_icon);
			TextView name = (TextView) row.findViewById(R.id.available_offer_name);
			TextView description = (TextView) row.findViewById(R.id.available_offer_description);
			TextView price = (TextView) row.findViewById(R.id.available_offer_price);

			icon.setImageResource(list.get(position).getIcon());
			name.setText(list.get(position).getName());
			description.setText(list.get(position).getDescription());
			price.setText(list.get(position).getPrice() + " Points");

			return row;
		}
	}

	class ActiveOffersAdapter extends BaseAdapter{

		ArrayList<Offer> list = new ArrayList<>();

		public ActiveOffersAdapter(Activity activity, ArrayList<Offer> list){
			super();
			this.list = list;

		}

		@Override
		public int getCount(){
			return list.size();
		}

		@Override
		public Object getItem(int position){
			return list.get(position);
		}

		@Override
		public long getItemId(int position){
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			LayoutInflater inflater = getActivity().getLayoutInflater();
			View row = inflater.inflate(R.layout.active_offer_row, parent, false);

			ImageView icon = (ImageView) row.findViewById(R.id.active_offer_icon);
			TextView name = (TextView) row.findViewById(R.id.active_offer_name);
			TextView description = (TextView) row.findViewById(R.id.active_offer_description);

			icon.setImageResource(list.get(position).getIcon());
			name.setText(list.get(position).getName());
			description.setText(list.get(position).getDescription());

			return row;
		}
	}

}
