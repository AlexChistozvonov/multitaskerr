package com.aldera.multitasker.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aldera.multitasker.R
import com.aldera.multitasker.databinding.ImagePickerDialogFragmentBinding
import com.aldera.multitasker.ui.extension.onClick
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ImagePickerDialogFragment : BottomSheetDialogFragment() {
    private val binding by viewBinding(ImagePickerDialogFragmentBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.image_picker_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() = with(binding) {
        btnCancel.onClick { findNavController().popBackStack() }
        btnCamera.onClick {
            setFragmentResult(CAMERA, Bundle.EMPTY)
            findNavController().popBackStack()
        }
        btnGallery.onClick {
            setFragmentResult(GALLERY, Bundle.EMPTY)
            findNavController().popBackStack()
        }
        btnDelete.onClick {
            setFragmentResult(DELETE, Bundle.EMPTY)
            findNavController().popBackStack()
        }
    }

    companion object {
        const val CAMERA = "camera"
        const val GALLERY = "gallery"
        const val DELETE = "delete"
    }
}
