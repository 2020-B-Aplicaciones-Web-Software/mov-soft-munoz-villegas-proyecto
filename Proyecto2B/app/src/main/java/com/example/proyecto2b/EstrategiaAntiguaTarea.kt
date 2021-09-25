package com.example.proyecto2b

import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.example.proyecto2b.Dto.FirestoreTarea
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EstrategiaAntiguaTarea:EstrategiaPersistencia {
    override fun guardarEnFirestore(
        tarea: FirestoreTarea,
        adapter: AdaptadorToDo,
        fragmento: DialogFragment
    ) {
        val db = Firebase.firestore
        val referencia = db
            .collection("usuario").document(AuthUsuario.usuario!!.email)
            .collection("tarea").document(tarea.uid)
        val mapa = mapOf(
            "tituloTarea" to tarea.tituloTarea,
            "fechaEntrega" to tarea.fechaEntrega,
            "intervaloRecordatorio" to tarea.intervaloRecordatorio,
            "uid" to tarea.uid
        )
        referencia
            .update(mapa)
            .addOnSuccessListener {
                adapter.removerElemento(tarea.uid)
                adapter.anadirElemento(tarea)
                adapter.notifyDataSetChanged()
                fragmento.dismiss()
            }
    }
}