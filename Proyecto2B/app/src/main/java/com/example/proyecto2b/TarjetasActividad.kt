package com.example.proyecto2b

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto2b.Dto.FirestoreCategoriaDto
import com.example.proyecto2b.Dto.FirestoreTarjetaDto
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TarjetasActividad : AppCompatActivity() {
    var listaTarjetas= arrayListOf<FirestoreTarjetaDto>()
    lateinit var categoria:FirestoreCategoriaDto
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarjetas_actividad)
        categoria= intent.getParcelableExtra<FirestoreCategoriaDto>("categoria")!!
        cargarTarjetas()



    }
    fun cargarTarjetas(){
        listaTarjetas.clear()

        val db= Firebase.firestore

        val referencia = db.collection("usuario").document(AuthUsuario.usuario!!.email)
            .collection("categoria").document(categoria.uid!!)
            .collection("tajeta")
            .orderBy("titulo", Query.Direction.DESCENDING)

        referencia
            .get()
            .addOnSuccessListener { tarjetas ->
                for (tajeta in tarjetas) {

                    if (tajeta != null) {
                        listaTarjetas.add(
                            FirestoreTarjetaDto(tajeta.id,
                                tajeta["titulo"].toString(),
                                tajeta["descripcion"].toString()

                            )
                        )

                    }

                }
                cargarInterfaz()
            }
        cargarInterfaz()

    }
    fun cargarInterfaz(){

        findViewById<TextView>(R.id.tv_tituloCategoria).setText(categoria?.nombre)
        Log.i("s","${categoria!!.nombre } ${categoria!!.uid }")
        val recyclerViewTarjetas=findViewById<RecyclerView>(
            R.id.rv_tarjetas
        )
        

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
        val tituloTarjeta=dialogoView.findViewById<TextInputEditText>(R.id.til_nombre_tarjeta_dialogo)
        val descripcionTarjeta=dialogoView.findViewById<TextInputEditText>(R.id.til_descripcion_tarjeta_dialogo)



        botonAceptar.setOnClickListener {
            if(tituloTarjeta.text.toString().length<=130
                && tituloTarjeta.text.toString().length>=5
                && descripcionTarjeta.text.toString().length<=130
                && descripcionTarjeta.text.toString().length>=5){
                    registrarTarjeta(tituloTarjeta.text.toString()
                        ,descripcionTarjeta.text.toString()
                        ,categoriaDialogo)

            }

        }
    }

    private fun registrarTarjeta(titulo: String, descripcion: String, categoriaDialogo: AlertDialog?) {
        val nuevaTarjeta = hashMapOf<String, Any>(
            "titulo" to titulo,
            "descripcion" to descripcion

        )
        val db = Firebase.firestore
        val referencia = db.collection("usuario").document(AuthUsuario.usuario!!.email)
            .collection("categoria").document(categoria.uid!!)
            .collection("tajeta")

        referencia
            .add(nuevaTarjeta)
            .addOnSuccessListener {
                categoriaDialogo!!.dismiss()
                cargarTarjetas()

            }.addOnFailureListener {}

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