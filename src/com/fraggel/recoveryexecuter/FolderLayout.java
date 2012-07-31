package com.fraggel.recoveryexecuter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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
        try {
			
		
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.folderview, this);

        myPath = (TextView) findViewById(R.id.path);
        lstView = (ListView) findViewById(R.id.list);
		//folderListener=
       
        getDir(root, lstView);
        } catch (Exception e) {
			new REException(e);
			
		}
    }

    public void setIFolderItemListener(IFolderItemListener folderItemListener) {
        this.folderListener = folderItemListener;
    }

    //Set Directory for view at anytime
    public void setDir(String dirPath) throws Exception{
        getDir(dirPath, lstView);
    }


    private void getDir(String dirPath, ListView v) throws Exception{
		myPath.setText(dirPath);
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
    public void setItemList(List<String> item) throws Exception{
		miAdapter fileList=new miAdapter(context,item);
        //ArrayAdapter<String> fileList = new ArrayAdapter<String>(context,R.layout.row, item);

        lstView.setAdapter(fileList);
        lstView.setOnItemClickListener(this);
    }

    public void onListItemClick(ListView l, View v, int position, long id) throws Exception {
		try{
			if(folderListener!=null)
				folderListener.OnFileClicked(new File(path.get(position)));


			File r =new File(path.get(position));
			if(r.canRead()){
				getDir(path.get(position), l);
			}
		}catch(Exception e){}
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
    	try {
    		onListItemClick((ListView) arg0, arg0, arg2, arg3);
    	} catch (Exception e) {
			new REException(e);
			
		}
    }

}
