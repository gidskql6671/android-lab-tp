package knu.dong.teamproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import knu.dong.teamproject.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}