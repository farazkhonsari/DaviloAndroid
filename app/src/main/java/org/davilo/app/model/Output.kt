package org.davilo.app.model

import java.util.ArrayList

data class Output<T>(
    val outputData: ArrayList<T>,
    val status: OutputStatus
)

