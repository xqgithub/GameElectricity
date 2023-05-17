package me.hgj.jetpackmvvm.base.adapter

import androidx.viewbinding.ViewBinding
import me.jingbin.library.adapter.BaseByViewHolder

/**
 * Date:2022/5/9
 * Time:9:58
 * author:dimple
 */
abstract class BaseFunBindingViewHolder<T, VB : ViewBinding>(val viewBinding: VB) :
    BaseByViewHolder<T>(viewBinding.root) {
    override fun onBaseBindView(holder: BaseByViewHolder<T>?, bean: T, position: Int) {
        onBindView(viewBinding, bean, position)
    }

    protected abstract fun onBindView(binding: VB, bean: T, position: Int)
}