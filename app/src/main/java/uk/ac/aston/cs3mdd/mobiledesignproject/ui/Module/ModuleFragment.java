package uk.ac.aston.cs3mdd.mobiledesignproject.ui.Module;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

//import java.util.ArrayList;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import uk.ac.aston.cs3mdd.mobiledesignproject.R;
import uk.ac.aston.cs3mdd.mobiledesignproject.databinding.FragmentModuleBinding;
import uk.ac.aston.cs3mdd.mobiledesignproject.databinding.PopupEditModuleBinding;
import uk.ac.aston.cs3mdd.mobiledesignproject.ui.Module.data.Module;
import uk.ac.aston.cs3mdd.mobiledesignproject.ui.Module.data.ModuleDatabase;
import uk.ac.aston.cs3mdd.mobiledesignproject.ui.Module.data.ModuleListAdapter;


public class ModuleFragment extends Fragment {

    private ModuleFragment moduleFragment;

    ModuleDatabase moduleDB;
    List<Module> moduleList;

    public Module modules;
    ModuleViewModel moduleViewModel;

    FragmentModuleBinding binding;

    private RecyclerView ModuleRecyclerView;


    private ModuleViewModel viewModel;

    private ModuleListAdapter moduleAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.popup_edit_module, container, false);

        // Initialize viewModel using ViewModelProvider
        viewModel = new ViewModelProvider(this).get(ModuleViewModel.class);

        binding = FragmentModuleBinding.inflate(inflater, container, false);

        RoomDatabase.Callback myCallBack = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
            }

            @Override
            public void onDestructiveMigration(@NonNull SupportSQLiteDatabase db) {
                super.onDestructiveMigration(db);
            }

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
            }
        };
            // this will clear the Database encase i want to add something new
//        moduleDB = Room.databaseBuilder(requireContext(), ModuleDatabase.class, "moduleDB").fallbackToDestructiveMigration().build();

        moduleDB = Room.databaseBuilder(requireContext(), ModuleDatabase.class, "moduleDB").addCallback(myCallBack).build();

        moduleViewModel = new ViewModelProvider(this).get(ModuleViewModel.class);


        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Get a handle to the RecyclerView.
        ModuleRecyclerView = view.findViewById(R.id.MFRecyclerView);
        // Create an adapter and supply the data to be displayed.
        moduleAdapter = new ModuleListAdapter(getContext(), viewModel.getAllModules().getValue());
        //calls the moduleDB defined in Module List and uses Line 89 to make sure it is not null
        moduleAdapter.setModuleDB(moduleDB);
        // Connect the adapter with the RecyclerView.
        ModuleRecyclerView.setAdapter(moduleAdapter);


        // Give the RecyclerView a default layout manager.
        ModuleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final Observer<List<Module>> ModuleListObserver = new Observer<List<Module>>() {
            @Override
            public void onChanged(@Nullable final List<Module> ModuleList) {
                // Update your adapter with the filtered list
                moduleAdapter.updateData(ModuleList);
                if (ModuleList != null) {
                    Log.i("MS", "Modules: " + ModuleList.size());
                } else {
                    Log.i("MS", "Modules: 0");
                }
            }
        };


        binding.addDatabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ModuleFragment.this).navigate(R.id.action_module_to_moduleAdd);
                Log.i("MS", "Navigating to Add Module Page");
            }
        });

        moduleViewModel.getAllModules().observe(getViewLifecycleOwner(), ModuleListObserver);
        getModuleListInBackground(moduleViewModel);

    }

    public void getModuleListInBackground(ModuleViewModel model) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                int ModuleSize = moduleDB.getModuleDAO().getAllModules().size();

                // background task
                    moduleList = moduleDB.getModuleDAO().getAllModules();

                    // on finish task
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            model.updateModule(moduleList);
                            // Create the text with line breaks
                            Toast.makeText(getContext(), "My Modules: " + ModuleSize, Toast.LENGTH_LONG).show();

                        }
                    });
            }
        });
    }
}
