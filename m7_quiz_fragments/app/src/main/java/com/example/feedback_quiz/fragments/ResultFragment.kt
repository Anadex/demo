package com.example.feedback_quiz.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.feedback_quiz.R
import com.example.feedback_quiz.databinding.FragmentResultBinding
import com.example.feedback_quiz.quiz.QuizStorage
import com.google.android.material.divider.MaterialDivider
import com.google.android.material.textview.MaterialTextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResultFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val args: ResultFragmentArgs by navArgs()

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentResultBinding.inflate(layoutInflater)

        val answersLayout = binding.answersLayout

        for (n in QuizStorage.quizRu.questions.indices) {

            val textViewTitleQuestion = MaterialTextView(this.requireContext()).apply {
                text = getText(R.string.question)
                setTextAppearance(R.style.questionTitle)
                setPadding(0, 50, 0, 10)
            }

            val question = QuizStorage.quizRu.questions[n]
            val textViewQuestion = MaterialTextView(this.requireContext()).apply {
                text = question.question
            }

            val textViewTitleAnswer = MaterialTextView(this.requireContext()).apply {
                text = getText(R.string.your_answer)
                setTextAppearance(R.style.questionTitle)
                setPadding(0, 50, 0, 10)
            }

            val answer = args.answersList[n]
            val textViewAnswer = MaterialTextView(this.requireContext()).apply {
                text = answer
                setPadding(0, 0, 0, 50)
            }

            answersLayout.addView(textViewTitleQuestion)
            answersLayout.addView(textViewQuestion)
            answersLayout.addView(textViewTitleAnswer)
            answersLayout.addView(textViewAnswer)
            answersLayout.addView(MaterialDivider(this.requireContext()))

        }

        val textViewAllAnswers = MaterialTextView(this.requireContext()).apply {
            text = args.answerList1
            setPadding(0, 50, 0, 50)
        }

        answersLayout.addView(textViewAllAnswers)

        binding.restart.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_questionFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack(R.id.startFragment, false)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = "Результаты"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

     companion object {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StartFragment.
     */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}