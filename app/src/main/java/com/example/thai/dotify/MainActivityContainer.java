package com.example.thai.dotify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.thai.dotify.Adapters.PlaylistsAdapter;
import com.example.thai.dotify.Adapters.SearchSongAdapter;
import com.example.thai.dotify.Fragments.CreatePlaylistFragment;
import com.example.thai.dotify.Fragments.ForYouFragment;
import com.example.thai.dotify.Fragments.FullScreenMusicControllerFragment;
import com.example.thai.dotify.Fragments.MiniMusicControllerFragment;
import com.example.thai.dotify.Fragments.PlaylistFragment;
import com.example.thai.dotify.Fragments.ProfileInfoFragment;
import com.example.thai.dotify.Fragments.SearchFragment;
import com.example.thai.dotify.Fragments.SongInAlbumFragment;
import com.example.thai.dotify.Fragments.SongsByArtistFragment;
import com.example.thai.dotify.Fragments.SongsListFragment;
import com.example.thai.dotify.Server.DotifySong;
import com.example.thai.dotify.Services.MusicService;
import com.example.thai.dotify.Utilities.SearchArtist;
import com.example.thai.dotify.Utilities.SentToServerRequest;
import com.example.thai.dotify.Utilities.GetFromServerRequest;
import com.example.thai.dotify.Utilities.UserUtilities;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.thai.dotify.MainActivityContainer.DisplayAdapter.SEARCH_SONG_ADAPTER;

/**
 * this object puts together all the parts of the application
 */
