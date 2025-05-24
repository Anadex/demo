package com.example.feedback_quiz.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.addCallback
import androidx.core.view.ViewCompat.generateViewId
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.example.feedback_quiz.R
import com.example.feedback_quiz.databinding.FragmentQuestionsBinding
import com.example.feedback_quiz.quiz.Quiz
import com.example.feedback_quiz.quiz.QuizStorage
import com.google.android.material.divider.MaterialDivider
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.textview.MaterialTextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [QuestionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuestionsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentQuestionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        val inflater = TransitionInflater.from(requireContext())

        exitTransition =
            inflater.inflateTransition(R.transition.exit_transition_for_question_fragment)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentQuestionsBinding.inflate(inflater, container, false)

        val radioGroupLayout = binding.rgLayout
        val radioGroupIDList = mutableListOf<Int>()

        val quiz: Quiz = QuizStorage.getQuiz()


        for (n in quiz.questions.indices) {

            val questionRadioGroupView = RadioGroup(this.context)
            questionRadioGroupView.id = generateViewId()
            radioGroupIDList.add(questionRadioGroupView.id)

            val question = quiz.questions[n]
            val questionTitleTextView = MaterialTextView(this.requireContext()).apply {
                text = question.question
                setTextAppearance(R.style.questionTitle)
                setPadding(0, 50, 0, 10)
            }

            questionRadioGroupView.addView(questionTitleTextView) //Добавляем заголовок (содержит вопрос)

            for (i in question.answers.indices) {
                val answerRadioButtonView = MaterialRadioButton(this.requireContext()).apply {
                    text = question.answers[i]
                    id = generateViewId()
                    alpha = 0f
                    if (i == 0) isChecked = true
                }
                answerRadioButtonView.animate().apply {
                    duration = 4000
                    alpha(1f)
                }

                questionRadioGroupView.addView(answerRadioButtonView) //Добавляем варианты ответов
            }

            questionRadioGroupView.addView(MaterialDivider(this.requireContext())) // Добавляем разделитель

            //Добавляем готовую радиогруппу с вопросом
            radioGroupLayout.addView(questionRadioGroupView, n)

        }

        binding.back.setOnClickListener {
            findNavController().popBackStack(R.id.startFragment, false)
        }

        binding.send.setOnClickListener {
            val answersList = mutableListOf<String>()
            for (i in radioGroupIDList.indices) {
                val rbID =
                    requireView().findViewById<RadioGroup>(radioGroupIDList[i]).checkedRadioButtonId
                val answer = requireView().findViewById<RadioButton>(rbID)?.text
                    ?: getText(R.string.no_answer)
                answersList.add(answer.toString())
            }

            val numsOfAnswers = mutableListOf<Int>()
            for (i in radioGroupIDList.indices) {

                val rbID =
                    requireView().findViewById<RadioGroup>(radioGroupIDList[i]).checkedRadioButtonId
                val answer = requireView().findViewById<RadioButton>(rbID)?.text

                quiz.questions[i].answers.forEachIndexed { index, ans ->
                    if (ans == answer) numsOfAnswers.add(index)
                }

            }

            val resultList = QuizStorage.answer(quiz, numsOfAnswers)

            val action =
                QuestionsFragmentDirections.actionQuestionFragmentToResultFragment(
                    answersList.toTypedArray(),
                    resultList
                )
            findNavController().navigate(action)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack(R.id.startFragment, false)
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = resources.getText(R.string.title_of_questions_fragment)
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