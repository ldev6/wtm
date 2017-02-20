package com.montreal.wtm.utils.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;


import com.montreal.wtm.R;
import com.montreal.wtm.utils.model.DataRowDebugItem;

import java.util.ArrayList;

public class MenuDebugAdapter extends BaseAdapter {

    private static final int TITLE = 0;
    private static final int SEPARATOR = 1;
    private static final int ITEM_SERVER = 2;
    private static final int ITEM_SWITCH = 3;
    private static final int ITEM_COMBO_BOX = 4;

    private final LayoutInflater layoutInflater;
    private final Resources resources;
    private Context context;
    private MenuDebugInterface menuDebugInterface;
    private ArrayList<DataRowDebugItem> dataRowItems;

    private static final String PROPERTY_LANG = "PROPERTY_LANG";


    public interface MenuDebugInterface {
        public void changeServer(String serverName);

        public void enableTelescope(boolean enable);

        public void changeLang(String lang);
    }

    public MenuDebugAdapter(Context context, MenuDebugInterface menuDebugInterface) {
        layoutInflater = LayoutInflater.from(context);
        resources = context.getResources();
        this.context = context;
        this.menuDebugInterface = menuDebugInterface;

        dataRowItems = new ArrayList<DataRowDebugItem>();
        dataRowItems.add(new DataRowDebugItem(TITLE, "DEBUG MENU"));
        dataRowItems.add(new DataRowDebugItem(SEPARATOR, "Change Server"));
        dataRowItems.add(new DataRowDebugItem(ITEM_SERVER, "Dev"));
        dataRowItems.add(new DataRowDebugItem(ITEM_SERVER, "Production"));
        dataRowItems.add(new DataRowDebugItem(SEPARATOR, ""));
        dataRowItems.add(new DataRowDebugItem(ITEM_SWITCH, "Enable screen report"));
        dataRowItems.add(new DataRowDebugItem(ITEM_COMBO_BOX, new ComboClass("Change language", new String[]{"fr", "en"})));
    }

    public static class DebugHolder {
        public TextView textView;
        public Switch aSwitch;
        public Spinner spinner;
    }

    private class ComboClass {
        public String textDesc;
        public String[] choices;

        public ComboClass(String textDesc, String[] choices) {
            this.textDesc = textDesc;
            this.choices = choices;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public int getCount() {
        return dataRowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return dataRowItems.get(position).data;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return dataRowItems.get(position).type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int itemViewType = getItemViewType(position);
        DebugHolder holder = null;
        if (convertView == null) {
            holder = new DebugHolder();
            switch (itemViewType) {
                case TITLE:
                    convertView = layoutInflater.inflate(R.layout.menu_debug_item_separator, parent, false);
                    holder.textView = (TextView) convertView.findViewById(R.id.text1);
                    holder.textView.setTextSize(25f);
                    final String item = (String) (dataRowItems.get(position).data);
                    holder.textView.setText(item);
                    break;
                case SEPARATOR:
                    convertView = layoutInflater.inflate(R.layout.menu_debug_item_separator, parent, false);
                    holder.textView = (TextView) convertView.findViewById(R.id.text1);
                    final String separator = (String) (dataRowItems.get(position).data);
                    holder.textView.setText(separator);
                    break;
                case ITEM_SERVER:
                    convertView = layoutInflater.inflate(R.layout.menu_debug_item, parent, false);
                    holder.textView = (TextView) convertView.findViewById(R.id.text1);
                    break;

                case ITEM_SWITCH:
                    convertView = layoutInflater.inflate(R.layout.menu_debug_item_switch, parent, false);
                    holder.textView = (TextView) convertView.findViewById(R.id.tvName);
                    holder.aSwitch = (Switch) convertView.findViewById(R.id.vSwitch);
                    break;

                case ITEM_COMBO_BOX:
                    convertView = layoutInflater.inflate(R.layout.menu_debug_combo_row, parent, false);
                    holder.textView = (TextView) convertView.findViewById(R.id.tvName);
                    holder.spinner = (Spinner) convertView.findViewById(R.id.spinner);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (DebugHolder) convertView.getTag();
        }


        switch (itemViewType) {
            case ITEM_SERVER:
                final String item = (String) (dataRowItems.get(position).data);
                holder.textView.setText(item);
                holder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menuDebugInterface.changeServer(item);
                    }
                });
                break;
            case ITEM_SWITCH:
                final String itemSwitch = (String) (dataRowItems.get(position).data);
                holder.textView.setText(itemSwitch);
                holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        menuDebugInterface.enableTelescope(isChecked);
                    }
                });
                break;
            case ITEM_COMBO_BOX:
                ComboClass comboClass = (ComboClass) (dataRowItems.get(position).data);
                holder.textView.setText(comboClass.textDesc);
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);
                spinnerAdapter.addAll(comboClass.choices);
                holder.spinner.setAdapter(spinnerAdapter);
                int langPosition = getLang(convertView.getContext());
                holder.spinner.setSelection(langPosition);
                holder.spinner.setOnItemSelectedListener(new SpinnerOnItemSelectedListener(spinnerAdapter));
                break;

        }
        return convertView;
    }

    private class SpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {


        private ArrayAdapter<String> spinnerAdapter;

        private SpinnerOnItemSelectedListener(ArrayAdapter<String> spinnerAdapter) {
            this.spinnerAdapter = spinnerAdapter;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String value = spinnerAdapter.getItem(position);
            saveInPreference(parent.getContext(), position);
            parent.getContext().getSharedPreferences(MenuDebugAdapter.class.getSimpleName(), Context.MODE_PRIVATE);

            menuDebugInterface.changeLang(value);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private void saveInPreference(Context context, int langPosition) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MenuDebugAdapter.class.getSimpleName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PROPERTY_LANG, langPosition);
        editor.commit();

    }

    private int getLang(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MenuDebugAdapter.class.getSimpleName(), Context.MODE_PRIVATE);
        return sharedPreferences.getInt(PROPERTY_LANG, 0);
    }


}
