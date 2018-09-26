package com.salamander.mo_sam_div4_dts1.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.Function;
import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.SessionManager;

/**
 * Created by benny_aziz on 03/17/2015.
 */
public class FragmentSetting extends Fragment {

    private Spinner spTerms;
    private ListView listView;
    private ArrayAdapter<String> adapter_list, adapter_terms;
    private SessionManager session;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting, container, false);

        spTerms = (Spinner) root.findViewById(R.id.setting_sp_terms);
        listView = (ListView) root.findViewById(R.id.setting_listview);

        session = new SessionManager(getActivity());

        final String[] lists = {"Backup Database", "Restore Database"};

        adapter_terms = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_checked, App.terms);
        spTerms.setAdapter(adapter_terms);
        adapter_list = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, lists);
        listView.setAdapter(adapter_list);

        if (App.defaultTerms != null)
            spTerms.setSelection(adapter_terms.getPosition(App.defaultTerms));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        new Function(getActivity()).backupDatabase();
                        break;
                    case 1:
                        new Function(getActivity()).restoreDatabase();
                        break;
                }
            }
        });

        spTerms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                App.defaultTerms = adapter_terms.getItem(i);
                session.createLoginSession(App.getUser(getActivity()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return root;
    }
}
