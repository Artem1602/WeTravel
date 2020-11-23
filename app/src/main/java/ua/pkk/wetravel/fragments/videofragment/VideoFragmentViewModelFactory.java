package ua.pkk.wetravel.fragments.videofragment;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ua.pkk.wetravel.utils.Video;

public class VideoFragmentViewModelFactory implements ViewModelProvider.Factory {
    private Video video;

    public VideoFragmentViewModelFactory(Video video) {
        this.video = video;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(VideoFragmentViewModel.class)) {
            return (T) new VideoFragmentViewModel(video);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
