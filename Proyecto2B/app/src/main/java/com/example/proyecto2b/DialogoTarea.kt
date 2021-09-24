package com.example.proyecto2b

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import java.util.*


class DialogoTarea:DialogFragment() {
    lateinit var botonAñadir:Button
    lateinit var textoTitulo:TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_tareas,container,false)
        val textoFecha = view.findViewById<EditText>(R.id.et_fecha_entrega)
        textoFecha.setOnClickListener {
            DatePickerFragment(textoFecha).show(
                childFragmentManager, "escogerNuevaFecha"
            )
        }
        botonAñadir = view.findViewById(R.id.btn_anadir_tarea)
        return view
    }
}