package com.example.proyecto2b

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto2b.Dto.FirestoreCategoriaDto
import com.example.proyecto2b.Dto.FirestoreTarjetaDto

class RecyclerViewAdaptadorTarjetas(
    private val contexto: TarjetasActividad,
    private var listaTarjeta: ArrayList<FirestoreTarjetaDto>,
    private val recyclerView: RecyclerView,
): RecyclerView.Adapter<RecyclerViewAdaptadorTarjetas.MyViewHolder>() {
    inner  class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tarjeta:CardView
        val ojo:ImageView
        val textoTarjeta:TextView



        init {
            tarjeta=view.findViewById(R.id.cv_tarjetaContenedor)
            ojo=view.findViewById(R.id.iv_tarjetaOjo)
            textoTarjeta=view.findViewById(R.id.tv_tarjetaNombre)


        }
    }

    //tamanio del arreglo
    override fun getItemCount(): Int {
        return listaTarjeta.size

    }
    //Setea los datos de cada iteracion del arreglo
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val tarjetaActual=listaTarjeta[position]
        holder.tarjeta.setCardBackgroundColor(contexto.getColor(R.color.amarilloTarjeta))
        holder.textoTarjeta.text=tarjetaActual.titulo
        holder.ojo.setImageResource(R.drawable.ic_ojocerrado)

        holder.tarjeta.setOnClickListener {
            if(holder.textoTarjeta.text.toString()==tarjetaActual.descripcion){
                holder.tarjeta.setCardBackgroundColor(contexto.getColor(R.color.amarilloTarjeta))
                holder.textoTarjeta.text=tarjetaActual.titulo
                holder.ojo.setImageResource(R.drawable.ic_ojocerrado)

            }else{
                holder.tarjeta.setCardBackgroundColor(contexto.getColor(R.color.azulTarjeta))
                holder.textoTarjeta.text=tarjetaActual.descripcion
                holder.ojo.setImageResource(R.drawable.ic_ojoabierto)

            }
        }






    }
    //definimos el layout a usar
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  itemView= LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.recycler_tarjetas,
                parent,
                false
            )
        return  MyViewHolder(itemView)
    }


}