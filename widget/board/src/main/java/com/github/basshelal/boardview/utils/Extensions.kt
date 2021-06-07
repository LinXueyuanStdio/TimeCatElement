@file:Suppress("NOTHING_TO_INLINE")

package com.github.basshelal.boardview.utils

import android.content.Context
import android.graphics.Color
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.Interpolator
import android.view.animation.Transformation
import androidx.core.graphics.toRectF
import androidx.core.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import java.util.*
import kotlin.collections.set
import kotlin.math.log2
import kotlin.random.Random

@PublishedApi
internal inline val Number.I: Int
    get() = this.toInt()

@PublishedApi
internal inline val Number.D: Double
    get() = this.toDouble()

@PublishedApi
internal inline val Number.F: Float
    get() = this.toFloat()

@PublishedApi
internal inline val Number.L: Long
    get() = this.toLong()

internal inline val now: Long get() = System.currentTimeMillis()

internal inline fun <K, V> HashMap<K, V>.putIfAbsentSafe(key: K, value: V) {
    if (!this.containsKey(key)) this[key] = value
}

internal inline fun View.changeParent(newParent: ViewGroup) {
    this.parentViewGroup?.removeView(this)
    newParent.addView(this)
}

internal inline val View.parentViewGroup: ViewGroup?
    get() = parent as? ViewGroup?

internal inline val View.rootViewGroup: ViewGroup?
    get() = this.rootView as? ViewGroup

@PublishedApi
internal inline val View.globalVisibleRect: Rect
    get() = Rect().also { this.getGlobalVisibleRect(it) }

@PublishedApi
internal inline val View.globalVisibleRectF: RectF
    get() = globalVisibleRect.toRectF()

val Context.windowManager: WindowManager
    get() = getSystemService(Context.WINDOW_SERVICE) as WindowManager

internal inline val View.millisPerFrame get() = 1000F / context.windowManager.defaultDisplay.refreshRate

class LogarithmicInterpolator : Interpolator {
    override fun getInterpolation(input: Float) = log2(input + 1)
}

internal inline operator fun Interpolator.get(float: Float) = this.getInterpolation(float)

internal inline fun View.assignID() {
    if (this.id == View.NO_ID) this.id = View.generateViewId()
}

@PublishedApi
internal inline fun ViewGroup.forEachReversed(action: (view: View) -> Unit) {
    for (index in childCount downTo 0) {
        getChildAt(index)?.also(action)
    }
}

internal inline fun View.updateLayoutParamsSafe(block: ViewGroup.LayoutParams.() -> Unit) {
    layoutParams?.apply(block)
    requestLayout()
}

@JvmName("updateLayoutParamsSafeTyped")
internal inline fun <reified T : ViewGroup.LayoutParams> View.updateLayoutParamsSafe(block: T.() -> Unit) {
    (layoutParams as? T)?.apply(block)
    requestLayout()
}

internal inline fun RecyclerView.findChildViewUnderRaw(pointF: PointF): View? {
    val rect = this.globalVisibleRectF
    val x = pointF.x - rect.left
    val y = pointF.y - rect.top
    this.forEachReversed {
        // This takes margins into account so the bounds box is larger
        if (x >= (it.left + it.translationX - it.marginLeft) &&
            x <= (it.right + it.translationX + it.marginRight) &&
            y >= (it.top + it.translationY - it.marginTop) &&
            y <= (it.bottom + it.translationY + it.marginBottom)) {
            return it
        }
    }
    return null
}

internal inline val RecyclerView.Adapter<*>.lastPosition: Int
    get() = this.itemCount - 1

internal inline fun RecyclerView.Adapter<*>.isAdapterPositionValid(position: Int): Boolean {
    return position >= 0 && position <= this.lastPosition
}

internal inline fun RecyclerView.Adapter<*>.isAdapterPositionNotValid(position: Int): Boolean {
    return !isAdapterPositionValid(position)
}

internal inline val RecyclerView.horizontalScrollOffset: Int get() = computeHorizontalScrollOffset()
internal inline val RecyclerView.verticalScrollOffset: Int get() = computeVerticalScrollOffset()
internal inline val RecyclerView.maxHorizontalScroll: Int get() = computeHorizontalScrollRange() - computeHorizontalScrollExtent()
internal inline val RecyclerView.maxVerticalScroll: Int get() = computeVerticalScrollRange() - computeVerticalScrollExtent()

internal inline val RecyclerView.canScrollVertically: Boolean
    get() = this.canScrollVertically(-1) || this.canScrollVertically(1)

internal inline val RecyclerView.canScrollHorizontally: Boolean
    get() = this.canScrollHorizontally(-1) || this.canScrollHorizontally(1)

internal inline val RecyclerView.firstVisibleViewHolder: RecyclerView.ViewHolder?
    get() = (this.layoutManager as? LinearLayoutManager)
        ?.findFirstVisibleItemPosition()
        ?.takeIf { it.isValidAdapterPosition }
        ?.let { this.findViewHolderForAdapterPosition(it) }

internal inline val RecyclerView.lastVisibleViewHolder: RecyclerView.ViewHolder?
    get() = (this.layoutManager as? LinearLayoutManager)
        ?.findLastVisibleItemPosition()
        ?.takeIf { it.isValidAdapterPosition }
        ?.let { this.findViewHolderForAdapterPosition(it) }

