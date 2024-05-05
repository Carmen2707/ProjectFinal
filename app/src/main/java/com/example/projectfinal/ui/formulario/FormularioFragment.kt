package com.example.projectfinal.ui.formulario

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.projectfinal.data.model.Reserva
import com.example.projectfinal.databinding.FragmentAnadirReservaBinding
import com.example.projectfinal.ui.reserva.ReservaViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date

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
    ): View? {
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
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.btnAumentar.setOnClickListener {
            binding.cantidad.text = (++valor).toString()
        }
        binding.btnDisminuir.setOnClickListener {
            binding.cantidad.text = (--valor).toString()
        }

        binding.tfFecha.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our edit text.
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
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { view, hourOfDay, minute ->
                    binding.tfHora.setText("$hourOfDay:$minute")

                },
                hour,
                minute,
                true
            )
            timePickerDialog.show()
        }

        binding.btnGuardar.setOnClickListener {
            val observaciones = binding.tfEditTextObservacion.text.toString()

            val reserva = Reserva( args.id,
                binding.tfFecha.text.toString(),
                binding.tfHora.text.toString(),
                FirebaseAuth.getInstance().currentUser?.email ?: "" ,
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