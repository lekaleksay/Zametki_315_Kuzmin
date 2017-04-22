package ru.javabegin.training.android.listick.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import ru.javabegin.training.android.todoproject.R;
import ru.javabegin.training.android.listick.adapters.TodoAdapter;
import ru.javabegin.training.android.listick.objects.AppContext;
import ru.javabegin.training.android.listick.objects.TodoDocument;
import ru.javabegin.training.android.listick.objects.TodoListComparator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

public class TodoList extends Activity {

	private ListView listviewTasks;
	private MenuItem menuSort;

	private EditText txtSearch;
	private ArrayList<TodoDocument> listDocuments;
	private Intent intent;
	private TodoAdapter todoAdapter;

	private CheckboxListener checkboxListener = new CheckboxListener();

	private static Comparator<TodoDocument> comparator = TodoListComparator
			.getDateComparator();

	private BroadcastReceiver refreshListViewReceiver = new RefreshListViewReceiver();

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list);

		listviewTasks = (ListView) findViewById(R.id.listTasks);
		listviewTasks.setOnItemClickListener(new ListViewClickListener());
		listviewTasks.setEmptyView(findViewById(R.id.emptyView));

		listDocuments = ((AppContext) getApplicationContext())
				.getListDocuments();

		txtSearch = (EditText) findViewById(R.id.txtSearch);
		txtSearch.addTextChangedListener(new TextChangeListener());

		getActionBar().setDisplayHomeAsUpEnabled(false);

		intent = new Intent(this, TodoDetails.class);

		LocalBroadcastManager.getInstance(this).registerReceiver(refreshListViewReceiver, new IntentFilter(AppContext.RECEIVER_REFRESH_LISTVIEW));

	}

	

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		sort();		
	}
	
	private void checkControlsActive() {
		if (menuSort == null)
			return;
		if (listDocuments.isEmpty()) {
			txtSearch.setEnabled(false);
		} else {
			txtSearch.setEnabled(true);
		}
	}

	private void sort() {

		Collections.sort(listDocuments, comparator);
		updateIndexes();

		todoAdapter = new TodoAdapter(this, listDocuments, checkboxListener);
		listviewTasks.setAdapter(todoAdapter);

		todoAdapter.getFilter().filter(txtSearch.getText());
		
		checkControlsActive();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo_list, menu);

		menuSort = menu.findItem(R.id.menu_sort);

		checkControlsActive();

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.isChecked()) {
			return true;
		}

		switch (item.getItemId()) {
		case R.id.menu_new_task: {

			Bundle bundle = new Bundle();
			bundle.putInt(AppContext.ACTION_TYPE, AppContext.ACTION_NEW_TASK);

			intent.putExtras(bundle);
			startActivity(intent);

			return true;
		}

		case R.id.menu_sort_name: {
			comparator = TodoListComparator.getNameComparator();
			sort();
			item.setChecked(true);
			return true;
		}

		case R.id.menu_sort_date: {
			comparator = TodoListComparator.getDateComparator();
			sort();
			item.setChecked(true);
			return true;
		}

		case R.id.menu_sort_priority: {
			comparator = TodoListComparator.getPriorityComparator();
			sort();
			item.setChecked(true);
			return true;
		}

		case R.id.menu_delete_check: {

			if (!indexesForDelete.isEmpty()) {

				Intent intent = new Intent(AppContext.RECEIVER_DELETE_DOCUMENT);

				intent.putIntegerArrayListExtra(AppContext.DOC_INDEXES,
						new ArrayList<Integer>(indexesForDelete));
				LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
				
				indexesForDelete.clear();

			}


			return true;
		}

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void updateIndexes() {
		int i = 0;
		for (TodoDocument doc : listDocuments) {
			doc.setNumber(i++);
		}
	}

	public void clearSearch(View view) {
		txtSearch.setText("");
	}

	private class ListViewClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Bundle bundle = new Bundle();
			bundle.putInt(AppContext.ACTION_TYPE, AppContext.ACTION_UPDATE);
			bundle.putInt(AppContext.DOC_INDEX, ((TodoDocument) parent
					.getAdapter().getItem(position)).getNumber());

			intent.putExtras(bundle);
			startActivity(intent);
		}

	}

	private class TextChangeListener implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			todoAdapter.getFilter().filter(s);
		}

	}

	private class RefreshListViewReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			sort();
		}

	}

	private Set<Integer> indexesForDelete = new TreeSet<Integer>();

	private class CheckboxListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			CheckBox checkBox = (CheckBox) v;
			TodoDocument todoDocument = (TodoDocument) checkBox.getTag();
			todoDocument.setChecked(checkBox.isChecked());
			
			if (checkBox.isChecked()) {
				indexesForDelete.add(todoDocument.getNumber());			
			} else {
				indexesForDelete.remove(todoDocument.getNumber());
			}

		}

	}
	
	

}
