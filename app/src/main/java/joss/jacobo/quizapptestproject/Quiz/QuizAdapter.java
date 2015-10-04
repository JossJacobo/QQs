package joss.jacobo.quizapptestproject.quiz;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import joss.jacobo.quizapptestproject.BR;
import joss.jacobo.quizapptestproject.R;
import joss.jacobo.quizapptestproject.models.Question;
import joss.jacobo.quizapptestproject.models.Quiz;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {

    public List<QuizItem> items = new ArrayList<>();

    public void setItems(List<QuizItem> items) {
        this.items.clear();
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, viewType, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        QuizItem item = items.get(position);
        int viewType = getItemViewType(position);
        switch (viewType) {
            case R.layout.item_quiz_header:
                holder.binding.setVariable(BR.quiz, item);
                break;

            case R.layout.item_quiz:
                holder.binding.setVariable(BR.question, item);
                break;
        }
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemViewType(int position) {
        QuizItem item = items.get(position);
        if (item instanceof Quiz) {
            return R.layout.item_quiz_header;
        } else if (item instanceof Question) {
            return R.layout.item_quiz;
        }
        throw new AssertionError();
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ViewDataBinding binding;

        public ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
