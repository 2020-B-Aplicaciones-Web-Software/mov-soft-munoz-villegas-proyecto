package com.example.proyecto2b

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto2b.Dto.FirestoreCategoriaDto
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class RecyclerViewAdaptadorCategorias (
    private val contexto: FragmentoListaCategorias,
    private var listaCategoria: ArrayList<FirestoreCategoriaDto>,
    private val recyclerView: RecyclerView,
):RecyclerView.Adapter<RecyclerViewAdaptadorCategorias.MyViewHolder>(){
    inner  class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val nombreCategoria: TextView
        val categoriaColor: TextView


        init {

            nombreCategoria = view.findViewById(R.id.tv_categorias_nombre)
            categoriaColor= view.findViewById(R.id.tv_colorCategoria)
            nombreCategoria.setOnLongClickListener {
                popupMenu(it)
                return@setOnLongClickListener true
            }


            }

        private fun popupMenu(view: View) {
            val categoria=listaCategoria[adapterPosition]
            val menu=PopupMenu(contexto.context,view)
            menu.inflate(R.menu.menu_categorias)
            menu.setOnMenuItemClickListener {
                when (it.itemId) {


                    //EDITAR
                    R.id.mi_editar_categorias -> {
                        val posicion=adapterPosition
                        mostrarEdicion(posicion)
                        true
                    }

                    //ELIMINAR
                    R.id.mi_eliminar_categoria -> {

                        val posicion=adapterPosition
                        mostrarEliminacion(posicion)


                        true
                    }

                    else-> true
                }
            }
            menu.show()
            val popup=PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible=true
            val menuPopup=popup.get(menu)
            menuPopup.javaClass.getDeclaredMethod("setForceShowIcon",Boolean::class.java)
                .invoke(menuPopup,true)

        }

        private fun mostrarEdicion(posicion: Int) {
            val dialogoView=LayoutInflater.from(contexto.context).inflate(R.layout.crear_categorias,null)
            val builder= androidx.appcompat.app.AlertDialog.Builder(contexto.requireContext())
                .setView(dialogoView)
                .setNegativeButton("CANCELAR", null)
                .setPositiveButton("EDITAR", null)
            dialogoView.findViewById<TextView>(R.id.tv_titulo_Categoria_Dialogo).text="Editar categoría"
            val categoriaDialogo=builder.show()
            categoriaDialogo.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).
            setTextColor(contexto.getResources().getColor(R.color.verde))
            categoriaDialogo.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).
            setTextColor(contexto.getResources().getColor(R.color.verde))
            dialogoView.findViewById<EditText>(R.id.et_titulo_categoria).setText(listaCategoria[posicion].nombre)
            val botonAceptar=categoriaDialogo.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
            botonAceptar.setOnClickListener {

                val valorNombre=dialogoView.findViewById<EditText>(R.id.et_titulo_categoria).text.toString()


                if(valorNombre==listaCategoria[posicion].nombre){
                    dialogoView.findViewById<TextView>(R.id.tv_error_crear_Categoria).setText("Cambie de nombre a la categoría.")

                } else  if(valorNombre.length>=3&&valorNombre.length<=25){
                    editarCategoriaFirebase(posicion,valorNombre,categoriaDialogo)

                }
                else{
                    dialogoView.findViewById<TextView>(R.id.tv_error_crear_Categoria).setText("La categoría debe tener entre 3 a 25 caracteres.")

                }

            }

        }

        private fun editarCategoriaFirebase(posicion: Int, valorNombre: String,
                                            categoriaDialogo: androidx.appcompat.app.AlertDialog) {
            val db = Firebase.firestore
            val referencia=db.collection("usuario").document(AuthUsuario.usuario!!.email)
                .collection("categoria").document(listaCategoria[posicion].uid!!)

            db.runTransaction { transaction ->
                transaction.update(referencia, mapOf(
                    "nombre" to valorNombre,
                    ))
            }.addOnSuccessListener {
                listaCategoria[posicion].nombre=valorNombre
                notifyDataSetChanged()
                categoriaDialogo.dismiss()
            }

        }

        private fun mostrarEliminacion(posicion: Int) {
            val builder=AlertDialog.Builder(contexto.context)
                .setTitle("Eliminación de categoría")
                .setMessage("¿Está seguro de que desea eliminar esta categoría, se eliminarán las tarjetas de estudio asociadas?")
                .setNegativeButton("CANCELAR", null)
                .setPositiveButton("ACEPTAR", null)

            val categoriaDialogo=builder.show()
            categoriaDialogo.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).
            setTextColor(contexto.getResources().getColor(R.color.verde))
            categoriaDialogo.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).
            setTextColor(contexto.getResources().getColor(R.color.verde))

            val botonAceptar=categoriaDialogo.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
            botonAceptar.setOnClickListener {
                eliminarCategoriaFireStore(posicion,categoriaDialogo)
            }

        }

        private fun eliminarCategoriaFireStore(posicion: Int, categoriaDialogo: AlertDialog?) {
            val db= Firebase.firestore

            val referencia = db.collection("usuario").document(AuthUsuario.usuario!!.email)
                .collection("categoria").document(listaCategoria[posicion].uid!!)
            referencia.delete()
                .addOnSuccessListener {
                    listaCategoria.removeAt(posicion)
                    notifyDataSetChanged()
                    categoriaDialogo!!.dismiss()
                }
        }

    }
    //tamanio del arreglo
    override fun getItemCount(): Int {
        return listaCategoria.size

    }
    //Setea los datos de cada iteracion del arreglo
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val categoria=listaCategoria[position]
        holder.nombreCategoria.text="      "+categoria.nombre
        holder.nombreCategoria.setOnClickListener(View.OnClickListener
        {
            contexto.abrirTarjetas(categoria)
        })
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



