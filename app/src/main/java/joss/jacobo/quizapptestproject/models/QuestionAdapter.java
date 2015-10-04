package joss.jacobo.quizapptestproject.models;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import joss.jacobo.quizapptestproject.BR;
import joss.jacobo.quizapptestproject.R;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private List<QuestionItem> items;

    public QuestionAdapter(List<QuestionItem> items) {
        this.items = items;
    }

    @Override
    public QuestionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, viewType, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(QuestionAdapter.ViewHolder holder, int position) {
        QuestionItem item = items.get(position);
        int viewType = getItemViewType(position);
        switch (viewType) {
            case R.layout.item_question_title:
                holder.binding.setVariable(BR.question, item);
                break;

            case R.layout.item_question_answer:
                holder.binding.setVariable(BR.choice, item);
                break;
        }
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public int getItemViewType(int position) {
        QuestionItem item = items.get(position);
        if (item instanceof Question) {
            return R.layout.item_question_title;
        } else if (item instanceof MultipleChoiceAnswer) {
            return R.layout.item_question_answer;
        }
        throw new AssertionError();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ViewDataBinding binding;

        public ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
