package dev.ronnie.imageloaderdagger2.presentation.viewmodels

import androidx.lifecycle.ViewModel
import dev.ronnie.imageloaderdagger2.data.repository.Repository
import javax.inject.Inject


/**
 *created by Ronnie Otieno on 03-Apr-21.
 **/
class SingleImageViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    var imageString: String? = null
    val shouldRound = false

}