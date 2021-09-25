package com.example.proyecto2b

import android.util.Log
import androidx.fragment.app.DialogFragment
import com.example.proyecto2b.Dto.FirestoreTarea
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EstrategiaNuevaTarea:EstrategiaPersistencia {
    override fun guardarEnFirestore(
        tarea: FirestoreTarea,
        adapter: AdaptadorToDo,
        fragmento: DialogFragment
    ) {
        val db = Firebase.firestore
        val referencia = db
            .collection("usuario").document(AuthUsuario.usuario!!.email)
            .collection("tarea")
        referencia.add(tarea).addOnSuccessListener {
            adapter.dataSet.add(tarea)
            adapter.notifyDataSetChanged()
            fragmento.dismiss()
        }
            .addOnFailureListener {
                Log.i("Firestore",it.toString())
            }
    }
}