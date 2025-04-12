package com.livescore.soccerscore.matchlive.util.calendar_collapse.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TableLayout
import android.widget.TextView
import com.livescore.soccerscore.matchlive.R
import com.livescore.soccerscore.matchlive.util.calendar_collapse.data.Day
import com.livescore.soccerscore.matchlive.util.calendar_collapse.view.LockScrollView
import com.livescore.soccerscore.matchlive.util.calendar_collapse.view.OnSwipeTouchListener
import java.util.Locale


@SuppressLint("ClickableViewAccessibility")
abstract class UICalendar constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    protected var mInflater: LayoutInflater

    protected var mLayoutRoot: LinearLayout
    protected var mTxtTitle: TextView
    protected var mTableHead: TableLayout
    protected var mScrollViewBody: LockScrollView
    protected var mTableBody: TableLayout

    protected var mIvLeft: ImageView
    protected var mIvRight: ImageView

    var datePattern = "MMMM YYYY"
        set(value: String) {
            field = value

        }

    var isShowWeek = true
        set(showWeek) {
            field = showWeek

            if (showWeek) {
                mTableHead.visibility = View.VISIBLE
            } else {
                mTableHead.visibility = View.GONE
            }
        }
    var firstDayOfWeek = SUNDAY
        set(firstDayOfWeek) {
            field = firstDayOfWeek
            reload()
        }
    var hideArrow = true
        set(value: Boolean) {
            field = value
            hideButton()
        }
    open var state = STATE_EXPANDED
        set(state) {
            field = state
        }

    var textColor = Color.parseColor("#0A1640")
        set(textColor) {
            field = textColor
            redraw()

            mTxtTitle.setTextColor(this.textColor)
        }
    var primaryColor = Color.WHITE
        set(primaryColor) {
            field = primaryColor
            redraw()

            mLayoutRoot.setBackgroundColor(this.primaryColor)
        }

    var todayItemTextColor = Color.parseColor("#0A1640")
        set(todayItemTextColor) {
            field = todayItemTextColor
            redraw()
        }
    var todayItemBackgroundDrawable =
        resources.getDrawable(R.drawable.bg_radius_100_trans)
        set(todayItemBackgroundDrawable) {
            field = todayItemBackgroundDrawable
            redraw()
        }
    var selectedItemTextColor = Color.parseColor("#D7F3FF")
        set(selectedItemTextColor) {
            field = selectedItemTextColor
            redraw()
        }
    var selectedItemBackgroundDrawable =
        resources.getDrawable(R.drawable.bg_radius_100_this_day)
        set(selectedItemBackground) {
            field = selectedItemBackground
            redraw()
        }

    var selectedItem: Day? = null

    private var mButtonLeftDrawableTintColor = Color.BLACK
    private var mButtonRightDrawableTintColor = Color.BLACK

    private var mExpandIconColor = Color.BLACK
    var eventColor = Color.BLACK
        private set(eventColor) {
            field = eventColor
            redraw()

        }

    fun getSwipe(context: Context): OnSwipeTouchListener {
        return object : OnSwipeTouchListener(context) {
            override fun onSwipeTop() {
            }

            override fun onSwipeLeft() {
                //collapse calendar
                mIvRight.performClick()
            }

            override fun onSwipeRight() {
                //collapse calendar
                mIvLeft.performClick()
            }

            override fun onSwipeBottom() {
            }
        }
    }

    fun getCurrentLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales.get(0)
        } else {

            context.resources.configuration.locale
        }
    }

    init {
        mInflater = LayoutInflater.from(context)

        val rootView = mInflater.inflate(R.layout.layout_calendar_collapse_toolbar, this, true)

        mLayoutRoot = rootView.findViewById(R.id.layout_root)
        mTxtTitle = rootView.findViewById(R.id.txt_title)
        mTableHead = rootView.findViewById(R.id.table_head)
        mTableBody = rootView.findViewById(R.id.table_body)
        mIvLeft = rootView.findViewById(R.id.iv_a_left)
        mIvRight = rootView.findViewById(R.id.iv_a_right)
        mScrollViewBody = rootView.findViewById(R.id.scroll_view_body)

        mLayoutRoot.setOnTouchListener(getSwipe(context));
        mScrollViewBody.setOnTouchListener(getSwipe(context))
        mScrollViewBody.setParams(getSwipe(context))
        val attributes = context.theme.obtainStyledAttributes(
            attrs, R.styleable.UICalendar, defStyleAttr, 0
        )
        setAttributes(attributes)
        attributes.recycle()
    }

    protected abstract fun redraw()
    protected abstract fun reload()
    private fun hideButton() {

    }

    protected fun setAttributes(attrs: TypedArray) {
        isShowWeek = attrs.getBoolean(R.styleable.UICalendar_showWeek, isShowWeek)
        firstDayOfWeek = attrs.getInt(R.styleable.UICalendar_firstDayOfWeek, firstDayOfWeek)
        hideArrow = attrs.getBoolean(R.styleable.UICalendar_hideArrows, hideArrow)
        datePattern = attrs.getString(R.styleable.UICalendar_datePattern) ?: datePattern
        state = attrs.getInt(R.styleable.UICalendar_state, state)

        textColor = attrs.getColor(R.styleable.UICalendar_textColor, textColor)
        primaryColor = attrs.getColor(R.styleable.UICalendar_primaryColor, primaryColor)

        eventColor = attrs.getColor(R.styleable.UICalendar_eventColor, eventColor)


        todayItemTextColor = attrs.getColor(
            R.styleable.UICalendar_todayItem_textColor, todayItemTextColor
        )

        attrs.getDrawable(R.styleable.UICalendar_todayItem_background)
            .also { this.todayItemBackgroundDrawable = it }

        selectedItemTextColor = attrs.getColor(
            R.styleable.UICalendar_selectedItem_textColor, selectedItemTextColor
        )
        this.selectedItemBackgroundDrawable =
            attrs.getDrawable(R.styleable.UICalendar_selectedItem_background)

        setButtonLeftDrawableTintColor(
            attrs.getColor(
                R.styleable.UICalendar_buttonLeft_drawableTintColor, mButtonLeftDrawableTintColor
            )
        )
        setButtonRightDrawableTintColor(
            attrs.getColor(
                R.styleable.UICalendar_buttonRight_drawableTintColor, mButtonRightDrawableTintColor
            )
        )
        setExpandIconColor(attrs.getColor(R.styleable.UICalendar_expandIconColor, mExpandIconColor))
        val selectedItem: Day? = null
    }

    fun setButtonLeftDrawableTintColor(color: Int) {
        this.mButtonLeftDrawableTintColor = color
        redraw()
    }

    fun setButtonRightDrawableTintColor(color: Int) {
        this.mButtonRightDrawableTintColor = color
        redraw()
    }

    fun setExpandIconColor(color: Int) {
        this.mExpandIconColor = color
    }

    abstract fun changeToToday()

    companion object {
        val SUNDAY = 0
        val MONDAY = 1
        val TUESDAY = 2
        val WEDNESDAY = 3
        val THURSDAY = 4
        val FRIDAY = 5
        val SATURDAY = 6

        val STATE_EXPANDED = 0
        val STATE_COLLAPSED = 1
        val STATE_PROCESSING = 2
    }


}
