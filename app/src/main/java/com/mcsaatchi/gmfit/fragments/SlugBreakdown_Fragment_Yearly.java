package com.mcsaatchi.gmfit.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mcsaatchi.gmfit.R;
import com.mcsaatchi.gmfit.classes.Constants;
import com.mcsaatchi.gmfit.rest.SlugBreakdownResponseYearly;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SlugBreakdown_Fragment_Yearly extends Fragment {
    @Bind(R.id.slugBreakdownListView)
    ListView slugBreakdownListView;
    @Bind(R.id.allTimeValueTV)
    TextView allTimeValueTV;

    private String measurementUnitForMetric;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_slug_breakdown_yearly, null);

        ButterKnife.bind(this, fragmentView);

        Bundle fragmentBundle = getArguments();

        if (fragmentBundle != null) {
            ArrayList<Parcelable> slugBreakdownData = fragmentBundle.getParcelableArrayList(Constants.BUNDLE_SLUG_BREAKDOWN_DATA_YEARLY);

            measurementUnitForMetric = fragmentBundle.getString(Constants.BUNDLE_SLUG_BREAKDOWN_MEASUREMENT_UNIT, "");

            float slugBreakdownYearlyTotal =  fragmentBundle.getFloat(Constants.BUNDLE_SLUG_BREAKDOWN_YEARLY_TOTAL, 0);
            allTimeValueTV.setText(NumberFormat.getNumberInstance(Locale.US).format((int) Double.parseDouble(String.valueOf(slugBreakdownYearlyTotal))) + " " + measurementUnitForMetric);

            hookupListWithItems(slugBreakdownData, measurementUnitForMetric);
        }

        return fragmentView;
    }

    private void hookupListWithItems(ArrayList<Parcelable> items, String measurementUnit) {
        SlugBreakdown_ListAdapter slugBreakdownListAdapter = new SlugBreakdown_ListAdapter(getActivity(), items, measurementUnit);
        slugBreakdownListView.setAdapter(slugBreakdownListAdapter);
    }

    public class SlugBreakdown_ListAdapter extends BaseAdapter {

        private ArrayList<Parcelable> slugBreakdownData;
        private Context context;
        private String measurementUnit;

        public SlugBreakdown_ListAdapter(Context context, ArrayList<Parcelable> slugBreakdownData, String measurementUnit) {
            super();
            this.context = context;
            this.slugBreakdownData = slugBreakdownData;
            this.measurementUnit = measurementUnit;
        }

        @Override
        public int getCount() {
            return slugBreakdownData.size();
        }

        @Override
        public SlugBreakdownResponseYearly getItem(int index) {
            return (SlugBreakdownResponseYearly) slugBreakdownData.get(index);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_slug_yearly_breakdown, parent,
                        false);

                holder = new ViewHolder();

                holder.slugDateTV = (TextView) convertView.findViewById(R.id.slugDateTV);
                holder.slugTotalTV = (TextView) convertView.findViewById(R.id.slugTotalTV);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.slugDateTV.setText(getItem(position).getDate());
            holder.slugTotalTV.setText(NumberFormat.getNumberInstance(Locale.US).format((int) Double.parseDouble(getItem(position).getTotal())) + " " + measurementUnit);

            return convertView;
        }

        class ViewHolder {
            TextView slugDateTV;
            TextView slugTotalTV;
        }
    }
}