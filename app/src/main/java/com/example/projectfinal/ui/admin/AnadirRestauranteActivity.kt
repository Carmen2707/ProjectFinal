package com.example.projectfinal.ui.admin

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.Manifest
import android.app.TimePickerDialog
import android.provider.Settings
import android.text.TextUtils
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.viewModels
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.get
import androidx.navigation.fragment.findNavController
import com.example.projectfinal.R
import com.example.projectfinal.data.model.Restaurante
import com.example.projectfinal.databinding.ActivityAnadirRestauranteBinding
import com.example.projectfinal.ui.restaurante.RestauranteViewModel
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.Calendar
import java.util.UUID

@AndroidEntryPoint
class AnadirRestauranteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnadirRestauranteBinding
    private val viewModel: RestauranteViewModel by viewModels()
    private val listaImagenesEjemplos = mutableListOf<String>()
    private lateinit var restaurante: Restaurante
    companion object {
        const val REQUEST_CODE_IMAGES = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAnadirRestauranteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        restaurante = Restaurante(

        )
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

                viewModel.imageUrl1.value?.let { it1 -> listaImagenesEjemplos.add(it1) }
                viewModel.imageUrl2.value?.let { it1 -> listaImagenesEjemplos.add(it1) }
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
                    binding.tfHoraApertura.setText("$hourOfDay:$minute")
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
                    binding.tfHoraCierre.setText("$hourOfDay:$minute")
                },
                hour,
                minute,
                true
            )
            timePickerDialog.show()
        }
    }
    fun validate(): Boolean {
        var isValid = true

        // Verificar si el campo de nombre está vacío
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
                Toast.makeText(this, "El permiso para acceder a imágenes es necesario.", Toast.LENGTH_SHORT).show()
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
                // Verificar que la URI no sea nula
                uri?.let {
                    uploadImageToFirebaseStorage(uri)
                    // Cargar la imagen en Picasso
                    Picasso.get().load(uri).into(binding.imgPortada)
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
                    // Asignar la URL de descarga al campo de imagen del restaurante
                    restaurante.imagen = downloadUrl
                }
            }
            .addOnFailureListener { exception ->
                // Manejar errores en la carga de la imagen
                Toast.makeText(this, "Error al cargar la imagen en Firebase Storage: ${exception.message}", Toast.LENGTH_SHORT).show()
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
                // Mostrar una explicación al usuario de por qué necesitas el permiso
                Toast.makeText(this, "El permiso para acceder a imágenes es necesario.", Toast.LENGTH_SHORT).show()
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
                // Verificar que la URI no sea nula
                uri.let {
                    uploadImageToFirebaseStorage1(uri)
                    // Cargar la imagen en Picasso
                    Picasso.get().load(uri).into(binding.imgEjemplo1)
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
                Toast.makeText(this, "Error al cargar la imagen en Firebase Storage: ${exception.message}", Toast.LENGTH_SHORT).show()
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
                // Mostrar una explicación al usuario de por qué necesitas el permiso
                Toast.makeText(this, "El permiso para acceder a imágenes es necesario.", Toast.LENGTH_SHORT).show()
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
                // Verificar que la URI no sea nula
                uri.let {
                    uploadImageToFirebaseStorage1(uri)
                    // Cargar la imagen en Picasso
                    Picasso.get().load(uri).into(binding.imgEjemplo2)
                }
            }

        }
    }
    private fun pickPhotoFromGallery2() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startForActivityGallery2.launch(intent)
    }
}