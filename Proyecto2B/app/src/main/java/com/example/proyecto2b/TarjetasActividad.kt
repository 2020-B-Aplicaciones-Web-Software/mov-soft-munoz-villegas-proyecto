package com.example.proyecto2b

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto2b.Dto.FirestoreCategoriaDto
import com.example.proyecto2b.Dto.FirestoreTarjetaDto

class TarjetasActividad : AppCompatActivity() {
    var listaTarjetas= arrayListOf<FirestoreTarjetaDto>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarjetas_actividad)
        cargarnterfaz()


    }
    fun cargarnterfaz(){
        val categoria=intent.getParcelableExtra<FirestoreCategoriaDto>("categoria")
        findViewById<TextView>(R.id.tv_tituloCategoria).setText(categoria?.nombre)
        Log.i("s","${categoria!!.nombre } ${categoria!!.uid }")
        val recyclerViewTarjetas=findViewById<RecyclerView>(
            R.id.rv_tarjetas
        )

        listaTarjetas.add(FirestoreTarjetaDto("s","sssss","aaaaa"))
        listaTarjetas.add(FirestoreTarjetaDto("s","sssss","aaaaa"))
        listaTarjetas.add(FirestoreTarjetaDto("s","sssss","aaaaa"))
        listaTarjetas.add(FirestoreTarjetaDto("s","sssss","aaaaa"))
        listaTarjetas.add(FirestoreTarjetaDto("s","sssss","aaaaa"))
        listaTarjetas.add(FirestoreTarjetaDto("s","sssss","aaaaa"))
        listaTarjetas.add(FirestoreTarjetaDto("s","sssss","aaaaa"))
        listaTarjetas.add(FirestoreTarjetaDto("s","sssss","aaaaa"))
        listaTarjetas.add(FirestoreTarjetaDto("s","sssss","aaaaa"))

        iniciarRecyclerView(listaTarjetas,this,recyclerViewTarjetas)
        val botonRegresar=findViewById<ImageView>(R.id.btn_regresarTarjetas)
        val botonAgregar=findViewById<ImageView>(R.id.btn_agregarCategoria)
        botonRegresar.setOnClickListener {
            abrirActividad(ContenedorFragmentos::class.java)

        }
        botonAgregar.setOnClickListener {
            abrirDialogoTarjeta()

        }

    }

    private fun abrirDialogoTarjeta() {
        val dialogoView= LayoutInflater.from(this).inflate(R.layout.crear_tajetas,null)
        val builder= AlertDialog.Builder(this)
            .setView(dialogoView)
        val categoriaDialogo=builder.show()
        val botonAceptar=dialogoView.findViewById<Button>(R.id.btn_crearTajeta_dialogo)
        val textoDescripcion=dialogoView.findViewById<EditText>(R.id.et_titulo_tarjeta_dialogo2)
        val textoTitulo=dialogoView.findViewById<EditText>(R.id.et_titulo_tarjeta_dialogo)

        botonAceptar.setOnClickListener {
            categoriaDialogo.dismiss()
        }
    }

    fun abrirActividad(
        clase: Class<*>
    ){
        val intentExplicito= Intent(
            this,
            clase
        )
        startActivity(intentExplicito)
    }

    fun iniciarRecyclerView(
        lista: ArrayList<FirestoreTarjetaDto>,
        actividad: TarjetasActividad,
        recyclerView: RecyclerView
    ) {
        val adaptador=RecyclerViewAdaptadorTarjetas(
            actividad,
            lista,
            recyclerView
        )
        recyclerView.adapter=adaptador
        recyclerView.itemAnimator=androidx.recyclerview.widget.DefaultItemAnimator()
        recyclerView.layoutManager=androidx.recyclerview.widget.GridLayoutManager(actividad,2)
        adaptador.notifyDataSetChanged()
    }
}