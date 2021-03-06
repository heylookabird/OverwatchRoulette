package roulette.overwatchroulette.navigation;

/**
 * Created by Harjit on 5/14/2016.
 */

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import roulette.overwatchroulette.R;
import roulette.overwatchroulette.navigation.NavDrawer;

/**
 * This adapter provides access to the data items.
 * Class also handle how classes that extend NavBaseActivity are involved with showing
 * the navigation drawer.
 */

public class NavDrawerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavDrawer> navDrawers;

    public NavDrawerAdapter(Context context, ArrayList<NavDrawer> navDrawers){
        this.context = context;
        this.navDrawers = navDrawers;
    }

    @Override
    public int getCount() {
        return navDrawers.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     *
     * @param position position of object on the drawer list
     * @param convertView View that is displayed
     * @param parent
     * @return Updated View that reflects changes to drawer
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }

        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Linux_Libertine.ttf");
        txtTitle.setTypeface(font);
        //settings icons and title
        txtTitle.setText(navDrawers.get(position).getTitle());

        return convertView;
    }

}
