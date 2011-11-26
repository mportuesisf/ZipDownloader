package com.zipdownloader;

/**
 * ZipDownloader
 * 
 * A simple app to demonstrate downloading and unpacking a .zip file
 * as a background task.
 * 
 * Copyright (c) 2011 Michael J. Portuesi (http://www.jotabout.com)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.zipdownloader.util.DecompressZip;
import com.zipdownloader.util.DownloadFile;
import com.zipdownloader.util.ExternalStorage;

/**
 * Example app to download and unpack a .zip file from an internet URL.
 * 
 * @author portuesi
 *
 */
public class MainActivity extends Activity {
	
	//////////////////////////////////////////////////////////////////////////
	// State
	//////////////////////////////////////////////////////////////////////////
	
	protected ProgressDialog mProgressDialog;
	
	//////////////////////////////////////////////////////////////////////////
	// Activity Lifecycle
	//////////////////////////////////////////////////////////////////////////
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main );
        
        // Keep the screen (and device) active as long as this app is frontmost.
        // This is to avoid going to sleep during the download.
        // http://stackoverflow.com/questions/4376902/difference-between-wakelock-and-flag-keep-screen-on
        getWindow().addFlags( WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON );
    }
    
	//////////////////////////////////////////////////////////////////////////
	// Event handlers
	//////////////////////////////////////////////////////////////////////////
    
    /**
     * Invoked when user presses "Start download" button.
     */
    public void startDownload( View v ) {
    	String url = ((TextView) findViewById(R.id.url_field)).getText().toString();
    	new DownloadTask().execute( url );
    }
    
	//////////////////////////////////////////////////////////////////////////
	// Background Task
	//////////////////////////////////////////////////////////////////////////
    
    /**
     * Background task to download and unpack .zip file in background.
     */
    private class DownloadTask extends AsyncTask<String,Void,Exception> {

		@Override
		protected void onPreExecute() {
	    	showProgress();
		}

		@Override
		protected Exception doInBackground(String... params) {
			String url = (String) params[0];
			
			try {
				downloadAllAssets(url);
			} catch ( Exception e ) { return e; }
	    	
	    	return null;
		}

		@Override
		protected void onPostExecute(Exception result) {
			dismissProgress();
			if ( result == null ) { return; }
			// something went wrong, post a message to user - you could use a dialog here or whatever
			Toast.makeText(MainActivity.this, result.getLocalizedMessage(), Toast.LENGTH_LONG ).show();
		}
    }
    
	//////////////////////////////////////////////////////////////////////////
	// Progress Dialog
	//////////////////////////////////////////////////////////////////////////
    
 	protected void showProgress( ) {
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle( R.string.progress_title );
		mProgressDialog.setMessage( getString(R.string.progress_detail) );
		mProgressDialog.setIndeterminate( true );
		mProgressDialog.setCancelable( false );
		mProgressDialog.show();
	}

	protected void dismissProgress() {
		// You can't be too careful.
		if (mProgressDialog != null && mProgressDialog.isShowing() && mProgressDialog.getWindow() != null) {
			try {
				mProgressDialog.dismiss();
			} catch ( IllegalArgumentException ignore ) { ; }
		}
		mProgressDialog = null;
	}
	
	//////////////////////////////////////////////////////////////////////////
	// File Download
	//////////////////////////////////////////////////////////////////////////

	/**
 	 * Download .zip file specified by url, then unzip it to a folder in external storage.
	 *
	 * @param url
	 */
	private void downloadAllAssets( String url ) {
		// Temp folder for holding asset during download
		File zipDir =  ExternalStorage.getSDCacheDir(this, "tmp");
		// File path to store .zip file before unzipping
		File zipFile = new File( zipDir.getPath() + "/temp.zip" );
		// Folder to hold unzipped output
		File outputDir = ExternalStorage.getSDCacheDir( this, "unzipped" );

		try {
			DownloadFile.download( url, zipFile, zipDir );
			unzipFile( zipFile, outputDir );
		} finally {
			zipFile.delete();
		}
	}
	
	//////////////////////////////////////////////////////////////////////////
	// Zip Extraction
	//////////////////////////////////////////////////////////////////////////

	/**
	 * Unpack .zip file.
	 * 
	 * @param zipFile
	 * @param destination
	 */
	protected void unzipFile( File zipFile, File destination ) {
		DecompressZip decomp = new DecompressZip( zipFile.getPath(), 
				 destination.getPath() + File.separator );
		decomp.unzip();
	}
	
}