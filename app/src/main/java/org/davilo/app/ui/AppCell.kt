package org.davilo.app.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import org.davilo.app.R
import org.davilo.app.databinding.AppCellBinding
import org.davilo.app.model.App
import org.davilo.app.model.AppType

class AppCell(
    context: Context
) : FrameLayout(context) {
    var app: App? = null
    val binding = AppCellBinding.inflate(
        LayoutInflater.from(context),
        null,
        false
    )

    init {
        addView(
            binding.root,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

    }

    fun bindCategory(app: App?) {
        this.app = app
        binding.app = app
        if (app?.done == true) {
            binding.imageViewCheck.setImageResource(R.drawable.checked)
        } else {
            binding.imageViewCheck.setImageDrawable(null)
        }
        when (app?.type) {
            AppType.GroupAssignment -> {
                binding.imageViewApp.setImageResource(R.drawable.groupassignment)
            }
            AppType.FreeTextInput -> {
                binding.imageViewApp.setImageResource(R.drawable.ic_app_freetextinput)
            }
            AppType.SimpleOrder -> {
                binding.imageViewApp.setImageResource(R.drawable.ic_app_simple_order)
            }
            AppType.MatchingPairs -> {
                binding.imageViewApp.setImageResource(R.drawable.ic_app_matchpairing)
            }
            AppType.MatchingPairsOnImage -> {
                binding.imageViewApp.setImageResource(R.drawable.ic_app_matchpairing)
            }

            AppType.MultipleChoiceQuiz -> {
                binding.imageViewApp.setImageResource(R.drawable.ic_app_multiple_choicequiz)
            }
            AppType.ClozeText -> {
                binding.imageViewApp.setImageResource(R.drawable.ic_app_cloze_text)
            }
            AppType.GroupPuzzle -> {
                binding.imageViewApp.setImageResource(R.drawable.ic_app_group_puzzle)
            }
            AppType.Crossword -> {
                binding.imageViewApp.setImageResource(R.drawable.ic_app_crossword)
            }
            AppType.MarkInTexts -> {
                binding.imageViewApp.setImageResource(R.drawable.ic_app_mark_in_text)
            }
            AppType.FillTable -> {
                binding.imageViewApp.setImageResource(R.drawable.ic_app_filltable)
            }
            AppType.TheMillionAirGame -> {
                binding.imageViewApp.setImageResource(R.drawable.ic_app_mlioner)
            }
            AppType.WordGrid -> {
                binding.imageViewApp.setImageResource(R.drawable.ic_app_wordgrid)
            }
            AppType.HangMan -> {
                binding.imageViewApp.setImageResource(R.drawable.ic_app_hangman)
            } AppType.QuizWithTextInput -> {
            binding.imageViewApp.setImageResource(R.drawable.ic_app_quiz_with_textinput)
        }
            else -> {
                binding.imageViewApp.setImageDrawable(null);
            }
        }

    }
}