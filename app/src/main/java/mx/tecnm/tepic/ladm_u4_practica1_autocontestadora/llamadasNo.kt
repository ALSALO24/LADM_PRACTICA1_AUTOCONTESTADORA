package mx.tecnm.tepic.ladm_u4_practica1_autocontestadora

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.PhoneStateListener
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class llamadasNo:  BroadcastReceiver() {
    val baseRemota = FirebaseFirestore.getInstance()
    var telephonyManager: TelephonyManager? = null
    var status = false
    var contacto = ""


    override fun onReceive(context: Context, intent: Intent?) {

        telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val listenerTelefono: PhoneStateListener = object : PhoneStateListener() {
                
            override fun onCallStateChanged(state: Int, phoneNumber: String) {
                super.onCallStateChanged(state, phoneNumber)
                status = false

                when (state) {
                    TelephonyManager.CALL_STATE_RINGING -> {


                        baseRemota.collection("listaNegra")
                            .whereEqualTo("telefono", phoneNumber)
                            .get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    contacto = document.getString("nombre")!!
                                }
                                SmsManager.getDefault().sendTextMessage(
                                    phoneNumber,
                                    null,
                                    "NO ME LLAMES POR FAVOR NO INCISTAS",
                                    null,
                                    null
                                )
                                Toast.makeText(context, "Se envio en SMS", Toast.LENGTH_LONG).show()



                            }

                    }
                }
            }
        }

        if(!isLitening) {
            telephonyManager!!.listen(listenerTelefono, PhoneStateListener.LISTEN_CALL_STATE)
            isLitening = true
        }
    }
    companion object{
        var isLitening = false
    }

}