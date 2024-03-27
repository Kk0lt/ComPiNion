package com.example.cumpinion;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cumpinion.loginFragments.LoggedUserViewModel;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import classes.RetrofitInstance;
import classes.streaks.Streak;
import classes.characters.Compinion;
import classes.characters.CompinionReponseServer;
import classes.streaks.StreaksAdapterList;
import classes.streaks.StreaksReponseServer;
import interfaces.InterfaceServeur;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private static final long MINUIT = 60000; //86400000;
    TextView tvPseudo, nbMerit, nbStreak;
    ImageView imgProfile;
    String url;
    Button btSmoke, btnTestMQTT;
    RecyclerView rvStreaks;
    StreaksAdapterList streaksAdapterListe;
    Streak streak;
    Mqtt5Client client;
    Handler handler;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = Mqtt5Client.builder()
                .identifier(UUID.randomUUID().toString())
                .serverHost("172.16.87.61")
                .serverPort(1883)
                .simpleAuth()
                .username("cedric")
                .password("q".getBytes())
                .applySimpleAuth()
                .build();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Appel du serveur
        InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);

        //Layout
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //On va chercher l'usager connecté
        LoggedUserViewModel loggedUserViewModel = new ViewModelProvider(getActivity()).get(LoggedUserViewModel.class);

        //Get des éléments de mon xml
        tvPseudo = view.findViewById(R.id.tvHome);
        nbMerit = view.findViewById(R.id.merit);
        nbStreak = view.findViewById(R.id.serie);
        imgProfile = view.findViewById(R.id.ivHome);
        btSmoke = view.findViewById(R.id.btnSmoked);
        Picasso.get().load(loggedUserViewModel.getUserMutableLiveData().getValue().getCompanionImage()).into(imgProfile);
        Log.d("IMAGE", "IMAGE COMPANION: " + loggedUserViewModel.getUserMutableLiveData().getValue().getCompanionImage());
        //getImg(loggedUserViewModel.getUserMutableLiveData().getValue().getId(), imgProfile);

        //Gestion du RecyclerView des streaks
        rvStreaks = view.findViewById(R.id.rvStreaks);
        rvStreaks.setHasFixedSize(true);
        rvStreaks.setLayoutManager(new LinearLayoutManager(getContext()));
        getStreaks(serveur, loggedUserViewModel.getUserMutableLiveData().getValue().getId());

        //Handler pour l'update à minuit
        handler = new Handler();
        Calendar curTime = Calendar.getInstance();
        long now = curTime.getTimeInMillis();
        long minuit = curTime.getTimeInMillis();
        minuit += MINUIT - (now % MINUIT);
        //handler.post(new UpdateTask(loggedUserViewModel));

        //Connexion des éléments du xml avec les données de l'utilisateur
        tvPseudo.setText(loggedUserViewModel.getUserMutableLiveData().getValue().getPseudo());
        nbMerit.setText(String.valueOf(loggedUserViewModel.getUserMutableLiveData().getValue().getMerite()));
        nbStreak.setText(String.valueOf(loggedUserViewModel.getUserMutableLiveData().getValue().getJours()));

        //Modals pour le bouton fumé
        btnSmoked(serveur, loggedUserViewModel);

        return view;
    }
    /*======== MQTT ========*/
    public void connexion(String _username, String _merites, String _img, String _streak) {
        client.toAsync().connect()
                .whenComplete((connAck, throwable) -> {
                    if (throwable != null) {
                        Log.d("Fail", "ERREUR MQTT ");
                    } else {
                        // setup subscribes or start publishing
                        publishUser(_username);
                        publishMerites(_merites);
                        publishImage(_img);
                        publishStreak(_streak);
                        client.toAsync().disconnect();
                    }
                });
    }

    /**
     * METHODE DE PUBLISH DES STREAKS
     */

    private void publishStreak(String streak){
        client.toAsync().publishWith()
                .topic("streak")
                .payload(streak.getBytes())
                .send()
                .whenComplete((publish, throwable) -> {
                    if (throwable != null) {
                        Log.d("Erreur", throwable.toString());
                    } else {
                        Log.d("Youpi!", streak.getBytes().toString() );
                    }
                });
    }

    /**
     * METHODE DE PUBLISH DU NOM D'UTILISATEUR
     */

    private void publishUser(String username){
        client.toAsync().publishWith()
                .topic("user")
                .payload(username.getBytes())
                .send()
                .whenComplete((publish, throwable) -> {
                    if (throwable != null) {
                        // handle failure to publish
                    } else {
                        // handle successful publish, e.g. logging or incrementing a metric
                        Log.d("publishConnexion", "user published" );

                    }
                });
    }

    /**
     * METHODE DE PUBLISH DE L'IMAGE
     */

    private void publishImage(String img){
        client.toAsync().publishWith()
                .topic("pic")
                .payload(img.getBytes())
                .send()
                .whenComplete((publish, throwable) -> {
                    if (throwable != null) {
                        // handle failure to publish
                    } else {
                        // handle successful publish, e.g. logging or incrementing a metric
                        Log.d("publishConnexion", "connexion published" );

                    }
                });
    }

    /**
     * METHODE DE PUBLISH DES POINTS DE MERITES
     */

    private void publishMerites(String level){
        client.toAsync().publishWith()
                .topic("level")
                .payload(level.getBytes())
                .send()
                .whenComplete((publish, throwable) -> {
                    if (throwable != null) {
                        // handle failure to publish
                    } else {
                        // handle successful publish, e.g. logging or incrementing a metric
                        Log.d("publishConnexion", "connexion published" );

                    }
                });
    }

    /*======== METHODES PRIVÉS ========*/
    private void btnSmoked(InterfaceServeur serveur, LoggedUserViewModel loggedUserViewModel) {
        btSmoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Comment ça va aujourd'hui?");

                //J'ai fumé
                builder.setPositiveButton("J'ai fumé.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder bSmoked = new AlertDialog.Builder(getContext());
                        bSmoked.setTitle("Dommage, mais ne te décourage pas!");
                        //Si ma limite est écoulé texte

                        if(loggedUserViewModel.getUserMutableLiveData().getValue().getLimite()-1 < 0) {
                            bSmoked.setMessage("Tu as écoulé ta limite de cigarette pour la journée. " +
                                    "Nous reprenons la série du début, mais continue tes efforts pour battre ton record!");
                        //Si ma limite n'est pas écoulée texte
                        } else {
                            bSmoked.setMessage("N'abandonne pas! Tu peux encore améliorer ta série et ton score!" +
                                    "\n" + (loggedUserViewModel.getUserMutableLiveData().getValue().getLimite()-1) + "/" + loggedUserViewModel.getUserMutableLiveData().getValue().getLimite());
                        }

                        //Confirmation
                        bSmoked.setPositiveButton("Très bien", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int i = loggedUserViewModel.getUserMutableLiveData().getValue().getLimite()-1;
                                //Si ma limite est écoulée, on choisit une nouvelle limite et on update la streak
                                if(i < 0) {
                                    connexion(loggedUserViewModel.getUserMutableLiveData().getValue().getPseudo(),
                                            String.valueOf(loggedUserViewModel.getUserMutableLiveData().getValue().getMerite()),
                                            loggedUserViewModel.getUserMutableLiveData().getValue().getCompanionPNG(),
                                            String.valueOf(loggedUserViewModel.getUserMutableLiveData().getValue().getJours()));
                                    endStreak(loggedUserViewModel);
                                    resetStreak(serveur, loggedUserViewModel);
                                    nbStreak.setText(String.valueOf(loggedUserViewModel.getUserMutableLiveData().getValue().getJours()));
                                    AlertDialog.Builder bLimite = new AlertDialog.Builder(getContext());
                                    bLimite.setTitle("Choisissez une nouvelle limite de cigarette par jours");
                                    bLimite.setPositiveButton("1", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            updateLimite(loggedUserViewModel, 1);
                                            Toast.makeText(getContext(), "Votre nouvelle série est commencée! Bonne chance!", Toast.LENGTH_LONG).show();
                                        }
                                    });

                                    bLimite.setNegativeButton("2", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            updateLimite(loggedUserViewModel,2);
                                            Toast.makeText(getContext(), "Votre nouvelle série est commencée! Bonne chance!", Toast.LENGTH_LONG).show();
                                        }
                                    });

                                    bLimite.setNeutralButton("3", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            updateLimite(loggedUserViewModel, 3);
                                            Toast.makeText(getContext(), "Votre nouvelle série est commencée! Bonne chance!", Toast.LENGTH_LONG).show();
                                        }
                                    });

                                    AlertDialog alertLimite = bLimite.create();
                                    alertLimite.show();
                                //Si ma limite n'est pas écoulée, on fait juste mettre à jour la limite
                                } else {
                                    updateLimite(loggedUserViewModel, i);
                                }
                            }
                        });

                        bSmoked.setNegativeButton("C'est une erreur.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(), "La mise à jour a été annulée.", Toast.LENGTH_LONG).show();
                            }
                        });
                        AlertDialog alertSmoke = bSmoked.create();
                        alertSmoke.show();
                    }
                });

                //Je n'ai pas fumé aujourd'hui
                builder.setNegativeButton("Je n'ai pas fumé!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Bravo!!", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNeutralButton("J'y pense!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        connexion(loggedUserViewModel.getUserMutableLiveData().getValue().getPseudo(),
                                String.valueOf(loggedUserViewModel.getUserMutableLiveData().getValue().getMerite()),
                                loggedUserViewModel.getUserMutableLiveData().getValue().getCompanionPNG(),
                                String.valueOf(loggedUserViewModel.getUserMutableLiveData().getValue().getJours()));
                        Toast.makeText(getContext(), "Ne lâche pas!", Toast.LENGTH_LONG).show();
                        int i = loggedUserViewModel.getUserMutableLiveData().getValue().getMerite();
                        updateMerite(loggedUserViewModel, i+1);
                        publishMerites(String.valueOf(i+1));
                        nbMerit.setText(String.valueOf(i+1));
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }

    private void getStreaks(InterfaceServeur serveur, int i) {
        Call<StreaksReponseServer> call = serveur.getStreaks(i);
        call.enqueue(new Callback<StreaksReponseServer>() {
            @Override
            public void onResponse(Call<StreaksReponseServer> call, Response<StreaksReponseServer> response) {
                if (response.isSuccessful()) {
                    StreaksReponseServer streaksResponse = response.body();
                    if (streaksResponse != null && streaksResponse.isSuccess()) {
                        List<Streak> lstreaks = streaksResponse.getData();
                        streaksAdapterListe = new StreaksAdapterList(lstreaks);
                        rvStreaks.setAdapter(streaksAdapterListe);
                    } else {
                        Log.d("erreur", "Réponse invalide");
                    }
                } else {
                    Log.d("erreur", "Réponse non réussie: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<StreaksReponseServer> call, Throwable t) {
                Log.d("erreur", "onFailure Erreur");
                Log.d("erreur", t.getMessage());
            }
        });
    }

    private static void updateMerite(LoggedUserViewModel loggedUserViewModel, int i) {
        InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
        Call<Void> call = serveur.updateMerite(loggedUserViewModel.getUserMutableLiveData().getValue().getId(), i);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                loggedUserViewModel.setUserMerit(i);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("failed", "Failed: "+ t.getMessage());
            }
        });
    }

    private void endStreak(LoggedUserViewModel loggedUserViewModel) {

        InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
        Call<Void> call = serveur.endStreak(loggedUserViewModel.getUserMutableLiveData().getValue().getId());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                loggedUserViewModel.setUserStreak(0);
                String s = String.valueOf(0);
                publishStreak(s);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("failed", "Failed: "+ t.getMessage());
            }
        });

    }

    private void resetStreak(InterfaceServeur serveur, LoggedUserViewModel loggedUserViewModel) {

        Call<Void> call = serveur.resetStreak(loggedUserViewModel.getUserMutableLiveData().getValue().getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getContext(), "La mise à jour a été effectuée avec succès.", Toast.LENGTH_LONG).show();
                getStreaks(serveur, loggedUserViewModel.getUserMutableLiveData().getValue().getId());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("erreur", "onFailure Erreur");
                Log.d("erreur", t.getMessage());
            }
        });

    }

    private static void updateLimite(LoggedUserViewModel loggedUserViewModel, int i) {
        InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
        Call<Void> call = serveur.updateLimite(loggedUserViewModel.getUserMutableLiveData().getValue().getId(), i);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                loggedUserViewModel.setUserLimit(i);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("failed", "Failed: "+ t.getMessage());
            }
        });
    }

    private void getImg(int i, ImageView imageView) {
        InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
        Call<CompinionReponseServer> call = serveur.getCompinion(i);
        call.enqueue(new Callback<CompinionReponseServer>() {
            @Override
            public void onResponse(Call<CompinionReponseServer> call, Response<CompinionReponseServer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Compinion c = response.body().getCompinion();
                    if (c != null) {
                        String imgUrl = c.getImage();
                        Picasso.get().load(Uri.parse(imgUrl)).into(imageView);
                    } else {
                        Log.d("failed", "La réponse est null");
                    }
                } else {
                    Log.d("failed", "La réponse a échoué");
                }
            }

            @Override
            public void onFailure(Call<CompinionReponseServer> call, Throwable t) {
                Log.d("failed", "Failed: "+ t.getMessage());
            }
        });
    }

/*   Update des données à l'objet

    private class UpdateTask implements Runnable {
        LoggedUserViewModel logged;

        // Constructeur personnalisé pour passer les paramètres
        public UpdateTask(LoggedUserViewModel logged) {
            this.logged = logged;
        }

        @Override
        public void run() {
            String pseudo = logged.getUserMutableLiveData().getValue().getPseudo();
            int merit = logged.getUserMutableLiveData().getValue().getMerite();
            int streak = logged.getUserMutableLiveData().getValue().getJours();
            String urlimg = logged.getUserMutableLiveData().getValue().getCompanionPNG();
            Log.d("allo", "allo");
            InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
            Call<Integer> call = serveur.getJours(logged.getUserMutableLiveData().getValue().getId());
            call.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String s = response.body().toString();
                        nbStreak.setText(s);
                        connexion(pseudo, String.valueOf(merit), urlimg, String.valueOf(streak));
                        client.toAsync().disconnect();
                    } else {
                        Log.d("failed", "La réponse est null");
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Log.d("failed", t.getMessage());
                }
                });
            }
        };*/
    }
