package ua.pkk.wetravel.fragments.allUserVideo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.FragmentShowVideoBinding;
import ua.pkk.wetravel.utils.Video;

public class ShowVideoFragment extends Fragment {
    private FragmentShowVideoBinding binding;
    private VideoAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_show_video, container, false);

        adapter = new VideoAdapter();

        RecyclerView recyclerView = binding.videos;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        initVideoList(ShowVideoFragmentArgs.fromBundle(getArguments()));

        return binding.getRoot();
    }

    private void initVideoList(ShowVideoFragmentArgs args) {
        List<Video> videos = Arrays.asList(args.getVideos());
        adapter.submitList(videos);
        adapter.notifyDataSetChanged();
    }
}