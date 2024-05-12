package com.example.projectfinal.ui.formulario

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.projectfinal.data.model.Reserva
import com.example.projectfinal.databinding.FragmentAnadirReservaBinding
import com.example.projectfinal.ui.reserva.ReservaViewModel
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@AndroidEntryPoint
class FormularioFragment : Fragment() {
    val args: FormularioFragmentArgs by navArgs()
    private lateinit var binding: FragmentAnadirReservaBinding
    private val viewModel: ReservaViewModel by activityViewModels()

    private var valor = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnadirReservaBinding.inflate(inflater, container, false)
        // Retorna la vista inflada por el binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAnadirReservaBinding.bind(view)

        if (args.isEdit) {
            // Estamos editando una reserva existente
            binding.tfFecha.setText(args.fecha)
            binding.tfHora.setText(args.hora)
            binding.tfEditTextObservacion.setText(args.observaciones)
            binding.tfEditTextNombre.setText(args.nombreUsuario)

        } else {
            // Estamos creando una nueva reserva
            // Los campos del formulario deben estar vacíos
            // Puedes dejarlos vacíos o establecer valores predeterminados
        }

        binding.tvTitulo.text = args.restauranteNombre
        binding.btnCerrar.setOnClickListener {
            findNavController().popBackStack()
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
                requireContext(),
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
                requireContext(),
                { _, hourOfDay, minute ->
                    val selectedHour = hourOfDay.toString().padStart(2, '0')
                    val selectedMinute = minute.toString().padStart(2, '0')
                    val selectedTime = "$selectedHour:$selectedMinute"

                    // Validar si la hora seleccionada está dentro del horario de apertura y cierre
                    if (isValidTime(selectedTime, args.horaApertura, args.horaCierre)) {
                        binding.tfHora.setText(selectedTime)
                    } else {
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setMessage("El restaurante está cerrado en este horario. Por favor, elija otra hora.")
                        builder.setPositiveButton("Aceptar") { dialog, _ ->
                            dialog.dismiss()
                        }
                        val dialog: AlertDialog = builder.create()
                        dialog.show()
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
                val observaciones = binding.tfEditTextObservacion.text.toString()

                val reserva = Reserva(
                    args.id,
                    binding.tfFecha.text.toString(),
                    binding.tfHora.text.toString(),
                    FirebaseAuth.getInstance().currentUser?.email ?: "",
                    observaciones,
                    valor,
                    args.restauranteNombre
                )


                if (args.isEdit) {
                    viewModel.actualizarReserva(reserva)
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setMessage("Reserva actualizada correctamente")
                    builder.setPositiveButton("Aceptar") { dialog, _ ->

                        dialog.dismiss()
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                } else {
                    viewModel.addReserva(reserva = reserva)
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setMessage("Reserva realizada correctamente")
                    builder.setPositiveButton("Aceptar") { dialog, _ ->

                        dialog.dismiss()
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }
            }
        }
    }

    private fun isValidTime(selectedTime: String, horaApertura: String, horaCierre: String): Boolean {
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


    /**
     * Display/hides TextInputLayout error.
     *
     * @param msg the message, or null to hide
     */
    private fun toggleTextInputLayoutError(
        @NonNull textInputLayout: TextInputLayout,
        msg: String?
    ) {
        textInputLayout.error = msg
        textInputLayout.isErrorEnabled = msg != null
    }

}