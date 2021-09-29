package com.example.proyecto2b

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CuentraRegresivaFragment : Fragment(R.layout.fragment_cuentra_regresiva) {
    var estaContando = false
    var debeFinalizar = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_cuentra_regresiva, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val paquete = requireArguments()
        val minutosT = paquete.getInt("minutosTrabajo")
        val segundosT = paquete.getInt("segundosTrabajo")
        val minutosD = paquete.getInt("minutosDescanso")
        val segundosD = paquete.getInt("segundosDescanso")
        val tiempoDeTrabajo = minutosT.times(60).plus(segundosT).toLong()
        val tiempoDeDescanso = minutosD.times(60).plus(segundosD).toLong()
        iniciarTemportizador(tiempoDeTrabajo,tiempoDeDescanso,view)
        val botonReiniciar = view.findViewById<FloatingActionButton>(R.id.btn_detener_temp)
        botonReiniciar.setOnClickListener {
            if(!estaContando){
                iniciarTemportizador(tiempoDeTrabajo,tiempoDeDescanso,view)
            }
        }
        val botonRegresar = view.findViewById<ImageButton>(R.id.btn_eliminar_temp)
        botonRegresar.setOnClickListener{
            debeFinalizar = true
            setFragmentResult("retorno", bundleOf())
        }
    }

    fun iniciarTemportizador(tiempoDeTrabajo:Long, tiempoDeDescanso:Long,view: View){
        val tvMarcador = view.findViewById<TextView>(R.id.tv_marcador_tiempo)
        val tvTrabajo = view.findViewById<TextView>(R.id.tv_trabajo)
        val tvDescanso = view.findViewById<TextView>(R.id.tv_descanso)
        tvTrabajo.setTextColor(resources.getColor(R.color.white,null))
        tvTrabajo.setBackgroundResource(R.drawable.estilo_tv_background)
        tvTrabajo.setPadding(25,15,25,15)
        object : CountDownTimer(tiempoDeTrabajo * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (debeFinalizar) {
                    this.cancel()
                } else {
                    estaContando = true
                    val minutosRestantes = (millisUntilFinished / (60 * 1000)).toInt()
                    val segundosRestantes = (millisUntilFinished / 1000).mod(60)
                    tvMarcador.setText(
                        "${
                            String.format(
                                "%02d",
                                minutosRestantes
                            )
                        }:${String.format("%02d", segundosRestantes)}"
                    )
                }
            }

            override fun onFinish() {
                if (debeFinalizar) {
                    this.cancel()
                } else {
                    tvTrabajo.setTextColor(resources.getColor(R.color.black, null))
                    tvTrabajo.setBackgroundResource(R.drawable.estilo_tv_background_white)
                    tvDescanso.setTextColor(resources.getColor(R.color.white, null))
                    tvDescanso.setBackgroundResource(R.drawable.estilo_tv_background)
                    tvDescanso.setPadding(25, 15, 25, 15)
                    object : CountDownTimer(tiempoDeDescanso * 1000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            if (debeFinalizar) {
                                this.cancel()
                            } else {
                                estaContando = true
                                val minutosRestantes = (millisUntilFinished / (60 * 1000)).toInt()
                                val segundosRestantes = (millisUntilFinished / 1000).mod(60)
                                tvMarcador.setText(
                                    "${
                                        String.format(
                                            "%02d",
                                            minutosRestantes
                                        )
                                    }:${String.format("%02d", segundosRestantes)}"
                                )
                            }
                        }

                        override fun onFinish() {
                            if (debeFinalizar) {
                                this.cancel()
                            } else {
                                tvMarcador.setText("00:00")
                                tvDescanso.setTextColor(resources.getColor(R.color.black, null))
                                tvDescanso.setBackgroundResource(R.drawable.estilo_tv_background_white)
                                estaContando = false
                            }
                        }
                    }.start()
                }
            }
        }.start()
    }
}