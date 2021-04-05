package dev.ronnie.imageloaderdagger2.data.model

data class SearchResponse(
    val results: List<ImagesResponse>,
    val total: Int,
    val total_pages: Int
)