package com.example.projectfinal.ui.admin

import android.Manifest
import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.projectfinal.R
import com.example.projectfinal.data.model.Restaurante
import com.example.projectfinal.databinding.ActivityAnadirRestauranteBinding
import com.example.projectfinal.ui.restaurante.RestauranteViewModel
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.UUID

@AndroidEntryPoint
class AnadirRestauranteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnadirRestauranteBinding
    private val viewModel: RestauranteViewModel by viewModels()
    private val listaImagenesEjemplos = mutableListOf<String>()
    private lateinit var restaurante: Restaurante
    private var idPortada: Boolean = false
    private var idEjemplo1: Boolean = false
    private var idEjemplo2: Boolean = false

    companion object {
        const val REQUEST_CODE_IMAGES = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnadirRestauranteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        restaurante = Restaurante()
        binding.btnPortada.setOnClickListener { requestPermission() }
        binding.btnEjemplo1.setOnClickListener { requestPermission1() }
        binding.btnEjemplo2.setOnClickListener { requestPermission2() }

        binding.btnGuardar.setOnClickListener {
            if (validate()) {
                val nombre = binding.tfEditTextNombreRest.text.toString()
                val direccion = binding.tfEditTextDirecRest.text.toString()
                val horaApertura = binding.tfHoraApertura.text.toString()
                val horaCierre = binding.tfHoraCierre.text.toString()
                val contacto = binding.tfEditTextContactoRest.text.toString().toLongOrNull()
                val categoria = binding.spinner.selectedItem.toString()
                val web = binding.tfEditTextWeb.text.toString()
                val listaImg = listaImagenesEjemplos
                val idRestaurante = System.currentTimeMillis()

                restaurante = Restaurante(
                    idRestaurante,
                    nombre,
                    direccion,
                    "Todos los días de",
                    horaApertura,
                    horaCierre,
                    contacto,
                    restaurante.imagen,
                    categoria,
                    listaImg,
                    web
                )
                viewModel.crearRestaurante(restaurante)
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Restaurante creado correctamente")
                builder.setPositiveButton("Aceptar") { dialog, _ ->
                    dialog.dismiss()
                    finish()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }

        binding.tfHoraApertura.setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    val selectedHour = hourOfDay.toString().padStart(2, '0')
                    val selectedMinute = minute.toString().padStart(2, '0')
                    binding.tfHoraApertura.setText("$selectedHour:$selectedMinute")
                },
                hour,
                minute,
                true
            )
            timePickerDialog.show()
        }
        binding.tfHoraCierre.setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    val selectedHour = hourOfDay.toString().padStart(2, '0')
                    val selectedMinute = minute.toString().padStart(2, '0')
                    binding.tfHoraCierre.setText("$selectedHour:$selectedMinute")
                },
                hour,
                minute,
                true
            )
            timePickerDialog.show()
        }
        binding.btnCerrar.setOnClickListener {
            finish()
        }
    }

    fun validate(): Boolean {
        var isValid = true

        val nombre = binding.tfEditTextNombreRest.text.toString()
        if (TextUtils.isEmpty(nombre)) {
            toggleTextInputLayoutError(binding.tfNombreRest, "Campo obligatorio")
            isValid = false
        } else {
            toggleTextInputLayoutError(binding.tfNombreRest, null)
        }

        val contacto = binding.tfEditTextContactoRest.text.toString()
        if (TextUtils.isEmpty(contacto)) {
            toggleTextInputLayoutError(binding.tfContactoRest, "Campo obligatorio")
            isValid = false
        } else {
            toggleTextInputLayoutError(binding.tfContactoRest, null)
        }

        val direccion = binding.tfEditTextDirecRest.text.toString()
        if (TextUtils.isEmpty(direccion)) {
            toggleTextInputLayoutError(binding.tfDireccionRest, "Campo obligatorio")
            isValid = false
        } else {
            toggleTextInputLayoutError(binding.tfDireccionRest, null)
        }

        val horaApertura = binding.tfHoraApertura.text.toString()
        if (TextUtils.isEmpty(horaApertura)) {
            toggleTextInputLayoutError(binding.textInputLayoutHoraApertura, "Campo obligatorio")
            isValid = false
        } else {
            toggleTextInputLayoutError(binding.textInputLayoutHoraApertura, null)
        }
        val horaCierre = binding.tfHoraCierre.text.toString()
        if (TextUtils.isEmpty(horaCierre)) {
            toggleTextInputLayoutError(binding.textInputLayoutHoraCierre, "Campo obligatorio")
            isValid = false
        } else {
            toggleTextInputLayoutError(binding.textInputLayoutHoraCierre, null)
        }
        val web = binding.tfEditTextWeb.text.toString()
        if (TextUtils.isEmpty(web)) {
            toggleTextInputLayoutError(binding.tfWeb, "Campo obligatorio")
            isValid = false
        } else {
            toggleTextInputLayoutError(binding.tfWeb, null)
        }

        if (!idPortada || !idEjemplo1 || !idEjemplo2) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Inserta una foto para crear el restaurante.")
            builder.setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
            isValid = false
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

    private fun requestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED -> {
                pickPhotoFromGallery()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES) -> {
                // Mostrar una explicación al usuario de por qué necesitas el permiso
                Toast.makeText(
                    this,
                    "El permiso para acceder a imágenes es necesario.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                REQUEST_CODE_IMAGES
            )
        }
    }

    private val startForActivityGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            data?.data?.let { uri ->
                uri?.let {
                    uploadImageToFirebaseStorage(uri)
                    Picasso.get().load(uri).into(binding.imgPortada)
                    idPortada = true
                }
            }
        }
    }

    private fun pickPhotoFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startForActivityGallery.launch(intent)
    }

    private fun uploadImageToFirebaseStorage(uri: Uri) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imagesRef = storageRef.child("images/${UUID.randomUUID()}")

        imagesRef.putFile(uri)
            .addOnSuccessListener {
                imagesRef.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    restaurante.imagen = downloadUrl
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Error al cargar la imagen en Firebase Storage: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun requestPermission1() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED -> {
                pickPhotoFromGallery1()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES) -> {
                Toast.makeText(
                    this,
                    "El permiso para acceder a imágenes es necesario.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                REQUEST_CODE_IMAGES
            )
        }
    }


    private val startForActivityGallery1 = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            data?.data?.let { uri ->
                uri.let {
                    uploadImageToFirebaseStorage1(uri)
                    Picasso.get().load(uri).into(binding.imgEjemplo1)
                    idEjemplo1 = true
                }
            }
        }
    }

    private fun pickPhotoFromGallery1() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startForActivityGallery1.launch(intent)
    }

    private fun uploadImageToFirebaseStorage1(uri: Uri) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imagesRef = storageRef.child("images/${UUID.randomUUID()}")

        imagesRef.putFile(uri)
            .addOnSuccessListener {
                imagesRef.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    listaImagenesEjemplos.add(downloadUrl)
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Error al cargar la imagen en Firebase Storage: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun requestPermission2() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED -> {
                pickPhotoFromGallery2()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES) -> {
                Toast.makeText(
                    this,
                    "El permiso para acceder a imágenes es necesario.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                REQUEST_CODE_IMAGES
            )
        }
    }


    private val startForActivityGallery2 = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            data?.data?.let { uri ->
                uri.let {
                    uploadImageToFirebaseStorage2(uri)
                    Picasso.get().load(uri).into(binding.imgEjemplo2)
                    idEjemplo2 = true
                }
            }
        }
    }

    private fun pickPhotoFromGallery2() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startForActivityGallery2.launch(intent)
    }

    private fun uploadImageToFirebaseStorage2(uri: Uri) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imagesRef = storageRef.child("images/${UUID.randomUUID()}")

        imagesRef.putFile(uri)
            .addOnSuccessListener {
                imagesRef.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    listaImagenesEjemplos.add(downloadUrl)
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Error al cargar la imagen en Firebase Storage: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}