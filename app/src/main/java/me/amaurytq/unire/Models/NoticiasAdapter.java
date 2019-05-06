package me.amaurytq.unire.Models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.amaurytq.unire.R;

public class NoticiasAdapter extends RecyclerView.Adapter<NoticiasAdapter.ViewHolder> {

    private List<Noticia> items;
    private NoticiasListener mListener;

    public NoticiasAdapter(NoticiasListener listener) {
        this.mListener = listener;
        this.items = new ArrayList<>();
    }

    public void setItems(List<Noticia> _items) {
        items = _items;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.noticia = items.get(position);
        holder.noticiaTitle.setText(items.get(position).title);
        holder.noticiaShortBody.setText(items.get(position).shortBody);
        holder.mView.setOnClickListener(v -> mListener.onClick(holder.noticia));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.noticia_item_title) TextView noticiaTitle;
        @BindView(R.id.noticia_item_shortbody) TextView noticiaShortBody;

        public Noticia noticia;
        public final View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, mView);
        }
    }

    public interface NoticiasListener {
        void onClick(Noticia item);
    }

}
