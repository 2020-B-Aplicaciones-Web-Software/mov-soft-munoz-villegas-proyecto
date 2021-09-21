package com.example.proyecto2b

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit

class ContenedorFragmentos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contenedor_fragmentos)
        if( savedInstanceState == null){
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<FragmentoListaCategorias>(R.id.fl_contenedor)
            }
        }
    }
}