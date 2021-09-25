package com.example.proyecto2b

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto2b.Dto.FirestoreTarea
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ToDoFragment : Fragment(R.layout.fragment_to_do) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_to_do, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val botonNuevo = view.findViewById<FloatingActionButton>(R.id.btn_nueva_tarea)
        val recyclerTareas = view.findViewById<RecyclerView>(R.id.rv_lista_tareas)
        val db = Firebase.firestore
        val referencia = db
            .collection("usuario").document(AuthUsuario.usuario!!.email)
            .collection("tarea")
        referencia.orderBy("fechaEntrega").get()
            .addOnSuccessListener {
                var arregloTarea = ArrayList(it.map {
                    var tarea = it.toObject<FirestoreTarea>()
                    tarea.uid = it.id
                    return@map tarea
                })
                if(arregloTarea == null){
                    arregloTarea = arrayListOf<FirestoreTarea>()
                }

                val adaptador = iniciarRecyclerView(arregloTarea,recyclerTareas)
                botonNuevo.setOnClickListener {
                    val dialogo = DialogoTarea(
                        algoritmo = EstrategiaNuevaTarea(),
                        adaptador = adaptador
                    )
                    dialogo.show(childFragmentManager,"nuevaTarea")
                }
                //--esto es de la eliminacion
                val desplizamiento=object:DeslizarParaEliminarTarea(this.requireContext()){
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        when(direction){
                            ItemTouchHelper.LEFT->{
                                adaptador.eliminarFireStore(arregloTarea[viewHolder.adapterPosition].uid)

                            }
                        }
                    }
                }
                val touchHelper=ItemTouchHelper(desplizamiento)
                touchHelper.attachToRecyclerView(recyclerTareas)
                //--------------------


                adaptador.setOnClickListener(
                    object :View.OnClickListener{
                        override fun onClick(v: View) {
                            val elemento = recyclerTareas.getChildAdapterPosition(v)
                            DialogoTarea(arregloTarea.get(elemento)
                                ,EstrategiaAntiguaTarea(),
                                adaptador).show(childFragmentManager,"edicionTarea")
                        }

                    }
                )
            }
    }

    fun iniciarRecyclerView(listaTareas:ArrayList<FirestoreTarea>, recycler:RecyclerView):AdaptadorToDo{
        val adaptador = AdaptadorToDo(listaTareas,childFragmentManager)
        recycler.adapter = adaptador
        recycler.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        recycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        adaptador.notifyDataSetChanged()
        return adaptador
    }
}