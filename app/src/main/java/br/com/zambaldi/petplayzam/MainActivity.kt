package br.com.zambaldi.petplayzam

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import androidx.annotation.RequiresApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import br.com.zambaldi.petplayzam.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SensorEventListener {

    lateinit var binding: ActivityMainBinding
    private var sensorManager: SensorManager? = null
    private var accelerometerSensor: Sensor? = null
    private var isAccelerometerSensorAvailable = false
    private var currentEventX = 0.0f
    private var currentEventY = 0.0f
    private var currentEventZ = 0.0f
    private var lastEventX = 0.0f
    private var lastEventY = 0.0f
    private var lastEventZ = 0.0f
    private var difEventX = 0.0f
    private var difEventY = 0.0f
    private var difEventZ = 0.0f
    private var shakeThreshold = 5.0f
    private var vibrator: Vibrator? = null
    var isShakeDetected = false
    private var isFirstTime = false

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityCompat.requestPermissions(this, arrayOf(
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.READ_MEDIA_AUDIO,
            android.Manifest.permission.READ_MEDIA_VIDEO,
            android.Manifest.permission.ACCESS_MEDIA_LOCATION,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.VIBRATE,
            ), 0)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_audios, R.id.navigation_play ,R.id.navigation_groups
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if(sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometerSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            isAccelerometerSensorAvailable = true
        } else {
            isAccelerometerSensorAvailable = false
        }

    }

    override fun onSensorChanged(event: SensorEvent?) {
        currentEventX = event?.values?.get(0) ?: 0.0f
        currentEventY = event?.values?.get(1) ?: 0.0f
        currentEventZ = event?.values?.get(2) ?: 0.0f

        if(!isFirstTime) {
            difEventX = Math.abs(lastEventX - currentEventX)
            difEventY = Math.abs(lastEventY - currentEventY)
            difEventZ = Math.abs(lastEventZ - currentEventZ)

            if(difEventX > shakeThreshold && difEventY > shakeThreshold ||
                    difEventX > shakeThreshold && difEventZ > shakeThreshold ||
                difEventY > shakeThreshold && difEventZ > shakeThreshold)
            {

//                vibrator?.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
                isShakeDetected = true

            }

        }

        lastEventX = currentEventX
        lastEventY = currentEventY
        lastEventZ = currentEventZ
        isFirstTime = false

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onResume() {
        super.onResume()
        if(isAccelerometerSensorAvailable) {
            sensorManager!!.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        if(isAccelerometerSensorAvailable) {
            sensorManager!!.unregisterListener(this)
        }
    }

}