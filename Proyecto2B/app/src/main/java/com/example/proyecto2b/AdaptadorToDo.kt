package com.example.proyecto2b

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto2b.Dto.FirestoreTarea
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdaptadorToDo(val dataSet:ArrayList<FirestoreTarea>,
var manejador:FragmentManager):
RecyclerView.Adapter<AdaptadorToDo.TareaViewHolder>(), View.OnClickListener{
    lateinit var escuchador:View.OnClickListener
    inner class TareaViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val textoTitulo:TextView
        val textoFecha:TextView
        var linearLay:LinearLayout
        val recursoRojo = view.resources.getColor(R.color.rojo,null)
        init {
            textoTitulo = view.findViewById<TextView>(R.id.tv_titulo_tarea)
            textoFecha = view.findViewById<TextView>(R.id.tv_fecha_entrega)
            linearLay = view.findViewById(R.id.ll_base)
        }
        fun marcarImportante(){
            textoTitulo.setTextColor(recursoRojo)
            textoFecha.setTextColor(recursoRojo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_todo,parent,false)
        itemView.setOnClickListener(this)
        return TareaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        //259200000 es el intervalo EPOCH entre tres dias
        val formato = SimpleDateFormat("dd/MM")
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

    fun setOnClickListener(listener:View.OnClickListener){
        escuchador= listener
    }
    override fun onClick(v: View?) {
        escuchador?.onClick(v)
    }

    fun removerElemento(uid:String){
        val elemento = dataSet.filter {
            return@filter it.uid.equals(uid)
        }
        if(elemento.isNotEmpty()){
            dataSet.remove(elemento[0])
        }
    }

    fun anadirElemento(element:FirestoreTarea){
        dataSet.add(element)
    }
}