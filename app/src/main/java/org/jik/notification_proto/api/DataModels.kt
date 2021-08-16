package org.jik.notification_proto.api

data class PostModel(
    var major: String? = null,
    var token: String? = null,
    var keyword: String? = null
)

data class DeleteModel(
        var token: String? = null,
        var keyword: String? = null
)

data class UpdateModel(
        var token: String? = null,
        var major: String? = null
)