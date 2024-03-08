package interfaces;

import classes.ReponseServer;
import classes.User;
import classes.characters.CompinionsReponseServer;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface InterfaceServeur {

    // Création de compte
    @POST("register")
    Call<Void> register(@Body RequestBody requestBody);

    // Login
    @POST("login")
    Call<ResponseBody> login(@Body RequestBody requestBody);

    // Usager logged in
    @GET("user/{id}")
    Call<User> user(@Path("id") int id);

    // Tous les usagers
    @GET("users")
    Call<ReponseServer> getUsers();

    // Logout
    @POST("logout")
    Call<Void> logout();

    // Modification compte
    @PATCH("compinion/{id}")
    Call<Void> update(@Path("id") int id, @Body RequestBody requestBody);

    // Suppression du compte
    @DELETE("compinion/{id}")
    Call<Void> destroy(@Path("id") int id);

    // Afficher la liste des personnages
    @GET("showAllCharacters")
    Call<CompinionsReponseServer> getAllCompinions();

}
