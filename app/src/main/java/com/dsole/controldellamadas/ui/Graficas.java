package com.dsole.controldellamadas.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.dsole.controldellamadas.R;
import com.dsole.controldellamadas.classes.Años;
import com.dsole.controldellamadas.classes.BarChartItem;
import com.dsole.controldellamadas.classes.ChartItem;
import com.dsole.controldellamadas.classes.Contacto;
import com.dsole.controldellamadas.classes.LineChartItem;
import com.dsole.controldellamadas.classes.PieChartItem;
import com.dsole.controldellamadas.providers.CallLogHelper;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Graficas extends ActionBarActivity {

    private Spinner mSpinnerAño;

    private int mAñoSeleccionado;
    private int primerDiaCiclo;
    private ArrayList<ChartItem> list;
    private ListView lv;

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MIS_PREFERENCIAS", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.putInt("AÑO_SELECCIONADO_GRAFICAS", mAñoSeleccionado);
        edit.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficas);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lv = (ListView) findViewById(R.id.listView1);

        list = new ArrayList<ChartItem>();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        primerDiaCiclo = Integer.parseInt(sp.getString("PREF_DIA_CICLO", "1"));

        mSpinnerAño = (Spinner) findViewById(R.id.spinnerAño);

        List<String> listAño = Años.setLista();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listAño);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerAño.setAdapter(dataAdapter);
        mSpinnerAño.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        SharedPreferences settings = getSharedPreferences("MIS_PREFERENCIAS", Context.MODE_PRIVATE);
        mAñoSeleccionado= settings.getInt("AÑO_SELECCIONADO_GRAFICAS", Calendar.getInstance().get(Calendar.YEAR));

        int posicion = Años.setPosicionAño(dataAdapter, mAñoSeleccionado);
        mSpinnerAño.setSelection(posicion);

        loadData();
    }

    private void loadData() {
        list.clear();

        list.add(new LineChartItem(generateDataLine(primerDiaCiclo, mAñoSeleccionado), getApplicationContext()));
        list.add(new BarChartItem(generateDataBar(primerDiaCiclo, mAñoSeleccionado), getApplicationContext()));
        list.add(new PieChartItem(generateDataPie(primerDiaCiclo, mAñoSeleccionado), getApplicationContext()));

        ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), list);

        lv.setAdapter(cda);
    }
    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            String pos2 = parent.getItemAtPosition(pos).toString();
            mAñoSeleccionado = Integer.parseInt(pos2);
            loadData();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

        public ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            // return the views type
            return getItem(position).getItemType();
        }

        @Override
        public int getViewTypeCount() {
            return 3; // we have 3 different item-types
        }
    }

    private LineData generateDataLine(int primerDiaCiclo, int año) {

        ArrayList<Entry> e1 = CallLogHelper.getGraficosResumenMinutosRealizadasAnuales(getContentResolver(), primerDiaCiclo, año);

        LineDataSet d1 = new LineDataSet(e1, getString(R.string.minutos_en_llamadas_realizadas));
        d1.setLineWidth(3f);
        d1.setCircleSize(5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));

        ArrayList<Entry> e2 = CallLogHelper.getGraficosResumenMinutosRecibidosAnuales(getContentResolver(), primerDiaCiclo, año);

        LineDataSet d2 = new LineDataSet(e2, getString(R.string.minutos_en_llamadas_recibidas));
        d2.setLineWidth(3f);
        d2.setCircleSize(5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);

        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(d1);
        sets.add(d2);

        LineData cd = new LineData(getMonths(), sets);
        return cd;
    }

    private BarData generateDataBar(int primerDiaCiclo, int año) {

        ArrayList<BarEntry> entries = CallLogHelper.getGraficosResumenLlamadasRealizadasAnuales(getContentResolver(), primerDiaCiclo, año);

        BarDataSet d = new BarDataSet(entries, getString(R.string.numero_de_llamadas_realizadas));
        d.setBarSpacePercent(20f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(getMonths(), d);
        return cd;
    }

    private PieData generateDataPie(int primerDiaCiclo, int año) {

        ArrayList<Contacto> contactos = CallLogHelper.getGraficosResumenContactosRealizadasAnuales(getApplicationContext(), getContentResolver(), primerDiaCiclo, año);

        ArrayList<String> nombresContactos = new ArrayList<String>();
        ArrayList<Entry> llamadas = new ArrayList<Entry>();

        for (int i = 0; i < contactos.size(); i++) {
            String nombre = contactos.get(i).getNombre();
            String numero = contactos.get(i).getNumero();

            if (numero.equals(getString(R.string.ningun_contacto))) nombresContactos.add(numero);
            else nombresContactos.add(nombre);

            llamadas.add(new Entry(contactos.get(i).getTotalLlamadas(), i));
        }

        PieDataSet d = new PieDataSet(llamadas, "");

        // space between slices
        d.setSliceSpace(5f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);

        PieData cd = new PieData(nombresContactos, d);
        return cd;
    }

    private ArrayList<String> getMonths() {
        ArrayList<String> m = new ArrayList<String>();
        m.add(getString(R.string.Enero_abreviado));
        m.add(getString(R.string.Febrero_abreviado));
        m.add(getString(R.string.Marzo_abreviado));
        m.add(getString(R.string.Abril_abreviado));
        m.add(getString(R.string.Mayo_abreviado));
        m.add(getString(R.string.Junio_abreviado));
        m.add(getString(R.string.Julio_abreviado));
        m.add(getString(R.string.Agosto_abreviado));
        m.add(getString(R.string.Septiembre_abreviado));
        m.add(getString(R.string.Octubre_abreviado));
        m.add(getString(R.string.Noviembre_abreviado));
        m.add(getString(R.string.Diciembre_abreviado));

        return m;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_graficas, menu);
        return true;
    }

    /*
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }
    */


}