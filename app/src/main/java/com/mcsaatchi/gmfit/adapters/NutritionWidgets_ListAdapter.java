package com.mcsaatchi.gmfit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcsaatchi.gmfit.R;
import com.mcsaatchi.gmfit.models.NutritionWidget;

import java.util.ArrayList;

public class NutritionWidgets_ListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NutritionWidget> widgetsMap;
    private int drawableResId;

    public NutritionWidgets_ListAdapter(Context context, ArrayList<NutritionWidget> widgetsMap, int drawableResId) {
        super();
        this.context = context;
        this.widgetsMap = widgetsMap;
        this.drawableResId = drawableResId;
    }

    public void notifyData() {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return widgetsMap.size();
    }

    @Override
    public Object getItem(int index) {
        return null;
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
            convertView = inflater.inflate(R.layout.list_item_one_with_icon, parent,
                    false);

            holder = new ViewHolder();

            holder.itemNameTV = (TextView) convertView.findViewById(R.id.itemNameTV);
            holder.itemIconRightIMG = (ImageView) convertView.findViewById(R.id.itemIconRightIMG);
            holder.itemLayout = (RelativeLayout) convertView.findViewById(R.id.itemLayout);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.itemIconRightIMG.setImageResource(drawableResId);
        holder.itemNameTV.setText((widgetsMap.get(position)).getTitle());

        if (position > 4) {
            holder.itemLayout.setAlpha(0.2f);
        }

        return convertView;
    }

    class ViewHolder {
        RelativeLayout itemLayout;
        TextView itemNameTV;
        ImageView itemIconRightIMG;
    }
}