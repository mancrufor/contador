package com.example.contador

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    inner class EventoTeclado : OnEditorActionListener {
        override fun onEditorAction(textView: TextView, i: Int, keyEvent: KeyEvent): Boolean {
            if (i == EditorInfo.IME_ACTION_DONE) {
                resetear(null)
            }
            return false
        }
    }

    private var contador = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        contador = 0
        setContadorVw()
    }

    fun incrementar(view: View?) {
        contador++
        setContadorVw()
    }

    fun decrementar(view: View?) {
        val numNegativos = findViewById<View>(R.id.numNeg) as CheckBox
        contador--
        if (!numNegativos.isChecked && contador < 0) {
            contador = 0
        }
        setContadorVw()
    }

    fun resetear(view: View?) {
        val nuevoValor = findViewById<View>(R.id.nuevoValor) as TextView
        val textoValor = nuevoValor.text.toString()

        // Captura el origen de la entrada
        val teclado = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        // Oculta el teclado
        teclado.hideSoftInputFromWindow(nuevoValor.windowToken, 0)

        // Verifica si la cadena tiene extensión 0, es decir, es ""
        // Controla un error que hace que se cierre la aplicación
        contador = if (textoValor.isEmpty()) {
            0
        } else {
            textoValor.toInt()
        }
        setContadorVw()
    }

    fun setContadorVw() {
        val contadorVw = findViewById<View>(R.id.contador) as TextView
        contadorVw.text = contador.toString()
    }

    /*    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("valor", contador);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        contador = savedInstanceState.getInt("valor", 0);
        super.onRestoreInstanceState(savedInstanceState);
        setContadorVw();
    }*/
    override fun onPause() {
        super.onPause()

        // Shared preferences xml es un archivo ya existente
        val datos = PreferenceManager.getDefaultSharedPreferences(this) as SharedPreferences
        val ed = datos.edit()
        ed.putInt("valor", contador)
        ed.apply()
    }

    override fun onResume() {
        super.onResume()
        val datos = PreferenceManager.getDefaultSharedPreferences(this) as SharedPreferences
        contador = datos.getInt("valor", 0)
        setContadorVw()
    }
}