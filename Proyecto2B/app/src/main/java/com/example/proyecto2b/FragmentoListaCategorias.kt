package com.example.proyecto2b

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView

class FragmentoListaCategorias : Fragment(R.layout.fragment_fragmento_lista_categorias) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewRoot = inflater.inflate(R.layout.fragment_fragmento_lista_categorias,container,false)
        val categorias: List<String> = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
        val recyclerCategoria=viewRoot.findViewById<RecyclerView>(R.id.rv_categorias)
        iniciarRecyclerView(
            categorias,
            this,
            recyclerCategoria
        )
        return viewRoot
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