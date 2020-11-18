package ua.pkk.wetravel.fragments.showVideo;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import ua.pkk.wetravel.databinding.ItemVideoBinding;
import ua.pkk.wetravel.utils.Video;

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


    static class VideoHolder extends RecyclerView.ViewHolder{
        @SuppressLint("StaticFieldLeak")
        private static ItemVideoBinding binding;

        public VideoHolder(@NonNull ItemVideoBinding itemView) {
            super(itemView.getRoot());
        }

        public void bind(Video item){
            binding.setVideoItem(item);
            binding.executePendingBindings();
        }

        public static VideoHolder from(ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            binding = ItemVideoBinding.inflate(inflater,parent,false);
            return new VideoHolder(binding);
        }
    }


    //TODO put into class VIDEO https://hackathon-blog-42.medium.com/listadapter-renewed-9b5b496198e2
    public static final DiffUtil.ItemCallback<Video> DIFF_CALLBACK = new DiffUtil.ItemCallback<Video>() {
        @Override
        public boolean areItemsTheSame(@NonNull Video oldItem, @NonNull Video newItem) {
            return oldItem.getName().equals(newItem.getName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Video oldItem, @NonNull Video newItem) {
            //TODO oldUser.equals(newUser);
            return oldItem.getName().equals(newItem.getName());
        }
    };
}
