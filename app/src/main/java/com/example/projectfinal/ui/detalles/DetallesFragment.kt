package com.example.projectfinal.ui.detalles

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.projectfinal.R
import com.example.projectfinal.data.model.Restaurante
import com.example.projectfinal.databinding.FragmentDetallesBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.carousel.CarouselSnapHelper
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class DetallesFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentDetallesBinding
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private val args: DetallesFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCarouselRecyclerView()

        binding.btnHacerReserva.setOnClickListener {
            val currentDestinationId = findNavController().currentDestination?.id
            val detallesFragmentId = R.id.detallesFragment

            if (currentDestinationId == detallesFragmentId) {
                val restaurante = args.objRestaurante
                val action = DetallesFragmentDirections.actionDetallesFragmentToFormularioFragment(
                    restauranteNombre = args.restauranteNombre,
                    horaApertura = restaurante.horaApertura,
                    horaCierre = restaurante.horaCierre,
                    personas = 1,
                    observaciones = "",
                    fecha = "",
                    hora = "",
                    isEdit = false,
                    nombreUsuario = "",
                    id = ""
                )
                findNavController().navigate(action)
            } else {
                Log.e("Navigation", "No se puede navegar desde el fragmento actual")
            }
        }
        binding.btnVolver.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.tvTitle.text = args.restauranteNombre
        binding.numTel.text = args.objRestaurante.contacto.toString()
        binding.numTel.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${args.objRestaurante.contacto}")
            startActivity(intent)
        }
        binding.tvHoraApertura.text = args.objRestaurante.horaApertura
        binding.tvHoraCierre.text = args.objRestaurante.horaCierre

        binding.btnWeb.setOnClickListener {
            val webUrl = args.objRestaurante.web

            if (!webUrl.isNullOrEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webUrl))
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "La URL no está disponible", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    private fun setupCarouselRecyclerView() {
        val carouselRecyclerView: RecyclerView = binding.carouselRecyclerView
        CarouselSnapHelper().attachToRecyclerView(carouselRecyclerView)
        carouselRecyclerView.adapter = DetallesAdapter(imagenes = getImages(args.objRestaurante))
    }

    private fun getImages(restaurante: Restaurante): List<String> {
        return restaurante.imagenes
    }

    override fun onMapReady(p0: GoogleMap) {
        p0?.let { googleMap ->
            this.googleMap = googleMap
            val direccionRestaurante = args.objRestaurante.direccion

            // Geocodificar la dirección para obtener las coordenadas de latitud y longitud
            val geocoder = Geocoder(requireContext())
            try {
                val addressList: MutableList<Address>? =
                    direccionRestaurante?.let { geocoder.getFromLocationName(it, 1) }
                if (addressList?.isNotEmpty() == true) {
                    val address: Address = addressList[0]
                    val restauranteLocation = LatLng(address.latitude, address.longitude)

                    googleMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            restauranteLocation,
                            15f
                        )
                    )
                    googleMap.addMarker(
                        MarkerOptions().position(restauranteLocation).title(args.restauranteNombre)
                    )
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetallesBinding.inflate(inflater, container, false)
        return binding.root
    }
}