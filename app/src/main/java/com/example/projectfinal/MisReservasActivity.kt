package com.example.projectfinal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectfinal.databinding.ActivityIniciarBinding
import com.example.projectfinal.databinding.ActivityMisReservasBinding

class MisReservasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMisReservasBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMisReservasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvReservas.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
}