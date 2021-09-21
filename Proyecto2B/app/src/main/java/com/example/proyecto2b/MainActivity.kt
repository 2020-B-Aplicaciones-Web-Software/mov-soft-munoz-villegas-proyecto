package com.example.proyecto2b

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {
    private val callbackManager= CallbackManager.Factory.create()
    private val GOOGLE_INICIO=200
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val LoginFacebook = findViewById<ImageView>(R.id.iv_loginFacebook)
        val LoginGoogle = findViewById<ImageView>(R.id.iv_loginGoogle)
        val boton = findViewById<Button>(R.id.button2)
        boton.setOnClickListener {
            val intent=Intent(
                this,
                ContenedorFragmentos::class.java
            )
            startActivity(intent)
        }

        LoginFacebook.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
            LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult>{
                override fun onSuccess(result: LoginResult?) {
                    result?.let {
                       val token:AccessToken=it.accessToken
                        val credential:AuthCredential=FacebookAuthProvider.getCredential(token.token)
                        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener{
                            if(it.isSuccessful){


                            }else{
                                showAlert()

                            }
                        }
                    }


                }

                override fun onCancel() {

                }

                override fun onError(error: FacebookException?) {
                    showAlert()

                }
            })

        }
        LoginGoogle.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("98895353922-ie1hnr279nhkr10c4kdmo8j8c3k7dc1h.apps.googleusercontent.com")
                .requestEmail()
                .build()

            val googleClient=GoogleSignIn.getClient(this,googleConf)
            startActivityForResult(googleClient.signInIntent,GOOGLE_INICIO)

        }
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
        callbackManager.onActivityResult(requestCode,resultCode,data)
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==GOOGLE_INICIO) {
            try {

                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {

                            } else {
                                showAlert()
                            }
                        }

                }
            }catch (e:ApiException){
                showAlert()
            }
            }
        }

}