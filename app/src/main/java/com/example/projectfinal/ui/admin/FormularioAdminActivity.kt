package com.example.projectfinal.ui.admin

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.projectfinal.R
import com.example.projectfinal.data.model.Reserva
import com.example.projectfinal.databinding.ActivityFormularioAdminBinding
import com.example.projectfinal.ui.reserva.ReservaViewModel
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class FormularioAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormularioAdminBinding
    private var valor = 1
    private val viewModel: ReservaViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFormularioAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val reserva = intent.getSerializableExtra("reserva") as? Reserva
        if (reserva != null) {
            binding.tfFecha.setText(reserva.fecha)
            binding.tfHora.setText(reserva.hora)
            binding.tfEditTextObservacion.setText(reserva.observaciones)
            binding.tfEditTextNombre.setText(reserva.nombreUsuario)
            valor = reserva.personas
            binding.cantidad.text = valor.toString()
            binding.tvTitulo.text = reserva.restaurante


        }
        binding.btnCerrar.setOnClickListener {
            finish()
        }
        binding.btnAumentar.setOnClickListener {
            binding.cantidad.text = (++valor).toString()
        }

        binding.btnDisminuir.setOnClickListener {
            if (valor > 1) {
                binding.cantidad.text = (--valor).toString()
            } else {
            }
        }

        binding.tfFecha.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            c.add(Calendar.DAY_OF_MONTH, 1)
            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    binding.tfFecha.setText(dat)
                },
                year,
                month,
                day
            )
            datePickerDialog.datePicker.minDate = c.timeInMillis
            datePickerDialog.show()
        }

        binding.tfHora.setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    val selectedHour = hourOfDay.toString().padStart(2, '0')
                    val selectedMinute = minute.toString().padStart(2, '0')
                    val selectedTime = "$selectedHour:$selectedMinute"

                    // Validar si la hora seleccionada está dentro del horario de apertura y cierre
                    if (reserva != null) {
                        if (isValidTime(selectedTime, reserva.horaApertura, reserva.horaCierre)) {
                            binding.tfHora.setText(selectedTime)
                        } else {
                            val builder = AlertDialog.Builder(this)
                            builder.setMessage("El restaurante está cerrado en este horario. Por favor, elija otra hora.")
                            builder.setPositiveButton("Aceptar") { dialog, _ ->
                                dialog.dismiss()
                            }
                            val dialog: AlertDialog = builder.create()
                            dialog.show()
                        }
                    }
                },
                hour,
                minute,
                true
            )

            timePickerDialog.show()
        }

        binding.btnGuardar.setOnClickListener {
            if (validate()) {
                val reservaActualizada = reserva?.let { it1 ->
                    Reserva(
                        it1.id,
                        binding.tfFecha.text.toString(),
                        binding.tfHora.text.toString(),
                        reserva.usuario,
                        binding.tfEditTextNombre.text.toString(),
                        binding.tfEditTextObservacion.text.toString(),
                        valor, reserva.restaurante, reserva.horaApertura, reserva.horaCierre
                    )
                }
                if (reservaActualizada != null) {
                    viewModel.actualizarReserva(reservaActualizada)

                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("Reserva actualizada correctamente")
                    builder.setPositiveButton("Aceptar") { dialog, _ ->
                        dialog.dismiss()
                        finish()
                    }
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }
            }
        }
    }

    private fun isValidTime(
        selectedTime: String,
        horaApertura: String,
        horaCierre: String
    ): Boolean {
        if (selectedTime.isEmpty()) {
            return false
        }
        Log.d("FormularioFragment", "Hora de apertura: $horaApertura")
        Log.d("FormularioFragment", "Hora de cierre: $horaCierre")
        Log.d("FormularioFragment", "Hora seleccionada: $selectedTime")
        val horaSeleccionada = convertirAMinutos(selectedTime)
        val aperturra = convertirAMinutos(horaApertura)
        val cierre = convertirAMinutos(horaCierre)

        if (cierre < aperturra) {
            // el rango de tiempo atraviesa la medianoche
            return horaSeleccionada >= aperturra || horaSeleccionada <= cierre
        } else {
            // el rango de tiempo está dentro del mismo día
            return horaSeleccionada in aperturra..cierre
        }
    }

    fun convertirAMinutos(hora: String): Int {
        if (hora.isEmpty()) {
            return 0
        }
        val partes = hora.split(":")
        var horas = partes[0].toInt()
        val minutos = partes[1].toInt()

        if (horas == 24) {
            horas = 0
        }

        return horas * 60 + minutos
    }

    fun validate(): Boolean {
        var isValid = true

        // Verificar si el campo de nombre está vacío
        val nombre = binding.tfEditTextNombre.text.toString()
        if (TextUtils.isEmpty(nombre)) {
            toggleTextInputLayoutError(binding.tfNombre, "Campo obligatorio")
            isValid = false
        } else {
            toggleTextInputLayoutError(binding.tfNombre, null)
        }

        // Verificar si el campo de fecha está vacío
        val fecha = binding.tfFecha.text.toString()
        if (TextUtils.isEmpty(fecha)) {
            toggleTextInputLayoutError(binding.textInputLayoutFecha, "Campo obligatorio")
            isValid = false
        } else {
            toggleTextInputLayoutError(binding.textInputLayoutFecha, null)
        }

        val hora = binding.tfHora.text.toString()
        if (TextUtils.isEmpty(hora)) {
            toggleTextInputLayoutError(binding.textInputLayoutHora, "Campo obligatorio")
            isValid = false
        } else {
            toggleTextInputLayoutError(binding.textInputLayoutHora, null)
        }

        return isValid
    }

    private fun toggleTextInputLayoutError(
        @NonNull textInputLayout: TextInputLayout,
        msg: String?
    ) {
        textInputLayout.error = msg
        textInputLayout.isErrorEnabled = msg != null
    }
}
