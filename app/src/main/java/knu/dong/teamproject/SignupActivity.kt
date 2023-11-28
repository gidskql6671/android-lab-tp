package knu.dong.teamproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import knu.dong.teamproject.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignup.isEnabled = false

        val signupFormWatcher = object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                val email = binding.email.text.toString()
                val verifyCode = binding.emailVerify.text.toString()
                val password = binding.password.text.toString()
                val confirmPassword = binding.passwordVerify.text.toString()

                val isAllFieldValid = isValidPassword(email)
                        && isValidVerifyCode(verifyCode)
                        && isValidPassword(password)
                        && isValidConfirmPassword(password, confirmPassword)
                binding.btnSignup.isEnabled = isAllFieldValid
            }
        }
        binding.email.addTextChangedListener(signupFormWatcher)
        binding.emailVerify.addTextChangedListener(signupFormWatcher)
        binding.password.addTextChangedListener(signupFormWatcher)
        binding.passwordVerify.addTextChangedListener(signupFormWatcher)

        binding.btnVerifyEmail.setOnClickListener {
            val email = binding.email.text.toString()
            if (isValidEmail(email)) {
                verifyEmail(email)
                Toast.makeText(this@SignupActivity, "입력하신 이메일로 인증코드가 발송 되었습니다", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this@SignupActivity, "이메일 형식이 올바른지 확인해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSignup.setOnClickListener {
            val email = binding.email.text.toString()
            val verifyCode = binding.emailVerify.text.toString()
            val password = binding.password.text.toString()

            signUp(email, verifyCode, password)
        }

        binding.titleBar.btnBack.setOnClickListener {
            onBackPressed()
        }
    }
    private fun signUp(email: String, verifyCode: String, password: String) {
        // TODO: 추후 회원가입 요청 구현, 현재는 그냥 가입 완료 메시지만
        val result = true
        if (result) {
            Toast.makeText(this@SignupActivity, "성공적으로 회원가입 되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
        else {
            Toast.makeText(this@SignupActivity, "회원가입 요청이 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun isValidEmail(email: String) =
        email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isValidVerifyCode(verifyCode: String) =
        verifyCode.isNotBlank()

    private fun isValidPassword(password: String) =
        password.isNotBlank()

    private fun isValidConfirmPassword(password: String, confirmPassword: String) =
        confirmPassword.isNotBlank() && password == confirmPassword

    private fun verifyEmail(email: String) {
        // TODO: 추후 이메일 인증 구현
    }


}