package com.fraggel.recexec;

import android.app.*;
import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;
public class FolderLayout extends LinearLayout implements AdapterView.OnItemClickListener
 {

    Context context;
    IFolderItemListener folderListener;
    private List<String> item = null;
    private List<String> path = null;
    private String root = "/mnt";
    private TextView myPath;
    private ListView lstView;
	AlertDialog diag;

    public FolderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.context = context;
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.folderview, this);

        myPath = (TextView) findViewById(R.id.path);
        lstView = (ListView) findViewById(R.id.list);
		//folderListener=
        getDir(root, lstView);

    }

    public void setIFolderItemListener(IFolderItemListener folderItemListener) {
        this.folderListener = folderItemListener;
    }

    //Set Directory for view at anytime
    public void setDir(String dirPath){
        getDir(dirPath, lstView);
    }


    private void getDir(String dirPath, ListView v) {
		
        item = new ArrayList<String>();
        path = new ArrayList<String>();
        File f = new File(dirPath);
        File[] files = f.listFiles();
		java.util.Arrays.sort(files);

        if (!dirPath.equals(root)) {
            item.add("../");
			path.add(f.getParent());
        }
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
			if(file.isDirectory()){
               path.add(file.getPath());
               item.add(file.getName() + "/");
			}
		}
        setItemList(item);
    }

    //can manually set Item to display, if u want
    public void setItemList(List<String> item){
        ArrayAdapter<String> fileList = new ArrayAdapter<String>(context,
                R.layout.row, item);

        lstView.setAdapter(fileList);
        lstView.setOnItemClickListener(this);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
		 if(folderListener!=null)
			 folderListener.OnFileClicked(new File(path.get(position)));
		 getDir(path.get(position), l);
		 //edittext= (EditText) findViewById(R.id.path);
		 //edittext.setText(path.get(position));
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        onListItemClick((ListView) arg0, arg0, arg2, arg3);
    }

}
