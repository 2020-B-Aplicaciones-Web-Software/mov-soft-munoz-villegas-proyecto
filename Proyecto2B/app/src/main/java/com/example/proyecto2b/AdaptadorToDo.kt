package com.example.proyecto2b

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto2b.Dto.FirestoreTarea
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdaptadorToDo(private val dataSet:ArrayList<FirestoreTarea>):
RecyclerView.Adapter<AdaptadorToDo.TareaViewHolder>(){
    inner class TareaViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val textoTitulo:TextView
        val textoFecha:TextView
        val recursoRojo = view.resources.getColor(R.color.rojo,null)
        init {
            textoTitulo = view.findViewById<TextView>(R.id.tv_titulo_tarea)
            textoFecha = view.findViewById<TextView>(R.id.tv_fecha_entrega)
        }
        fun marcarImportante(){
            textoTitulo.setTextColor(recursoRojo)
            textoFecha.setTextColor(recursoRojo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_todo,parent,false)
        return TareaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        //259200000 es el intervalo EPOCH entre tres dias
        val formato = SimpleDateFormat("dd/mm")
        val fechaActual = Calendar.getInstance().time
        val data = dataSet.get(position)
        if(data.fechaEntrega != null){
            val strFechaPresentacion = formato.format(data.fechaEntrega)
            val tresDiasAntes =Date(data.fechaEntrega.time - 259200000)
            holder.textoTitulo.setText(data.tituloTarea)
            holder.textoFecha.setText(strFechaPresentacion)
            if (fechaActual.after(tresDiasAntes)){
                holder.marcarImportante()
            }
        }
    }

    override fun getItemCount() = dataSet.size
}