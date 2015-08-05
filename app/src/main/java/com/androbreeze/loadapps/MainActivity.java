package com.androbreeze.loadapps;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private PackageManager manager;
    private List<AppAttributes> applist;
    private ListView list;
    ImageView appIcon;
    TextView appLabel;
    TextView appName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadallApps();
        addtoListView();
        addAppClickListener();
    }

    private void loadallApps(){
        manager = getPackageManager();
        applist = new ArrayList<AppAttributes>();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);
        for(ResolveInfo resinfo:availableActivities){
            AppAttributes app = new AppAttributes();
            app.label = resinfo.loadLabel(manager);
            app.name = resinfo.activityInfo.packageName;
            app.icon = resinfo.activityInfo.loadIcon(manager);
            applist.add(app);
        }
    }

    private void addtoListView(){
        list = (ListView)findViewById(R.id.apps_list);

        ArrayAdapter<AppAttributes> adapter = new ArrayAdapter<AppAttributes>(this,R.layout.list_item,applist) {
            @Override
            public View getView(int position, View itemView, ViewGroup parent) {
                if(itemView == null){
                    itemView = getLayoutInflater().inflate(R.layout.list_item, null);
                }

                appIcon = (ImageView)itemView.findViewById(R.id.item_app_icon);
                appIcon.setImageDrawable(applist.get(position).icon);

                appLabel = (TextView)itemView.findViewById(R.id.item_app_label);
                appLabel.setText(applist.get(position).label);

                appName = (TextView)itemView.findViewById(R.id.item_app_name);
                appName.setText(applist.get(position).name);

                return itemView;
            }
        };

        list.setAdapter(adapter);
    }
    private void addAppClickListener(){
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos,
                                    long id) {
                Intent i = manager.getLaunchIntentForPackage(applist.get(pos).name.toString());
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
