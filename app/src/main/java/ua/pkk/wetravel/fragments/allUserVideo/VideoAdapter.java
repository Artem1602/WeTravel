package ua.pkk.wetravel.fragments.allUserVideo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.ItemVideoBinding;
import ua.pkk.wetravel.utils.Keys;
import ua.pkk.wetravel.utils.Video;

import static ua.pkk.wetravel.utils.Video.DIFF_CALLBACK;

public class VideoAdapter extends ListAdapter<Video, VideoAdapter.VideoHolder> {
    private final Context context;

    protected VideoAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemVideoBinding itemView = ItemVideoBinding.inflate(inflater, parent, false);
        return new VideoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        Video video = getItem(position);
        holder.bind(video);
    }

    class VideoHolder extends RecyclerView.ViewHolder {
        private final ItemVideoBinding binding;

        public VideoHolder(@NonNull ItemVideoBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
            itemView.getRoot().setOnClickListener(v ->
                    Navigation.findNavController((Activity) itemView.getRoot().getContext(), R.id.nav_host_fragment)
                            .navigate(ShowVideoFragmentDirections.actionShowVideoFragmentToVideoFragment(itemView.getVideoItem(), Keys.VIDEO_FROM_ADAPTER.getValue())));
        }

        public void bind(Video item) {
            binding.setVideoItem(item);
            Glide.with(context)
                    .load(item.getUri())
                    .apply(new RequestOptions().placeholder(R.drawable.progress_bar_animation).error(R.drawable.video_editor))
                    .into(binding.imageItem);

            binding.executePendingBindings();
        }
    }
}
