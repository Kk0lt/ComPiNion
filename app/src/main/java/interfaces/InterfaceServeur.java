package interfaces;

import classes.ReponseServer;
import classes.Streak;
import classes.StreakReponseServer;
import classes.StreaksReponseServer;
import classes.User;
import classes.UserResponseServer;
import classes.characters.CompinionsReponseServer;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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
    Call<ResponseBody> login(@Body RequestBody requestBody);

    // Usager logged in
    @GET("user/{id}")
    Call<UserResponseServer> user(@Path("id") int id);

    // Tous les usagers
    @GET("users/{id}")
    Call<ReponseServer> getUsers(@Path("id") int id);

    // Tous les usagers
    @GET("user/{id}/amis")
    Call<ReponseServer> getAmis(@Path("id") int id);

    // Logout
    @POST("logout")
    Call<Void> logout();

    //Block
    @POST("user/{id1}/block/{id2}")
    Call<Void> block(@Path("id1") int id1, @Path("id2") int id2, @Body RequestBody requestBody);

    //Unblock
    @DELETE("user/{id1}/unblock/{id2}")
    Call<Void> unblock(@Path("id1") int id1, @Path("id2") int id2);

    // Suppression du compte
    @DELETE("user/{id}/delete")
    Call<Void> destroy(@Path("id") int id);

    // Afficher la liste des personnages
    @GET("showAllCharacters")
    Call<CompinionsReponseServer> getAllCompinions();

    // Afficher l'image du personnage du joueur

    @GET("showCharacter/{id}")
    Call<CompinionsReponseServer> getCompinion(@Path("id") int id);

    // Afficher la/les streaks relative à un utilisateur
    @GET("user/{id}/streaks")
    Call<StreaksReponseServer> getStreaks(@Path("id") int id);

    @GET("user/{id}/streak")
    Call<StreakReponseServer> getStreak(@Path("id") int id);

    /*======Modification des infos de user=====*/

    @PATCH("user/{id}/update/prenom")
    Call<Void> updatePrenom(@Path("id") int id, @Query("prenom") String prenom);

    @PATCH("user/{id}/update/nom")
    Call<Void> updateNom(@Path("id") int id, @Query("nom") String nom);

    @PATCH("user/{id}/update/pseudo")
    Call<Void> updatePseudo(@Path("id") int id, @Query("pseudo") String pseudo);

    @PATCH("user/{id}/update/email")
    Call<Void> updateEmail(@Path("id") int id, @Query("email") String email);

    @PATCH("user/{id}/update/merite")
    Call<Void> updateMerite(@Path("id") int id, @Query("merite") int merite);

    @PATCH("user/{id}/update/jours")
    Call<Void> updateJours(@Path("id") int id, @Query("jours") int jours);

    @PATCH("user/{id}/update/limite")
    Call<Void> updateLimite(@Path("id") int id, @Query("limite") int limite);

    @POST("user/{id}/update/endstreak")
    Call<Void> endStreak(@Path("id") int id);

    // Update password endpoint
    @FormUrlEncoded
    @PATCH("compinion/{id}/update/password")
    Call<Void> updatePassword(
            @Path("id") int id,
            @Field("current_password") String currentPassword,
            @Field("new_password") String newPassword
    );


}
