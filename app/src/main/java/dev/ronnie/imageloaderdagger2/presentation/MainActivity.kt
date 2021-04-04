package dev.ronnie.imageloaderdagger2.presentation

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import dev.ronnie.imageloaderdagger2.R
import dev.ronnie.imageloaderdagger2.databinding.ActivityMainBinding

class MainActivity : DaggerAppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_ImageLoaderDagger2)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}