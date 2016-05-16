package roulette.overwatchroulette.favorites;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import roulette.overwatchroulette.R;
import roulette.overwatchroulette.maps.MapInformation;
import roulette.overwatchroulette.maps.MapsActivity;
import roulette.overwatchroulette.maps.MapsListAdapter;
import roulette.overwatchroulette.maps.TeamAdapterView;
import roulette.overwatchroulette.navigation.NavBaseActivity;
import roulette.overwatchroulette.roulette.StratRouletteActivity;

public class FavoritesActivity extends NavBaseActivity {
    public static FavoritesDB db;
    ArrayAdapter<String> adapter;
    MapInformation.MAP_STATE state;
    String mapSelected;
    String teamSelected;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(FavoritesActivity.db == null){
            FavoritesActivity.db = new FavoritesDB(this);
            FavoritesActivity.db.open();

            fillWithDummys();
        }
        setContentView(R.layout.activity_maps);
        listView = (ListView) findViewById(R.id.listView);
        adapter = new MapsListAdapter(this);
        listView.setAdapter(adapter);
        state = MapInformation.MAP_STATE.MAP_SELECTION;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (state == MapInformation.MAP_STATE.MAP_SELECTION) {
                    mapSelected = adapter.getItem(position);
                    adapter = new TeamAdapterView(view.getContext(), MapInformation.getDrawable(mapSelected));
                    listView.setAdapter(adapter);
                    state = MapInformation.MAP_STATE.TEAM_SELECTION;
                } else if (state == MapInformation.MAP_STATE.TEAM_SELECTION) {
                    if (position < 3) {
                        teamSelected = adapter.getItem(position);
                        Cursor c = FavoritesActivity.db.getStrats(mapSelected, teamSelected);
                        //Cursor c = FavoritesActivity.db.getAllRows();
                        setUpList(c);
                        state = MapInformation.MAP_STATE.STRAT_SELECTION;
                    } else {
                        adapter = new MapsListAdapter(getApplicationContext());
                        listView.setAdapter(adapter);
                        state = MapInformation.MAP_STATE.MAP_SELECTION;
                    }
                }else if (state == MapInformation.MAP_STATE.STRAT_SELECTION){
                    if(position < adapter.getCount() - 1) {
                        Intent i = new Intent(getApplicationContext(), StratRouletteActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("map", mapSelected);
                        bundle.putString("team", teamSelected);
                        bundle.putString("description", adapter.getItem(position));
                        i.putExtras(bundle);
                        startActivity(i);
                    }else{
                        adapter = new TeamAdapterView(getApplicationContext(), MapInformation.getDrawable(mapSelected));
                        listView.setAdapter(adapter);
                        state = MapInformation.MAP_STATE.TEAM_SELECTION;
                    }
                }
            }
        });
        activateNavBar();
    }

    public void setUpList(Cursor c){
        ArrayList<String> list = new ArrayList<String>();
        Toast.makeText(this, "NUMBER OF RESULTS " + c.getCount(),Toast.LENGTH_LONG).show();
        while(!c.isAfterLast()){
            list.add(c.getString(c.getColumnIndex(FavoritesDB.KEY_DESCRIPTION)));
            c.moveToNext();
        }
        list.add("Go Back");
        adapter = new FavoritesListAdapter(this, list);
        listView.setAdapter(adapter);
    }

    void fillWithDummys(){
        Cursor c = FavoritesActivity.db.getAllRows();
        String team = "Both";
        String strat = "Throw flash bangs";
        if(c.getCount() < 1){
            String[] maps= getResources().getStringArray(R.array.map_names);
            for(int i = 0; i < maps.length; i++){
                FavoritesActivity.db.insertRow(i,maps[i], team, strat);
            }
        }
    }

    @Override
    public void onDestroy(){
        FavoritesActivity.db.close();
        super.onDestroy();
    }

    @Override
    public void onBackPressed(){
        if(state == MapInformation.MAP_STATE.TEAM_SELECTION){
            adapter = new MapsListAdapter(getApplicationContext());
            listView.setAdapter(adapter);
            state = MapInformation.MAP_STATE.MAP_SELECTION;
        }else if(state == MapInformation.MAP_STATE.MAP_SELECTION){
            Intent i = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(i);
        }else if(state == MapInformation.MAP_STATE.STRAT_SELECTION){
            adapter = new TeamAdapterView(getApplicationContext(), MapInformation.getDrawable(mapSelected));
            listView.setAdapter(adapter);
            state = MapInformation.MAP_STATE.TEAM_SELECTION;
        }
    }

}