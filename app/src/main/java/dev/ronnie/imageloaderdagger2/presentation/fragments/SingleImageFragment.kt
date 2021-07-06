package dev.ronnie.imageloaderdagger2.presentation.fragments

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import dev.ronnie.imageloaderdagger2.R
import dev.ronnie.imageloaderdagger2.data.model.ImagesResponse
import dev.ronnie.imageloaderdagger2.databinding.FragmentSingleImageBinding
import dev.ronnie.imageloaderdagger2.presentation.viewmodels.SingleImageViewModel
import dev.ronnie.imageloaderdagger2.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 *created by Ronnie Otieno on 04-Apr-21.
 **/
class SingleImageFragment : DaggerFragment(R.layout.fragment_single_image),
    ChooseQualityDialog.ChooseInterface {

    private var snackBar: Snackbar? = null
    private lateinit var binding: FragmentSingleImageBinding
    private val args = SingleImageFragmentArgs

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SingleImageViewModel by viewModels {
        viewModelFactory
    }

    private var image: ImagesResponse? = null
    private val chooseQualityDialog = ChooseQualityDialog()

    private val storagePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            when {
                granted -> {
                    chooseQualityDialog.show(childFragmentManager, null)

                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE) -> {
                    showUserWhyPermission()
                }
                else -> requireContext().toast("Permission denied")

            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSingleImageBinding.bind(view)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = null

        setHasOptionsMenu(true)

        image = arguments?.let { args.fromBundle(it).image }

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            chooseQualityDialog.show(childFragmentManager, null)
        } else {
            storagePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.download_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun startDownload() {
        makeSnackBar()
        viewModel.notifyDownloading.observe(viewLifecycleOwner, {
            when (it) {
                STARTING_DOWNLOAD -> {
                    snackBar?.show()
                }
                ERROR_DOWNLOADING -> {
                    snackBar?.dismiss()
                }
                HAS_DOWNLOADED -> {
                    snackBar?.setText("Saving...")
                }
                HAS_SAVED -> {
                    snackBar?.dismiss()
                }
            }
        })
        lifecycleScope.launch(Dispatchers.Default) {
            image?.let { viewModel.getBitmapFromURL(it.urls.full, it.id) }
        }
    }

    private fun makeSnackBar() {
        val parentLayout = requireActivity().findViewById<View>(android.R.id.content)
        snackBar = Snackbar.make(parentLayout, "Downloading...", Snackbar.LENGTH_INDEFINITE)

    }

    override fun downLoadFull() {
        requireContext().toast("Downloading full resolution may take a while")
        startDownload()

    }

    override fun downLoadRegular() {
        lifecycleScope.launch(Dispatchers.Default) {
            val bitmap = (binding.imageView.drawable).toBitmap()
            image?.id?.let { viewModel.saveImage(bitmap, it) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        snackBar?.dismiss()
    }

    private fun showUserWhyPermission() {
        AlertDialog.Builder(requireContext())
            .setTitle("Write storage permission needed")
            .setMessage("This permission is required inorder to save the image to the gallery")
            .setPositiveButton(
                "Okay"
            ) { _, _ ->
                storagePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }.create().show()
    }

}