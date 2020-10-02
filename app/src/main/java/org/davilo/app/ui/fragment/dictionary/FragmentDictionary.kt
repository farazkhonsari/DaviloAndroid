package org.davilo.app.ui.fragment.dictionary

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.davilo.app.R
import org.davilo.app.databinding.CellDictionaryBinding
import org.davilo.app.databinding.FragmentDictionaryBinding
import org.davilo.app.model.WordInfo
import org.davilo.app.ui.activity.login.afterTextChanged
import org.davilo.app.ui.adapter.myBindingAdapter.MyBindingAdapter
import org.davilo.app.ui.base.BaseFragment
import org.davilo.app.utils.AndroidUtilities


@AndroidEntryPoint
class FragmentDictionary : BaseFragment<FragmentDictionaryBinding>(), View.OnClickListener {

    override fun getLayoutId() = R.layout.fragment_dictionary

    private val viewModel: FragmentDictionaryViewModel by viewModels()

    private val listResult: ArrayList<WordInfo> by lazy {
        ArrayList<WordInfo>()
    }

    private var mediaPlayer: MediaPlayer? = null
    private var currentSoundLink: String? = null
    private var selectedItemPosition = -1
    private var isExpand = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initMediaPlayer()

        initView()

        observeLiveData()

    }

    private fun initView() {

        showLookupView(true)

        binding.edtSearchBar.afterTextChanged { query ->
            hideAllViews()
            if (query.isEmpty()) {
                showClearView(false)
                showLookupView(true)
            } else {
                showClearView(true)
                showLoading(true)
                viewModel.query(query)
            }
        }


        binding.rvResult.itemAnimator = null
        binding.rvResult.adapter = MyBindingAdapter<WordInfo, CellDictionaryBinding>(
            context = activity,
            items = listResult,
            layout = R.layout.cell_dictionary,
            onBindViewHolder = { item, position, binder ->

                binder.icExpand.setImageResource(R.drawable.ic_expand)
                binder.lytDetails.visibility = View.GONE
                binder.lytTable.visibility = View.GONE
                binder.lytPronunciation.visibility = View.GONE
                binder.icPlay.visibility = View.VISIBLE
                binder.pbPlay.visibility = View.GONE
                binder.lytMeaning.visibility = View.GONE
                binder.lytExamples.visibility = View.GONE
                binder.lytSynonyms.visibility = View.GONE
                binder.lytAntonyms.visibility = View.GONE

                binder.txtWordType.text = item.word_type

                // create table
                if (item.table?.rows.orEmpty().isNotEmpty()) {

                    val weight =
                        1 / (item.table?.rows?.first()?.columns.orEmpty().size + 1).toFloat()

                    binder.lytTable.removeAllViews()
                    binder.lytTable.visibility = View.VISIBLE
                    //create header layout
                    val lytHeader = LinearLayout(activity).apply {
                        orientation = LinearLayout.HORIZONTAL
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        binder.lytTable.addView(this)
                    }

                    //add empty column
                    lytHeader.addView(createTextView(weight = weight))

                    // add header titles
                    item.table?.rows?.first()?.columns?.forEach { column ->
                        lytHeader.addView(
                            createTextView(
                                text = column.titile,
                                isBold = true,
                                weight = weight
                            )
                        )
                    }

                    item.table?.rows?.forEach { row ->

                        //create row layout
                        val lytRow = LinearLayout(activity).apply {
                            orientation = LinearLayout.HORIZONTAL
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            binder.lytTable.addView(this)
                        }

                        //add row title
                        lytRow.addView(
                            createTextView(
                                text = row.title,
                                isBold = true,
                                weight = weight,
                                height = LinearLayout.LayoutParams.MATCH_PARENT,
                                layoutGravity = Gravity.CENTER
                            )
                        )

                        //add other columns
                        row.columns?.forEach { column ->

                            //create each column rows
                            val lytColumnRows = LinearLayout(activity).apply {
                                setBackgroundResource(R.drawable.bg_border_cell_dictionary)
                                orientation = LinearLayout.VERTICAL
                                gravity = Gravity.CENTER
                                layoutParams = LinearLayout.LayoutParams(
                                    0,
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    weight
                                )
                                lytRow.addView(this)
                            }

                            //add column elements
                            column.elements?.forEach { element ->
                                lytColumnRows.addView(
                                    createTextView(
                                        text = element,
                                        addBackground = false,
                                        width = LinearLayout.LayoutParams.MATCH_PARENT
                                    )
                                )
                            }

                        }

                    }

                    item.table?.tail?.let { tail ->

                        //create tail layout
                        val lytRowTail = LinearLayout(activity).apply {
                            orientation = LinearLayout.HORIZONTAL
                            setBackgroundResource(R.drawable.bg_border_cell_dictionary)
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            binder.lytTable.addView(this)
                        }
                        val tailTextView = createTextView(
                            text = tail.text,
                            textColor = R.color.colorPrimary,
                            isBold = true,
                            width = LinearLayout.LayoutParams.MATCH_PARENT
                        )
                        tailTextView.setOnClickListener {
                            //TODO: open url
                            Toast.makeText(activity, tail.link, Toast.LENGTH_SHORT).show()
                        }
                        lytRowTail.addView(tailTextView)
                    }

                }

                item.pronunciation?.let { soundLink ->

                    binder.lytPronunciation.visibility = View.VISIBLE

                    binder.icPlay.setOnClickListener {
                        playAudio(soundLink, binder.pbPlay, binder.icPlay)
                    }
                }

                if (item.meanings.orEmpty().isNotEmpty()) {

                    binder.lytMeaning.visibility = View.VISIBLE
                    binder.lytMeaningRows.removeAllViews()
                    item.meanings?.forEach { meaning ->
                        binder.lytMeaningRows.addView(
                            createTextView(
                                text = meaning,
                                gravity = Gravity.START,
                                addBackground = false,
                                width = LinearLayout.LayoutParams.MATCH_PARENT
                            )
                        )
                    }
                }

                if (item.examples.orEmpty().isNotEmpty()) {

                    binder.lytExamples.visibility = View.VISIBLE
                    binder.lytExamplesRows.removeAllViews()
                    item.examples?.forEach { example ->
                        binder.lytExamplesRows.addView(
                            createTextView(
                                text = example,
                                gravity = Gravity.START,
                                addBackground = false,
                                width = LinearLayout.LayoutParams.MATCH_PARENT
                            )
                        )
                    }
                }

                if (item.synonyms_elements.orEmpty().isNotEmpty()) {

                    binder.lytSynonyms.visibility = View.VISIBLE
                    binder.lytSynonymsRows.removeAllViews()
                    item.synonyms_elements?.forEach { synonym ->
                        binder.lytSynonymsRows.addView(
                            createTextView(
                                text = synonym,
                                gravity = Gravity.START,
                                addBackground = false,
                                width = LinearLayout.LayoutParams.MATCH_PARENT
                            )
                        )
                    }
                }

                if (item.antonyms_elements.orEmpty().isNotEmpty()) {

                    binder.lytAntonyms.visibility = View.VISIBLE
                    binder.lytAntonymsRows.removeAllViews()
                    item.antonyms_elements?.forEach { antonym ->
                        binder.lytAntonymsRows.addView(
                            createTextView(
                                text = antonym,
                                gravity = Gravity.START,
                                addBackground = false,
                                width = LinearLayout.LayoutParams.MATCH_PARENT
                            )
                        )
                    }
                }


                binder.lytHeader.setOnClickListener {

                    if (selectedItemPosition == position) {
                        isExpand = !isExpand
                        if (!isExpand) {
                            binder.icExpand.setImageResource(R.drawable.ic_expand)
                            binder.lytDetails.visibility = View.GONE
                            selectedItemPosition = -1
                        } else {
                            binder.icExpand.setImageResource(R.drawable.ic_colapse)
                            binder.lytDetails.visibility = View.VISIBLE
                        }
                    } else {
                        if (selectedItemPosition != -1) {
                            val lastSelectedItemPosition = selectedItemPosition
                            binding.rvResult.adapter?.notifyItemChanged(lastSelectedItemPosition)
                        }
                        isExpand = true
                        selectedItemPosition = position
                        binder.icExpand.setImageResource(R.drawable.ic_colapse)
                        binder.lytDetails.visibility = View.VISIBLE
                        val offset = if (position == 0) {
                            0
                        } else {
                            binder.lytHeader.measuredHeight
                        }
                        (binding.rvResult.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                            selectedItemPosition,
                            offset
                        )
                    }
                }

                if (selectedItemPosition == position && isExpand) {
                    binder.icExpand.setImageResource(R.drawable.ic_colapse)
                    binder.lytDetails.visibility = View.VISIBLE
                }
            }
        )


        binding.icRetry.setOnClickListener(this)
        binding.icClear.setOnClickListener(this)

    }

    private fun createTextView(
        text: String? = null,
        isBold: Boolean = false,
        textColor: Int? = null,
        addBackground: Boolean = true,
        width: Int = 0,
        height: Int = LinearLayout.LayoutParams.WRAP_CONTENT,
        weight: Float? = null,
        gravity: Int = Gravity.CENTER,
        layoutGravity: Int = Gravity.TOP or Gravity.START
    ): TextView {

        return TextView(activity).apply {
            if (addBackground) {
                setBackgroundResource(R.drawable.bg_border_cell_dictionary)
            }
            this.text = text
            this.gravity = gravity
            if (isBold) {
                setTypeface(typeface, Typeface.BOLD)
            }
            textColor?.let {
                this.setTextColor(textColor)
            }
            val _4dp = AndroidUtilities.dp(4f)
            val _8dp = AndroidUtilities.dp(8f)
            this.setPadding(_8dp, _4dp, _8dp, _4dp)
            layoutParams =
                run {
                    if (weight != null) {
                        LinearLayout.LayoutParams(0, height, weight)
                    } else {
                        LinearLayout.LayoutParams(width, height)
                    }
                }.apply {
                    this.gravity = layoutGravity
                }
        }

    }

    private fun observeLiveData() {

        viewModel.errorLiveData.observe(viewLifecycleOwner, Observer { error ->
            hideAllViews()
            showErrorView(true)
            error.printStackTrace()
        })

        viewModel.wordInformationLiveData.observe(viewLifecycleOwner, Observer { res ->
            if (res.first == binding.edtSearchBar.text.toString()) {
                hideAllViews()
                if (binding.edtSearchBar.text.toString().isNotEmpty()) {
                    if (res.second.word?.word_info.orEmpty().isNotEmpty()) {
                        listResult.clear()
                        listResult.addAll(res.second.word?.word_info.orEmpty())
                        if (listResult.size == 1) {
                            selectedItemPosition = 0
                            isExpand = true
                        }
                        binding.rvResult.adapter?.notifyDataSetChanged()
                        showResultView(true)
                    } else {
                        showEmptyView(true)
                    }
                }
            }
        })
    }

    private fun hideAllViews() {
        showLookupView(false)
        showErrorView(false)
        showEmptyView(false)
        showLoading(false)
        showResultView(false)
    }

    private fun showLoading(isShow: Boolean) {
        binding.progressBar.visibility = if (isShow) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun showLookupView(isShow: Boolean) {
        binding.txtEmptyResult.text = "Look up word in the dictionary"
        binding.txtEmptyResult.visibility = if (isShow) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun showEmptyView(isShow: Boolean) {
        binding.txtEmptyResult.text = "There is no result."
        binding.txtEmptyResult.visibility = if (isShow) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun showErrorView(isShow: Boolean) {
        binding.txtEmptyResult.text = "The search was failed,\n Please try again"
        binding.txtEmptyResult.visibility = if (isShow) {
            View.VISIBLE
        } else {
            View.GONE
        }
        binding.icRetry.visibility = if (isShow) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun showResultView(isShow: Boolean) {
        binding.rvResult.visibility = if (isShow) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun showClearView(isShow: Boolean) {
        binding.icClear.visibility = if (isShow) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    private fun clearSearchBar() {

        binding.edtSearchBar.text.clear()

        showClearView(false)
    }

    private fun initMediaPlayer() {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
    }

    private fun playAudio(url: String?, loading: View, playButton: View) {
        try {
            playButton.visibility = View.INVISIBLE
            loading.visibility = View.VISIBLE

            if (currentSoundLink != url) {
                mediaPlayer?.reset()
                mediaPlayer?.setDataSource(url)
                mediaPlayer?.prepareAsync()
            } else {
                mediaPlayer?.start()
            }
            currentSoundLink = url
            mediaPlayer?.setOnPreparedListener {
                mediaPlayer?.start()
            }
            mediaPlayer?.setOnCompletionListener {
                loading.visibility = View.INVISIBLE
                playButton.visibility = View.VISIBLE
            }
            mediaPlayer?.setOnErrorListener { _, _, _ ->
                loading.visibility = View.INVISIBLE
                playButton.visibility = View.VISIBLE
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            loading.visibility = View.INVISIBLE
            playButton.visibility = View.VISIBLE
        }
    }

    private fun killMediaPlayer() {
        try {
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        killMediaPlayer()
    }

    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.ic_clear -> {
                hideAllViews()
                clearSearchBar()
            }

            R.id.ic_retry -> {
                hideAllViews()
                showLoading(true)
                viewModel.query(binding.edtSearchBar.text.toString())
            }

        }
    }

    companion object {

        fun navigate(context: Context?) {
            context?.startActivity(Intent(context, FragmentDictionary::class.java))
        }

    }
}

