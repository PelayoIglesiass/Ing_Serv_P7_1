package es.uniovi.converter

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var editTextEuros: EditText
    private lateinit var editTextDollars: EditText
    private lateinit var textViewRate: TextView

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        editTextEuros = findViewById(R.id.editTextEuros)
        editTextDollars = findViewById(R.id.editTextDollars)
        textViewRate = findViewById(R.id.textViewRate)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.euroToDollar.observe(this) { rate ->
            textViewRate.text = "Cambio EUR→USD: $rate"
        }
    }

    private fun convert(source: EditText, destination: EditText, factor: Double) {
        val text = source.text.toString()
        val value = text.toDoubleOrNull()
        if (value == null) {
            destination.setText("")
            return
        }
        destination.setText((value * factor).toString())
    }

    fun onClickToDollars(view: View) {
        val factor = viewModel.euroToDollar.value ?: 1.16
        convert(editTextEuros, editTextDollars, factor)
    }

    fun onClickToEuros(view: View) {
        val factor = viewModel.euroToDollar.value ?: 1.16
        if (factor != 0.0) {
            convert(editTextDollars, editTextEuros, 1 / factor)
        }
    }
}
