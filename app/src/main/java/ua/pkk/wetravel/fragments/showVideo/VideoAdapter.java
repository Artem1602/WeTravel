package ua.pkk.wetravel.fragments.showVideo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.ItemVideoBinding;
import ua.pkk.wetravel.utils.Video;

import static ua.pkk.wetravel.utils.Video.DIFF_CALLBACK;

public class VideoAdapter extends ListAdapter<Video, VideoAdapter.VideoHolder> {

    protected VideoAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return VideoHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        Video video = getItem(position);
        holder.bind(video);
    }


    static class VideoHolder extends RecyclerView.ViewHolder {
        @SuppressLint("StaticFieldLeak")
        private static ItemVideoBinding binding;

        public VideoHolder(@NonNull ItemVideoBinding itemView) {
            super(itemView.getRoot());
            itemView.getRoot().setOnClickListener(v -> Navigation.findNavController((Activity) itemView.getRoot().getContext(), R.id.nav_host_fragment).navigate(ShowVideoFragmentDirections.actionShowVideoFragmentToVideoFragment(itemView.getVideoItem())));
        }

        public void bind(Video item) {
            binding.setVideoItem(item);
            binding.executePendingBindings();
        }

        public static VideoHolder from(ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            binding = ItemVideoBinding.inflate(inflater, parent, false);
            return new VideoHolder(binding);
        }
    }
}
