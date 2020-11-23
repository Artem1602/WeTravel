package ua.pkk.wetravel.fragments.videoItem;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.FragmentVideoBinding;
import ua.pkk.wetravel.utils.Keys;

//TODO Load video into videoView. Try to stream it. (maybe byte[])...
public class VideoFragment extends Fragment {
    private FragmentVideoBinding binding;
    private VideoFragmentViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_video, container, false);
        VideoFragmentArgs args = VideoFragmentArgs.fromBundle(getArguments());
        binding.setVideo(args.getVideo());

        changeUIbbyKey(args);

        VideoFragmentViewModelFactory factory = new VideoFragmentViewModelFactory(args.getVideo());
        viewModel = new ViewModelProvider(this, factory).get(VideoFragmentViewModel.class);
        binding.setVideoViewModel(viewModel);

        //TODO Add play btn or else...
        viewModel.getVideoUri();

        viewModel.videoUri.observe(getViewLifecycleOwner(), uri -> {
            if (uri != null) {
                showVideo(uri);
            }
        });

        viewModel.successDelete.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                closeFragment();
            }
        });
        return binding.getRoot();
    }

    private void closeFragment() {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(VideoFragmentDirections.actionVideoFragmentToShowVideoFragment());
    }

    private void showVideo(Uri uri) {
        MediaController mediaController = new MediaController(getContext());
        mediaController.setAnchorView(binding.videoView);
        binding.videoView.setMediaController(mediaController);
        binding.videoView.setVideoURI(uri);
        binding.videoView.requestFocus();
        binding.videoView.start();
    }

    private void changeUIbbyKey(VideoFragmentArgs args) {
        if (args.getSourceKey() == Keys.VIDEO_FROM_MAP.getValue()) {
            binding.videoActions.setVisibility(View.GONE);
        } else {
            //Do nothing
        }
    }

}