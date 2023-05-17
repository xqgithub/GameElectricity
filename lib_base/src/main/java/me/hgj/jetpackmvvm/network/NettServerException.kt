package me.hgj.jetpackmvvm.network

/**
 *
 * @ClassName:      NettServerException
 */
class NettServerException(val errorCode: Int, val errorMessage: String) : Exception() {

    override fun toString(): String {
        return super.toString().plus(" errorCode: $errorCode; errorMessage: $errorMessage")
    }
}