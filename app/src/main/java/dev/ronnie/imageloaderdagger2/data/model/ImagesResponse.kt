package dev.ronnie.imageloaderdagger2.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * created by Ronnie Otieno on 31-Mar-21.
 */

@Parcelize
data class ImagesResponse(
    var id: String,
    var color: String,
    var likes: Int,
    var description: String,
    var urls: PictureUrl,
    var user: User
) : Parcelable