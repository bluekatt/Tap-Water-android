package com.android.parkjongseok.tapwater.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.parkjongseok.tapwater.databinding.FragmentOpenSourceLicenseDetailBinding

class OpenSourceLicenseDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding = FragmentOpenSourceLicenseDetailBinding.inflate(inflater, container, false)

        val args = OpenSourceLicenseDetailFragmentArgs.fromBundle(requireArguments())

        binding.licenseContent.text = args.licenseContent
        return binding.root
    }

}