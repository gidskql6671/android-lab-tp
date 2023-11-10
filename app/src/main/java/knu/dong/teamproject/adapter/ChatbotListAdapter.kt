package knu.dong.teamproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import knu.dong.teamproject.databinding.ListviewItemRoleBinding
import knu.dong.teamproject.dto.Chatbot

class ChatbotListAdapter(val context: Context, val chatbotList: MutableList<Chatbot>) : BaseAdapter() {
    override fun getCount(): Int {
        return chatbotList.size
    }

    override fun getItem(position: Int): Any {
        return chatbotList[position]
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = ListviewItemRoleBinding.inflate(LayoutInflater.from(context))

        binding.itemId.text = chatbotList[position].name

        return binding.root
    }
}