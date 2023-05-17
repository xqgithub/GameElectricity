package com.sn.gameelectricity.ui.adapter

import android.text.TextUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.ext.setAdapterAnimation
import com.sn.gameelectricity.app.util.SettingUtil
import com.sn.gameelectricity.data.model.bean.AriticleResponse
import me.hgj.jetpackmvvm.ext.util.toHtml


class AriticleAdapter(data: MutableList<AriticleResponse>?) :
    BaseDelegateMultiAdapter<AriticleResponse, BaseViewHolder>(data) {

    private val Ariticle = 1//文章类型
    private val Project = 2//项目类型 本
    private var showTag = false//是否展示标签

    constructor(data: MutableList<AriticleResponse>?, showTag: Boolean) : this(data) {
        this.showTag = showTag
    }

    init {
        setAdapterAnimation(SettingUtil.getListMode())
        // 第一步，设置代理
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<AriticleResponse>() {
            override fun getItemType(data: List<AriticleResponse>, position: Int): Int {
                return if (TextUtils.isEmpty(data[position].envelopePic)) Ariticle else Project
            }
        })
        // 第二步，绑定 item 类型
        getMultiTypeDelegate()?.let {
            it.addItemType(Ariticle, R.layout.item_ariticle)
            it.addItemType(Project, R.layout.item_project)
        }
    }

    override fun convert(helper: BaseViewHolder, item: AriticleResponse) {
        when (helper.itemViewType) {
            Ariticle -> {
                //文章布局的赋值
                item.run {
                    helper.setText(
                        R.id.item_home_author,
                        if (author.isNotEmpty()) author else shareUser
                    )
                    helper.setText(R.id.item_home_content, title.toHtml())
                    helper.setText(R.id.item_home_type2, "$superChapterName·$chapterName".toHtml())
                    helper.setText(R.id.item_home_date, niceDate)
                    if (showTag) {
                        //展示标签
                        helper.setGone(R.id.item_home_new, !fresh)
                        helper.setGone(R.id.item_home_top, type != 1)
                        if (tags.isNotEmpty()) {
                            helper.setGone(R.id.item_home_type1, false)
                            helper.setText(R.id.item_home_type1, tags[0].name)
                        } else {
                            helper.setGone(R.id.item_home_type1, true)
                        }
                    } else {
                        //隐藏所有标签
                        helper.setGone(R.id.item_home_top, true)
                        helper.setGone(R.id.item_home_type1, true)
                        helper.setGone(R.id.item_home_new, true)
                    }
                }
            }
            Project -> {
                //项目布局的赋值
                item.run {
                    helper.setText(
                        R.id.item_project_author,
                        if (author.isNotEmpty()) author else shareUser
                    )
                    helper.setText(R.id.item_project_title, title.toHtml())
                    helper.setText(R.id.item_project_content, desc.toHtml())
                    helper.setText(
                        R.id.item_project_type,
                        "$superChapterName·$chapterName".toHtml()
                    )
                    helper.setText(R.id.item_project_date, niceDate)
                    if (showTag) {
                        //展示标签
                        helper.setGone(R.id.item_project_new, !fresh)
                        helper.setGone(R.id.item_project_top, type != 1)
                        if (tags.isNotEmpty()) {
                            helper.setGone(R.id.item_project_type1, false)
                            helper.setText(R.id.item_project_type1, tags[0].name)
                        } else {
                            helper.setGone(R.id.item_project_type1, true)
                        }
                    } else {
                        //隐藏所有标签
                        helper.setGone(R.id.item_project_top, true)
                        helper.setGone(R.id.item_project_type1, true)
                        helper.setGone(R.id.item_project_new, true)
                    }
                    Glide.with(context).load(envelopePic)
                        .transition(DrawableTransitionOptions.withCrossFade(500))
                        .into(helper.getView(R.id.item_project_imageview))
                }
            }
        }
    }
}


