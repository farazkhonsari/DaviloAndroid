package org.davilo.app.model

import com.google.gson.annotations.SerializedName

data class Output<T>(
    @SerializedName("data")
    val outputData: T,
    val status: OutputStatus
)

data class OutputArrat<T>(
    @SerializedName("data")
    val outputData: ArrayList<T>,
    val status: OutputStatus
)

