package ru.javabegin.training.android.listick.adapters;

import java.util.ArrayList;

import ru.javabegin.training.android.todoproject.R;
import ru.javabegin.training.android.listick.objects.TodoDocument;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class TodoAdapter extends ArrayAdapter<TodoDocument> {
	
	private View.OnClickListener checkListener;

	public TodoAdapter(Context context, ArrayList<TodoDocument> objects, View.OnClickListener checkListener) {
		super(context, R.id.txt_todo_name, objects);
		this.checkListener = checkListener;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.custom_listview_row,
					parent, false);

			ViewHolder viewHolder = new ViewHolder();

			viewHolder.todoName = (TextView) convertView
					.findViewById(R.id.txt_todo_name);
			viewHolder.todoDate = (TextView) convertView
					.findViewById(R.id.txt_todo_date);
			viewHolder.imagePriority = (ImageView) convertView
					.findViewById(R.id.image_priority);
			viewHolder.checkBox = (CheckBox) convertView
					.findViewById(R.id.checkBox);

			convertView.setTag(viewHolder);

			viewHolder.checkBox.setOnClickListener(checkListener);

		}

		ViewHolder holder = (ViewHolder) convertView.getTag();

		TodoDocument todoDocument = getItem(position);

		holder.todoName.setText(todoDocument.getName());

		holder.todoDate.setText(DateFormat.format("dd MMMM, yyyy,  hh:mm",
				todoDocument.getCreateDate()));

		switch (todoDocument.getPriorityType()) {
		case HIGH:
			holder.imagePriority.setImageResource(R.drawable.ic_priority_high);
			break;
		case MIDDLE:
			holder.imagePriority
					.setImageResource(R.drawable.ic_priority_middle);
			break;

		case LOW:
			holder.imagePriority.setImageResource(R.drawable.ic_priority_low);
			break;

		default:
			break;
		}
	
		todoDocument.setChecked(false);		

		holder.checkBox.setTag(todoDocument);		

		return convertView;
	}

	static class ViewHolder {
		public TextView todoName;
		public TextView todoDate;
		public ImageView imagePriority;
		public CheckBox checkBox;
	}
	
	
	public void setCheckboxListener(View.OnClickListener checkListener){
		this.checkListener = checkListener;
	}
	


}
