package kr.carepet.service.app.navi.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kr.carepet.service.app.navi.R

class LoginActivity : AppCompatActivity() {

    private lateinit var toSignUp: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        toSignUp = findViewById(R.id.intro_btn_signup)

        toSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}