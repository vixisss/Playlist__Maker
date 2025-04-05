package com.example.playlist__maker.root

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlist__maker.R
import com.example.playlist__maker.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController
        fun setBottomNavigationVisibility(visible: Boolean) {
            binding.bottomNavigationView.visibility = if (visible) View.VISIBLE else View.GONE
        }

        binding.bottomNavigationView.setupWithNavController(navController)

        // Слушаем изменения навигации, чтобы скрывать/показывать BottomNavigationView
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.createPlaylistFragment -> setBottomNavigationVisibility(false) // Скрываем для экрана создания плейлиста
                R.id.playerActivity -> setBottomNavigationVisibility(false) // Скрываем для экрана создания плейлиста
                else -> setBottomNavigationVisibility(true) // Показываем для остальных
            }
        }
    }



}