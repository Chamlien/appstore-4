
package com.example.service.download;


import com.example.service.ProgressListener;

import java.io.File;

public abstract class DownloadListener implements ProgressListener<File> {

    public final Object tag;

    public DownloadListener(Object tag) {
        this.tag = tag;
    }
}
