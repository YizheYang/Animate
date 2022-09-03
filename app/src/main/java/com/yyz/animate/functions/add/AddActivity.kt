package com.yyz.animate.functions.add

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.yyz.animate.R
import com.yyz.animate.base.BaseActivity2
import com.yyz.animate.databinding.ActivityAddBinding

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.7.31 下午 10:16
 * version 1.0
 * update none
 **/
class AddActivity : BaseActivity2() {
    companion object {
        @JvmStatic
        fun add(context: Context) {
            val intent = Intent(context, AddActivity::class.java)
            context.startActivity(intent)
        }

        @JvmStatic
        fun edit(context: Context, id: Int) {
            val intent = Intent(context, AddActivity::class.java)
            val bundle = Bundle()
            bundle.putInt("id", id)
            intent.putExtra("bundle", bundle)
            context.startActivity(intent)
        }

        @JvmStatic
        fun add(context: Context, launcher: ActivityResultLauncher<Intent>) {
            val intent = Intent(context, AddActivity::class.java)
            launcher.launch(intent)
        }

        @JvmStatic
        fun edit(context: Context, id: Int, launcher: ActivityResultLauncher<Intent>) {
            val intent = Intent(context, AddActivity::class.java)
            val bundle = Bundle()
            bundle.putInt("id", id)
            intent.putExtra("bundle", bundle)
            launcher.launch(intent)
        }

        const val ADD_MODE = 0
        const val EDIT_MODE = 1
    }

//    override fun getLayoutId() = R.layout.activity_add

    private lateinit var vm: AddViewModel
    private lateinit var binding: ActivityAddBinding

    override fun initViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add)
        binding.lifecycleOwner = this
        vm = ViewModelProvider(
            this, AddVIewModelFactory(
                this,
                intent.getBundleExtra("bundle")?.getInt("id") ?: -1
            )
        )[AddViewModel::class.java]
        binding.vm = vm
        binding.nsAddType.attachDataSource(vm.typeList)
        binding.nsAddState.attachDataSource(vm.stateList)
    }

    override fun initListener() {}
}