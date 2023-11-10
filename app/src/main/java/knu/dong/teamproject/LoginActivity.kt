package knu.dong.teamproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import knu.dong.teamproject.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textSignup.setOnClickListener {
            val intent = Intent(this, SignupgActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            //로그인 로직 없이 바로 성공
            val intent = Intent(this, SelectChatbotActivity::class.java)
            startActivity(intent)
        }

        initTextWatcher()
    }

    private fun initTextWatcher() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val email = binding.editEmail.text.toString()
                val password = binding.editPassword.text.toString()
                binding.btnLogin.isEnabled = validateEmail(email) && validatePassword(password)
            }
        }
        binding.editEmail.addTextChangedListener(textWatcher)
        binding.editPassword.addTextChangedListener(textWatcher)
    }

    private fun validatePassword(password: String): Boolean {
        return password.isNotBlank();
    }

    private fun validateEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}