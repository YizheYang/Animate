package com.yyz.animate.utils;

import android.util.Log;
import android.view.View;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;

import com.yyz.animate.constants.Constants;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

/**
 * description none
 * author ez_yang@qq.com
 * date 2022.9.3 下午 5:16
 * version 1.0
 * update none
 **/
@InverseBindingMethods(
		@InverseBindingMethod(type = NiceSpinner.class, attribute = "selectedIndex", event = "selectedIndexAttrChanged")
)
public class NiceSpinnerBindingAdapter {
	@BindingAdapter("selectedIndex")
	public static void setSelectedIndex(NiceSpinner view, int index) {
		if (view.getSelectedIndex() != index) {
			view.setSelectedIndex(index);
		}
	}

	@InverseBindingAdapter(attribute = "selectedIndex", event = "selectedIndexAttrChanged")
	public static int getSelectedIndex(NiceSpinner view) {
		return view.getSelectedIndex();
	}

	@BindingAdapter(value = {"selectedIndexChanged", "selectedIndexAttrChanged"}, requireAll = false)
	public static void setListeners(NiceSpinner view, final OnSpinnerItemSelectedListener onSpinnerItemSelectedListener,
									final InverseBindingListener inverseBindingListener) {
		if (inverseBindingListener == null) {
			view.setOnSpinnerItemSelectedListener(onSpinnerItemSelectedListener);
		} else {
			view.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
				@Override
				public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
					if (onSpinnerItemSelectedListener != null) {
						onSpinnerItemSelectedListener.onItemSelected(parent, view, position, id);
					}
					inverseBindingListener.onChange();
				}
			});
		}
	}
}
