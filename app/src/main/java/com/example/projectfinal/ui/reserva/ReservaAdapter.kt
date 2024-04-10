package com.example.projectfinal.ui.reserva

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectfinal.R
import com.example.projectfinal.data.model.Reserva

class ReservaAdapter() :
    RecyclerView.Adapter<ReservaViewHolder>() {
    private var reservas: MutableList<Reserva> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservaViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_reserva, parent, false)
        return ReservaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReservaViewHolder, position: Int) {
        holder.render(reservas[position])
    }

    override fun getItemCount(): Int = reservas.size
    fun updateList(lista: MutableList<Reserva>){
        this.reservas.clear()
        this.reservas.addAll(lista)
        notifyDataSetChanged()
    }
}