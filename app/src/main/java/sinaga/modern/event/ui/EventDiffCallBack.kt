package sinaga.modern.event.ui

import ListEventsItem
import androidx.recyclerview.widget.DiffUtil

class EventDiffCallback(
    private val oldList: List<ListEventsItem>,
    private val newList: List<ListEventsItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
