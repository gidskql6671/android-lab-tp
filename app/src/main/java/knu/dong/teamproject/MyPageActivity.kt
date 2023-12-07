package knu.dong.teamproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.edit
import knu.dong.teamproject.common.HttpRequestHelper
import knu.dong.teamproject.databinding.ActivityMyPageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.titleBar.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.titleBar.title.text = "유저 페이지"
        binding.titleBar.btnAccount.visibility = View.INVISIBLE
        binding.btnLogout.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                logout()
            }
        }
    }

    private suspend fun logout() {
        HttpRequestHelper(this).post("api/users/logout")

        getSharedPreferences("user_info", MODE_PRIVATE).edit {
            remove("id")
        }

        val intent: Intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}