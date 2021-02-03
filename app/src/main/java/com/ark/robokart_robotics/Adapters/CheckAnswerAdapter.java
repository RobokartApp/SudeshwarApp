package com.ark.robokart_robotics.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ark.robokart_robotics.Activities.Quiz.QuizActivity;
import com.ark.robokart_robotics.Fragments.AnswerExplanation.BottomSheetExplanation;
import com.ark.robokart_robotics.Model.CorrectAnswersModel;
import com.ark.robokart_robotics.Model.Question;
import com.ark.robokart_robotics.R;

import java.util.ArrayList;
import java.util.List;

//import static com.facebook.FacebookSdk.getApplicationContext;

public class CheckAnswerAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final String TAG = "BlogAdapter";
    private final List<Question> mStandardList;
    private List<CorrectAnswersModel> correctAnswersModelList;
    private final Context mcontext;




    public CheckAnswerAdapter(Context context, List<Question> recommendationsList) {
        this.mcontext = context;
        mStandardList = recommendationsList;

    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            correctAnswersModelList = new ArrayList<>();
        return new CheckAnswerAdapter.ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.show_correct_answers_item, parent, false));
    }
    @Override
    public int getItemViewType(int position) {
        return 0;
    }
    @Override
    public int getItemCount() {
        if (mStandardList != null && mStandardList.size() > 0) {
            return mStandardList.size();
        } else {
            return 0;
        }
    }
    public class ViewHolder extends BaseViewHolder {

        ImageView answer_bg;
        TextView question_count;


        public ViewHolder(View itemView) {
            super(itemView);

            answer_bg = itemView.findViewById(R.id.answer_bg);
            question_count = itemView.findViewById(R.id.question_count);

        }
        protected void clear() {
            question_count.setText("");
        }
        public void onBind(int position) {
            super.onBind(position);
            final Question question = mStandardList.get(position);

            question_count.setText(String.valueOf(question.getQ_no()));

            correctAnswersModelList = QuizActivity.correctAnswersList;

            try {
                final CorrectAnswersModel correctAnswersModel = QuizActivity.correctAnswersList.get(position);

                int co_id = correctAnswersModel.getAnswer();

                int q_answer = question.getAnswer();

                if(co_id == q_answer){
                    answer_bg.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.right_answer_item_bg));
                }
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }

            answer_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences sharedPreferences = mcontext.getSharedPreferences("explanation",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("question",question.getQuestion_name());
                    editor.putString("explaination",question.getAnswer_explaination());
                    editor.putString("answer",""+question.getAnswer());
                    editor.putString("answer1",""+question.getAnswer1());
                    editor.putString("answer2",""+question.getAnswer2());
                    editor.putString("answer3",""+question.getAnswer3());
                    editor.putString("answer4",""+question.getAnswer4());
                    editor.apply();

                    BottomSheetExplanation bottomSheetExplanation = new BottomSheetExplanation();
                    bottomSheetExplanation.show(((FragmentActivity) v.getContext()).getSupportFragmentManager(), bottomSheetExplanation.getTag());
                    }
            });
        }
    }


}


