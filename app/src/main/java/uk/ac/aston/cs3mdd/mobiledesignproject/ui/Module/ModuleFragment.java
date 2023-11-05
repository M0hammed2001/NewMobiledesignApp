package uk.ac.aston.cs3mdd.mobiledesignproject.ui.Module;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.LinearLayout;
import uk.ac.aston.cs3mdd.mobiledesignproject.R;

public class ModuleFragment extends Fragment {

    private Button add; // Button to add a new module card
    private AlertDialog dialog; // Dialog for entering module details
    private LinearLayout layout; // Layout for displaying module cards

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment's layout
        View rootView = inflater.inflate(R.layout.fragment_module, container, false);

        // Initialize UI elements
        add = rootView.findViewById(R.id.button_addmodule);
        layout = rootView.findViewById(R.id.container);

        // Build the dialog for entering module details
        buildDialog();

        // Set an OnClickListener for the "Add" button
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the dialog when the "Add" button is clicked
                dialog.show();
            }
        });

        return rootView; // Return the fragment's view
    }

    private void buildDialog() {
        // Create a dialog for entering module details
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = getLayoutInflater().inflate(R.layout.dialog, null);

        // Initialize EditText fields for module details
        final EditText modulenameEdit = view.findViewById(R.id.modulename_Edit);

        final EditText assignmentsNameEdit = view.findViewById(R.id.assignmentsName_Edit);
        final EditText assignmentsDateEdit = view.findViewById(R.id.assignmentsDateTime_Edit);

        final EditText examNameEdit = view.findViewById(R.id.examName_Edit);
        final EditText examDateEdit = view.findViewById(R.id.examDate_Edit);

        final EditText lessonDateTimeEdit = view.findViewById(R.id.lessonDateTime_Edit);
        final EditText lessonRoomEdit = view.findViewById(R.id.RoomNumber_edit);

        builder.setView(view);
        builder.setTitle("Enter Module Details")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // When the "OK" button is clicked, add a new module card with details
                        String moduleName = modulenameEdit.getText().toString();

                        String assignmentsName = assignmentsNameEdit.getText().toString();
                        String assignmentsDate = assignmentsDateEdit.getText().toString();


                        String examName = examNameEdit.getText().toString();
                        String examDate = examDateEdit.getText().toString();


                        String lessonDateTime = lessonDateTimeEdit.getText().toString();
                        String LessonRoom = lessonRoomEdit.getText().toString();

                        addCard(moduleName,assignmentsName,assignmentsDate,examName,examDate,lessonDateTime,LessonRoom);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle the "Cancel" button click (you can add functionality here)
                    }
                });

        dialog = builder.create(); // Create the dialog
    }

    private void addCard(String moduleName, String assignmentsName,String assignmentsDate , String examName, String examDate, String lessonDateTime, String LessonRoom) {
        // Create a new module card and populate it with the entered details
        Module module = new Module(moduleName,assignmentsName,assignmentsDate,examName,examDate,lessonDateTime,LessonRoom);
        final View view = getLayoutInflater().inflate(R.layout.modulecard, null);

        // Initialize TextView fields on the module card
        TextView ModulenameView = view.findViewById(R.id.ModulenameField);

        TextView assignmentNameView = view.findViewById(R.id.AssignmentsNameField);
        TextView assignmentsDateView = view.findViewById(R.id.AssignmentsDateField);


        TextView examNameView = view.findViewById(R.id.ExamNameField);
        TextView examDateView = view.findViewById(R.id.ExamDateField);


        TextView lessonDateTimeView = view.findViewById(R.id.LessonDateTimeField);
        TextView lessonRoomView = view.findViewById(R.id.RoomField);


        Button delete = view.findViewById(R.id.button_delete);

        // Set the details on the card
        ModulenameView.setText(moduleName);
        assignmentNameView.setText("Assignments: " + assignmentsName );
        assignmentsDateView.setText("Date" + assignmentsDate);
        examNameView.setText("Exam: " + examName );
        examDateView.setText("date:" + examDate);
        lessonDateTimeView.setText("Lesson: " + lessonDateTime);
        lessonRoomView.setText("Room:" + LessonRoom);

        // Set a click listener to remove the card when the "Delete" button is clicked
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeView(view);
            }
        });

        // Add the module card to the layout
        layout.addView(view);
    }


}