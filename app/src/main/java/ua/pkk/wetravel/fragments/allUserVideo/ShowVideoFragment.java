package ua.pkk.wetravel.fragments.allUserVideo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ua.pkk.wetravel.R;
import ua.pkk.wetravel.databinding.FragmentShowVideoBinding;

public class ShowVideoFragment extends Fragment {
    private FragmentShowVideoBinding binding;
    private ShowVideoFragmentViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_show_video, container, false);
        viewModel = new ViewModelProvider(this).get(ShowVideoFragmentViewModel.class);
        viewModel.is_loaded.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean)
                binding.loadVideoPb.setVisibility(View.GONE);
        });
        VideoAdapter adapter = new VideoAdapter();
        RecyclerView recyclerView = binding.videos;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        viewModel.videos.observe(getViewLifecycleOwner(), videos -> {
            //TODO adapter must work with Live data
            adapter.submitList(videos);
            adapter.notifyDataSetChanged();
        });
        viewModel.loadVideo();
        return binding.getRoot();
    }

}