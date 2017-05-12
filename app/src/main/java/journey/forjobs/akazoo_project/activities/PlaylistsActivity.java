package journey.forjobs.akazoo_project.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import journey.forjobs.akazoo_project.R;
import journey.forjobs.akazoo_project.database.DBTableHelper;
import journey.forjobs.akazoo_project.database.PlaylistContentProvider;
import journey.forjobs.akazoo_project.listadapters.PlaylistsListAdapter;
import journey.forjobs.akazoo_project.model.Playlist;
import journey.forjobs.akazoo_project.utils.Const;

public class PlaylistsActivity extends AkazooActivity {

    private MyMessageReceiver mMessageReceiver = new MyMessageReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            if (intent.getAction().equals(Const.SERVICE_BIND)) {
                getAkazooController().getPlaylists();
            } else if (message.equals(Const.REST_PLAYLISTS_SUCCESS)) {
                updatePlaylistList();
            }
        }
    };

    @InjectView(R.id.playlist_list)
    ListView mPlaylistsList;

    private void updatePlaylistList() {
        ArrayList<Playlist> allPlaylists = new ArrayList<>();
        Cursor mCursor;

        //used to know which columns name we need to retrieve
        String[] mProjection = {
                DBTableHelper.COLUMN_PLAYLISTS_NAME,
                DBTableHelper.COLUMN_PLAYLISTS_TRACK_COUNT
        };

        //queries the database
        mCursor = getContentResolver().query(
                PlaylistContentProvider.CONTENT_URI,  // The content URI of the words table - You need to use TracksContentProvider.CONTENT_URI to test yours
                mProjection,                       // The columns to return for each row
                null,
                null,
                null);

        if (mCursor != null) {
            while (mCursor.moveToNext()) {
                //create empty object
                Playlist playlist = new Playlist();

                String playlistName = mCursor.getString(mCursor.getColumnIndex(DBTableHelper.COLUMN_PLAYLISTS_NAME));
                int numberOfTracks = mCursor.getInt(mCursor.getColumnIndex(DBTableHelper.COLUMN_PLAYLISTS_TRACK_COUNT));

                playlist.setName(playlistName);
                playlist.setItemCount(numberOfTracks);

                Log.d("MA TAG", "the track name is " + playlist.getName());
                Log.d("MA TAG", "the track artist name is  " + Integer.toString(playlist.getItemCount()));

                allPlaylists.add(playlist);
            }

            final PlaylistsListAdapter mPlaylistsListAdapter = new PlaylistsListAdapter(this, allPlaylists);
            mPlaylistsList.setAdapter(mPlaylistsListAdapter);

            mPlaylistsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    showSnackbar("You have clicked on: " + mPlaylistsListAdapter.getPlaylists().
                            get(position).getName());

                    // TODO Create intent for launching tracks activity
                }

            });
        }
    }

    protected void showSnackbar(String message) {
        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.root),
                message, Snackbar.LENGTH_LONG);
        mySnackbar.show();
    }

    @Override
    protected MyMessageReceiver getmMessageReceiver() {
        return mMessageReceiver;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlists);
        ButterKnife.inject(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(getmMessageReceiver(),
                new IntentFilter(Const.SERVICE_BIND));
    }

}
