package ru.minilan.vocabularytrainer;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.ViewHolder> {

    private ArrayList<WordCard> words = new ArrayList<>();

    private OnMenuItemClickListener itemMenuClickListener;

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.itemMenuClickListener = onMenuItemClickListener;
    }

    public interface OnMenuItemClickListener {
        void onItemEditClick(WordCard wordCard);

        void onItemDeleteClick(WordCard wordCard);
    }

    public void setWords(ArrayList<WordCard> items) {
        words.clear();
        words.addAll(items);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public WordsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_wordcard, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WordsAdapter.ViewHolder viewHolder, int position) {
        viewHolder.bind(words.get(position));
    }


    @Override
    public int getItemCount() {
        return words.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView1, textView2;
        private WordCard wordCard;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            cardView = itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemMenuClickListener != null) {
                        showPopupMenu(cardView);
                    }
                }
            });

        }

        public void bind(WordCard wordCard) {
            this.wordCard = wordCard;
            textView1.setText(wordCard.getWord1());
            textView2.setText(wordCard.getWord2());
        }

        private void showPopupMenu(View view) {

            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.popup_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_edit:
                            itemMenuClickListener.onItemEditClick(wordCard);
                            return true;
                        case R.id.menu_delete:
                            itemMenuClickListener.onItemDeleteClick(wordCard);
                            return true;
                    }
                    return false;
                }
            });
            popupMenu.show();

        }
    }


}
