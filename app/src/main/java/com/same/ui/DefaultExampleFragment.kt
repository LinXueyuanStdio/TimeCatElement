package com.same.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.github.basshelal.boardview.BoardAdapter
import com.github.basshelal.boardview.BoardColumnViewHolder
import com.github.basshelal.boardview.BoardContainerAdapter
import com.github.basshelal.boardview.BoardItemViewHolder
import com.github.basshelal.boardview.BoardListAdapter
import com.github.basshelal.boardview.BoardViewContainer
import com.github.basshelal.boardview.drag.ObservableDragBehavior
import com.timecat.demo.element.R

class DefaultExampleFragment : Fragment() {

    lateinit var boardViewContainer: BoardViewContainer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.view_db_board, container, false)
        boardViewContainer = view.findViewById(R.id.defaultBoardViewContainer)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        boardViewContainer.adapter = ExampleBoardContainerAdapter(DEFAULT_EXAMPLE_BOARD)

        boardViewContainer.listDragShadow.dragBehavior.addDragListenerIfNotExists(
                object : ObservableDragBehavior.SimpleDragListener() {
                    override fun onStartDrag(dragView: View) {
                        boardViewContainer.draggingColumn?.itemView?.alpha = 0F
                    }

                    override fun onEndDrag(dragView: View) {
                        boardViewContainer.draggingColumn?.itemView?.alpha = 1F
                    }
                })

        boardViewContainer.itemDragShadow.dragBehavior.addDragListenerIfNotExists(
                object : ObservableDragBehavior.SimpleDragListener() {
                    override fun onStartDrag(dragView: View) {
                        boardViewContainer.draggingItem.itemViewHolder?.itemView?.alpha = 0F
                    }

                    override fun onEndDrag(dragView: View) {
                        boardViewContainer.draggingItem.itemViewHolder?.itemView?.alpha = 1F
                    }
                })
    }
}

private class ExampleBoardContainerAdapter(val board: Board<String>) : BoardContainerAdapter() {

    override val boardViewAdapter get() = ExampleBoardAdapter()

    override fun onCreateListAdapter(position: Int) = ExampleBoardListAdapter(position)

    override val headerLayoutRes: Int? = R.layout.view_db_board_header

    override val footerLayoutRes: Int? = R.layout.view_db_board_footer

    override fun onMoveColumn(draggingColumn: BoardColumnViewHolder,
                              targetPosition: Int): Boolean {
        val from = draggingColumn.adapterPosition
        return if (from != NO_POSITION && targetPosition != NO_POSITION) {
            val value = board[from]
            board.boardLists.removeAt(from)
            board.boardLists.add(targetPosition, value)
            true
        } else false
    }

    override fun onMoveItem(draggingItem: BoardItemViewHolder, targetPosition: Int,
                            draggingColumn: BoardColumnViewHolder, targetColumn: BoardColumnViewHolder): Boolean {
        val draggingColumnPos = draggingColumn.adapterPosition
        val targetColumnPos = targetColumn.adapterPosition
        val draggingItemPos = draggingItem.adapterPosition
        val targetItemPos = targetPosition

        if (draggingColumnPos == NO_POSITION || targetColumnPos == NO_POSITION ||
                draggingItemPos == NO_POSITION || targetItemPos == NO_POSITION) return false

        if (draggingColumnPos == targetColumnPos) {
            if (draggingItemPos == targetItemPos) return false
            else {
                val boardList = board.boardLists[draggingColumnPos]
                val value = boardList[draggingItemPos]
                boardList.items.removeAt(draggingItemPos)
                boardList.items.add(targetItemPos, value)
                return true
            }
        } else {
            val fromBoardList = board.boardLists[draggingColumnPos]
            val toBoardList = board.boardLists[targetColumnPos]
            val value = fromBoardList[draggingItemPos]
            fromBoardList.items.removeAt(draggingItemPos)
            toBoardList.items.add(targetItemPos, value)
            return true
        }
    }

    private inner class ExampleBoardAdapter : BoardAdapter<ExampleColumnVH>(this) {

        private var boardMode: BoardMode = BoardMode.MULTI

        override fun createViewHolder(itemView: View): ExampleColumnVH {
            return ExampleColumnVH(itemView)
        }

        override fun onViewHolderLaidOut(holder: ExampleColumnVH) {
            holder.header?.also {
                it.setOnClickListener {
                    when (boardMode) {
                        BoardMode.MULTI -> boardViewContainer.boardView.switchToSingleColumnModeAt(holder.adapterPosition)
                        BoardMode.SINGLE -> boardViewContainer.boardView.switchToMultiColumnMode(500)
                    }
                    boardMode = boardMode.toggle()
                }
                it.setOnLongClickListener {
                    boardViewContainer.startDraggingColumn(holder)
                    true
                }
            }
            holder.footer?.also {
                it.setOnClickListener {
                    val list = board[holder.adapterPosition]
                    val new = if (list.items.isNotEmpty()) {
                        val last = list.items.last()
                        StringListItem(last.id + 1, "Item #${last.id + 1}")
                    } else StringListItem(0, "Item #0")
                    list.items.add(new)
                    holder.boardListAdapter?.notifyItemInserted(list.items.lastIndex)
                    holder.list?.smoothScrollToPosition(list.items.lastIndex)
                }
            }
        }

        override fun getItemId(position: Int): Long {
            return board[position].id
        }

        override fun getItemCount(): Int {
            return board.boardLists.size
        }

        override fun onBindViewHolder(holder: ExampleColumnVH, position: Int) {
            super.onBindViewHolder(holder, position)
            val boardList = board[position]
            holder.itemView.findViewById<TextView>(R.id.header_textView)?.text = boardList.name
        }
    }

    private enum class BoardMode {
        SINGLE, MULTI;

        fun toggle(): BoardMode {
            return if (this == SINGLE) MULTI else SINGLE
        }
    }

    private inner class ExampleBoardListAdapter(position: Int) : BoardListAdapter<ItemVH>() {

        var boardList = board[position]

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
            return ItemVH(parent).also { itemVH ->
                itemVH.itemView.setOnLongClickListener {
                    boardViewContainer.startDraggingItem(itemVH)
                    true
                }
            }
        }

        override fun getItemId(position: Int): Long = boardList[position].id

        override fun getItemCount(): Int = boardList.items.size

        override fun onBindViewHolder(holder: ItemVH, position: Int) {
            val listItem = boardList[position]
            holder.textView.text = listItem.value
            holder.itemView.setOnClickListener {
                val pos = holder.adapterPosition
                if (pos != NO_POSITION && boardList.items.isNotEmpty()) {
                    boardList.items.removeAt(pos)
                    notifyItemRemoved(pos)
                }
            }
        }

        override fun bindAdapter(holder: BoardColumnViewHolder, position: Int) {
            boardList = board[position]
        }

    }
}

private class ExampleColumnVH(itemView: View) : BoardColumnViewHolder(itemView)

private class ItemVH(itemView: View) : BoardItemViewHolder(itemView) {
    val textView: TextView = itemView.findViewById(R.id.cardText_textView)

    constructor(parent: ViewGroup) : this(LayoutInflater.from(parent.context)
            .inflate(R.layout.view_db_board_item, parent, false))
}