package com.dsole.controldellamadas.ui;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.dsole.controldellamadas.R;
import com.dsole.controldellamadas.classes.Ciclo;
import com.dsole.controldellamadas.providers.CallLogHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Busquedas extends ActionBarActivity implements View.OnClickListener {
    private TextView mDeFecha;
    private TextView mAFecha;
    private Button mBuscarContacto;
    private Button mTodos;
    private ImageButton mBuscar;
    private EditText mNumero;
    private CheckBox mRealizadas;
    private CheckBox mRecibidas;
    private CheckBox mPerdidas;
    private NumberPicker mDeMinutos;
    private NumberPicker mAMinutos;

    private DatePickerDialog deFechaDatePickerDialog;
    private DatePickerDialog aFechaPickerDialog;

    private SimpleDateFormat formatoFecha;

    private int mDeMinutosValor;
    private int mAMinutosValor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busquedas);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNumero = (EditText) findViewById(R.id.numero);
        //mDummy = (EditText) findViewById(R.id.dummy);
        mBuscarContacto = (Button) findViewById(R.id.buscarContacto);
        mBuscar = (ImageButton) findViewById(R.id.search_button);
        mTodos = (Button) findViewById(R.id.todos);

        mRealizadas = (CheckBox) findViewById(R.id.chkRealizadas);
        mRecibidas = (CheckBox) findViewById(R.id.chkRecibidas);
        mPerdidas = (CheckBox) findViewById(R.id.chkPerdidas);

        mBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar();
            }
        });

        mBuscarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(i, 0);
            }
        });

        mTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNumero.setText("");
            }
        });

        mDeFecha = (TextView) findViewById(R.id.deFecha);
        //mDeFecha.requestFocus();
        //mDeFecha.setInputType(InputType.TYPE_NULL);

        mAFecha = (TextView) findViewById(R.id.aFecha);
        //mAFecha.setInputType(InputType.TYPE_NULL);

        mDeFecha.setOnClickListener(this);
        mAFecha.setOnClickListener(this);

        mDeMinutos = (NumberPicker) findViewById(R.id.npDeMinutos);
        mDeMinutos.setOnValueChangedListener( new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Toast.makeText(getApplicationContext(),"oldVal: " + String.valueOf(oldVal) + " newVal: " + String.valueOf(newVal),Toast.LENGTH_LONG).show();
            }
        });

        mAMinutos = (NumberPicker) findViewById(R.id.npAMinutos);
        mAMinutos.setOnValueChangedListener( new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Toast.makeText(getApplicationContext(),"oldVal: " + String.valueOf(oldVal) + " newVal: " + String.valueOf(newVal),Toast.LENGTH_LONG).show();
            }
        });

        inicializarFiltros();

        //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(mNumero.getWindowToken(), 0);

    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MIS_PREFERENCIAS", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.putInt("DE_MINUTOS", mDeMinutosValor);
        edit.putInt("A_MINUTOS", mAMinutosValor);
        edit.putString("NUMERO", mNumero.getText().toString());
        edit.putString("DE_FECHA", mDeFecha.getText().toString());
        edit.putString("A_FECHA", mAFecha.getText().toString());
        edit.putBoolean("REALIZADAS", mRealizadas.isChecked());
        edit.putBoolean("RECIBIDAS", mRecibidas.isChecked());
        edit.putBoolean("PERDIDAS", mPerdidas.isChecked());
        edit.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            ContentResolver cr = getContentResolver();
            String number = CallLogHelper.loadContact(cr, contactUri);
            mNumero.setText(number);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_busquedas, menu);
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

    @Override
    public void onClick(View v) {
        if(v == mDeFecha) {
            deFechaDatePickerDialog.show();
        } else if(v == mAFecha) {
            aFechaPickerDialog.show();
        }
    }

    private void inicializarFiltros(){
        SharedPreferences settings = getSharedPreferences("MIS_PREFERENCIAS", Context.MODE_PRIVATE);
        mDeMinutosValor= settings.getInt("DE_MINUTOS", 0);
        mAMinutosValor= settings.getInt("A_MINUTOS", 300);
        mNumero.setText(settings.getString("NUMERO", ""));

        mRealizadas.setChecked(settings.getBoolean("REALIZADAS",true));
        mRecibidas.setChecked(settings.getBoolean("RECIBIDAS",true));
        mPerdidas.setChecked(settings.getBoolean("PERDIDAS", true));

        formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        int primerDiaCiclo = Integer.parseInt(sp.getString("PREF_DIA_CICLO", "1"));

        Calendar newCalendar = Calendar.getInstance();

        String fechaInicio = settings.getString("DE_FECHA", "");
        String fechaFinal = settings.getString("A_FECHA", "");

        SimpleDateFormat sdf;

        if(fechaInicio.equals("") && fechaFinal.equals("")) {
            fechaInicio = Ciclo.fechaPrimerDiaCiclo(primerDiaCiclo, newCalendar.get(Calendar.MONTH) + 1, newCalendar.get(Calendar.YEAR));
            fechaFinal = Ciclo.fechaUltimoDiaCiclo(primerDiaCiclo, newCalendar.get(Calendar.MONTH) + 1, newCalendar.get(Calendar.YEAR));

            sdf = new SimpleDateFormat("yyyy-MM-dd");
        } else {
            sdf = new SimpleDateFormat("dd/MM/yyyy");
        }

        Date date = null;
        Date date2 = null;
        try {
            date = sdf.parse(fechaInicio);
            date2 = sdf.parse(fechaFinal);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        mDeFecha.setText(formatoFecha.format(date));
        mAFecha.setText(formatoFecha.format(date2));

        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);

        deFechaDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mDeFecha.setText(formatoFecha.format(newDate.getTime()));
            }

        },c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DAY_OF_MONTH));

        aFechaPickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mAFecha.setText(formatoFecha.format(newDate.getTime()));
            }

        },c2.get(Calendar.YEAR), c2.get(Calendar.MONTH), c2.get(Calendar.DAY_OF_MONTH));

        mDeMinutos.setMinValue(0);
        mDeMinutos.setMaxValue(300);
        mDeMinutos.setValue(mDeMinutosValor);
        mDeMinutos.setWrapSelectorWheel(true);

        mAMinutos.setMinValue(0);
        mAMinutos.setMaxValue(300);
        mAMinutos.setValue(mAMinutosValor);
        mDeMinutos.setWrapSelectorWheel(true);
    }


    private void buscar() {
        Intent i = new Intent(Busquedas.this, Resultados.class);
        i.putExtra("DE_FECHA", mDeFecha.getText().toString());
        i.putExtra("A_FECHA", mAFecha.getText().toString());
        i.putExtra("NUMERO", mNumero.getText().toString());
        i.putExtra("DE_MINUTOS", mDeMinutos.getValue());
        i.putExtra("A_MINUTOS", mAMinutos.getValue());
        i.putExtra("REALIZADAS", mRealizadas.isChecked());
        i.putExtra("RECIBIDAS", mRecibidas.isChecked());
        i.putExtra("PERDIDAS", mPerdidas.isChecked());

        //AsyncTask task = new ProgressTask(this, "Buscando contactos...").execute();

        startActivity(i);

        //intent a otra activity. Pasar parámetros
        //ArrayList<CallLog> callLogs = CallLogHelper.getCallLogs(getContentResolver(), fechaInicio, fechaFinal);
    }

    public class ProgressTask extends AsyncTask<String, Void, Boolean> {
        private Context context;
        private String mensaje;
        private ProgressDialog dialog;

        public ProgressTask (Context context, String mensaje) {
            this.context = context;
            this.mensaje = mensaje;
            this.dialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage(mensaje);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            dialog.dismiss();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return null;
        }
    }

}
