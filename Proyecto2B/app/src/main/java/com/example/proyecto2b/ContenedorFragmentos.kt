package com.example.proyecto2b

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.bottomnavigation.BottomNavigationView

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

        val navBar = findViewById<BottomNavigationView>(R.id.bnv_navbar)
        navBar.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.mi_ir_notas ->{
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<FragmentoListaCategorias>(R.id.fl_contenedor)
                        addToBackStack(null)
                    }
                    true
                }
                R.id.mi_ir_temporizador -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<TemporizadorFragment>(R.id.fl_contenedor)
                        addToBackStack(null)
                    }
                    true
                }
                R.id.mi_ir_todo -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<ToDoFragment>(R.id.fl_contenedor)
                        addToBackStack(null)
                    }
                    true
                }
                else -> false
            }
        }
    }
}