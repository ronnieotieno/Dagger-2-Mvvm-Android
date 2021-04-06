package dev.ronnie.imageloaderdagger2.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import dagger.android.support.DaggerFragment
import dev.ronnie.imageloaderdagger2.R
import dev.ronnie.imageloaderdagger2.databinding.FragmentSingleImageBinding
import dev.ronnie.imageloaderdagger2.presentation.viewmodels.SingleImageViewModel
import javax.inject.Inject


/**
 *created by Ronnie Otieno on 04-Apr-21.
 **/
class SingleImageFragment : DaggerFragment(R.layout.fragment_single_image) {

    private lateinit var binding: FragmentSingleImageBinding
    private val args = SingleImageFragmentArgs

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SingleImageViewModel by viewModels {
        viewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSingleImageBinding.bind(view)

        val image = arguments?.let { args.fromBundle(it).image }

        viewModel.imageString = image?.urls?.regular

        binding.viewmodel = viewModel

        setImageView()
    }

    private fun setImageView() {
        val scaleImage = binding.imageView
        scaleImage.setOnDismissRateChange { _, isCanNowDismiss ->
            if (isCanNowDismiss) {
                binding.root.findNavController().navigateUp()
            }
        }
    }
}