package online.javalab.poly.adapters.lesson;

import androidx.recyclerview.widget.SortedList;
import androidx.recyclerview.widget.SortedListAdapterCallback;

import java.util.ArrayList;
import java.util.List;

import online.javalab.poly.R;
import online.javalab.poly.base.adapter.BaseRecyclerViewAdapter;
import online.javalab.poly.databinding.ItemChatBinding;
import online.javalab.poly.model.lesson.Chat;

/**
 * Created by CanhNamDinh
 * on 07,December,2021
 */

public class ChatAdapter extends BaseRecyclerViewAdapter<ItemChatBinding> {

    private List<Chat> chatList = new ArrayList<>();
    private SortedList<Chat> mList;
    private ClickItemChat clickItemChat;
    private SortedListAdapterCallback<Chat> mSortListAdapter;

    public ChatAdapter() {

    }

    public void sortDESC() {
        mSortListAdapter = new SortedListAdapterCallback<Chat>(ChatAdapter.this) {
            @Override
            public int compare(Chat o1, Chat o2) {
                return (int) (o2.getVote() - o1.getVote());
            }

            @Override
            public boolean areContentsTheSame(Chat oldItem, Chat newItem) {
                return false;
            }

            @Override
            public boolean areItemsTheSame(Chat item1, Chat item2) {
                return false;
            }
        };
        mList = new SortedList<Chat>(Chat.class, mSortListAdapter);
    }

    public void setListData(List<Chat> list) {
        mList.clear();
        mList.beginBatchedUpdates();
        for (Chat i : list) {
            mList.add(i);
        }
        mList.endBatchedUpdates();
    }


    public void setClickItemChat(ClickItemChat clickItemChat) {
        this.clickItemChat = clickItemChat;
    }

    @Override
    protected int layoutResId() {
        return R.layout.item_chat;
    }

    @Override
    protected BaseViewHolder solvedOnCreateViewHolder(ItemChatBinding binding) {
        return new ChatViewHolder(binding);
    }

    @Override
    protected void solvedOnBindViewHolder(BaseViewHolder holder, int position) {
        ChatViewHolder viewHolder = (ChatViewHolder) holder;
        if (!mList.get(position).getMessage().equals("")) {
            Chat chat = mList.get(position);
            viewHolder.bindData(chat);
            viewHolder.setClickItemChat(clickItemChat);
        }
    }


    @Override
    public int getItemCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }


    static class ChatViewHolder extends BaseViewHolder<Chat> {
        private ItemChatBinding binding;
        private ClickItemChat clickItemChat;
        private Chat item;

        protected ChatViewHolder(ItemChatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.imgChatLike.setOnClickListener(v -> {
                clickItemChat.clickLike(item);
            });
        }

        @Override
        protected void bindData(Chat data) {
            if (data.getMessage() != null) {
                binding.setChat(data);
                this.item = data;
            }

        }

        public void setClickItemChat(ClickItemChat clickItemChat) {
            this.clickItemChat = clickItemChat;
        }
    }

    public interface ClickItemChat {
        void clickLike(Chat chat);
    }
}
