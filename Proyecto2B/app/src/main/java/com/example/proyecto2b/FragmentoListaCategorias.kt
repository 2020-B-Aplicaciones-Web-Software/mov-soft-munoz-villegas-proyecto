package com.example.proyecto2b

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto2b.Dto.FirestoreCategoriaDto
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FragmentoListaCategorias : Fragment(R.layout.fragment_fragmento_lista_categorias) {
    var listaCategorias= arrayListOf<FirestoreCategoriaDto>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewRoot = inflater.inflate(R.layout.fragment_fragmento_lista_categorias,container,false)

        cargarCategorias(viewRoot)

        return viewRoot
    }
    fun cargarCategorias(viewRoot: View){
        listaCategorias.clear()

        val db= Firebase.firestore

        val referencia = db.collection("usuario").document(AuthUsuario.usuario!!.email)
            .collection("categoria")
        referencia
            .get()
            .addOnSuccessListener { categorias ->
                for (categoria in categorias) {

                    if (categoria != null) {
                        listaCategorias.add(
                            FirestoreCategoriaDto(categoria.id,
                                categoria["nombre"].toString()
                            )
                        )

                    }

                }
                cargarInterfaz(viewRoot)
            }
    }
    fun cargarInterfaz(viewRoot: View) {


        val recyclerCategoria=viewRoot.findViewById<RecyclerView>(R.id.rv_categorias)
        val agregarCategoria=viewRoot.findViewById<FloatingActionButton>(R.id.btn_nueva_categoria)


        iniciarRecyclerView(
            listaCategorias,
            this,
            recyclerCategoria
        )
        agregarCategoria.setOnClickListener {
            abrirDialogoCategoria(viewRoot)

        }

    }
    fun abrirDialogoCategoria(viewRoot: View){
        val dialogoView=LayoutInflater.from(this.context).inflate(R.layout.crear_categorias,null)
        val builder=AlertDialog.Builder(this.requireContext())
            .setView(dialogoView)
            .setNegativeButton("CANCELAR", null)
            .setPositiveButton("Agregar", null)

        val categoriaDialogo=builder.show()
        categoriaDialogo.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.verde))
        categoriaDialogo.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.verde))
        val botonAceptar=categoriaDialogo.getButton(AlertDialog.BUTTON_POSITIVE)
        botonAceptar.setOnClickListener {
            val valorNombre=dialogoView.findViewById<EditText>(R.id.et_titulo_categoria).text.toString()

            if(valorNombre.length>=3&&valorNombre.length<=25){
                registrarCetegoria(valorNombre)
                categoriaDialogo.dismiss()
                cargarCategorias(viewRoot)

            }else{
                dialogoView.findViewById<TextView>(R.id.tv_error_crear_Categoria).setText("La categoría debe tener entre 3 a 25 caracteres.")

            }

        }

    }
    fun registrarCetegoria(nombre:String){
        val nuevaCategoria = hashMapOf<String, Any>(
            "nombre" to nombre
        )
        val db = Firebase.firestore
        val referencia = db.collection("usuario").document(AuthUsuario.usuario!!.email)
            .collection("categoria")

        referencia
            .add(nuevaCategoria)
            .addOnSuccessListener {

            }.addOnFailureListener {}
    }

    fun iniciarRecyclerView(
        lista: ArrayList<FirestoreCategoriaDto>,
        actividad: FragmentoListaCategorias,
        recyclerView: RecyclerView
    ) {
        val adaptador=RecyclerViewAdaptadorCategorias(
            actividad,
            lista,
            recyclerView
        )
        recyclerView.adapter=adaptador
        recyclerView.itemAnimator=androidx.recyclerview.widget.DefaultItemAnimator()
        recyclerView.layoutManager=androidx.recyclerview.widget.LinearLayoutManager(actividad.context)
        adaptador.notifyDataSetChanged()
    }
}