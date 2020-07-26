package com.alanford.carpediem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alanford.carpediem.data.QuoteRepository
import com.alanford.carpediem.quotelist.QuoteListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        QuoteRepository.initialize(this)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if(currentFragment == null) {
            val fragment = QuoteListFragment.newInstance()
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit()
        }
    }
}
