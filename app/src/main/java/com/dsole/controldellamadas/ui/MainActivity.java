package com.dsole.controldellamadas.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.media.SoundPool;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.dsole.controldellamadas.classes.Años;
import com.dsole.controldellamadas.classes.CallLog;
import com.dsole.controldellamadas.providers.CallLogHelper;
import com.dsole.controldellamadas.classes.Ciclo;
import com.dsole.controldellamadas.classes.DFragment;
import com.dsole.controldellamadas.classes.Meses;
import com.dsole.controldellamadas.db.MySQLiteHelper;
import com.dsole.controldellamadas.classes.MySwipeRefreshLayout;
import com.dsole.controldellamadas.R;
import com.dsole.controldellamadas.classes.Contacto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends ActionBarActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    //region Declaraciones
    private ListView mListView;
    private ArrayList<CallLog> callLogs;
    private ProgressBar mProgressBar;
    private TextView mMinutosConsumidos;
    private TextView mCicloActual;
    private TextView mLlamadasEntrantes;
    private TextView mLlamadasSalientes;
    private TextView mLlamadasPerdidas;
    private TextView mTiempoLlamadasRealizadas;
    private TextView mTiempoLlamadasRecibidas;
    private TextView mTopNombreRecibidas;
    private TextView mTopNumeroRecibidas;
    private TextView mTopTotalLlamadasRecibidas;
    private TextView mTopTotalMinutosRecibidas;
    private TextView mTopNombreRealizadas;
    private TextView mTopNumeroRealizadas;
    private TextView mTopNombreMasMinutos;
    private TextView mTopNumeroMasMinutos;
    private TextView mTopTotalLlamadasRealizadas;
    private TextView mTopTotalMinutosRealizadas;
    private TextView mTopTotalMinutosMasMinutos;
    private TextView mTopNombreMasRato;
    private TextView mTopNumeroMasRato;
    private TextView mTopTotalMinutosMasRato;
    private TextView mTopTotalLlamadasMasRato;
    private Spinner mSpinner;
    private Spinner mSpinnerAño;

    private ImageView mImagenContactoSaliente;
    private ImageView mImagenContactoEntrante;
    private ImageView mImagenContactoMasMinutos;
    private ImageView mImagenContactoMasRato;

    private ImageButton mBusquedas;

    private MySwipeRefreshLayout swipeRefresh;
    private SoundPool soundPool;
    private int soundID;
    private boolean loaded = false;

    private int mMesSeleccionado;
    private int mAñoSeleccionado;

    private SharedPreferences sp;

    private android.support.v7.widget.Toolbar toolbar;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(this);

        mBusquedas = (ImageButton) findViewById(R.id.search_button);

        mLlamadasEntrantes = (TextView) findViewById(R.id.tvLlamadasEntrantes);
        mLlamadasSalientes = (TextView) findViewById(R.id.tvLlamadasSalientes);
        mLlamadasPerdidas = (TextView) findViewById(R.id.tvLlamadasPerdidas);

        mTiempoLlamadasRealizadas = (TextView) findViewById(R.id.tvTiempoLlamadasRealizadas);
        mTiempoLlamadasRecibidas = (TextView) findViewById(R.id.tvTiempoLlamadasRecibidas);

        mTopNombreRecibidas = (TextView) findViewById(R.id.nombreTopEntrante);
        mTopNumeroRecibidas = (TextView) findViewById(R.id.numTopEntrante);
        mTopTotalLlamadasRecibidas = (TextView) findViewById(R.id.totalLlamadasTopEntrante);
        mTopTotalMinutosRecibidas = (TextView) findViewById(R.id.totalMinutosTopEntrante);
        mTopNombreRealizadas = (TextView) findViewById(R.id.nombreTopSaliente);
        mTopNumeroRealizadas = (TextView) findViewById(R.id.numTopSaliente);
        mTopTotalLlamadasRealizadas = (TextView) findViewById(R.id.totalLlamadasTopSaliente);
        mTopTotalMinutosRealizadas = (TextView) findViewById(R.id.totalMinutosTopSaliente);

        mTopNombreMasMinutos = (TextView) findViewById(R.id.nombreTopMasMinutos);
        mTopNumeroMasMinutos = (TextView) findViewById(R.id.numTopMasMinutos);
        mTopTotalMinutosMasMinutos = (TextView) findViewById(R.id.totalMinutosTopMasMinutos);

        mTopNombreMasRato = (TextView) findViewById(R.id.nombreTopMasRato);
        mTopNumeroMasRato = (TextView) findViewById(R.id.numTopMasRato);
        mTopTotalMinutosMasRato = (TextView) findViewById(R.id.totalMinutosTopMasRato);
        mTopTotalLlamadasMasRato = (TextView) findViewById(R.id.totalLlamadasTopMasRato);

        mImagenContactoSaliente = (ImageView) findViewById(R.id.imagenContactoSaliente);
        mImagenContactoEntrante = (ImageView) findViewById(R.id.imagenContactoEntrante);
        mImagenContactoMasMinutos = (ImageView) findViewById(R.id.imagenContactoMasMinutos);
        mImagenContactoMasRato = (ImageView) findViewById(R.id.imagenContactoMasRato);

        //mListView = (ListView) findViewById(R.id.listView);
        mMinutosConsumidos = (TextView) findViewById(R.id.tvMinutosConsumidos);
        mCicloActual = (TextView) findViewById(R.id.tvCicloActual);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mSpinner = (Spinner) findViewById(R.id.spinner);
        mSpinnerAño = (Spinner) findViewById(R.id.spinnerAño);

        mBusquedas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Busquedas.class));

            }
        });

        List<String> list = Meses.setLista();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner.setAdapter(dataAdapter);
        addListenerOnSpinnerItemSelection();

        List<String> listAño = Años.setLista();
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listAño);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerAño.setAdapter(dataAdapter2);
        addListenerOnSpinnerItemSelection();

        SharedPreferences settings = getSharedPreferences("MIS_PREFERENCIAS", Context.MODE_PRIVATE);
        mMesSeleccionado= settings.getInt("MES_SELECCIONADO", Calendar.getInstance().get(Calendar.MONTH) + 1);
        mAñoSeleccionado= settings.getInt("AÑO_SELECCIONADO", Calendar.getInstance().get(Calendar.YEAR));

        //mAñoSeleccionado = 2015;
        int posicion = Meses.setPosicionMes(dataAdapter, mMesSeleccionado);
        mSpinner.setSelection(posicion);

        int posicion2 = Años.setPosicionAño(dataAdapter, mAñoSeleccionado);
        mSpinnerAño.setSelection(posicion2);

        avisoPreferencias();
        //inicializarSwipeRefresh();

    }

    private void avisoPreferencias() {
        MySQLiteHelper db = new MySQLiteHelper(this);

        int res = db.getPreferenciasModificadas();
        if (res == 0 || res == 1) {
            DFragment dialogo = new DFragment();
            dialogo.show(getSupportFragmentManager(), "");

            //if (res == 0) db.insertaAviso();
            db.insertaAviso();
            db.actualizarAvisoPreferencias(1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //sp.registerOnSharedPreferenceChangeListener(this);

        loadData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //sp.unregisterOnSharedPreferenceChangeListener(this);
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MIS_PREFERENCIAS", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.putInt("MES_SELECCIONADO", mMesSeleccionado);
        edit.putInt("AÑO_SELECCIONADO", mAñoSeleccionado);
        edit.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //guardar lo necesario para cuando giremos la pantalla o android decida finalizar nuestra aplicacion, poder recuperarlo posteriormente
        //outState.putInt("MES_SELECCIONADO", mMesSeleccionado);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //mMesSeleccionado = savedInstanceState.getInt("MES_SELECCIONADO");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent i = new Intent(MainActivity.this, PreferenceActivity.class);
            startActivity(i);
            return true;
        } else if (id == R.id.graficos) {
            Intent i = new Intent(MainActivity.this, Graficas.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        /*
        //Para que el aviso del principio no vuelva a salir
        MySQLiteHelper db = new MySQLiteHelper(this);
        if (db.getPreferenciasModificadas() != 2) db.actualizarAvisoPreferencias(1);
        */
        //TODO recoger las preferencias y actuar en consecuencia

    }

    public void loadData() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int limiteMinutos = Integer.parseInt(sp.getString("PREF_LIMITE_MINUTOS", "100"));
        int limiteAviso = Integer.parseInt(sp.getString("PREF_AVISO_LIMITE_MINUTOS", "90"));
        int primerDiaCiclo = Integer.parseInt(sp.getString("PREF_DIA_CICLO", "1"));

        String fechaInicio = Ciclo.fechaPrimerDiaCiclo(primerDiaCiclo, mMesSeleccionado, mAñoSeleccionado);
        String fechaFinal = Ciclo.fechaUltimoDiaCiclo(primerDiaCiclo, mMesSeleccionado, mAñoSeleccionado);

        int segundosConsumidos = CallLogHelper.getSegundosConsumidos(getContentResolver(), fechaInicio, fechaFinal);

        String minutos = Ciclo.formatSegundos(segundosConsumidos);

        mMinutosConsumidos.setText(minutos + " de " + String.valueOf(limiteMinutos) + " m");

        mCicloActual.setText(" " + Ciclo.formatFecha(fechaInicio, fechaFinal));
/*
        int color;
        if (segundosConsumidos < limiteAviso * 60) {
            color = Color.GREEN;
        } else if (segundosConsumidos >= limiteAviso * 60 && segundosConsumidos < limiteMinutos * 60) {
            color = Color.YELLOW;
        } else {
            color = Color.RED;
        }
        //mProgressBar.setBackgroundColor(color);
        //mProgressBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
*/
        mProgressBar.setMax(limiteMinutos * 60);

        mProgressBar.setProgress(segundosConsumidos);


        int[] llamadas = CallLogHelper.getTotalTipoLlamadas(getContentResolver(), fechaInicio, fechaFinal);

        mLlamadasEntrantes.setText(String.valueOf(llamadas[0]));
        mLlamadasSalientes.setText(String.valueOf(llamadas[1]));
        mLlamadasPerdidas.setText(String.valueOf(llamadas[2]));

        String[] tiempo = CallLogHelper.getTotalMinutos(getContentResolver(), fechaInicio, fechaFinal);

        mTiempoLlamadasRecibidas.setText(String.valueOf(tiempo[0]));
        mTiempoLlamadasRealizadas.setText(String.valueOf(tiempo[1]));

        Contacto topContactoSaliente = CallLogHelper.getTopContacto(getContentResolver(), fechaInicio, fechaFinal, "SALIENTE");
        Contacto topContactoEntrante = CallLogHelper.getTopContacto(getContentResolver(), fechaInicio, fechaFinal, "ENTRANTE");

        //INICIO LLAMADAS RECIBIDAS
        mTopNombreRecibidas.setText(topContactoEntrante.getNombre());
        mTopNumeroRecibidas.setText(topContactoEntrante.getNumero());

        String aux = "";
        if (topContactoEntrante.getTotalLlamadas() == 1)
            aux = String.valueOf(topContactoEntrante.getTotalLlamadas()) + " llamada";
        else aux = String.valueOf(topContactoEntrante.getTotalLlamadas()) + " llamadas";

        mTopTotalLlamadasRecibidas.setText(aux);
        mTopTotalMinutosRecibidas.setText(topContactoEntrante.getTotalMinutos());

        if (topContactoEntrante.getImagen() != null)
            mImagenContactoEntrante.setImageBitmap(getRoundedShape(topContactoEntrante.getImagen()));
        else mImagenContactoEntrante.setImageBitmap(null);


        //INICIO LLAMADAS REALIZADAs
        aux = "";
        if (topContactoSaliente.getTotalLlamadas() == 1)
            aux = String.valueOf(topContactoSaliente.getTotalLlamadas()) + " llamada";
        else aux = String.valueOf(topContactoSaliente.getTotalLlamadas()) + " llamadas";

        mTopTotalLlamadasRealizadas.setText(aux);
        mTopTotalMinutosRealizadas.setText(topContactoSaliente.getTotalMinutos());

        mTopNombreRealizadas.setText(topContactoSaliente.getNombre());
        mTopNumeroRealizadas.setText(topContactoSaliente.getNumero());

        if (topContactoSaliente.getImagen() != null)
            mImagenContactoSaliente.setImageBitmap(getRoundedShape(topContactoSaliente.getImagen()));
        else mImagenContactoSaliente.setImageBitmap(null);


        //Contacto con la llamada más larga
        Contacto topContactoMasMinutos = CallLogHelper.getTopContactoMasMinutos(getContentResolver(), fechaInicio, fechaFinal);

        mTopNombreMasMinutos.setText(topContactoMasMinutos.getNombre());
        mTopNumeroMasMinutos.setText(topContactoMasMinutos.getNumero());

        mTopTotalMinutosMasMinutos.setText(topContactoMasMinutos.getTotalMinutos());

        if (topContactoMasMinutos.getImagen() != null)
            mImagenContactoMasMinutos.setImageBitmap(getRoundedShape(topContactoMasMinutos.getImagen()));
        else mImagenContactoMasMinutos.setImageBitmap(null);

        //contacto con el que más hablas
        Contacto topContactoMasRato = CallLogHelper.getTopContactoMasRato(getContentResolver(), fechaInicio, fechaFinal);

        aux = "";
        if (topContactoMasRato.getTotalLlamadas() == 1)
            aux = String.valueOf(topContactoMasRato.getTotalLlamadas()) + " llamada en total";
        else aux = String.valueOf(topContactoMasRato.getTotalLlamadas()) + " llamadas en total";

        mTopTotalLlamadasMasRato.setText(aux);

        mTopNombreMasRato.setText(topContactoMasRato.getNombre());
        mTopNumeroMasRato.setText(topContactoMasRato.getNumero());

        mTopTotalMinutosMasRato.setText(topContactoMasRato.getTotalMinutos() + "  en total");

        if (topContactoMasRato.getImagen() != null)
            mImagenContactoMasRato.setImageBitmap(getRoundedShape(topContactoMasRato.getImagen()));
        else mImagenContactoMasRato.setImageBitmap(null);
/*
        callLogs = CallLogHelper.getAllCallLogs(getContentResolver(), fechaInicio, fechaFinal);
        CallLogAdapter adapter = new CallLogAdapter(getApplicationContext(), R.layout.listview, callLogs);
        mListView.setAdapter(adapter);
        */
    }

    private Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        //int targetWidth = (int) scaleBitmapImage.getWidth();
        //int targetHeight = (int) scaleBitmapImage.getHeight();

        int targetWidth = 190;
        int targetHeight = 189;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        canvas.drawBitmap(scaleBitmapImage,
                new Rect(0, 0, scaleBitmapImage.getWidth(),
                        scaleBitmapImage.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }

    public void addListenerOnSpinnerItemSelection() {
        mSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        mSpinnerAño.setOnItemSelectedListener(new CustomOnItemSelectedListener2());
    }

    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            mMesSeleccionado = Meses.setPosicionMes(parent.getItemAtPosition(pos).toString());
            loadData();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    public class CustomOnItemSelectedListener2 implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            String pos2 = parent.getItemAtPosition(pos).toString();
            mAñoSeleccionado = Integer.parseInt(pos2);
            //mAñoSeleccionado = Años.setPosicionAño(parent.getItemAtPosition(pos).toString());
            loadData();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    /*
        private void inicializarSwipeRefresh() {
            swipeRefresh = (MySwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //Refresca el contenido de la pantalla usando el SwipeRefreshLayout
                    // Reproduccion de sonido
                    if (loaded) {
                        soundPool.play(soundID, 0.3f, 0.3f, 1, 0, 1f);
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadData();
                            // Finalizar swipeRefresh
                            swipeRefresh.setRefreshing(false);
                        }
                    }, 1000);
                }
            });
            swipeRefresh.setColorScheme(android.R.color.holo_blue_dark,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_green_dark,
                    android.R.color.holo_red_light);

            //swipeRefresh.setOnChildScrollUpListener(new MySwipeRefreshLayout.OnChildScrollUpListener() {
            //    @Override
            //    public boolean canChildScrollUp() {
            //        return mListView.getFirstVisiblePosition() > 0 ||
            //                mListView.getChildAt(0) == null ||
            //                mListView.getChildAt(0).getTop() < 0;
            //    }
            //});

            // Extra: sonido al actualizar con SwipeRefreshLayout
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
            soundID = soundPool.load(this, R.raw.ping, 1);

            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    loaded = true;
                }
            });
        }
    */
}
