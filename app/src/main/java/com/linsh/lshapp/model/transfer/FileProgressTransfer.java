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

public class FileProgressTransfer implements Func1<ResponseBody, Observable<Float>> {

    private File destFile;

    public FileProgressTransfer(String destFile) {
        this(new File(destFile));
    }

    public FileProgressTransfer(File destFile) {
        this.destFile = destFile;
    }

    @Override
    public Observable<Float> call(ResponseBody responseBody) {
        return Observable.create(subscriber -> {
            try {
                saveFile(responseBody, subscriber);
                subscriber.onCompleted();
            } catch (IOException e) {
                subscriber.onError(e);
            }
        });
    }

    private void saveFile(ResponseBody response, Subscriber<? super Float> subscriber) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.byteStream();
            final long total = response.contentLength();

            long sum = 0;

            if (destFile == null) {
                throw new RuntimeException("没有传入文件");
            }
            destFile.getParentFile().mkdirs();

            fos = new FileOutputStream(destFile);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
                final long finalSum = sum;
                subscriber.onNext(finalSum * 1.0f / total);
            }
            fos.flush();
        } finally {
            LshIOUtils.close(response, is, fos);
        }
    }
}
