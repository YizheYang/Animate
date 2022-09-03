package com.yyz.animate.functions.info

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.yyz.animate.R
import com.yyz.animate.base.BaseActivity2
import com.yyz.animate.databinding.ActivityInfoBinding
import com.yyz.animate.utils.DateConverter

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.7.31 下午 10:17
 * version 1.0
 * update none
 **/
class InfoActivity : BaseActivity2() {

    companion object {
        @JvmStatic
        fun start(context: Context, id: Int) {
            val intent = Intent(context, InfoActivity::class.java)
            val bundle = Bundle()
            bundle.putInt("id", id)
            intent.putExtra("bundle", bundle)
            context.startActivity(intent)
        }

    }

//    override fun getLayoutId() = R.layout.activity_info

    private lateinit var vm: InfoViewModel

    //    private val vm by viewModels<InfoViewModel> {
//        InfoViewModelFactory(
//            this,
//            AnimateDatabase.getInstance(this).getAnimateInfoDao().getInfoWithNameFromId(
//                intent.getBundleExtra("bundle")?.getInt("id") ?: -1
//            )
//        )
//    }
    private lateinit var binding: ActivityInfoBinding

    override fun initViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_info)
        binding.lifecycleOwner = this
        vm = ViewModelProvider(
            this,
            InfoViewModelFactory(this, intent.getBundleExtra("bundle")?.getInt("id") ?: throw NullPointerException())
        )[InfoViewModel::class.java]
        binding.vm = vm
    }

    override fun initListener() {
        vm.beanLD.observe(this) {
            vm.name.value = it.nameBean.name + it.infoBean.type.type + it.infoBean.season
            vm.date.value = DateConverter.converter(it.infoBean.airTime)
            vm.state.value = it.infoBean.state.state
            vm.observeBean(it, binding.rvInfo)
        }
    }

}