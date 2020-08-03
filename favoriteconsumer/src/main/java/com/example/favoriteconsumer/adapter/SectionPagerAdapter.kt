package com.example.favoriteconsumer.adapter

import android.content.Context
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.favoriteconsumer.R
import com.example.favoriteconsumer.detail.FollowerFragment
import com.example.favoriteconsumer.detail.FollowingFragment

class SectionPagerAdapter(private val mContext: Context, fm: FragmentManager, private val dataName: String): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    @StringRes
    private val TAB_TITLE = intArrayOf(
        R.string.follower,
        R.string.following
    )

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> {fragment =
                FollowerFragment()
                val mBundle = Bundle()
                mBundle.putString(FollowerFragment.EXTRA_NAME, dataName)
                fragment.arguments = mBundle
            }
            1 -> {fragment = FollowingFragment()
                val mBundle = Bundle()
                mBundle.putString(FollowingFragment.EXTRA_NAME, dataName)
                fragment.arguments = mBundle
            }
        }
        return fragment as Fragment
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLE[position])
    }

    override fun getCount(): Int {
        return 2
    }
}