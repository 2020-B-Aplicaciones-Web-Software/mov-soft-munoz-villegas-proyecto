package com.example.proyecto2b

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.example.proyecto2b.Dto.FirestoreUsuarioDto

import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {


    val CODIGO_INICIO_SESION = 102
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val Login = findViewById<Button>(R.id.btn_login)


        Login.setOnClickListener {
            llamarLoginUsuario()
        }



    }

    fun llamarLoginUsuario() {
        val providers = arrayListOf(
            //Lista de los provedores
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),

        )
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTosAndPrivacyPolicyUrls(
                    "https://example.com/terms.html",
                    "https://example.com/privacy.html"
                ).setLogo(R.drawable.ic_logo)
                .build(),
            CODIGO_INICIO_SESION

        )

    }
    fun showAlert(){
        val builder=AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando el usuario")
        builder.setPositiveButton("Aceptar",null)
        val dialog:AlertDialog=builder.create()
        dialog.show()
    }
    fun showInicio(){

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CODIGO_INICIO_SESION -> {
                if (resultCode == Activity.RESULT_OK) {
                    val usuario: IdpResponse? = IdpResponse.fromResultIntent(data)
                    if (usuario != null) {
                        if (usuario.isNewUser == true) {
                            Log.i("firebase-login", "Nuevo Usuario")
                            registrarUsuarioPorPrimeraVez(usuario)

                        } else {
                            setearUsuarioFirebase()
                            Log.i("firebase-login", "Usuario Antiguo")
                        }
                    }
                } else {
                    Log.i("firebase-login", "El usuario cancelo")
                }
            }

        }

        }
    fun registrarUsuarioPorPrimeraVez(usuario: IdpResponse) {
        val usuarioLogeado = FirebaseAuth
            .getInstance()
            .getCurrentUser()
        if (usuario.email != null && usuarioLogeado != null) {
            // roles : ["usuario", "admin"]
            // uid

            val db = Firebase.firestore // obtenemos referencia
            val rolesUsuario = arrayListOf("usuario") // creamos el arreglo de permisos
            val nuevoUsuario = hashMapOf<String, Any>( // { roles:... uid:...}
                "roles" to rolesUsuario,
                "uid" to usuarioLogeado.uid,
                "email" to usuario.email.toString()
            )
            val identificadorUsuario = usuario.email
            db.collection("usuario")
                // Forma a) Firestore crea identificador
                //.add(nuevoUsuario)
                // Forma b) Yo seteo el identificador
                .document(identificadorUsuario.toString())
                .set(nuevoUsuario)
                .addOnSuccessListener {
                    Log.i("firebase-firestore", "Se creo el usuario")
                    setearUsuarioFirebase()
                }
                .addOnFailureListener {
                    Log.i("firebase-firestore", "Fallo")
                }
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

    fun setearUsuarioFirebase() {
        val instanciaAuth = FirebaseAuth.getInstance()
        val usuarioLocal = instanciaAuth.currentUser
        if (usuarioLocal != null) {
            if (usuarioLocal.email != null) {
                //bucar en el firestore el usario y traerlo con todos los datos
                val db= Firebase.firestore
                val referencia=db
                    .collection("usuario")
                    .document(usuarioLocal.email.toString())
                referencia
                    .get()
                    .addOnSuccessListener {
                        val usuarioCargado=it.toObject(FirestoreUsuarioDto::class.java)
                        if(usuarioCargado !=null) {
                            AuthUsuario.usuario = UsuarioFirebase(
                                usuarioCargado.uid,
                                usuarioCargado.email,
                                usuarioCargado.roles,

                                )
                            abrirActividad(ContenedorFragmentos::class.java)


                        }
                        Log.i("firebase-firestore", "Usuario Cargado")

                    }
                    .addOnFailureListener{
                        Log.i("firebase-firestore", "Fallo cargar usuario")

                    }
            }
        }
    }

}