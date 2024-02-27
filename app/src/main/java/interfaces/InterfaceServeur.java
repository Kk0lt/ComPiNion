package interfaces;

import java.util.List;

import classes.User;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface InterfaceServeur {

    // Création de compte
    @POST("register")
    Call<Void> register(@Body RequestBody requestBody);

    // Login
    @POST("login")
    Call<Void> login(@Body RequestBody requestBody);

    // Usager logged in
    @GET("user/{id}")
    Call<User> user(@Path("id") int id);

    // Tous les usagers
    @GET("users")
    Call<List<User>> users();

    // Logout
    @POST("logout")
    Call<Void> logout();

    // Modification compte
    @PATCH("compinion/{id}")
    Call<Void> update(@Path("id") int id, @Body RequestBody requestBody);

    // Suppression du compte
    @DELETE("compinion/{id}")
    Call<Void> destroy(@Path("id") int id);

}
