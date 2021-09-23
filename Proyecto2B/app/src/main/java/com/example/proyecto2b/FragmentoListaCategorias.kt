package com.example.proyecto2b

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FragmentoListaCategorias : Fragment(R.layout.fragment_fragmento_lista_categorias) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewRoot = inflater.inflate(R.layout.fragment_fragmento_lista_categorias,container,false)
        val categorias: List<String> = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
        val recyclerCategoria=viewRoot.findViewById<RecyclerView>(R.id.rv_categorias)
        val agregarCategoria=viewRoot.findViewById<FloatingActionButton>(R.id.btn_nueva_categoria)


        iniciarRecyclerView(
            categorias,
            this,
            recyclerCategoria
        )
        agregarCategoria.setOnClickListener {
            abrirDialogoCategoria()

        }

        return viewRoot
    }
    fun abrirDialogoCategoria(){
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
        val referencia = db.collection("categoria")

        referencia
            .add(nuevaCategoria)
            .addOnSuccessListener {

            }.addOnFailureListener {}
    }
    fun iniciarRecyclerView(
        lista: List<String>,
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