package com.example.proyecto2b

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdaptadorCategorias (
    private val contexto: FragmentoListaCategorias,
    private val listaCategoria: List<String>,
    private val recyclerView: RecyclerView,
):RecyclerView.Adapter<RecyclerViewAdaptadorCategorias.MyViewHolder>(){
    inner  class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val nombreCategoria: TextView
        val categoriaColor: TextView


        init {

            nombreCategoria = view.findViewById(R.id.tv_categorias_nombre)
            categoriaColor= view.findViewById(R.id.tv_colorCategoria)


            }

        }
    //tamanio del arreglo
    override fun getItemCount(): Int {
        return listaCategoria.size

    }
    //Setea los datos de cada iteracion del arreglo
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val categoria=listaCategoria[position]
        holder.nombreCategoria.text="      "+categoria
        holder.nombreCategoria.setOnClickListener(View.OnClickListener
        {  })
        if((position)%3==0){
            holder.categoriaColor.setBackgroundResource(R.drawable.estilo_categoria_rojo)

        } else if ((position)%3==1){
            holder.categoriaColor.setBackgroundResource(R.drawable.estilo_categoria_azul)

        }else{
            holder.categoriaColor.setBackgroundResource(R.drawable.estilo_categoria_amarillo)

        }





    }
    //definimos el layout a usar
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  itemView= LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.recycler_categorias,
                parent,
                false
            )
        return  MyViewHolder(itemView)
    }




}



