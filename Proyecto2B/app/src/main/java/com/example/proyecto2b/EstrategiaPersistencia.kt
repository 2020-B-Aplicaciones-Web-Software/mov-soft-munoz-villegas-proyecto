package com.example.proyecto2b

import androidx.fragment.app.DialogFragment
import com.example.proyecto2b.Dto.FirestoreTarea

interface EstrategiaPersistencia {
    fun guardarEnFirestore(tarea:FirestoreTarea, adapter:AdaptadorToDo, fragmento:DialogFragment)
}