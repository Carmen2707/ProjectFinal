package com.example.projectfinal.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectfinal.R
import com.example.projectfinal.data.model.Reserva

class ReservasAdminAdapter(
    private var reservas: MutableList<Reserva>
) :
    RecyclerView.Adapter<ReservasAdminViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservasAdminViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_reserva_admin, parent, false)
        return ReservasAdminViewHolder(view)
    }

    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onEditarClick(position: Int)
        fun onBorrarClick(position: Int)

    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun getItemCount(): Int = reservas.size

    override fun onBindViewHolder(holder: ReservasAdminViewHolder, position: Int) {
        holder.render(
            reservas[position]
        )
        holder.btnEditar.setOnClickListener {
            listener?.onEditarClick(position)
        }
        holder.btnBorrar.setOnClickListener {
            listener?.onBorrarClick(position)
        }
    }

    fun updateList(lista: MutableList<Reserva>) {
        reservas = lista
        notifyDataSetChanged()
    }

    fun getItemAtPosition(position: Int): Reserva {
        return reservas[position]
    }
}