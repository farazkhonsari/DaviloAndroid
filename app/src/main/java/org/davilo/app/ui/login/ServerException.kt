package org.davilo.app.ui.login

import org.davilo.app.model.Output

class ServerException(var output: Output<*>?) : Throwable() {

}
