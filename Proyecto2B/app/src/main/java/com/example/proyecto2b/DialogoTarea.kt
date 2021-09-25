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
import com.example.proyecto2b.Dto.FirestoreTarea
import java.text.SimpleDateFormat
import java.util.*


class DialogoTarea(
    val tarea:FirestoreTarea? = null,
    val algoritmo:EstrategiaPersistencia,
    val adaptador:AdaptadorToDo
):DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_tareas,container,false)
        val textoFecha = view.findViewById<EditText>(R.id.et_fecha_entrega)
        val botonAñadir = view.findViewById<Button>(R.id.btn_anadir_tarea)
        val textoTitulo = view.findViewById<TextView>(R.id.tv_titulo_dialogo)
        val etTitulo = view.findViewById<EditText>(R.id.et_titulo_tarea)
        val etIntervalo = view.findViewById<EditText>(R.id.et_intervalo_recordatorio)
        val formato = SimpleDateFormat("dd/MM/yyyy")
        if(tarea != null){
            textoTitulo.setText("Editar Tarea")
            etTitulo.setText(tarea.tituloTarea!!)
            textoFecha.setText(formato.format(tarea.fechaEntrega!!))
            etIntervalo.setText(tarea.intervaloRecordatorio!!.toString())
            botonAñadir.setText("Editar")
        }
        textoFecha.setOnClickListener {
            DatePickerFragment(textoFecha)
                .show(
                    childFragmentManager, "escogerNuevaFecha"
                )
        }

        botonAñadir.setOnClickListener {
            val titulo = etTitulo.text.toString()
            val fecha = textoFecha.text.toString()
            val intervalo = etIntervalo.text.toString()
            if((!titulo.equals("")) && (!fecha.equals("")) && (!intervalo.equals(""))){
                algoritmo.guardarEnFirestore(
                    FirestoreTarea(
                        tituloTarea = titulo,
                        fechaEntrega = formato.parse(fecha),
                        intervaloRecordatorio = intervalo.toInt(),
                        uid = if(tarea!=null) tarea.uid else ""
                    ),
                    adaptador,
                    this
                )
            }
        }
        return view
    }

}