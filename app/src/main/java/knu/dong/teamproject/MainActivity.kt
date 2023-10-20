package knu.dong.teamproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import knu.dong.teamproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}