package com.example.sensor_luz_ambiental

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.mikhaellopez.circularprogressbar.CircularProgressBar

class MainActivity : AppCompatActivity(), SensorEventListener {
    //Declaracion de variables
    private lateinit var sensorManager: SensorManager
    private var brillo: Sensor? = null
    private lateinit var texto: TextView
    private lateinit var pb: CircularProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Desactivar el modo oscuro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        //Inicializar las vistas
        texto = findViewById(R.id.tv_text)
        pb = findViewById(R.id.circularProgressBar)

        //Configurar el sensor y sus funciones
        setUpSensorStuff()
    }

    private fun setUpSensorStuff() {
        //Obtener el servicio del SensorManager
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        //Obtener el sensor de la luz ambiental
        brillo = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        //Verifcar si el evento proviene del sensor de brillo
        if(event?.sensor?.type== Sensor.TYPE_LIGHT){
            val luz = event.values[0]

            //Actualizar el texto con la intensidad de luz y la descripcion del brillo
            texto.text="Intensidad de Luz:\n$luz\n\n${obtenerBrillo(luz)}"

            //Actualizar el progreso de la barra circular con la cantidad de luz
            pb.setProgressWithAnimation(luz)
        }
    }

    private fun obtenerBrillo(brillo: Float): String{
        return when(brillo.toInt()){
            0 -> "Oscuridad Total"
            in 1..10->"Oscuro"
            in 11..50->"Poco Oscuro"
            in 51..1000->"Normal"
            in 1001..5000->"Increiblemente brillante"
            else-> "Esta luz te cegará"
        }
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    override fun onResume() {
        super.onResume()
        //Registrar un escuchador para el sensor de brillo con una frecuencia normal
        sensorManager.registerListener(this, brillo, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        //Detener la escucha del sensor de brillo al pausar la actividad
        sensorManager.unregisterListener(this)
    }

}