package ru.mirea.tsybulko.lesson5

import android.R
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.SimpleAdapter
import androidx.appcompat.app.AppCompatActivity
import ru.mirea.tsybulko.lesson5.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensorsList = sensorManager.getSensorList(Sensor.TYPE_ALL)
        val pressureSensorList = sensorManager.getSensorList(Sensor.TYPE_PRESSURE)

        val listView = binding.sensorList

        val formattedSensorsList: ArrayList<HashMap<String, Any?>> = ArrayList()
        for (i in 0 until sensorsList.size) {
            val sensorTypeList: HashMap<String, Any?> = HashMap()
            sensorTypeList["Name"] = sensorsList[i].name
            sensorTypeList["Value"] = sensorsList[i].maximumRange
            formattedSensorsList.add(sensorTypeList)
        }

        val mHistory = SimpleAdapter(
            this,
            formattedSensorsList,
            R.layout.simple_list_item_2,
            arrayOf("Name", "Value"),
            intArrayOf(
                R.id.text1, R.id.text2
            )
        )
        listView.adapter = mHistory
    }
}