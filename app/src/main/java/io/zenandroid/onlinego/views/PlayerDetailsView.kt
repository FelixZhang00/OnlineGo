package io.zenandroid.onlinego.views

import android.content.Context
import android.content.res.ColorStateList
import android.support.annotation.ColorRes
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import io.zenandroid.onlinego.R
import io.zenandroid.onlinego.extensions.showIf
import io.zenandroid.onlinego.model.StoneType
import io.zenandroid.onlinego.model.local.Player
import io.zenandroid.onlinego.utils.convertCountryCodeToEmojiFlag
import io.zenandroid.onlinego.utils.egfToRank
import io.zenandroid.onlinego.utils.formatRank
import kotlinx.android.synthetic.main.view_player_details.view.*
import java.lang.Math.*
import java.util.regex.Pattern


/**
 * Created by alex on 17/11/2017.
 */
class PlayerDetailsView : FrameLayout {

    private val gravatarRegex = Pattern.compile("(.*gravatar.com/avatar/[0-9a-fA-F]*+).*")
    private val rackcdnRegex = Pattern.compile("(.*rackcdn.com.*)-\\d*\\.png")

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    var player: Player? = null
        set(value) {
            if(field == value) {
                return
            }
            nameView.text = value?.username
            rankView.text = formatRank(egfToRank(value?.rating))
            value?.country?.let {
                flagView.text = convertCountryCodeToEmojiFlag(it)
            }
            value?.icon?.let {
                Glide.with(this)
                        .load(processGravatarURL(it))
                        .transition(withCrossFade(DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()))
                        .apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_person_outline))
                        .apply(RequestOptions().circleCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                        .into(iconView)
            }
            field = value
        }

    fun setStatus(text: String?, @ColorRes color: Int = R.color.colorAccent) {
        text?.let {
            theirTurnLabel.text = text
            ViewCompat.setBackgroundTintList(theirTurnLabel, ColorStateList.valueOf(resources.getColor(color)))
            theirTurnLabel.animate().alpha(1f)
        } ?: theirTurnLabel.animate().alpha(0f)

    }

    var passed: Boolean = false
        set(value) {
            if(field == value) {
                return
            }
            if(value) {
                passedLabel.animate().alpha(1f)
            } else {
                passedLabel.animate().alpha(0f)
            }
            field = value
        }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        iconContainer.radius = iconContainer.width / 2f
    }

    var timerFirstLine: String? = null
        set(value) {
            timerFirstLineView.text = value
            timerFirstLineView.showIf(value?.isNotEmpty() ?: false)
            timerFirstLineLabelView.showIf(timerFirstLineView.visibility == View.VISIBLE)
        }

    var timerSecondLine: String? = null
        set(value) {
            timerSecondLineView.text = value
            timerSecondLineView.showIf(value?.isNotEmpty() ?: false)
            timerSecondLineLabelView.showIf(timerSecondLineView.visibility == View.VISIBLE)
        }

    var score: Float = 0f
        set(value) {
            field = value
            scoreView.text = score.toString()
        }

    var color: StoneType = StoneType.BLACK
        set(value) {
            field = value
            colorIndicatorBlack.showIf(value == StoneType.BLACK)
            colorIndicatorWhite.showIf(value == StoneType.WHITE)
        }

    private fun processGravatarURL(url: String): String {
        var matcher = gravatarRegex.matcher(url)
        if(matcher.matches()) {
            return "${matcher.group(1)}?s=${iconView.width}&d=404"
        }

        matcher = rackcdnRegex.matcher(url)
        if(matcher.matches()) {
            val desired = max(512.0, pow(2.0, round(log(iconView.width.toDouble()) / log(2.0)).toDouble())).toInt()
            return "${matcher.group(1)}-${desired}.png"
        }
        return url
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        View.inflate(context, R.layout.view_player_details, this)
    }

}