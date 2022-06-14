package com.example.tikitrendingproject.retrofit

import android.util.Log
import com.example.tikitrendingproject.model.ResponseObject
import retrofit2.HttpException
import retrofit2.Response

class BaseResponse {
    companion object{
        inline fun <reified T> process(response: Response<ResponseObject<T>>, data: (T?)-> Unit) {
            if (response.isSuccessful && response.body()?.status == 200) {
                response.body()?.data?.let { data.invoke(it) }
            } else if (response.code() in 401..510) {
                data.invoke(null)
                Log.d("HIEN", "Process api ${HttpException(response).localizedMessage} - ${response.errorBody()?.string()}")
            } else {
                data.invoke(response.body()?.data)
            }
        }

        inline fun <reified T> processReturnValue(response: Response<ResponseObject<T>>): T? {
            if (response.isSuccessful && response.body()?.status == 200) {
                response.body()?.data?.let { return it}
            } else if (response.code() in 401..510) {
                Log.d(
                    "HIEN",
                    "Process api ${HttpException(response).localizedMessage} - ${
                        response.errorBody()?.string()
                    }"
                )
                return null
            }
            return response.body()?.data
        }
    }

}

interface Result<T> {
    fun responseFromApi(data: T?, throwable: Throwable?)
}


