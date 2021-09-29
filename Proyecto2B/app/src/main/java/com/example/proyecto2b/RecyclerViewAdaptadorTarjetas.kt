package com.example.proyecto2b

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto2b.Dto.FirestoreTarjetaDto
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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
            tarjeta.setOnLongClickListener {
                popupMenu(it)
                return@setOnLongClickListener true
                
            }
            
        }
        private fun popupMenu(view: View) {
            val categoria=listaTarjeta[adapterPosition]
            val menu= PopupMenu(contexto,view)
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
            val popup= PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible=true
            val menuPopup=popup.get(menu)
            menuPopup.javaClass.getDeclaredMethod("setForceShowIcon",Boolean::class.java)
                .invoke(menuPopup,true)

        }

        private fun mostrarEliminacion(posicion: Int) {
            val builder= AlertDialog.Builder(contexto)
                .setTitle("Eliminación de tarjeta")
                .setMessage("¿Está seguro que desea eliminar esta tarjeta?")
                .setNegativeButton("CANCELAR", null)
                .setPositiveButton("ACEPTAR", null)

            val tarjetaDialogo=builder.show()
            tarjetaDialogo.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).
            setTextColor(contexto.getResources().getColor(R.color.verde))
            tarjetaDialogo.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).
            setTextColor(contexto.getResources().getColor(R.color.verde))

            val botonAceptar=tarjetaDialogo.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
            botonAceptar.setOnClickListener {
                eliminarTarjetaFireStore(posicion,tarjetaDialogo)
            }

        }

        private fun eliminarTarjetaFireStore(posicion: Int, tarjetaDialogo: AlertDialog?) {
            val db= Firebase.firestore


            val referencia = db.collection("usuario").document(AuthUsuario.usuario!!.email)
                .collection("categoria").document(contexto.categoria.uid!!)
                .collection("tarjeta").document(listaTarjeta[posicion].uid!!)
            referencia.delete()
                .addOnSuccessListener {
                    listaTarjeta.removeAt(posicion)
                    notifyDataSetChanged()
                    tarjetaDialogo!!.dismiss()
                }

        }

        private fun mostrarEdicion(posicion: Int) {
            val tarjeta=listaTarjeta[posicion]
            val dialogoView= LayoutInflater.from(contexto).inflate(R.layout.crear_tajetas,null)
            val builder= androidx.appcompat.app.AlertDialog.Builder(contexto)
                .setView(dialogoView)
            val tarjetaDialogo=builder.show()
            val botonAceptar=dialogoView.findViewById<Button>(R.id.btn_crearTajeta_dialogo)
            botonAceptar.setText("Actualizar")
            val tituloTarjeta=dialogoView.findViewById<TextInputEditText>(R.id.til_nombre_tarjeta_dialogo)
            val descripcionTarjeta=dialogoView.findViewById<TextInputEditText>(R.id.til_descripcion_tarjeta_dialogo)
            dialogoView.findViewById<TextView>(R.id.tv_titulo_tarjeta_dialogo).text="Editar Tarjeta"
            tituloTarjeta.setText(tarjeta.titulo)
            descripcionTarjeta.setText(tarjeta.descripcion)
            botonAceptar.setOnClickListener {
                if(tituloTarjeta.text.toString().length<=130
                    && tituloTarjeta.text.toString().length>=5
                    && descripcionTarjeta.text.toString().length<=130
                    && descripcionTarjeta.text.toString().length>=5){
                    registrarTarjeta(tituloTarjeta.text.toString()
                        ,descripcionTarjeta.text.toString()
                        ,tarjetaDialogo,tarjeta)

                }

            }


        }

        private fun registrarTarjeta(titulo: String, descripcion: String, tarjetaDialogo: androidx.appcompat.app.AlertDialog, tarjeta: FirestoreTarjetaDto) {
            val db = Firebase.firestore
            val referencia=db.collection("usuario").document(AuthUsuario.usuario!!.email)
                .collection("categoria").document(contexto.categoria.uid!!)
                .collection("tarjeta").document(tarjeta.uid!!)

            db.runTransaction { transaction ->
                transaction.update(referencia, mapOf(
                    "titulo" to titulo,
                    "descripcion" to descripcion
                ))
            }.addOnSuccessListener {
                tarjeta.titulo=titulo
                tarjeta.descripcion=descripcion
                notifyDataSetChanged()
                tarjetaDialogo.dismiss()
            }
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