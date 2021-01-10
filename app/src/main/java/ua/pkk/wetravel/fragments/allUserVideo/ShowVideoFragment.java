package ua.pkk.wetravel.fragments.allUserVideo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.ViewUtils;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.FragmentShowVideoBinding;
import ua.pkk.wetravel.utils.Video;

public class ShowVideoFragment extends Fragment {
    private FragmentShowVideoBinding binding;
    private VideoAdapter adapter;
    private ShowVideoFragmentViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_show_video, container, false);
        viewModel = new ViewModelProvider(this).get(ShowVideoFragmentViewModel.class);

        adapter = new VideoAdapter(getContext());
        RecyclerView recyclerView = binding.videos;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.videos.observe(getViewLifecycleOwner(), new Observer<List<Video>>() {
            @Override
            public void onChanged(List<Video> videos) {
                if (videos != null){
                    binding.showVideosPb.setVisibility(View.GONE);
                    adapter.submitList(videos);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        viewModel.loadVideo();

        return binding.getRoot();
    }
}