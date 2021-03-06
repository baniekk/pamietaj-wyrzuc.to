package pl.mod3city.powiadomienia.wyrzucto.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import pl.mod3city.powiadomienia.wyrzucto.api.JSONParser;
import pl.mod3city.powiadomienia.wyrzucto.res.JsonResponse;
import pl.mod3city.powiadomienia.wyrzucto.R;
import pl.mod3city.powiadomienia.wyrzucto.api.RestClient;
import pl.mod3city.powiadomienia.wyrzucto.fragments.GlownyFragment;
import pl.mod3city.powiadomienia.wyrzucto.fragments.SegregowanieFragment;
import pl.mod3city.powiadomienia.wyrzucto.fragments.WystawkiFragment;


public class MainTabActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private String[] tabs = {"Zwykłe śmieci", "Wystawki", "Jak segregować"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);


        final Context context = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        //aktualizujDaneJezeliToWymagane(context);

        //Przycisk do pobierania danych ze stony bihapi
   /*     FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Snackbar.make(view, "Pobieram dane z BIHAPI", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


                //Uzyskanie dostępu do menadźera połączeń internetowych i sprwadzenie czy użytkownika
                //ma połaczenie z internetem
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                if (!isConnected) {
                    Snackbar.make(view, "Brak połączenia z internetem", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else {
                    //Po naciśnięciu różowego przycisku odświeżane są dane BIHAPI - zwykłe śmieci
                    RestClient.getInstance().pobierzJsonaOdpadyMokreSucheZmieszaneDanaUlica(new JsonResponse() {
                        //Dzięki temu pieknemu zabiegowi, po pobraniu danych z Resta zostanie wywowołana poniższa metoda
                        @Override
                        public void onJsonResponse(boolean success, JSONObject response) {
                            //Tu możemy parsować Json lub przekazać go do klasy JsonParser do dalszej obróbki
                            Log.i("mainActivity", response.toString());
                            //Wywołanie parsowania
                            if (success) {
                                JSONParser.getInstance().parsowanieOdpadow(context, response);
                                //Przeładowanie widoku po pobraniu danych z BIHAPI
                                Fragment currentFragment = mSectionsPagerAdapter.getItem(0);
                                FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
                                tr.detach(currentFragment);
                                tr.attach(currentFragment);
                                tr.commit();
                            } else {
                                //Serwer zwrócił błąd
                                Snackbar.make(view, "Brak danych do pobrania dla wywozu śmieci. Sprawdź nazwę ulicy w ustawieniach.", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }
                    }, getBaseContext());

                   //Wystawki
                    RestClient.getInstance().pobierzJsonaWystawki(new JsonResponse() {
                        //Dzięki temu pieknemu zabiegowi, po pobraniu danych z Resta zostanie wywowołana poniższa metoda
                        @Override
                        public void onJsonResponse(boolean success, JSONObject response) {
                            //Tu możemy parsować Json lub przekazać go do klasy JsonParser do dalszej obróbki
                            Log.i("mainActivity", response.toString());

                            if (success) {
                                JSONParser.getInstance().parsowanieWystawek(context, response);
                                Fragment currentFragment = mSectionsPagerAdapter.getItem(0);
                                FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
                                tr.detach(currentFragment);
                                tr.attach(currentFragment);
                                tr.commit();
                            }
                            else {
                                //Serwer zwrócił błąd
                                Snackbar.make(view, "Brak danych do pobrania dla wystawek. Sprawdź nazwę ulicy w ustawieniach.", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }
                    }, getBaseContext());
                }
            }
        });*/
    }


    private void aktualizujDaneJezeliToWymagane(Context context){
        SharedPreferences shr = context.getSharedPreferences("wyrzucto_preferences", context.MODE_PRIVATE);
        boolean czyJest = shr.contains("Dzisiejsza_Data");

        Date data = new Date();
        String dzisiejszaDataString = data.toString();

        //Sprawdzenie ze względu na pierwsze uruchomienie aplikacji
        if(czyJest == false){
            Toast.makeText(context, "Ustaw ulicę w ustawieniach, aby pobrać dane.", Toast.LENGTH_LONG).show();
/*            SharedPreferences.Editor editor = shr.edit();
            editor.putString("Dzisiejsza_Data", dzisiejszaDataString);
            editor.commit();*/
        }else {

            String ostatniaData = shr.getString("Dzisiejsza_Data", "0:0");
            if(ostatniaData.equals("0:0")){
                //Błąd w zwracanej wartości, została zwrócona wartośc domyslana
            }else {
                //Jezeli data rózni się od ostatniej aktualizacji to atualizauj
                if( dzisiejszaDataString.compareTo(ostatniaData) != 0 ){
                    new PobieranieWyswietlanie(this,context).execute();
                }
            }
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivityForResult(i, RESULT_OK);

        }else if(id==R.id.action_refresh){
            new PobieranieWyswietlanie(this,getBaseContext()).execute();
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

            private GlownyFragment glo;
            private WystawkiFragment wys;
            private SegregowanieFragment seg;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            seg = new SegregowanieFragment();
            wys = new WystawkiFragment();
            glo = new GlownyFragment();
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) {
                return glo;
            } else if (position == 1) {
                return wys;
            } else if (position == 2) {
                return seg;
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }

    public class PobieranieWyswietlanie extends AsyncTask<Void, Void, Void>{
       Activity wywolujaceActivity;
       Context context;

        public PobieranieWyswietlanie(Activity wywolujaceActivity, Context context){
            this.wywolujaceActivity = wywolujaceActivity;
            this.context = context;
        }


        @Override
        protected Void doInBackground(Void... params) {
/*            Snackbar.make(wywolujaceActivity.findViewById(R.id.container), "Pobieram dane z BIHAPI", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();*/

            //Uzyskanie dostępu do menadźera połączeń internetowych i sprwadzenie czy użytkownika
            //ma połaczenie z internetem
            ConnectivityManager cm = (ConnectivityManager) wywolujaceActivity.getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            if (!isConnected) {
                Snackbar.make(wywolujaceActivity.findViewById(R.id.container), "Brak połączenia z internetem.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            } else {

                //Po naciśnięciu różowego przycisku odświeżane są dane BIHAPI - zwykłe śmieci
                RestClient.getInstance().pobierzJsonaOdpadyMokreSucheZmieszaneDanaUlica(new JsonResponse() {
                    //Dzięki temu pieknemu zabiegowi, po pobraniu danych z Resta zostanie wywowołana poniższa metoda
                    @Override
                    public void onJsonResponse(boolean success, JSONObject response) {
                        //Tu możemy parsować Json lub przekazać go do klasy JsonParser do dalszej obróbki
                        Log.i("mainActivity", response.toString());
                        //Wywołanie parsowania
                        if (success) {
                            JSONParser.getInstance().parsowanieOdpadow(context, response);
                            //Przeładowanie widoku po pobraniu danych z BIHAPI
                            Fragment currentFragment = mSectionsPagerAdapter.getItem(0);
                            FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
                            tr.detach(currentFragment);
                            tr.attach(currentFragment);
                            tr.commit();
                        } else {
                            //Serwer zwrócił błąd
                            Snackbar.make(wywolujaceActivity.findViewById(R.id.container), "Brak danych do pobrania dla wywozu śmieci. Sprawdź nazwę ulicy w ustawieniach.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                }, getBaseContext());

                //Wystawki
                RestClient.getInstance().pobierzJsonaWystawki(new JsonResponse() {
                    //Dzięki temu pieknemu zabiegowi, po pobraniu danych z Resta zostanie wywowołana poniższa metoda
                    @Override
                    public void onJsonResponse(boolean success, JSONObject response) {
                        //Tu możemy parsować Json lub przekazać go do klasy JsonParser do dalszej obróbki
                        Log.i("mainActivity", response.toString());

                        if (success) {
                            JSONParser.getInstance().parsowanieWystawek(context, response);
                            Fragment currentFragment = mSectionsPagerAdapter.getItem(1);
                            FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
                            tr.detach(currentFragment);
                            tr.attach(currentFragment);
                            tr.commit();
                        }
                        else {
                            //Serwer zwrócił błąd
                            Snackbar.make(wywolujaceActivity.findViewById(R.id.container), "Brak danych do pobrania dla wystawek. Sprawdź nazwę ulicy w ustawieniach.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                }, getBaseContext());

            }
            return null;
        }


    }
}