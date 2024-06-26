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
    public void setUserCompanion(int id){
        utilisateurMutableLiveData.getValue().setCompanion_id(id);
    }

    //setter la limite
    public void setUserLimit(int limite){
        utilisateurMutableLiveData.getValue().setLimite(limite);
    }

}
