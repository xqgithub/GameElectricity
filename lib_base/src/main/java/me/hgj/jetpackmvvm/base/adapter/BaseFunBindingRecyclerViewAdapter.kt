package me.hgj.jetpackmvvm.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import me.jingbin.library.adapter.BaseByRecyclerViewAdapter
import me.jingbin.library.adapter.BaseByViewHolder
import java.lang.reflect.ParameterizedType

/**
 * Date:2022/5/9
 * Time:9:57
 * author:dimple
 */
abstract class BaseFunBindingRecyclerViewAdapter<T, VB : ViewBinding> :
    BaseByRecyclerViewAdapter<T, BaseFunBindingViewHolder<T, VB>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseFunBindingViewHolder<T, VB> {
        val type = javaClass.genericSuperclass as ParameterizedType
        val clazz0 = type.actualTypeArguments[1] as Class<VB>
        val layoutInflater = LayoutInflater.from(parent.context)
        val method = clazz0.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        val binding = method.invoke(null, layoutInflater, parent, false) as VB
        return ViewHolder(binding)
    }

    private inner class ViewHolder(binding: VB) : BaseFunBindingViewHolder<T, VB>(binding) {
        override fun onBindView(binding: VB, bean: T, position: Int) {
            bindView(this, binding, bean, position)
        }
    }

    protected open fun bindView(
        holder: BaseByViewHolder<T>,
        binding: VB,
        bean: T,
        position: Int
    ) {
        bindView(binding, bean, position)
    }

    protected abstract fun bindView(binding: VB, bean: T, position: Int)


    var maxPageSize = 10
    override fun setNewData(data: List<T>) {
        super.setNewData(data)
        if (data.size < maxPageSize && recyclerView != null && recyclerView.isLoadMoreEnabled) recyclerView.loadMoreEnd()
    }

    open fun setNewDataIgnoreSize(data: List<T>) {
        super.setNewData(data)
    }

}