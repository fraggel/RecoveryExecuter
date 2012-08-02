package com.fraggel.recoveryexecuter.pro;

import java.io.File;

public interface IAPKItemListener {

	void OnCannotFileRead(File file);// implement what to do folder is
										// Unreadable

	void OnFileClicked(File file);// What to do When a file is clicked
}
