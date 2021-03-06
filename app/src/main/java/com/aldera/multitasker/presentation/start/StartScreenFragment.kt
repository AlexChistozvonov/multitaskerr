package com.aldera.multitasker.presentation.start

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aldera.multitasker.R
import com.aldera.multitasker.databinding.StartScreenFragmentBinding
import com.aldera.multitasker.ui.extension.navigateSafe
import com.aldera.multitasker.ui.util.PreferencesKey
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StartScreenFragment : Fragment(R.layout.start_screen_fragment) {
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private val binding by viewBinding(StartScreenFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() = with(binding) {
        mlAnimation.addTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
                // no op
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                // no op
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                if (currentId == R.id.end_dot) {
                    navigateToApp()
                }
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
                // no op
            }
        })
    }

    private fun navigateToApp() {
        val token = sharedPreferences.getString(PreferencesKey.ACCESS_TOKEN, "")
        if (token.isNullOrEmpty()) {
            findNavController().navigateSafe(StartScreenFragmentDirections.openLaunchFragment())
        } else {
            findNavController().navigateSafe(StartScreenFragmentDirections.openProfileFragment())
        }
    }
}
