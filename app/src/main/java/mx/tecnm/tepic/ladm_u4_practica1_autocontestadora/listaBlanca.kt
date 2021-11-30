package mx.tecnm.tepic.ladm_u4_practica1_autocontestadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_lista_blanca.*
import kotlinx.android.synthetic.main.activity_lista_negra.*
import kotlinx.android.synthetic.main.activity_lista_negra.agregar
import kotlinx.android.synthetic.main.activity_lista_negra.regresar

class listaBlanca : AppCompatActivity() {
    var listaID = ArrayList<String>()
    val agregarLista = listaBlancaNube(this)
    var baseRemota = FirebaseFirestore.getInstance()
    var datalista = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_blanca)

        agregar.setOnClickListener {
            val nombre = EditText(this)
            val telefono = EditText(this)
            nombre.inputType = InputType.TYPE_CLASS_TEXT
            telefono.inputType = InputType.TYPE_CLASS_NUMBER
            AlertDialog.Builder(this)
                .setTitle("Agregar")
                .setMessage("Escriba Nombre")
                .setView(nombre)
                .setPositiveButton("AGREGAR") { d, i ->
                    AlertDialog.Builder(this)
                        .setTitle("Agregar")
                        .setMessage("Escriba Telefono")
                        .setView(telefono)
                        .setPositiveButton("AGREGAR") { d, i ->
                            agregarLista.nombre = nombre.text.toString()
                            agregarLista.telefono = telefono.text.toString()
                            agregarLista.insertar()
                            consultar()
                        }.setNegativeButton("CANCELAR") { d, i ->
                            d.cancel()
                        }.show()
                }
                .setNegativeButton("CANCELAR") { d, i ->
                    d.cancel()
                }
                .show()
        }//agregar

        regresar.setOnClickListener {
            finish()
        }

        verListaBlanca.setOnClickListener {
            consultar()
        }//verListaBlanca
    }

    private fun consultar() {
        baseRemota.collection("listaBlanca")
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    //mensaje(error.message!!)
                    return@addSnapshotListener
                }//if
                datalista.clear()
                listaID.clear()
                for (document in querySnapshot!!) {
                    var cadena = "${document.getString("nombre")} -- ${document.get("telefono")}"
                    datalista.add(cadena)
                    listaID.add(document.id.toString())
                }
                listaBlancaView.adapter = ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_expandable_list_item_1,
                    datalista
                )
                listaBlancaView.setOnItemClickListener { adapterView, view, posicion, l ->
                    eliminar(posicion)
                }
            }//.addSnapshow

    }//consultar

    private fun eliminar(posicion: Int) {
        var idElegido = listaID.get(posicion)
        AlertDialog.Builder(this).setTitle("ATENCION")
            .setMessage("ESTA SEGURO QUE DESEA ELIMINAR \n${datalista.get(posicion)}?")
            .setPositiveButton("ELIMINAR") { d, i ->
                agregarLista.borrar(idElegido)
            }
            .show()
    }//eliminar
}