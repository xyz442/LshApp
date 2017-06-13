package com.linsh.lshapp.model.transfer;

import com.linsh.lshutils.utils.Basic.LshIOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by Senh Linsh on 17/5/19.
 */

public class FileTransfer implements Func1<ResponseBody, Observable<File>> {

    private File destFile;

    public FileTransfer(String destFile) {
        this(new File(destFile));
    }

    public FileTransfer(File destFile) {
        this.destFile = destFile;
    }

    @Override
    public Observable<File> call(ResponseBody responseBody) {
        return Observable.create(subscriber -> {
            try {
                saveFile(responseBody, subscriber);
                subscriber.onCompleted();
            } catch (IOException e) {
                subscriber.onError(e);
            }
        });
    }

    private void saveFile(ResponseBody response, Subscriber<? super File> subscriber) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.byteStream();

            if (destFile == null) {
                throw new RuntimeException("没有传入文件");
            }
            destFile.getParentFile().mkdirs();

            fos = new FileOutputStream(destFile);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            subscriber.onNext(destFile);
        } finally {
            LshIOUtils.close(response, is, fos);
        }
    }
}
