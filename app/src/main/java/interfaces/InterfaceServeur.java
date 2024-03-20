package interfaces;

import classes.ReponseServer;
import classes.User;
import classes.UserResponseServer;
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
    Call<ReponseServer> getAmis();

    // Logout
    @POST("logout")
    Call<Void> logout();

    //Block
    @POST("user/{id1}/block/{id2}")
    Call<Void> block(@Path("id1") int id1, @Path("id2") int id2, @Body RequestBody requestBody);

    //Unblock
    @DELETE("user/{id1}/unblock/{id2}")
    Call<Void> unblock(@Path("id1") int id1, @Path("id2") int id2);

    // Modification compte
    @PATCH("compinion/{id}/update")
    Call<Void> update(@Path("id") int id, @Body RequestBody requestBody);

    // Suppression du compte
    @DELETE("compinion/{id}/delete")
    Call<Void> destroy(@Path("id") int id);

    // Afficher la liste des personnages
    @GET("showAllCharacters")
    Call<CompinionsReponseServer> getAllCompinions();

    // Afficher l'image du personnage du joueur

    @GET("showCharacter/{id}")
    Call<CompinionsReponseServer> getCompinion(@Path("id") int id);


    /*======Modification des infos de user=====*/

    @PATCH("compinion/{id}/update/prenom")
    Call<Void> updatePrenom(@Path("id") int id, @Query("prenom") String prenom);

    @PATCH("compinion/{id}/update/nom")
    Call<Void> updateNom(@Path("id") int id, @Query("nom") String nom);

    @PATCH("compinion/{id}/update/pseudo")
    Call<Void> updatePseudo(@Path("id") int id, @Query("pseudo") String pseudo);

    @PATCH("compinion/{id}/update/email")
    Call<Void> updateEmail(@Path("id") int id, @Query("email") String email);

    @PATCH("compinion/{id}/update/merite")
    Call<Void> updateMerite(@Path("id") int id, @Query("merite") String merite);

    @PATCH("compinion/{id}/update/jours")
    Call<Void> updateJours(@Path("id") int id, @Query("jours") String jours);

}
