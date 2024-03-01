package classes;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import interfaces.InterfaceServeur;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel {

    private MutableLiveData<List<User>> listeUsers;
    private MutableLiveData<List<User>> listeFriends;
    InterfaceServeur interfaceServeur = RetrofitInstance.getInstance().create(InterfaceServeur.class);

    public LiveData<List<User>> getUsers() {
        if (listeUsers == null) {
            listeUsers = new MutableLiveData<List<User>>();
            listeUsers.setValue(new ArrayList<User>());
        }
        return listeUsers;
    }

    public LiveData<List<User>> getFriends() {
        if (listeFriends == null) {
            listeFriends = new MutableLiveData<List<User>>();
            listeFriends.setValue(new ArrayList<User>());
        }
        return listeFriends;
    }

    public void addUser(User u) {
        List<User> liste = getUsers().getValue();
        liste.add(u);
        listeUsers.setValue(liste);
    }

    public void addFriend(User u) {
        List<User> liste = getFriends().getValue();
        liste.add(u);
        listeFriends.setValue(liste);
    }

    public void getUsersFromBDD() {
//        Call<List<User>> call = interfaceServeur.getUsers();
//        call.enqueue(new Callback<List<User>>() {
//            @Override
//            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
//                if (response.isSuccessful()) {
//                    listeUsers.setValue(response.body());
//                } else {
//                    Log.d("isNotSuccessful", "Fuck");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<User>> call, Throwable t) {
//                Log.d("onFailure", t.getMessage());
//            }
//        });
    }

}
