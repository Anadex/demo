package com.anadex.recyclerview.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.anadex.recyclerview.R
import com.anadex.recyclerview.data.PhotoDTO
import com.anadex.recyclerview.databinding.FragmentStartBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StartViewModel by viewModels()
    private val myAdapter = MyAdapter { onItemClick(it) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = myAdapter.withLoadStateFooter(MyLoadStateAdapter())

        viewModel.pagedPhotosFlow.onEach {
            myAdapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.swipeRefresh.setOnRefreshListener {
            myAdapter.refresh()
        }

        myAdapter.loadStateFlow.onEach {
            binding.swipeRefresh.isRefreshing = it.refresh == LoadState.Loading
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun onItemClick(item: PhotoDTO){
        val bundle: Bundle = Bundle().apply {  putParcelable("photoDtoItem", item)}
        findNavController().navigate(R.id.zoomFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}