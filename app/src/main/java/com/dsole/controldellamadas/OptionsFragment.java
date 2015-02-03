package com.dsole.controldellamadas;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.EditText;

/**
 * Created by dsole on 08/01/2015.
 */
public class OptionsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        MySQLiteHelper db = new MySQLiteHelper(getActivity());

        int res = db.getPreferenciasModificadas();
        if (res == 2) inicializarSummarys();
    }

    private void inicializarSummarys() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String valor = "";
        String key = "";

        key = "PREF_DIA_CICLO";
        Preference pref = findPreference(key);
        valor = sp.getString(key, "");
        if(valor!="") pref.setSummary(getSummary(key, valor));
        valor = "";

        key = "PREF_LIMITE_MINUTOS";
        Preference pref2 = findPreference(key);
        valor = sp.getString(key, "");
        if(valor!="") pref2.setSummary(getSummary(key, valor));
        valor = "";

        key = "PREF_AVISO_LIMITE_MINUTOS";
        Preference pref3 = findPreference(key);
        valor = sp.getString(key, "");
        if(valor!="") pref3.setSummary(getSummary(key, valor));
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //String summaryStr = sharedPreferences.g
        Preference pref = findPreference(key);
        //String summary = (String) pref.getSummary();

        String valor = sharedPreferences.getString(key, "");

        String summary = getSummary(key, valor);

        pref.setSummary(summary);
    }

    private String getSummary(String key, String valor) {
        String summary = "";

        switch (key) {
            case "PREF_DIA_CICLO":
                summary = "El día " + valor + " es el primer día del ciclo";
                break;
            case "PREF_LIMITE_MINUTOS":
                summary = "El límite de minutos es de " + valor + " minutos";
                break;
            case "PREF_AVISO_LIMITE_MINUTOS":
                summary = "Notificar a partir de los " + valor + " minutos";
                break;
            default:
        }

        return summary;
    }
}
