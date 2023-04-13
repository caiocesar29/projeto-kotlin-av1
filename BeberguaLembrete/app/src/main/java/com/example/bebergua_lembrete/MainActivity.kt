package com.example.bebergua_lembrete


import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import android.renderscript.ScriptGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.bebergua_lembrete.model.CalcularInfestoaDiaria
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {



    private lateinit var edit_peso: EditText
    private lateinit var edit_idade: EditText
    private lateinit var bt_calcular: Button
    private lateinit var txt_resulado_ml: TextView
    private lateinit var ic_redefinir_dados: ImageView
    private lateinit var bt_lembrete: Button
    private lateinit var bt_alarme: Button
    private lateinit var txt_hora: TextView
    private lateinit var txt_minutos: TextView

    private lateinit var calcularIngestaoDiaria: CalcularInfestoaDiaria
    private var resultadoML = 0.0

    private lateinit var calcularInfestoaDiaria: CalcularInfestoaDiaria
    lateinit var calendario: Calendar
    var horaAtual = 0
    var minutosAtuais = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar!!.hide()
        IniciarComponentes()
        calcularIngestaoDiaria = CalcularInfestoaDiaria()

        bt_calcular.setOnClickListener {
            if (edit_peso.text.toString().isEmpty()) {
                Toast.makeText(this, R.string.toast_informe_peso, Toast.LENGTH_LONG).show()
            } else if (edit_idade.text.toString().isEmpty()) {
                Toast.makeText(this, R.string.toas_informe_idade, Toast.LENGTH_LONG).show()
            } else {
                val peso = edit_peso.text.toString().toDouble()
                val idade = edit_idade.text.toString().toInt()
                calcularIngestaoDiaria.CalcularTotalMl(peso, idade)
                resultadoML = calcularIngestaoDiaria.ResultadpMl()
                var formatar = NumberFormat.getNumberInstance(Locale("pt", "BR"))
                formatar.isGroupingUsed = false
                txt_resulado_ml.text = formatar.format(resultadoML) + "" + "ml"
            }
        }
        ic_redefinir_dados.setOnClickListener {
            val alertaDialog = AlertDialog.Builder(this)
            alertaDialog.setTitle(R.string.dialog_titulo)
                .setMessage(R.string.dialog_desc)
                .setPositiveButton("ok", { dialogoInterface, i ->
                    edit_peso.setText("")
                    edit_idade.setText("")
                    txt_resulado_ml.text = ""
                })
            alertaDialog.setNegativeButton("Cancelar", { dialogoInterface, i ->
            })
            val dialog = alertaDialog.create()
            dialog.show()
        }
        bt_lembrete.setOnClickListener {
            calendario = Calendar.getInstance()
            horaAtual = calendario.get(Calendar.HOUR_OF_DAY)
            minutosAtuais = calendario.get(Calendar.MINUTE)
            timePickerDialog=
                TimePickerDialog(this, { timePicker: TimePicker, hourOfDay: Int, minutis: Int ->
                    txt_hora.text = String.format("%02d", hourOfDay)
                    txt_minutos.text = String.format("%02d", minutis)
                }, horaAtual, minutosAtuais, true)
            timePickerDialog.show()
        }
        bt_alarme.setOnClickListener {
            if (!txt_hora.text.toString().isEmpty() && !txt_minutos.text.toString().isEmpty()) {
                val intent = Intent(AlarmClock.ACTION_SET_ALARM)
                intent.putExtra(AlarmClock.EXTRA_HOUR, txt_hora.toString().toInt())
                intent.putExtra(AlarmClock.EXTRA_MESSAGE, getString(R.string.alarme_mensagem))
                startActivity(intent)

                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            }
        }
        private fun IniciarComponentes() {
            edit_idade = findViewById(R.id.edit_idade)
            edit_idade = findViewById(R.id.edit_idade)
            bt_calcular = findViewById(R.id.bt_calcular)
            txt_resulado_ml = findViewById(R.id.txt_resultado_ml)
            ic_redefinir_dados = findViewById(R.id.ic_redefinir)
            bt_lembrete = findViewById(R.id.bt_definir_lembrete)
            bt_alarme = findViewById(R.id.bt_alarme)
            txt_hora = findViewById(R.id.txt_hora)
            txt_minutos = findViewById(R.id.txt_minutos)
        }
    }
}