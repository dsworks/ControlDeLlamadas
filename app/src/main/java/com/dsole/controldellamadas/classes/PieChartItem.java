package com.dsole.controldellamadas.classes;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;

import com.dsole.controldellamadas.R;
/**
 * Created by dsole on 17/02/2015.
 */
public class PieChartItem  extends ChartItem {

    private Typeface mTf;

    public PieChartItem(ChartData cd, Context c) {
        super(cd);

        mTf = Typeface.createFromAsset(c.getAssets(), "OpenSans-Regular.ttf");
    }

    @Override
    public int getItemType() {
        return TYPE_PIECHART;
    }

    @Override
    public View getView(int position, View convertView, Context c) {

        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = LayoutInflater.from(c).inflate(
                    R.layout.list_item_piechart, null);
            holder.chart = (PieChart) convertView.findViewById(R.id.chart);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // apply styling
        holder.chart.setValueTypeface(mTf);
        holder.chart.setDescription("Top 5 Contactos a los que más llevas llamando (anualmente)");
        holder.chart.setHoleRadius(60f);
        holder.chart.setTransparentCircleRadius(65f);
        holder.chart.setCenterText("Contactos");
        holder.chart.setCenterTextTypeface(mTf);
        holder.chart.setCenterTextSize(18f);
        holder.chart.setDrawXValues(false);
        holder.chart.setUsePercentValues(true);

        // set data
        holder.chart.setData((PieData) mChartData);

        Legend l = holder.chart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);

        // do not forget to refresh the chart
        // holder.chart.invalidate();
        holder.chart.animateXY(900, 900);

        return convertView;
    }

    private static class ViewHolder {
        PieChart chart;
    }
}