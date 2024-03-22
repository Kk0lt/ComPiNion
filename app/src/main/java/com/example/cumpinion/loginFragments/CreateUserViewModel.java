package com.example.cumpinion.loginFragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import classes.User;

public class CreateUserViewModel extends ViewModel {

    private MutableLiveData<User> utilisateurMutableLiveData = new MutableLiveData<User>();;
    public LiveData<User> getUserMutableLiveData(){
        return utilisateurMutableLiveData;
    }

    //Ajouter dans le viewModel
    public void addUser(User  utilisateur){
        utilisateurMutableLiveData.setValue(utilisateur);
    }


    //Ajouter un companion
    public void setUserCompanion(String url){
        utilisateurMutableLiveData.getValue().setCompanion_url(url);
    }

    //setter la limite
    public void setUserLimit(int limite){
        utilisateurMutableLiveData.getValue().setLimite(limite);
    }



//    Créer un nouveau compte dans la BD
//    public void createUser(@NonNull View view, EditText etPrenom, EditText etNom, EditText etPseudo, EditText etEmail, EditText etPassword, EditText etConfirmPassword,
//                           Button btCreate, InterfaceServeur serveur){
//        String prenom = etPrenom.getText().toString();
//        String nom = etNom.getText().toString();
//        String pseudo = etPseudo.getText().toString();
//        String email  = etEmail.getText().toString();
//        String password = etPassword.getText().toString();
//        String confirmPassword = etConfirmPassword.getText().toString();
//
//        if(confirmPassword.equals(password)){
//            // Créer un objet JSON contenant les informations d'identification
//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("prenom", prenom);
//                jsonObject.put("nom", nom);
//                jsonObject.put("pseudo", pseudo);
//                jsonObject.put("email", email);
//                jsonObject.put("password", password);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
//            Call<Void> call = serveur.register(requestBody);
//            call.enqueue(new Callback<Void>() {
//                @Override
//                public void onResponse(Call<Void> call, Response<Void> response) {
//                    Log.d("Réussi!", "!!!!!!!Compte Crée  : " + email +" "+ password);
//                    // Vous pouvez maintenant naviguer vers l'écran suivant ou effectuer d'autres actions
//                    NavController controller = Navigation.findNavController(view);
//                    controller.navigate(R.id.loginFragment);
//                }
//
//                @Override
//                public void onFailure(Call<Void> call, Throwable t) {
//                    Log.d("Réussi!", t.getMessage());
//
//                }
//            });
//        }
//        else{
//            Toast.makeText(getContext(), "Les mots de passe ne concordent pas", Toast.LENGTH_SHORT).show();
//
//        }
//
//
//    }

}
