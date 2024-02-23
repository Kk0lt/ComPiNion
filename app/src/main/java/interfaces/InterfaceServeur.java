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

    //Login
    @POST("login")
    Call<Void> login(@Body RequestBody requestBody);

    //Logout
    @POST("logout")
    Call<Void> logout();

    //Register
    @POST("register")
    Call<Void> register(@Body RequestBody requestBody);

    //Check logged User
    @GET("compinion/{id}")
    Call<User> getUser(@Path("id") int userId);

    //Get all users
    @GET("compinion/{id}")
    Call<List<User>> getUsers();

    //Modification utilisateur
    @PATCH("compinion/{id}/modification")
    Call<Void> updateUser(@Path("id") int userId, @Body RequestBody requestBody);

    //Suppression utilisateur
    @DELETE("compinion/{id}/suppression")
    Call<Void> deleteUser(@Path("id") int userId);

}
