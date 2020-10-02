package org.davilo.app.di.network.utils

import org.davilo.app.model.Output

class ServerException(var output: Output<*>?) : Throwable()
