package com.anadex.recyclerview.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.anadex.recyclerview.R
import com.anadex.recyclerview.data.PhotoDTO
import com.anadex.recyclerview.databinding.FragmentZoomBinding
import com.bumptech.glide.Glide

class ZoomFragment : Fragment() {

    private var _binding: FragmentZoomBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentZoomBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val photoDTOItem = arguments?.getParcelable<PhotoDTO>("photoDtoItem")

        with(binding){
            roverName.text = photoDTOItem?.roverName ?: ""
            cameraName.text = photoDTOItem?.cameraName ?: ""
            sol.text = photoDTOItem?.sol.toString()
            date.text = photoDTOItem?.earthDate ?: ""
            photoDTOItem.let{
                Glide
                    .with(root.context)
                    .load(it?.imgSrc)
                    .error(R.drawable.ic_launcher_background)
                    .into(photo)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}