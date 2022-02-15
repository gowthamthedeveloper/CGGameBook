package com.impeccthreads.cggamebook.activity

import android.os.Bundle
import android.support.multidex.BuildConfig
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.impeccthreads.cggamebook.R
import com.impeccthreads.cggamebook.adapters.SettingAdapter
import com.impeccthreads.cggamebook.application.BaseActivity
import com.impeccthreads.cggamebook.application.Constants
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity(), SettingAdapter.SettingAdapterListener {

    var settingsArrayList: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_settings)
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        settingsArrayList.add("Game Rules")
        settingsArrayList.add("Game History")
        settingsArrayList.add("Version: ${Constants.appVersionName}")

        listViewSettings.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        listViewSettings.adapter = SettingAdapter(this, settingsArrayList, this)
    }

    fun btnBackOnClick(view: View) {
        finish()
    }

    //Recycler Listener
    override fun didSelectViewAtPosition(position: Int) {

    }
}

