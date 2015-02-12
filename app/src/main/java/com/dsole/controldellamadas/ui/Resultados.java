package com.dsole.controldellamadas.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.dsole.controldellamadas.R;
import com.dsole.controldellamadas.adapters.ResultadosAdapter;
import com.dsole.controldellamadas.classes.CallLog;
import com.dsole.controldellamadas.providers.CallLogHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Resultados extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        TextView numLlamadas = (TextView) findViewById(R.id.numLlamadas);

        String numero = "";
        String deFecha = "";
        String aFecha = "";
        int deMinutos = 0;
        int aMinutos = 300;
        boolean realizadas = true;
        boolean recibidas = true;
        boolean perdidas = true;

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            numero = extras.getString("NUMERO",numero);
            deFecha = extras.getString("DE_FECHA", deFecha);
            aFecha = extras.getString("A_FECHA", aFecha);
            deMinutos = extras.getInt("DE_MINUTOS", deMinutos);
            aMinutos =  extras.getInt("A_MINUTOS", aMinutos);
            realizadas = extras.getBoolean("REALIZADAS", realizadas);
            recibidas = extras.getBoolean("RECIBIDAS", recibidas);
            perdidas = extras.getBoolean("PERDIDAS", perdidas);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            Date date = null;
            Date date2 = null;
            try {
                date = sdf.parse(deFecha);
                date2 = sdf.parse(aFecha);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            deFecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
            aFecha = new SimpleDateFormat("yyyy-MM-dd").format(date2);
        }

        ArrayList<CallLog> callLogs = CallLogHelper.getCallLogs(getContentResolver(),deFecha, aFecha,
                numero, realizadas, recibidas, perdidas, deMinutos, aMinutos);

        int cont = callLogs.size();
        if(cont == 1) numLlamadas.setText("1 llamada encontrada");
        else numLlamadas.setText(String.valueOf(cont) + " llamadas encontradas");

        ResultadosAdapter adapter = new ResultadosAdapter(this, callLogs,R.layout.card);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_resultados, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
