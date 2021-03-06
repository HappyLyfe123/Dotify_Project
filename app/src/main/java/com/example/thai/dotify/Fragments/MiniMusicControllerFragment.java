package com.example.thai.dotify.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.thai.dotify.R;
import com.example.thai.dotify.StartUpContainer;

/**
 * this object allows the user to play a song
 */
public class MiniMusicControllerFragment extends Fragment implements View.OnClickListener{

    private ImageButton playPauseMusicButton;
    private TextView songTitleInfo;
    private ConstraintLayout currLayout;
    private OnChangeFragmentListener onChangeFragmentListener;

    /**
     * object's own interface
     */
    public interface OnChangeFragmentListener {
        void buttonClicked(StartUpContainer.AuthFragmentType fragmentType);
    }

    /**
     * Sets the OnChangeFragmentListener to communicate from this fragment to the activity
     * @param onChangeFragmentListener The listener for communication
     */
    public void setOnChangeFragmentListener(OnChangeFragmentListener onChangeFragmentListener) {
        this.onChangeFragmentListener = onChangeFragmentListener;
    }

    /***
     * instantiate new object
     * @return new MiniMusicControllerFragment object
     */
    public static MiniMusicControllerFragment newInstance(){
        MiniMusicControllerFragment fragment = new MiniMusicControllerFragment();

        return fragment;
    }

    /***
     * create View object of fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return new View object
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mini_music_controller, container, false);

        // Initialize view layout
        playPauseMusicButton = (ImageButton) view.findViewById(R.id.mini_music_player_play_pause_image_button);
        playPauseMusicButton.setOnClickListener(this);
        songTitleInfo = (TextView) view.findViewById(R.id.mini_music_player_song_info_text_view);


        return view;
    }

    /**
     * add extra features to fragment after it's created
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    /**
     * add information about app's environment
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        System.out.println("Yes");
/*        try{
            onChangeFragmentListener = (OnChangeFragmentListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString());
        }*/
    }

    /***
     * invoked when one of the buttons on the page is selected
     * @param v - View object displaying the buttons
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mini_music_player_play_pause_image_button:
                //Check the state of the music player

//                if(PlayingMusicController.getSongPlayingStatus()){
//                    PlayingMusicController.setSongPlayingStatus(false);
//                }
//                else{
//                    PlayingMusicController.setSongPlayingStatus(true);
//                }
                changeMusicPlayerButtonImage();
                break;
        }
    }

    //Change the button image according to the music state
    public void changeMusicPlayerButtonImage(){
//        if(PlayingMusicController.getSongPlayingStatus()){
//            //Pause the music and show the play button
//            playPauseMusicButton.setImageResource(R.drawable.mini_pause_button_icon);
//        }
//        else{
//            //Play the music and show the pause button
//            playPauseMusicButton.setImageResource(R.drawable.mini_play_button_icon);
//        }
    }
}
