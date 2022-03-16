package app.touchlessChef.template.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.touchlessChef.R;
import app.touchlessChef.adapter.DatabaseAdapter;
import app.touchlessChef.adapter.InstructionAdapter;
import app.touchlessChef.model.Instruction;
import app.touchlessChef.model.Recipe;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeInstructionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeInstructionFragment extends NavigableFragment {
    private InstructionListener myListener;
    private List<Instruction> instructionList;
    private InstructionAdapter instructionAdapter;
    private DatabaseAdapter databaseAdapter;

    private RecyclerView instructionRecyclerView;
    private TextView emptyView;
    private Button addButton;
    private EditText instructionField;

    public RecipeInstructionFragment() {
        // Required empty public constructor
    }

    public static RecipeInstructionFragment newInstance(Recipe recipe) {
        RecipeInstructionFragment fragment = new RecipeInstructionFragment();

        if (recipe != null) {
            Bundle args = new Bundle();
            args.putParcelableArrayList("instruction", (ArrayList<Instruction>) recipe.getInstructions());
            fragment.setArguments(args);
        }

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_instruction, container, false);
        databaseAdapter = DatabaseAdapter.getInstance(getActivity());

        Bundle args = getArguments();
        if (args != null)
            instructionList = args.getParcelableArrayList("instruction");
        if (instructionList == null)
            instructionList = new ArrayList<>();

        instructionRecyclerView = view.findViewById(R.id.recyclerView);
        emptyView = view.findViewById(R.id.empty_view);
        addButton = view.findViewById(R.id.add_button);
        instructionField = view.findViewById(R.id.instructionField);
        instructionAdapter = new InstructionAdapter(getActivity(), instructionList);
        instructionAdapter.setInstructionListener(position -> {
            instructionList.remove(position);
            toggleEmptyView();
            instructionAdapter.notifyDataSetChanged();
        });

        toggleEmptyView();

        instructionRecyclerView.setHasFixedSize(true);
        instructionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        instructionRecyclerView.setAdapter(instructionAdapter);

        addButton.setOnClickListener(v -> {
            Log.i("DAO", "Add button pressed.");
            String newInstruction = instructionField.getText().toString();
            Log.i("DAO", "New instruction: " + newInstruction);
            if (!newInstruction.isEmpty()) {
                Log.i("DAO", "Instructions list BEFORE: " + instructionList);
                instructionField.setText("");
                instructionList.add(new Instruction(newInstruction));
                Log.i("DAO", "Instructions list AFTER: " + instructionList);
                toggleEmptyView();
                instructionAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    private void toggleEmptyView() {
        if (instructionList.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            instructionRecyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            instructionRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            myListener = (InstructionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity + " must implement InstructionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        myListener = null;
    }

    @Override
    public void onNext() {
        if (myListener != null) {
            Log.i("DAO", "Steps finished: " + instructionList);
            myListener.onStepsFinished(instructionList);
        }
    }

    public interface InstructionListener {
        void onStepsFinished(List<Instruction> instruction);
    }
}