package mx.tecnm.tepic.ladm_u4_practica1_autocontestadora

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var status = ""
    var baseRemota = FirebaseFirestore.getInstance()


    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        activarPorDefecto()


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val PERMISSION_ALL = 1
        val PERMISSIONS = arrayOf(
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.SEND_SMS
        )

        if (!hasPermissions(this, *PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
        }

        negra.setOnClickListener {
            val intent1 = Intent(applicationContext, listaNegra::class.java).apply {}
            startActivity(intent1)
        }//negra
        blanca.setOnClickListener {
            val intent2 = Intent(applicationContext, listaBlanca::class.java).apply {}
            startActivity(intent2)
        }//blanca

        activar.setOnClickListener{

               baseRemota.collection("estado").document("qBCglZ9ryiIrcDNjBN0f")
                    .update("status","1").addOnSuccessListener {
                        textView.setBackgroundColor(R.color.green)
                       textView.setText("ACTIVADO")
                   }


            Toast.makeText(this,"¡SE ACTIVO!" , Toast.LENGTH_LONG).show()


        }
        desactivar.setOnClickListener{

            baseRemota.collection("estado").document("qBCglZ9ryiIrcDNjBN0f")
                .update("status","0").addOnSuccessListener {
                    textView.setBackgroundColor(R.color.red)
                    textView.setText("DESACTIVADO")
                }


            Toast.makeText(this, "¡SE DESACTIVO!",Toast.LENGTH_LONG).show()
        }
    }
    fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    fun activarPorDefecto(){
        baseRemota.collection("estado").document("qBCglZ9ryiIrcDNjBN0f")
            .update("status","1")
    }

}