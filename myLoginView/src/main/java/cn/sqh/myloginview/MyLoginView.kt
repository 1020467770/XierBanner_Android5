package cn.sqh.myloginview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.login_layout.view.*

class MyLoginView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    lateinit var mOnLoginClickListener: OnLoginClickListener
    lateinit var usernameLayout: TextInputLayout
    lateinit var passwordLayout: TextInputLayout
    lateinit var usernameEdit: TextInputEditText
    lateinit var passwordEdit: TextInputEditText
    lateinit var loginBtn: ImageButton

    init {
        LayoutInflater.from(context).inflate(R.layout.login_layout, this)
//        usernameLayout = sr_name
//        passwordLayout = sr_password
//        usernameEdit = et_username
//        passwordEdit = et_password
        usernameLayout = findViewById(R.id.sr_name)
        passwordLayout = findViewById(R.id.sr_password)
        usernameEdit = findViewById(R.id.et_username)
        passwordEdit = findViewById(R.id.et_password)
        loginBtn = findViewById(R.id.loginBtn)
    }
    fun setCornerRadius(leftTop: Float, leftBottom: Float, rightTop: Float, rightBottom: Float) {
        usernameLayout.setBoxCornerRadii(leftTop, rightTop, leftBottom, rightBottom)
        passwordLayout.setBoxCornerRadii(leftTop, rightTop, leftBottom, rightBottom)
    }

    fun setBoxBackgroundMode(boxBackgroundMode: Int) {
        usernameLayout.boxBackgroundMode = boxBackgroundMode
        passwordLayout.boxBackgroundMode = boxBackgroundMode
    }

    fun getPasswordToggleEnabled() = passwordLayout.isPasswordVisibilityToggleEnabled

    fun setOnLoginClickListener(onLoginClickListener: OnLoginClickListener) {
        loginBtn.setOnClickListener(onLoginClickListener)
    }

    interface OnLoginClickListener: View.OnClickListener{
    }
}