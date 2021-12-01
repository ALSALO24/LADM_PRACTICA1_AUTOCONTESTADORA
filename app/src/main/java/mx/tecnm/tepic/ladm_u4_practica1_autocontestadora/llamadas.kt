package mx.tecnm.tepic.ladm_u4_practica1_autocontestadora

import android.R
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telecom.TelecomManager
import android.telephony.PhoneStateListener
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_lista_blanca.*
import kotlinx.android.synthetic.main.activity_lista_negra.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
private var telephonyState = false


class llamadas:  BroadcastReceiver() {
    val db = FirebaseFirestore.getInstance()
    val db2 = FirebaseFirestore.getInstance()
    var listaBlanca = ArrayList<String>()



    override fun onReceive(context: Context?, intent: Intent?) {

        if(intent!!.getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_RINGING){
            telephonyState = true
        }

        if(intent.getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_OFFHOOK){
            telephonyState = false
        }

        if(telephonyState && intent.getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_IDLE){
            val numero = intent.extras?.getString("incoming_number")
            var status = ""

            db.collection("listaBlanca").addSnapshotListener { value, error ->
                if(error !=null)
                {
                    return@addSnapshotListener
                }
                listaBlanca.clear()

                for (i in value!!){
                    val num = i.get("telefono")
                    listaBlanca.add(num.toString())
                }

                db2.collection("estado")
                    .addSnapshotListener { querySnapshot, error ->
                        if (error != null) {
                            //mensaje(error.message!!)
                            return@addSnapshotListener
                        }//if

                        for (document in querySnapshot!!) {
                            status = "${document.get("status")}"
                        }

                        if (status.equals("1")) {


                            if (listaBlanca.contains(numero.toString())) {
                                val SMS = SmsManager.getDefault()
                                Toast.makeText(context, "ENVIANDO MENSAJE...", Toast.LENGTH_LONG)
                                    .show()
                                SMS.sendTextMessage(
                                    numero,
                                    null,
                                    "TE LLAMO MAS TARDE AMIGO.",
                                    null,
                                    null
                                )

                                Toast.makeText(context, "¡MENSAJE ENVIADO!", Toast.LENGTH_LONG)
                                    .show()
                            }


                        }
                    }
            }
        }

    }

}