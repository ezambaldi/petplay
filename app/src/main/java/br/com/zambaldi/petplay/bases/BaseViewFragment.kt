package com.example.myapplicationtest.bases

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Job

abstract class BaseViewFragment(@LayoutRes resId: Int) : Fragment(resId) {
    abstract fun addEffectCollector(): Job
    abstract fun addStateCollector(): Job

    override fun onStart() {
        super.onStart()
        addEffectCollector()
        addStateCollector()
    }
}