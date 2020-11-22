package ua.pkk.wetravel.fragments.videofragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.FragmentVideoBinding;
import ua.pkk.wetravel.utils.Keys;
import ua.pkk.wetravel.utils.Video;

//TODO Load video into videoView. Try to stream it. (maybe byte[])...
public class VideoFragment extends Fragment {
    private FragmentVideoBinding binding;
    private Video video;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_video, container, false);
        VideoFragmentArgs args = VideoFragmentArgs.fromBundle(getArguments());

        medicateUIbbyKey(args);
        //TODO

        MediaController mediaController = new MediaController(getContext());
        mediaController.setAnchorView(binding.videoView);
        binding.videoView.setMediaController(mediaController);


        binding.setVideo(args.getVideo());
        args.getVideo().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                binding.videoView.setVideoURI(uri);
            }
        });
        binding.videoView.requestFocus();
        binding.videoView.start();
        return binding.getRoot();
    }

    private void medicateUIbbyKey(VideoFragmentArgs args) {
        if (args.getSourceKey() == Keys.VIDEO_FROM_MAP.getValue()){
            binding.videoActions.setVisibility(View.GONE);
        }else {
            //Do nothing
        }
    }

}