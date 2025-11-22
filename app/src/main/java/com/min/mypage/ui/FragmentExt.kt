package com.min.mypage.ui

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.min.mypage.R   // ✅ 프로젝트 패키지로 변경

fun Fragment.replaceTo(
    fragment: Fragment,
    @IdRes containerId: Int = R.id.main_container,
    addToBackStack: Boolean = true,
    tag: String? = fragment::class.java.simpleName
) {
    parentFragmentManager.beginTransaction()
        .setCustomAnimations(
            R.anim.slide_in_right,
            R.anim.fade_out,
            R.anim.fade_in,
            R.anim.slide_out_right
        )
        .replace(containerId, fragment, tag)
        .apply { if (addToBackStack) addToBackStack(tag) }
        .commit()
}