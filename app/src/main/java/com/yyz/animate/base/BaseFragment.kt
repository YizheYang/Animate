package com.yyz.animate.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.yyz.animate.model.AnimateDatabase

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.7.31 下午 8:38
 * version 1.0
 * update none
 **/
abstract class BaseFragment : Fragment() {
    @LayoutRes
    abstract fun getLayoutId(): Int

    protected lateinit var db: AnimateDatabase
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = AnimateDatabase.getInstance(requireContext())
        initViews()
        initListener()
    }

    abstract fun initViews()

    abstract fun initListener()

    fun toast(msg: String, time: Int = Toast.LENGTH_SHORT) = Toast.makeText(requireContext(), msg, time).show()

}