package com.example.barbershop.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.barbershop.R
import com.example.barbershop.databinding.ActivityAgendamentoBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Calendar

class Agendamento : AppCompatActivity() {

    private lateinit var binding: ActivityAgendamentoBinding
    private val calendar: Calendar = Calendar.getInstance()
    private var data: String = ""
    private var hora: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgendamentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        val nome = intent.extras?.getString("nome").toString()

        val datePicker = binding.dataPicker
        datePicker.setOnDateChangedListener{_, year, monthOfYear, dayofMonth -> {
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayofMonth)

            var dia = dayofMonth.toString()
            val mes: String

            if (dayofMonth < 10){
                dia = "0$dayofMonth"
            }
            if (monthOfYear < 10){
                mes = "" + (monthOfYear+1)
            }else{
                mes = (monthOfYear +1).toString()
            }

            data = "$dia / $mes / $year"
        }
        binding.timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            val minuto: String
            if (minute < 10){
                minuto = "0$minute"
            }else {
                minuto = minute.toString()
            }

            hora = "$hourOfDay:$minuto"
        }
            binding.timePicker.setIs24HourView(true)

            binding.btnAgendar.setOnClickListener {
                val barbeiro1 = binding.barbeiro1
                val barbeiro2 = binding.barbeiro2
                val barbeiro3 = binding.barbeiro3
                val barbeiro4 = binding.barbeiro4

                when{
                    hora.isEmpty() -> {
                        mensagem(it, "Preencha o Horário!", "#FF0000")
                    }

                    hora < "8:00" && hora > "17:00" -> {
                        mensagem(it, "Barbeiro Shop está fechado - horário de atendimento das 08:00 as 19:00!", "#FF0000")
                    }
                    data.isEmpty() -> {
                        mensagem(it, "Coloque uma data!", "#FF0000")
                    }
                    barbeiro1.isChecked && data.isNotEmpty() && hora.isNotEmpty() -> {
                        salvarAgendamento(it, nome, "Pedro Silva", data, hora)
                    }
                    barbeiro2.isChecked && data.isNotEmpty() && hora.isNotEmpty() -> {
                        salvarAgendamento(it, nome, "Jose Carlos", data, hora)                    }
                    barbeiro3.isChecked && data.isNotEmpty() && hora.isNotEmpty() -> {
                        salvarAgendamento(it, nome, "João Pedro", data, hora)                    }
                    barbeiro4.isChecked && data.isNotEmpty() && hora.isNotEmpty() -> {
                        salvarAgendamento(it, nome, "Lucas Silva", data, hora)                    } else -> {
                        mensagem(it, "Escolha um barbeiro!", "#FF0000")
                    }
                }
            }
        }
    }
    private fun mensagem(view: View, mensagem: String, cor: String){
        val snackbar = Snackbar.make(view, mensagem, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(Color.parseColor(cor))
        snackbar.setTextColor(Color.parseColor("#FFFFFF"))
        snackbar.show()
    }

    private fun salvarAgendamento(view: View, cliente: String, barbeiro: String, data: String, hora: String){
        val db = Firebase.firestore


        val dadosUsuario = hashMapOf(
            "cliente" to cliente,
            "barbeiro" to barbeiro,
            "data" to data,
            "hora" to hora
        )

        db.collection("agendamento").document(cliente).set(dadosUsuario).addOnCompleteListener{
            mensagem(view, "Agendamento realizado com sucesso", "#FF03DAC5")
        }.addOnFailureListener {
            mensagem(view, "Erro no agendamento", "#FF03DAC5")
        }
    }
}