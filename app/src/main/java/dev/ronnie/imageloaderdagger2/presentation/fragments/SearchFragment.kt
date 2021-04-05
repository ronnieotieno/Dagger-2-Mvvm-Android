package dev.ronnie.imageloaderdagger2.presentation.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.LoadState
import dagger.android.support.DaggerFragment
import dev.ronnie.imageloaderdagger2.R
import dev.ronnie.imageloaderdagger2.data.model.ImagesResponse
import dev.ronnie.imageloaderdagger2.databinding.FragmentSearchBinding
import dev.ronnie.imageloaderdagger2.presentation.adapters.ImagesAdapter
import dev.ronnie.imageloaderdagger2.presentation.adapters.LoadingStateAdapter
import dev.ronnie.imageloaderdagger2.presentation.viewmodels.SearchViewModel
import dev.ronnie.imageloaderdagger2.utils.toast
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 *created by Ronnie Otieno on 05-Apr-21.
 **/
class SearchFragment : DaggerFragment(R.layout.fragment_search) {

    private var job: Job? = null

    private var hasInitiatedInitialCall = false
    private lateinit var binding: FragmentSearchBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val adapter =
        ImagesAdapter { imagesResponse, imageView ->
            navigate(
                imagesResponse,
                imageView
            )
        }

    private val viewModel: SearchViewModel by viewModels {
        viewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSearchBinding.bind(view)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)

        setAdapter()
        setSearchView()
        binding.searchView.requestFocus()

        //prevents the method being called again onbackpressed pressed.
        if (!hasInitiatedInitialCall) {
            viewModel.currentQuery()?.let { searchImage(it); binding.searchView.setText(it) }
            hasInitiatedInitialCall = true
        }

    }


    @SuppressLint("ClickableViewAccessibility")
    private fun setSearchView() {
        binding.searchView.setOnTouchListener { v, _ ->
            v.isFocusableInTouchMode = true
            false
        }
        binding.searchView.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchImage(binding.searchView.text.toString().trim())
                return@OnEditorActionListener true
            }
            false
        })


    }

    private fun searchImage(query: String) {
        hideSoftKeyboard()
        job?.cancel()
        job = lifecycleScope.launch {

            viewModel.searchImage(query).collect {
                adapter.submitData(it)
            }

        }
    }

    private fun hideSoftKeyboard() {
        val view = requireActivity().currentFocus

        view?.let {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

    }

    private fun setAdapter() {
        binding.imagesList.adapter = adapter.withLoadStateFooter(
            LoadingStateAdapter { adapter.retry() }
        )
        adapter.addLoadStateListener {

            binding.progress.isVisible = it.refresh is LoadState.Loading

            if (it.refresh is LoadState.Error) {
                requireContext().toast("There was a problem fetching data")
            }
        }
    }

    private fun navigate(imagesResponse: ImagesResponse, imageView: ImageView) {
        val action = SearchFragmentDirections.toSingleImageFragment(imagesResponse)
        binding.root.findNavController()
            .navigate(action)
    }


}