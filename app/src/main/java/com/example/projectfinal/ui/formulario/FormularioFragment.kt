package com.example.projectfinal.ui.formulario

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.TextUtils
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
import java.util.Calendar


@AndroidEntryPoint
class FormularioFragment : Fragment() {
    val args: FormularioFragmentArgs by navArgs()
    private lateinit var binding: FragmentAnadirReservaBinding
    private val viewModel: ReservaViewModel by activityViewModels()

    private var valor = 0
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
            // Llenar otros campos según sea necesario
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
            datePickerDialog.show()

            /* val currentDateTime = Calendar.getInstance()
              val startYear = currentDateTime.get(Calendar.YEAR)
              val startMonth = currentDateTime.get(Calendar.MONTH)
              val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
              val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
              val startMinute = currentDateTime.get(Calendar.MINUTE)
              DatePickerDialog(requireContext(), { _, year, month, day ->
                  TimePickerDialog(context, { _, hour, minute ->
                      val pickedDateTime = Calendar.getInstance()
                      pickedDateTime.set(year, month, day, hour, minute)
                      val formattedDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(pickedDateTime.time)
                      binding.tfFecha.setText(formattedDateTime)
                  }, startHour, startMinute, false).show()
              }, startYear, startMonth, startDay).show()
  */
        }

        binding.tfHora.setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            val horaApertura = args.horaApertura
            val horaCierre = args.horaCierre

            val horaAperturaParts = horaApertura.split(":")
            val horaAperturaHour = horaAperturaParts[0].toInt()
            val horaAperturaMinute = horaAperturaParts[1].toInt()

            val horaCierreParts = horaCierre.split(":")
            val horaCierreHour = horaCierreParts[0].toInt()
            val horaCierreMinute = horaCierreParts[1].toInt()

            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    // Verificar si la hora seleccionada está dentro del rango permitido
                    if (hourOfDay < horaAperturaHour || (hourOfDay == horaAperturaHour && minute < horaAperturaMinute)) {
                        // Si la hora seleccionada está antes de la hora de apertura, ajustar a la hora de apertura
                        binding.tfHora.setText(horaApertura)
                    } else if (hourOfDay > horaCierreHour || (hourOfDay == horaCierreHour && minute > horaCierreMinute)) {
                        // Si la hora seleccionada está después de la hora de cierre, ajustar a la hora de cierre
                        binding.tfHora.setText(horaCierre)
                    } else {
                        // La hora seleccionada está dentro del rango permitido, establecerla en el EditText
                        val selectedHour = if (hourOfDay < 10) "0$hourOfDay" else hourOfDay.toString()
                        val selectedMinute = if (minute < 10) "0$minute" else minute.toString()
                        val selectedTime = "$selectedHour:$selectedMinute"
                        binding.tfHora.setText(selectedTime)
                    }
                },
                hour,
                minute,
                true
            )

            // Deshabilitar las horas fuera del rango
            timePickerDialog.setOnTimeChangedListener { view, hourOfDay, minute ->
                if (hourOfDay < horaAperturaHour || (hourOfDay == horaAperturaHour && minute < horaAperturaMinute)) {
                    // Si la hora seleccionada está antes de la hora de apertura, establecer la hora de apertura
                    view.hour = horaAperturaHour
                    view.minute = horaAperturaMinute
                } else if (hourOfDay > horaCierreHour || (hourOfDay == horaCierreHour && minute > horaCierreMinute)) {
                    // Si la hora seleccionada está después de la hora de cierre, establecer la hora de cierre
                    view.hour = horaCierreHour
                    view.minute = horaCierreMinute
                }
            }

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