package com.trevorwiebe.caldav.domain.model

import java.time.LocalDateTime

data class EventModel(
    val id: String,
    val url: String,
    val status: String,
    val summary: String,
    val frequency: String,
    val description: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val color: String
)
