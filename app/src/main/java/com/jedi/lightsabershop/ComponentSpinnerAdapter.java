package com.jedi.lightsabershop;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jedi.jedishared.Component;

public class ComponentSpinnerAdapter extends ArrayAdapter<Component> {
  
  public ComponentSpinnerAdapter(Context context, Component[] componentValues) {
    super(context, android.R.layout.simple_spinner_item, componentValues);
  }
  
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    TextView view = (TextView) super.getView(position, convertView, parent);
    Component component = getItem(position);
    view.setText(component.getFormattedName()); // Assuming you have a getFormattedName() method in your Component enum
    return view;
  }
  
  @Override
  public View getDropDownView(int position, View convertView, ViewGroup parent) {
    TextView view = (TextView) super.getDropDownView(position, convertView, parent);
    Component component = getItem(position);
    view.setText(component.getFormattedName());
    return view;
  }
}