package com.impeccthreads.cggamebook.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.impeccthreads.cggamebook.R
import com.impeccthreads.cggamebook.application.BaseActivity
import java.util.*
import kotlin.concurrent.schedule

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash)

        configureView()
    }

    fun configureView() {

        Timer("SettingUp", false).schedule(1000) {
            startCardGameActivity()
        }
    }

    fun startCardGameActivity() {
        val intent = Intent(this, CardGameActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
    }

}
