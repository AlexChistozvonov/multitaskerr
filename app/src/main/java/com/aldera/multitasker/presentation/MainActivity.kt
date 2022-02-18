package com.aldera.multitasker.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aldera.multitasker.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Multitasker)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }


}
