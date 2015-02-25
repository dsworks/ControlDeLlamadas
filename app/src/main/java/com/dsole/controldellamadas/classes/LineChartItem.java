package com.dsole.controldellamadas.classes;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;

import com.dsole.controldellamadas.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;

/**
 * Created by dsole on 17/02/2015.
 */
public class LineChartItem extends ChartItem {

    private Typeface mTf;

    public LineChartItem(ChartData cd, Context c) {
        super(cd);

        mTf = Typeface.createFromAsset(c.getAssets(), "OpenSans-Regular.ttf");
    }

    @Override
    public int getItemType() {
        return TYPE_LINECHART;
    }

    @Override
    public View getView(int position, View convertView, Context c) {

        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = LayoutInflater.from(c).inflate(
                    R.layout.list_item_linechart, null);
            holder.chart = (LineChart) convertView.findViewById(R.id.chart);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        // apply styling
        // holder.chart.setValueTypeface(mTf);
        holder.chart.setDrawYValues(false);
        holder.chart.setDescription(c.getString(R.string.consumo_en_minutos));
        holder.chart.setDrawVerticalGrid(false);
        holder.chart.setDrawGridBackground(false);

        XLabels xl = holder.chart.getXLabels();
        xl.setCenterXLabelText(true);
        xl.setPosition(XLabels.XLabelPosition.BOTTOM);
        xl.setTypeface(mTf);

        YLabels yl = holder.chart.getYLabels();
        yl.setTypeface(mTf);
        yl.setLabelCount(5);
        yl.setFormatter(new MyValueFormatter());

        MyMarkerView mv = new MyMarkerView(c, R.layout.custom_marker_view);

        // set the marker to the chart
        holder.chart.setMarkerView(mv);

        // set data
        holder.chart.setData((LineData) mChartData);

        // do not forget to refresh the chart
        // holder.chart.invalidate();
        holder.chart.animateX(1000);

        holder.chart.setUnit(c.getString(R.string.m_espacio));
        holder.chart.setDrawUnitsInChart(true);

        return convertView;
    }

    private static class ViewHolder {
        LineChart chart;
    }

}
