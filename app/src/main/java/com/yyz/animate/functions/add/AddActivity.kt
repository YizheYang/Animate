package com.yyz.animate.functions.add

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
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
    private var listPopupWindow: ListPopupWindow? = null

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

    override fun initListener() {
        var old = listOf<String>()
        binding.etAddName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                listPopupWindow?.dismiss()
//                listPopupWindow = null
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() == "") {
                    listPopupWindow?.dismiss()
                    listPopupWindow = null
                }
                val tempList =
                    vm.nameBeanList.map { it.name }.filter { s.toString() != "" && it.contains(s.toString()) }
                if (old == tempList) {
                    return
                }
                if (listPopupWindow != null) {
                    listPopupWindow?.dismiss()
                    listPopupWindow = null
                }
                old = tempList
                listPopupWindow = ListPopupWindow(this@AddActivity)
                listPopupWindow?.run {
                    setAdapter(
                        ArrayAdapter(
                            this@AddActivity,
                            R.layout.item_add_namelist,
                            tempList
                        )
                    )
                    setDropDownGravity(Gravity.CENTER)
                    anchorView = binding.etAddName
                    setOnItemClickListener { parent, view, position, id ->
                        vm.chooseNameBean = vm.nameBeanList.filter { it.name == tempList[position] }[0]
                        dismiss()
                        listPopupWindow = null
                    }
                    show()
                }
            }

        })
    }
}