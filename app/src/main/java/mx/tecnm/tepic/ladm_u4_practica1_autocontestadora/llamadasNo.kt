package mx.tecnm.tepic.ladm_u4_practica1_autocontestadora

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.PhoneStateListener
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

private var telephonyState = false

class llamadasNo:  BroadcastReceiver() {
    val db = FirebaseFirestore.getInstance()
    val db2 = FirebaseFirestore.getInstance()
    var listaNegra = ArrayList<String>()


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
            db.collection("listaNegra").addSnapshotListener { value, error ->
                if(error !=null)
                {
                    return@addSnapshotListener
                }
                listaNegra.clear()

                for (i in value!!){
                    val num = i.get("telefono")
                    listaNegra.add(num.toString())
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

                            if (listaNegra.contains(numero.toString())) {
                                val SMS = SmsManager.getDefault()
                                Toast.makeText(context, "ENVIANDO MENSAJE...", Toast.LENGTH_LONG)
                                    .show()
                                SMS.sendTextMessage(
                                    numero,
                                    null,
                                    "PORFA NO ME LLAMES, BRO.",
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
