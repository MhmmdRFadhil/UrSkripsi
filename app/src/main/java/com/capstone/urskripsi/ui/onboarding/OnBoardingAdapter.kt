package com.capstone.urskripsi.ui.onboarding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.capstone.urskripsi.R

class OnBoardingAdapter(private val context: Context, private val onBoarding: List<OnBoarding>) :
    PagerAdapter() {
    override fun getCount(): Int = onBoarding.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.item_row_on_boarding, container, false)

        val layoutContent: View = view.findViewById(R.id.layoutContent)
        val imageViewOnBoarding: ImageView = layoutContent.rootView.findViewById(R.id.imgOnBoarding)
        val titleOnBoarding: TextView = layoutContent.rootView.findViewById(R.id.tvTitleOnBoarding)
        val descriptionOnBoarding: TextView =
            layoutContent.rootView.findViewById(R.id.tvDescriptionOnBoarding)

        val listOnBoarding = onBoarding[position]
        listOnBoarding.image?.let { imageViewOnBoarding.setImageResource(it) }
        listOnBoarding.title?.let { titleOnBoarding.text = it }
        listOnBoarding.description?.let { descriptionOnBoarding.text = it }

        container.addView(view)
        return view
    }
}