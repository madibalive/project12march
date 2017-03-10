package com.example.madiba.venualpha.Actions;

import java.io.File;
import java.util.List;

/**
 * Created by Madiba on 10/31/2016.
 */

public class SelectAction {

    public File file;
    public List<File> files;

    public SelectAction(List<File> files) {
        this.files = files;
    }

    SelectAction(File file) {
        this.file = file;
    }
}
