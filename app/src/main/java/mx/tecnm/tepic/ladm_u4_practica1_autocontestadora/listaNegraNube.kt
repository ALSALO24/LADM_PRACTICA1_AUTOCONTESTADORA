package mx.tecnm.tepic.ladm_u4_practica1_autocontestadora

import android.R
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore

class listaNegraNube(p: Context) {

    var nombre = ""
    var telefono = ""
    var baseRemota = FirebaseFirestore.getInstance()
    var pnt = p

    fun insertar() {
        var datosInsertar = hashMapOf(
            "nombre" to nombre.toString(),
            "telefono" to telefono.toString()
        )
        baseRemota.collection("listaNegra")
            .add(datosInsertar as Any)
            .addOnSuccessListener {
                AlertDialog.Builder(pnt).setMessage("Se ha realizado con exito la insercion")
                    .setPositiveButton("ACEPTAR "){d,i-> d.cancel()}
                    .show()
            }
            .addOnFailureListener{
                AlertDialog.Builder(pnt).setMessage("ERROR: ${it.message!!}")
                    .setPositiveButton("ACEPTAR "){d,i-> d.cancel()}
                    .show()
            }
    }//insertar

    fun borrar(idElegido:String){
        baseRemota.collection("listaNegra")
            .document(idElegido)
            .delete()
            .addOnSuccessListener {
                alerta("SE ELIMINO CON EXITO")
            }
            .addOnFailureListener {
                alerta("ERROR: ${it.message!!}")
            }
    }//borrar

    private fun alerta(s: String) {
        android.app.AlertDialog.Builder(pnt).setMessage(s)
            .setPositiveButton("ACEPTAR "){d,i-> d.cancel()}
            .show()
    }//alerta
}