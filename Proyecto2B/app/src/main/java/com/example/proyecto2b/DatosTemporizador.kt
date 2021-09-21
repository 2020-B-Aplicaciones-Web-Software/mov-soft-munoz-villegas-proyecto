package com.example.proyecto2b

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResult
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DatosTemporizador : Fragment(R.layout.fragment_datos_temporizador) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_datos_temporizador, container, false)
        val etMinutosTrabajo = root.findViewById<EditText>(R.id.et_minutos_trabajo)
        val etMinutosDescanso = root.findViewById<EditText>(R.id.et_minutos_descanso)
        val etSegundosTrabajo = root.findViewById<EditText>(R.id.et_segundos_trabajo)
        val etSegundosDescanso = root.findViewById<EditText>(R.id.et_segundos_descanso)
        val etNumIteraciones = root.findViewById<EditText>(R.id.et_iteraciones_pomodoro)
        val btnIniciarTemp = root.findViewById<FloatingActionButton>(R.id.btn_iniciar_temp)
        btnIniciarTemp.setOnClickListener {
            val arregloValores = arrayOf(etMinutosTrabajo.text.toString(),
                etMinutosDescanso.text.toString(),
                etSegundosTrabajo.text.toString(),
                etSegundosDescanso.text.toString(),
                etNumIteraciones.text.toString()
                )
            val resultado:Boolean = arregloValores
                .all {
                    return@all !it.equals("")
                }

            if (resultado){
                val paqueteDatos = bundleOf(
                    "minutosTrabajo" to etMinutosTrabajo.text.toString().toInt(),
                    "segundosTrabajo" to etSegundosTrabajo.text.toString().toInt(),
                    "minutosDescanso" to etMinutosDescanso.text.toString().toInt(),
                    "segundosDescanso" to etSegundosDescanso.text.toString().toInt(),
                    "numIteraciones" to etNumIteraciones.text.toString().toInt()
                )
                setFragmentResult("valoresTemporizador", paqueteDatos)
            }
        }
        return root
    }


}