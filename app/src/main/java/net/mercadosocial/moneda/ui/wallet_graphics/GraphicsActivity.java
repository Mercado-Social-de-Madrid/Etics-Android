package net.mercadosocial.moneda.ui.wallet_graphics;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class GraphicsActivity extends BaseActivity implements GraphicsView {

    private GraphicsPresenter presenter;
    private LineChart chart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = GraphicsPresenter.newInstance(this, this);
        setBasePresenter(presenter);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphics);

        configureSecondLevelActivity();
        setToolbarTitle(R.string.graphics);

        chart = findViewById(R.id.chart);

        presenter.onCreate();
    }

    @Override
    public void showTransactions(List<Transaction> transactions) {

        List<Entry> entries = new ArrayList<Entry>();
        final List<String> axisValues = new ArrayList<>();

        for (int i = 0; i < transactions.size(); i++) {
            Transaction transaction = transactions.get(i);
            // turn your data into Entry objects
            entries.add(new Entry(i, transaction.getCurrent_balance()));
            axisValues.add(transaction.getDateOnly());

        }

        LineDataSet dataSet = new LineDataSet(entries, getString(R.string.graphics_x_label));
        dataSet.setColor(Color.rgb(0, 155, 0));

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // refresh

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setLabelRotationAngle(-45);
        xAxis.setTextColor(ContextCompat.getColor(this, R.color.purple));
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return axisValues.get((int) value); // xVal is a string array
            }

        });

// set a custom value formatter
    }
}
