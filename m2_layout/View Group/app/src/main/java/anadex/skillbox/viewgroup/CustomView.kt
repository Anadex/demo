package anadex.skillbox.viewgroup

import anadex.skillbox.viewgroup.databinding.CustomViewBinding
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout


class CustomView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    val binding = CustomViewBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
    }

}