public class MainActivityContainer extends AppCompatActivity
        implements PlaylistFragment.OnFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener,
    SongsListFragment.OnFragmentInteractionListener, SongsByArtistFragment.OnFragmentInteractionListener,
    SongInAlbumFragment.OnFragmentInteractionListener{

    private SearchFragment searchFragment;
    private PlaylistFragment playlistFragment;
    private ProfileInfoFragment profileInfoFragment;
    private ForYouFragment forYouFragment;
    private CreatePlaylistFragment createPlaylistFragment;
    private MiniMusicControllerFragment miniMusicControllerFragment;
    private SongsListFragment songListScreenFragment;
    private SongsByArtistFragment songsByArtistFragment;
    private SongInAlbumFragment songInAlbumFragment;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout miniMusicControllerLayout;
    private FrameLayout mainDisplayLayout;
    private boolean isMusicPlaying;
    private PlayingMusicController musicController;
    private DotifyUser user;
    private Context activityContext;
    private SentToServerRequest sentToServerRequest;
    private GetFromServerRequest getFromServerRequest;
    private String TAG = MainActivityContainer.class.getSimpleName();


    //list of pages
    public enum PlaylistFragmentType{
        SEARCH,
        PLAYLISTS,
        FOR_YOU,
        PROFILE,
        CREATE_PLAYLIST,
        SONGS_LIST_PAGE,
        FULL_SCREEN_MUSIC,
        BACK_BUTTON,
        SONGS_BY_ARTIST,
        SONGS_IN_ALBUM,
    }

    public enum DisplayAdapter{
        SEARCH_SONG_ADAPTER,
        SEARCH_ARTIST_ADAPTER,
        SEARCH_ALBUM_ADAPTER
    }

    /***
     * creates the main object
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);

        //Initialize Main Activity Variables
        user = UserUtilities.getCachedUserInfo(this);
        getFromServerRequest = new GetFromServerRequest(getString(R.string.base_URL), getString(R.string.appKey), user.getUsername());
        sentToServerRequest = new SentToServerRequest(getString(R.string.base_URL), getString(R.string.appKey), user.getUsername());

        //Initialize view layout
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        miniMusicControllerLayout = (FrameLayout) findViewById(R.id.mini_music_player_controller_frame);
        mainDisplayLayout = (FrameLayout) findViewById(R.id.main_display_frame);
        //Instantiate fragments
        playlistFragment = PlaylistFragment.newInstance(sentToServerRequest, getFromServerRequest);
        searchFragment = SearchFragment.newInstance(sentToServerRequest, getFromServerRequest);
        songListScreenFragment = SongsListFragment.newInstance(sentToServerRequest, getFromServerRequest);

        forYouFragment = new ForYouFragment();
        profileInfoFragment = new ProfileInfoFragment();
        profileInfoFragment.setOnUserImageUploadedListener((dotifyUser) ->
            // Updates the current user object
            user = dotifyUser
        );

        songListScreenFragment.setOnFragmentInteractionListener(this);
        searchFragment.setOnFragmentInteractionListener(this);
        playlistFragment.setOnFragmentInteractionListener(this);

        miniMusicControllerFragment = miniMusicControllerFragment.newInstance();

        createBottomNavigationView();

        // Send a request to initialize a peer the current Android Client Session
        Intent musicIntent = new Intent(this, MusicService.class);
        musicIntent.setAction(MusicService.LOAD_PEER);
        startService(musicIntent);
    }

    /**
     * Initializes a peer for the client to connect to
     */
    @Override
    protected void onStart() {
        super.onStart();
    }


    /***
     * display playlist, search, and profile fragments via icons
     */
    private BottomNavigationView.OnNavigationItemSelectedListener NavItemListen =
            new BottomNavigationView.OnNavigationItemSelectedListener(){
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.playlists: //user selects playlists
                            startFragment(PlaylistFragmentType.PLAYLISTS, true, false);
                            break;
                        case R.id.search:
                            startFragment(PlaylistFragmentType.SEARCH,true, false);
                            break;
                        case R.id.for_you:
                            startFragment(PlaylistFragmentType.FOR_YOU,true, false) ;
                            break;
                        case R.id.profile:
                            //go to user account
                            startFragment(PlaylistFragmentType.PROFILE,true, false);
                            break;
                    }
                    return true;
                }
            };

    /**
     * Starts a fragment for the user
     * @param fragmentType The id of the fragment that is going to start
     * @return True if the fragment has started correctly
     */
    private boolean startFragment(PlaylistFragmentType fragmentType, boolean setTransition, boolean addToBackStack){

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        switch (fragmentType) {
            case SEARCH:
                fragmentTransaction.replace(mainDisplayLayout.getId(), searchFragment);
                break;
            case PLAYLISTS:
                fragmentTransaction.replace(mainDisplayLayout.getId(), playlistFragment);
                break;
            case FOR_YOU:
                fragmentTransaction.replace(mainDisplayLayout.getId(), forYouFragment);
                break;
            case PROFILE:
                fragmentTransaction.replace(mainDisplayLayout.getId(), profileInfoFragment);
                break;
            case CREATE_PLAYLIST:
                fragmentTransaction.replace(mainDisplayLayout.getId(),createPlaylistFragment);
                break;
            case FULL_SCREEN_MUSIC:
                // Initialize a FulLScreenMusicControllerFragment
                FullScreenMusicControllerFragment fullScreenMusicControllerFragment = new FullScreenMusicControllerFragment();
                // Set the current music controller for the FullScreenMusicControllerFragment
                fullScreenMusicControllerFragment.setMusicController(musicController);
                // set the fragment to be displayed
                fragmentTransaction.replace(mainDisplayLayout.getId(), fullScreenMusicControllerFragment);
                break;
            case SONGS_LIST_PAGE:
                fragmentTransaction.replace(mainDisplayLayout.getId(), songListScreenFragment);
                break;
            case SONGS_BY_ARTIST:
                fragmentTransaction.replace(mainDisplayLayout.getId(), songsByArtistFragment);
                break;
            case SONGS_IN_ALBUM:
                fragmentTransaction.replace(mainDisplayLayout.getId(), songInAlbumFragment);
            case BACK_BUTTON:
                getFragmentManager().popBackStackImmediate();
                break;
        }

        if(setTransition){
            fragmentTransaction.setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }
        //True then the fragment will be added to the backstack
        if(addToBackStack){
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
        return true;
    }

    /**
     * Creates the bottom navigation for the activity container. Sets the home screen as default.
     */
    private void createBottomNavigationView() {
        //Disables automatic shifting from the bottom navigation
        BottomNavigationBarShiftHelp.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.playlists);
        bottomNavigationView.setOnNavigationItemSelectedListener(NavItemListen);
        startFragment(PlaylistFragmentType.PLAYLISTS, false, false);

    }
    /**
     * Create mini music controller view
     */
    private void createMiniMusicControllerView(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(miniMusicControllerLayout.getId(), miniMusicControllerFragment);
        transaction.commit();
    }

    /**
     * Create full screen music player
     */
    public void fullMusicScreenClicked(View view) {
        miniMusicControllerLayout.setVisibility(View.GONE);
        bottomNavigationView.setVisibility(View.GONE);
        startFragment(PlaylistFragmentType.FULL_SCREEN_MUSIC, false, true);
    }

    /**
     * Create a mini screen of music player
     */
    public void miniMusicScreenClicked(View view){
        bottomNavigationView.setVisibility(View.VISIBLE);
        miniMusicControllerLayout.setVisibility(View.VISIBLE);
        miniMusicControllerFragment.changeMusicPlayerButtonImage();
        startFragment(PlaylistFragmentType.BACK_BUTTON, false, false);
    }

    /**
     * Returns the current user object
     */
    public DotifyUser getCurrentUser(){
        return user;
    }

    @Override
    public PlaylistsAdapter getPlaylistAdapter(){
        return playlistFragment.getPlaylistsAdapter();
    }

    /***
     * invoked when a playlist is selected
     * @param playlistName
     */
    @Override
    public void playlistClicked(String playlistName) {
        songListScreenFragment.setPlaylistTitle(playlistName);
        startFragment(PlaylistFragmentType.SONGS_LIST_PAGE, true, true);
    }

    /***
     * Invoked when back button is pressed
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startFragment(PlaylistFragmentType.BACK_BUTTON, false,false);
    }

    @Override
    public void backButtonPressed(){
        startFragment(PlaylistFragmentType.BACK_BUTTON, false, false);
    }

    /**
     * A song been selected
     * @param songGUID the guid of the song
     */
    @Override
    public void onSongClicked(String songGUID) {
        Intent startSongIntent = new Intent(this, MusicService.class);
        startSongIntent.putExtra("guid", songGUID);
        startSongIntent.setAction(MusicService.START_SONG);
        startService(startSongIntent);
    }

    /**
     * Play song
     * @param selectedSongPosition the song that will be playing
     * @param songsList the list of the song
     */
    @Override
    public void onSongClicked(int selectedSongPosition, HashMap<Integer, DotifySong> songsList) {


    }

    /**
     * User select an artist
     * @param artistName name of the artist
     * @param currArtistInfo artist information
     */
    @Override
    public void onArtistResultClicked(String artistName, JsonElement currArtistInfo) {
        songsByArtistFragment = SongsByArtistFragment.newInstance(sentToServerRequest, getFromServerRequest,
                artistName, currArtistInfo);
        songsByArtistFragment.setOnFragmentInteractionListener(this);
        startFragment(PlaylistFragmentType.SONGS_BY_ARTIST, true, true);
    }

    /**
     * The user select on an album
     * @param albumName name of the album
     * @param artistName the artist of the album
     * @param albumSongsList all of the song in the album
     */
    @Override
    public void onAlbumResultClicked(String albumName, String artistName, JsonArray albumSongsList){
        songInAlbumFragment = SongInAlbumFragment.newInstance(sentToServerRequest, getFromServerRequest,
                albumName, artistName, albumSongsList);
        songInAlbumFragment.setOnFragmentInteractionListener(this);
        startFragment(PlaylistFragmentType.SONGS_IN_ALBUM, true, true);
    }

    /**
     * Update the recycler view list in SearchFragment
     * @param updateAdapter SearchFragment class
     * @param updateDisplay Which adapter to update
     */
    @Override
    public void updateAdapterView(SearchFragment updateAdapter, DisplayAdapter updateDisplay) {
        runOnUiThread(new Thread(()-> {
            try {
                switch (updateDisplay) {
                    case SEARCH_SONG_ADAPTER:
                        updateAdapter.notifyRecyclerDataInsertedChanged(SearchFragment.RECYCLER_TYPE.SEARCH_SONG);
                        updateAdapter.changeQueryLayoutStates(SearchFragment.RECYCLER_TYPE.SEARCH_SONG);
                        break;
                    case SEARCH_ARTIST_ADAPTER:
                        updateAdapter.notifyRecyclerDataInsertedChanged(SearchFragment.RECYCLER_TYPE.SEARCH_ARTIST);
                        updateAdapter.changeQueryLayoutStates(SearchFragment.RECYCLER_TYPE.SEARCH_ARTIST);
                        break;
                    case SEARCH_ALBUM_ADAPTER:
                        updateAdapter.notifyRecyclerDataInsertedChanged(SearchFragment.RECYCLER_TYPE.SEARCH_ALBUM);
                        updateAdapter.changeQueryLayoutStates(SearchFragment.RECYCLER_TYPE.SEARCH_ALBUM);
                        break;
                }
            }
            catch (Exception ex){

            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Kills the peer object since we don't need it anymore because the application is closing
        MusicService.killPeer();
    }
}
