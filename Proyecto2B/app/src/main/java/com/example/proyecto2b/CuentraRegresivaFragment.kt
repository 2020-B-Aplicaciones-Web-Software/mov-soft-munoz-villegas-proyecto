package com.example.proyecto2b

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class CuentraRegresivaFragment : Fragment(R.layout.fragment_cuentra_regresiva) {
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
        val tvMarcador = view.findViewById<TextView>(R.id.tv_marcador_tiempo)
        val paquete = requireArguments()
        val minutos = paquete.getInt("minutosTrabajo")
        object : CountDownTimer(minutos.toLong()*60*1000,1000){
            override fun onTick(millisUntilFinished: Long) {
                val minutosRestantes = (millisUntilFinished/(60*1000)).toInt()
                val segundosRestantes = (millisUntilFinished/1000).mod(60)
                tvMarcador.setText("${String.format("%02d",minutosRestantes)}:${String.format("%02d",segundosRestantes)}")
            }

            override fun onFinish() {
                tvMarcador.setText("00:00")
            }
        }.start()
    }
}