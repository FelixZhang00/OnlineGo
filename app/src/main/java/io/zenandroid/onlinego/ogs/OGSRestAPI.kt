package io.zenandroid.onlinego.ogs

import io.reactivex.Single
import io.zenandroid.onlinego.BuildConfig
import io.zenandroid.onlinego.model.ogs.*
import retrofit2.http.*

/**
 * Created by alex on 02/11/2017.
 */
interface OGSRestAPI {

    @FormUrlEncoded
    @POST("oauth2/token/")
    fun login(@Field("username") username: String,
              @Field("password") password: String,
              @Field("client_id") client_id: String = BuildConfig.CLIENT_ID,
              @Field("client_secret") client_secret: String = BuildConfig.CLIENT_SECRET,
              @Field("grant_type") grant_type: String = "password"): Single<LoginToken>

    @FormUrlEncoded
    @POST("oauth2/token/")
    fun refreshToken(@Field("refresh_token") refresh_token: String,
                     @Field("client_id") client_id: String = BuildConfig.CLIENT_ID,
                     @Field("client_secret") client_secret: String = BuildConfig.CLIENT_SECRET,
                     @Field("grant_type") grant_type: String = "refresh_token"): Single<LoginToken>

    @FormUrlEncoded
    @POST("oauth2/token/")
    fun authWithCode(@Field("code") code: String,
                     @Field("client_id") client_id: String = BuildConfig.CLIENT_ID,
                     @Field("client_secret") client_secret: String = BuildConfig.CLIENT_SECRET,
                     @Field("grant_type") grant_type: String = "authorization_code"): Single<LoginToken>

    @GET("api/v1/ui/config/")
    fun uiConfig(): Single<UIConfig>

    @GET("api/v1/games/{game_id}")
    fun fetchGame(@Path("game_id") game_id: Long): Single<OGSGame>

    @GET("api/v1/ui/overview")
    fun fetchOverview(): Single<Overview>

    @POST("api/v0/register")
    fun createAccount(@Body request: CreateAccountRequest): Single<UIConfig>

    @GET("api/v1/players/{player_id}/games/?source=play&ended__isnull=false&annulled=false&ordering=-ended")
    fun fetchPlayerFinishedGames(
            @Path("player_id") playerId: Long,
            @Query("page_size") pageSize: Int = 10,
            @Query("page") page: Int = 1): Single<PagedResult>
}
