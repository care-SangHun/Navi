package kr.care_pet.service.app.navi.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import kr.care_pet.service.app.navi.R
import kr.care_pet.service.app.navi.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }
    private val PICK_IMAGE_REQUEST = 1

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) { binding.signupIvProfile.setImageURI(uri) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.signupTvMale.setOnClickListener { genderClick(it) }
        binding.signupTvFemale.setOnClickListener { genderClick(it) }
        binding.signupFab.setOnClickListener { fabClick() }
        binding.signupTvCate.setOnClickListener { cateClick() }
    }

    private fun cateClick() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Alert")
            .setMessage("You clicked the text!")
            .setPositiveButton("OK") { dialog, which ->
                // OK 버튼 클릭 시 처리할 로직을 작성합니다.
            }
            .setNegativeButton("Cancel") { dialog, which ->
                // Cancel 버튼 클릭 시 처리할 로직을 작성합니다.
            }
            .create()

        alertDialog.show()
    }

    private fun genderClick(view:View){
        if (view.id == R.id.signup_tv_male){
            view.setBackgroundResource(R.drawable.selector_signup_text_press)
            binding.signupTvFemale.setBackgroundResource(R.drawable.selector_signup_text_normal)
        }else{
            view.setBackgroundResource(R.drawable.selector_signup_text_press)
            binding.signupTvMale.setBackgroundResource(R.drawable.selector_signup_text_normal)
        }
    }

    private fun fabClick(){
        galleryLauncher.launch("image/*")
    }
}