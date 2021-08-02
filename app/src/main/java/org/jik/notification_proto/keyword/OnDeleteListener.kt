package org.jik.notification_proto.keyword

interface OnDeleteListener {
    fun onDeleteListener(keyword:KeywordEntity)
    fun onSubscribeListener(enteredKeyword:String)
}