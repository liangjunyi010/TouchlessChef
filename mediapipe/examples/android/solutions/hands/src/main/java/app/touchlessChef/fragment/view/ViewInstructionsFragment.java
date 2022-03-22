package app.touchlessChef.fragment.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.touchlessChef.R;
import app.touchlessChef.adapter.recipe.InstructionAdapter;
import app.touchlessChef.model.Instruction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewInstructionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewInstructionsFragment extends Fragment {
    private List<Instruction> instructionList;
    private RecyclerView instructionRecyclerView;
    private TextView emptyView;

    public ViewInstructionsFragment() {}

    public static ViewInstructionsFragment newInstance(List<Instruction> instructions) {
        ViewInstructionsFragment fragment = new ViewInstructionsFragment();
        if (instructions == null)
            instructions = new ArrayList<>();
        Bundle args = new Bundle();
        args.putParcelableArrayList("instructions", (ArrayList<Instruction>) instructions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_instructions, container, false);

        Bundle args = getArguments();
        if (args != null)
            instructionList = args.getParcelableArrayList("instructions");

        instructionRecyclerView = view.findViewById(R.id.recyclerView);
        emptyView = view.findViewById(R.id.empty_view);

        InstructionAdapter instructionAdapter = new InstructionAdapter(instructionList, false);
        toggleEmptyView();

        instructionRecyclerView.setHasFixedSize(true);
        instructionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        instructionRecyclerView.setAdapter(instructionAdapter);

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
}