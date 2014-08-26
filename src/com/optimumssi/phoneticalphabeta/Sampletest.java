package com.optimumssi.phoneticalphabeta;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentProvider.PipeDataWriter;
import android.content.ContentValues;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Sampletest extends ContentProvider implements PipeDataWriter<InputStream> {

	@Override
	public AssetFileDescriptor openAssetFile(Uri uri, String mode) throws FileNotFoundException {
		// Try to open an asset with the given name.
		try {
			InputStream is = getContext().getAssets().open(uri.getPath());
			// Start a new thread that pipes the stream data back to the caller.
			return new AssetFileDescriptor(
					openPipeHelper(uri, null, null, is, this), 0,
					AssetFileDescriptor.UNKNOWN_LENGTH);
		} catch (IOException e) {
			FileNotFoundException fnf = new FileNotFoundException("Unable to open " + uri);
			throw fnf;
		}
	}
	
	@Override
    public void writeDataToPipe(ParcelFileDescriptor output, Uri uri, String mimeType,
            Bundle opts, InputStream args) {
        // Transfer data from the asset to the pipe the client is reading.
        byte[] buffer = new byte[8192];
        int n;
        FileOutputStream fout = new FileOutputStream(output.getFileDescriptor());
        try {
            while ((n=args.read(buffer)) >= 0) {
                fout.write(buffer, 0, n);
            }
        } catch (IOException e) {
            Log.i("InstallApk", "Failed transferring", e);
        } finally {
            try {
                args.close();
            } catch (IOException e) {
            }
            try {
                fout.close();
            } catch (IOException e) {
            }
        }
    }

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
			String arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}

}
