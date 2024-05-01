package com.example.projectfinal.ui.reserva

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectfinal.R
import com.example.projectfinal.data.model.Reserva

class ReservaAdapter() :
    RecyclerView.Adapter<ReservaViewHolder>() {
    private var reservas: MutableList<Reserva> = mutableListOf()
    private var listener: OnItemClickListener? = null

    // Interfaz para manejar los clics en los botones
    interface OnItemClickListener {
        fun onEditarClick(position: Int)
        fun onBorrarClick(position: Int)

    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservaViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_reserva, parent, false)
        return ReservaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReservaViewHolder, position: Int) {
        holder.render(reservas[position])
        // Configura un OnClickListener para los botones
        holder.btnEditar.setOnClickListener {
            listener?.onEditarClick(position)
        }
        holder.btnBorrar.setOnClickListener {
            listener?.onBorrarClick(position)
        }
    }

    override fun getItemCount(): Int = reservas.size
    fun updateList(lista: MutableList<Reserva>){
        this.reservas.clear()
        this.reservas.addAll(lista)
        notifyDataSetChanged()
    }
    fun getItemAtPosition(position: Int): Reserva {
        return reservas[position]
    }
}