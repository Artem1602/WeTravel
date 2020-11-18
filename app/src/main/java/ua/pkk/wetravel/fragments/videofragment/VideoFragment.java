package ua.pkk.wetravel.fragments.videofragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.FragmentVideoBinding;

//TODO Load video into videoView. Try to stream it. (maybe byte[])...
public class VideoFragment extends Fragment {
    private FragmentVideoBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_video,container,false);
        VideoFragmentArgs args = VideoFragmentArgs.fromBundle(getArguments());
        binding.setVideo(args.getVideo());
        return binding.getRoot();
    }
}