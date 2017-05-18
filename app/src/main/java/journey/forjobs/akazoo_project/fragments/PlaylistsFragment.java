package journey.forjobs.akazoo_project.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import journey.forjobs.akazoo_project.R;
import journey.forjobs.akazoo_project.activities.TracksActivity;
import journey.forjobs.akazoo_project.database.DBTableHelper;
import journey.forjobs.akazoo_project.database.PlaylistContentProvider;
import journey.forjobs.akazoo_project.listadapters.PlaylistsListAdapter;
import journey.forjobs.akazoo_project.model.Playlist;


public class PlaylistsFragment extends Fragment {

  @InjectView(R.id.playlist_list)
  ListView mPlaylistsList;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View v = inflater.inflate(R.layout.fragment_playlists, container, false);
    ButterKnife.inject(this, v);
    return v;

  }

  public void updatePlaylistList() {
    ArrayList<Playlist> allPlaylists = new ArrayList<>();
    Cursor mCursor;

    //used to know which columns name we need to retrieve
    String[] mProjection = {
        DBTableHelper.COLUMN_PLAYLISTS_NAME,
        DBTableHelper.COLUMN_PLAYLISTS_TRACK_COUNT,
        DBTableHelper.COLUMN_PLAYLISTS_PLAYLIST_ID
    };

    //queries the database
    mCursor = getActivity().getContentResolver().query(
        PlaylistContentProvider.CONTENT_URI,
        // The content URI of the words table - You need to use TracksContentProvider.CONTENT_URI to test yours
        mProjection,                       // The columns to return for each row
        null,
        null,
        null);

    if (mCursor != null) {
      while (mCursor.moveToNext()) {
        //create empty object
        Playlist playlist = new Playlist();

        String playlistName = mCursor
            .getString(mCursor.getColumnIndex(DBTableHelper.COLUMN_PLAYLISTS_NAME));
        int numberOfTracks = mCursor
            .getInt(mCursor.getColumnIndex(DBTableHelper.COLUMN_PLAYLISTS_TRACK_COUNT));
        String playlistId = mCursor
            .getString(mCursor.getColumnIndex(DBTableHelper.COLUMN_PLAYLISTS_PLAYLIST_ID));

        playlist.setName(playlistName);
        playlist.setItemCount(numberOfTracks);
        playlist.setPlaylistId(playlistId);

        Log.d("MA TAG", "the track name is " + playlist.getName());
        Log.d("MA TAG", "the track artist name is  " + Integer.toString(playlist.getItemCount()));

        allPlaylists.add(playlist);
      }

      final PlaylistsListAdapter mPlaylistsListAdapter = new PlaylistsListAdapter(getActivity(),
          allPlaylists);
      mPlaylistsList.setAdapter(mPlaylistsListAdapter);

      mPlaylistsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    showSnackbar("(Fragment) You have clicked on: " + mPlaylistsListAdapter.getPlaylists().
//                            get(position).getName());

          // TODO Check if the server response is correct

          // TODO Create intent for launching tracks activity
          Intent intent = new Intent(getActivity(), TracksActivity.class);
          intent.putExtra("playlistId", mPlaylistsListAdapter.getPlaylists().get(position)
              .getPlaylistId());
          intent.putExtra("playlistTitle", mPlaylistsListAdapter.getPlaylists().get(position)
              .getName());
          startActivity(intent);
        }

      });
    }
  }

  protected void showSnackbar(String message) {
    Snackbar mySnackbar = Snackbar.make(getActivity().findViewById(R.id.root),
        message, Snackbar.LENGTH_LONG);
    mySnackbar.show();
  }

}

