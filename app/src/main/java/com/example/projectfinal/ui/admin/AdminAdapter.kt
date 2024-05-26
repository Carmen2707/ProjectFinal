package com.example.projectfinal.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.projectfinal.R
import com.example.projectfinal.data.model.Restaurante

class AdminAdapter(
    private val onItemSelected: (restaurante: Restaurante) -> Unit
) :
    ListAdapter<Restaurante, AdminViewHolder>(RESTAURANTE_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_restaurante_admin, parent, false)
        return AdminViewHolder(view)
    }

    override fun getItemCount(): Int = currentList.size
    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onBorrarClick(position: Int)

    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onBindViewHolder(holder: AdminViewHolder, position: Int) {
        val restaurante = currentList[position]
        holder.render(restaurante, onItemSelected)
        holder.btnBorrarRest.setOnClickListener {
            listener?.onBorrarClick(position)
        }
    }

    companion object {
        val RESTAURANTE_COMPARATOR = object : DiffUtil.ItemCallback<Restaurante>() {
            override fun areItemsTheSame(oldItem: Restaurante, newItem: Restaurante): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Restaurante, newItem: Restaurante): Boolean {
                return oldItem == newItem
            }

        }
    }
}