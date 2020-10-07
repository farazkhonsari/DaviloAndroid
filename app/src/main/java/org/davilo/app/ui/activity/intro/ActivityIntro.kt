package org.davilo.app.ui.activity.intro

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import org.davilo.app.R
import org.davilo.app.databinding.ActivityIntroBinding
import org.davilo.app.ui.activity.login.ActivityLogin
import org.davilo.app.ui.activity.register.ActivityRegister
import org.davilo.app.ui.base.BaseActivity

@AndroidEntryPoint
class ActivityIntro : BaseActivity<ActivityIntroBinding>(), View.OnClickListener {

    override fun getLayoutId(): Int = R.layout.activity_intro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

    }

    private fun initViews() {
        binding.login.setOnClickListener(this)
        binding.register.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.login -> {
                ActivityLogin.navigate(this)
            }
            R.id.register -> {
                ActivityRegister.navigate(this)
            }
        }
    }

    companion object {

        fun navigate(context: Context?) {
            context?.startActivity(Intent(context, ActivityIntro::class.java))
        }
    }
}

