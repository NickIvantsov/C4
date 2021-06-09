package ua.yandex.jere184.c4tappydefender.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ua.yandex.jere184.c4tappydefender.R
import ua.yandex.jere184.c4tappydefender.db.userRecords.UserRecordEntity
import ua.yandex.jere184.c4tappydefender.ui.adapters.viewHolders.UserRecordsViewHolder
import javax.inject.Inject

class UserRecordsAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var records: List<UserRecordEntity> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return onCreateViewHolderImpl(parent, viewType)
    }

    fun addRecords(records: List<UserRecordEntity>) {
        this.records = records
        notifyDataSetChanged()
    }

    private fun onCreateViewHolderImpl(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return viewHolderFor(inflater, parent)
    }

    private fun viewHolderFor(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = UserRecordsViewHolder(
        getView(
            inflater,
            parent,
            R.layout.user_record_item
        )
    )

    private fun getView(
        inflater: LayoutInflater,
        parent: ViewGroup,
        layoutId: Int
    ): View {
        return inflater.inflate(layoutId, parent, false)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val record = records[position]
        if (holder is UserRecordsViewHolder) {
            holder.binding.tvCurentTime.text = record.currentTime
            holder.binding.tvDistance.text = record.dist.toString()
            holder.binding.tvTime.text = record.time.toString()
        }

    }

    override fun getItemCount(): Int {
        return records.size
    }
}