internal inline val RecyclerView.ViewHolder.isAdapterPositionValid: Boolean
    get() = adapterPosition != NO_POSITION

internal inline val Number.isValidAdapterPosition: Boolean
    get() = this.I >= 0

internal inline fun animation(crossinline applyTransformation:
                              (interpolatedTime: Float, transformation: Transformation) -> Unit): Animation {
    return object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            applyTransformation(interpolatedTime, t)
        }
    }
}

inline fun animationListener(crossinline onStart: (Animation) -> Unit,
                             crossinline onRepeat: (Animation) -> Unit,
                             crossinline onEnd: (Animation) -> Unit) =
    object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) = onStart(animation)
        override fun onAnimationRepeat(animation: Animation) = onRepeat(animation)
        override fun onAnimationEnd(animation: Animation) = onEnd(animation)
    }

var DEBUG = false

internal inline fun logE(message: Any?, tag: String = "BoardView") {
    if (DEBUG) Log.e(tag, message.toString())
}

internal inline infix fun Context.dpToPx(dp: Number): Float =
    (dp.F * (this.resources.displayMetrics.densityDpi.F / DisplayMetrics.DENSITY_DEFAULT))


internal inline fun RectF.copy(block: RectF.() -> Unit) = RectF(this).apply(block)

internal inline fun RectF.show(view: View, color: Int = randomColor) {
    view.rootViewGroup?.addView(
        View(view.context).also {
            it.x = this.left
            it.y = this.top
            it.layoutParams = ViewGroup.LayoutParams(this.width().I, this.height().I)
            it.setBackgroundColor(color)
            it.alpha = 0.5F
            it.requestLayout()
        }
    )
}

internal inline fun PointF.copy(block: PointF.() -> Unit) = PointF(this.x, this.y).apply(block)

internal inline fun <T> List<T>.reversedForEachIndexed(action: (index: Int, T) -> Unit) {
    this.asReversed().forEachIndexed(action)
}

internal inline val randomColor: Int
    get() = Color.rgb(
        Random.nextInt(0, 256),
        Random.nextInt(0, 256),
        Random.nextInt(0, 256)
    )

/**
 * Return the sequence of children of the received [View].
 * Note that the sequence is not thread-safe.
 *
 * @return the [Sequence] of children.
 */
@Deprecated(message = "Use the Android KTX version", replaceWith = ReplaceWith("children", "androidx.core.view.children"))
fun View.childrenSequence(): Sequence<View> = ViewChildrenSequence(this)

/**
 * Return the [Sequence] of all children of the received [View], recursively.
 * Note that the sequence is not thread-safe.
 *
 * @return the [Sequence] of children.
 */
fun View.childrenRecursiveSequence(): Sequence<View> = ViewChildrenRecursiveSequence(this)

private class ViewChildrenSequence(private val view: View) : Sequence<View> {
    override fun iterator(): Iterator<View> {
        if (view !is ViewGroup) return emptyList<View>().iterator()
        return ViewIterator(view)
    }

    private class ViewIterator(private val view: ViewGroup) : Iterator<View> {
        private var index = 0
        private val count = view.childCount

        override fun next(): View {
            if (!hasNext()) throw NoSuchElementException()
            return view.getChildAt(index++)
        }

        override fun hasNext(): Boolean {
            checkCount()
            return index < count
        }

        private fun checkCount() {
            if (count != view.childCount) throw ConcurrentModificationException()
        }
    }
}

private class ViewChildrenRecursiveSequence(private val view: View) : Sequence<View> {
    override fun iterator(): Iterator<View> {
        if (view !is ViewGroup) return emptyList<View>().iterator()
        return RecursiveViewIterator(view)
    }

    private class RecursiveViewIterator(view: View) : Iterator<View> {
        private val sequences = arrayListOf(view.childrenSequence())
        private var current = sequences.removeLast().iterator()

        override fun next(): View {
            if (!hasNext()) throw NoSuchElementException()
            val view = current.next()
            if (view is ViewGroup && view.childCount > 0) {
                sequences.add(view.childrenSequence())
            }
            return view
        }

        override fun hasNext(): Boolean {
            if (!current.hasNext() && sequences.isNotEmpty()) {
                current = sequences.removeLast().iterator()
            }
            return current.hasNext()
        }

        @Suppress("NOTHING_TO_INLINE")
        private inline fun <T : Any> MutableList<T>.removeLast(): T {
            if (isEmpty()) throw NoSuchElementException()
            return removeAt(size - 1)
        }
    }
}

/**
 * Iterate the receiver [List] backwards using an index.
 *
 * @f an action to invoke on each list element (index, element).
 */
inline fun <T> List<T>.forEachReversedWithIndex(f: (Int, T) -> Unit) {
    var i = size - 1
    while (i >= 0) {
        f(i, get(i))
        i--
    }
}

/**
 * Iterate the receiver [List] backwards using an index.
 *
 * @f an action to invoke on each list element.
 */
inline fun <T> List<T>.forEachReversedByIndex(f: (T) -> Unit) {
    var i = size - 1
    while (i >= 0) {
        f(get(i))
        i--
    }
}
