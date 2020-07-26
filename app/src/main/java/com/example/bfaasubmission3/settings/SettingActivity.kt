package com.example.bfaasubmission3.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TimePicker
import com.example.bfaasubmission3.R
import java.text.SimpleDateFormat
import java.util.*

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        supportFragmentManager.beginTransaction().add(R.id.setting_holder, MyPreferenceFragment()).commit()
    }
}