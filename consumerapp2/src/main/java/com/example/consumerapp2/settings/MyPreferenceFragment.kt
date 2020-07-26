package com.example.consumerapp2.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.consumerapp2.R

class MyPreferenceFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var condition: String
    private lateinit var isActive: SwitchPreference
    private lateinit var remainderReceiver: RemainderReceiver

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.setting_preference)
        init()
        setSummary()
        remainderReceiver = RemainderReceiver()

        if (isActive.isChecked){
            remainderReceiver = RemainderReceiver()
            context?.let { remainderReceiver.setRemainder(it, "09:00", "See more awesome people in Github") }
            Toast.makeText(context, "Remainder actives", Toast.LENGTH_SHORT).show()
        } else{
            context?.let { remainderReceiver.cancelAlarm(it) }
            Toast.makeText(context, "Remainder inactive", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String){
        if (key == condition){
            isActive.isEnabled = sharedPreferences.getBoolean(condition, false)
        }
    }

    private fun init(){
        condition = resources.getString(R.string.key_switch)
        isActive = findPreference<SwitchPreference>(condition) as SwitchPreference
    }

    private fun setSummary(){
        val sh = preferenceManager.sharedPreferences
        isActive.isChecked = sh.getBoolean(condition, false)
    }
}
