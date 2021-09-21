package com.example.proyecto2b

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace

class TemporizadorFragment : Fragment(R.layout.fragment_temporizador) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        childFragmentManager.setFragmentResultListener("valoresTemporizador",this){ key, bundle ->
            childFragmentManager.commit {
                setReorderingAllowed(true)
                replace<CuentraRegresivaFragment>(R.id.fl_tempContainer,args = bundle)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(savedInstanceState == null){
            childFragmentManager.commit {
                setReorderingAllowed(true)
                add<DatosTemporizador>(R.id.fl_tempContainer)
            }
        }
        val viewRoot = inflater.inflate(R.layout.fragment_temporizador,container,false)
        return viewRoot
    }
}