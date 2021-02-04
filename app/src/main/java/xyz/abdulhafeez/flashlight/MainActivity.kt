package xyz.abdulhafeez.flashlight

import android.Manifest
import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener


class MainActivity : AppCompatActivity() {
    lateinit var torchButton:ImageButton
    var state=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        torchButton=findViewById(R.id.imageButton)
        Dexter.withContext(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                @RequiresApi(Build.VERSION_CODES.M)
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    runFlashLight()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Toast.makeText(this@MainActivity, "Permission Requerd", Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                }
            }).check()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun runFlashLight() {
        torchButton.setOnClickListener {
            if (state){
                val camM=getSystemService(Context.CAMERA_SERVICE) as CameraManager
                try {
                    val camId=camM.cameraIdList[0]
                    camM.setTorchMode(camId,false)
                    state=false
                    torchButton.setImageResource(R.drawable.torch_off)
                }catch (e:CameraAccessException){

                }
            }else{
                val camM=getSystemService(Context.CAMERA_SERVICE) as CameraManager
                try {
                    val camId=camM.cameraIdList[0]
                    camM.setTorchMode(camId,true)
                    state=true
                    torchButton.setImageResource(R.drawable.torch_on)
                }catch (e:CameraAccessException){

                }
            }
        }
    }
}