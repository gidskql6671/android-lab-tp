package knu.dong.teamproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import knu.dong.teamproject.databinding.ActivityChatsBinding

class ChatsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityChatsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}