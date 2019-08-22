package mustafaozhan.github.com.mycurrencies.base.api.backend

import io.reactivex.Observable
import mustafaozhan.github.com.mycurrencies.model.CurrencyResponse
import mustafaozhan.github.com.mycurrencies.tools.Currencies
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
interface BackendApiServices {
    @GET("byBase")
    fun getAllOnBase(@Query("base") base: Currencies):
        Observable<CurrencyResponse>
}
