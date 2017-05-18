package journey.forjobs.akazoo_project.listadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import journey.forjobs.akazoo_project.R;
import journey.forjobs.akazoo_project.model.Playlist;

public class PlaylistsListAdapter extends ArrayAdapter<Playlist> {

  List<Playlist> mPlaylists = new ArrayList<>();
  Context context;

  public PlaylistsListAdapter(Context context, List<Playlist> mPlaylists) {
    super(context, R.layout.vw_list_item_track, mPlaylists);
    this.mPlaylists = mPlaylists;
    this.context = context;
  }

  static class ViewHolder {

    @InjectView(R.id.playlist_name)
    TextView playlistName;
    @InjectView(R.id.number_of_tracks)
    TextView numberOfTrucks;

    public ViewHolder(View view) {
      ButterKnife.inject(this, view);
    }
  }

  @Override
  public View getView(int position, View view, ViewGroup parent) {
    ViewHolder holder;
    if (view != null) {
      holder = (ViewHolder) view.getTag();
    } else {
      LayoutInflater inflater = (LayoutInflater) context
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

      view = inflater.inflate(R.layout.vw_list_item_playlist, parent, false);

      holder = new ViewHolder(view);
      view.setTag(holder);
    }
    holder.playlistName.setText(mPlaylists.get(position).getName());
    holder.numberOfTrucks.setText(Integer.toString(mPlaylists.get(position).getItemCount())
        + " tracks");
    return view;
  }


  public List<Playlist> getPlaylists() {
    return mPlaylists;
  }

  public void setPlaylists(List<Playlist> playlists) {
    this.mPlaylists = playlists;
  }
}
