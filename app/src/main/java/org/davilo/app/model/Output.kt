package org.davilo.app.model

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

data class Output<T>(
    @SerializedName("data")
    val outputData: ArrayList<T>,
    val status: OutputStatus
)

