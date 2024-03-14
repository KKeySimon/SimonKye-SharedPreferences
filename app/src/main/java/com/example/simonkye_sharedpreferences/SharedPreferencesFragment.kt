package com.example.simonkye_sharedpreferences

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.simonkye_sharedpreferences.databinding.FragmentSharedPreferencesBinding
import kotlinx.coroutines.launch

class SharedPreferencesFragment : Fragment() {
    private var _binding: FragmentSharedPreferencesBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is nul. Is the view visible?"
        }

    val images = arrayOf(
        R.drawable.one,
        R.drawable.two,
        R.drawable.three,
        R.drawable.four,
        R.drawable.five,
        R.drawable.six,
        R.drawable.seven,
        R.drawable.eight,
        R.drawable.nine,
        R.drawable.zero
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSharedPreferencesBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val sharedPreferencesViewModel : SharedPreferencesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                sharedPreferencesViewModel.setQuery(s.toString())
            }
        })

        binding.button.setOnClickListener {
            val randomInd = (images.indices).random()
            val selectedImg = images[randomInd]
            binding.itemImageView.setImageResource(selectedImg)
            sharedPreferencesViewModel.setImgResId(selectedImg)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedPreferencesViewModel.uiState.collect { state ->
                    if (binding.editText.text.isEmpty()) {
                        binding.editText.setText(state.word)
                    }
                    binding.itemImageView.setImageResource(state.image)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}