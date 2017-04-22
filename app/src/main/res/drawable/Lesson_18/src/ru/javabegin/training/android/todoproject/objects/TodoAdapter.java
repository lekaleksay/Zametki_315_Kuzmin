package ru.javabegin.training.android.todoproject.objects;

import java.util.List;

import ru.javabegin.training.adnroid.todoproject.R;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TodoAdapter extends ArrayAdapter<TodoDocument> {

	private List<TodoDocument> list;

	public TodoAdapter(Context context, List<TodoDocument> list) {
		super(context, R.layout.custom_listview_row, R.id.todo_name, list);
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.custom_listview_row,
					parent, false);

			ViewHolder viewHolder = new ViewHolder();
			
			viewHolder.todoName = (TextView) convertView.findViewById(R.id.todo_name);
			viewHolder.todoDate = (TextView) convertView.findViewById(R.id.todo_date);
						
			convertView.setTag(viewHolder);
		}
		
		ViewHolder holder = (ViewHolder) convertView.getTag();

		TodoDocument todoDocument = list.get(position);

		holder.todoName.setText(todoDocument.getName());
		holder.todoDate.setText(DateFormat.format("dd MMMM, yyyy,  hh:mm",
				todoDocument.getCreateDate()));

		return convertView;
	}

	static class ViewHolder {
		public TextView todoName;
		public TextView todoDate;
	}

}
