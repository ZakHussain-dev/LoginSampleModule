package com.loginmodule.util.datahelper;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;
import timber.log.Timber;

/**
 * Created for mock HTTP response using JSON file
 */
public class FakeInterceptor implements Interceptor {
    private static final String TAG = FakeInterceptor.class.getSimpleName();
    private static final String FILE_EXTENSION = ".json";
    private Context mContext;

    private String mContentType = "application/json";

    public FakeInterceptor(Context context) {
        mContext = context;
    }

    /**
     * Set content type for header
     *
     * @param contentType Content type
     * @return FakeInterceptor
     */
    public FakeInterceptor setContentType(String contentType) {
        mContentType = contentType;
        return this;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<String> listSuggestionFileName = new ArrayList<>();
        String method = chain.request().method().toLowerCase();

        Response response = null;
        // Get Request URI.
        final URI uri = chain.request().url().uri();
        Timber.d("--> Request url: [" + method.toUpperCase() + "]" + uri.toString());

        String defaultFileName = getFileName(chain);

        Timber.d("Read data from file: %s", defaultFileName);

        //create file name with http method
        //eg: getLogin.json
        listSuggestionFileName.add(method + upCaseFirstLetter(defaultFileName));

        //eg: login.json
        listSuggestionFileName.add(defaultFileName);

        String responseFileName = getFirstFileNameExist(listSuggestionFileName, uri);
        if (responseFileName != null) {
            String fileName = getFilePath(uri, responseFileName);
            Timber.d("Read data from file: %s", fileName);
            try {
                InputStream is = mContext.getAssets().open(fileName);
                BufferedReader r = new BufferedReader(new InputStreamReader(is));
                StringBuilder responseStringBuilder = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    responseStringBuilder.append(line).append('\n');
                }
                Timber.d("Response: %s", responseStringBuilder.toString());
                response = new Response.Builder().code(200)
                        .message(responseStringBuilder.toString())
                        .request(chain.request())
                        .protocol(Protocol.HTTP_1_0)
                        .body(ResponseBody.create(MediaType.parse(mContentType),
                                responseStringBuilder.toString().getBytes()))
                        .addHeader("content-type", mContentType)
                        .build();
            } catch (IOException e) {
                Timber.e(e);
            }
        } else {
            for (String file : listSuggestionFileName) {
                Timber.e("File not exist: %s", getFilePath(uri, file));
            }
            response = chain.proceed(chain.request());
        }

        Timber.d("<-- END [" + method.toUpperCase() + "]" + uri.toString());
        return response;
    }

    private String upCaseFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private String getFirstFileNameExist(List<String> inputFileNames, URI uri) throws IOException {
        String mockDataPath = uri.getHost() + uri.getPath();
        mockDataPath = mockDataPath.substring(0, mockDataPath.lastIndexOf('/'));
        Timber.d("Scan files in: %s", mockDataPath);
        //List all files in folder
        String[] files = mContext.getAssets().list(mockDataPath);
        for (String fileName : inputFileNames) {
            if (fileName != null) {
                for (String file : files) {
                    if (fileName.equals(file)) {
                        return fileName;
                    }
                }
            }
        }
        return null;
    }

    private String getFileName(Chain chain) {
        String fileName =
                chain.request().url().pathSegments().get(chain.request().url().pathSegments().size() - 1);
        return fileName.isEmpty() ? "index" + FILE_EXTENSION : fileName;
    }

    private String getFilePath(URI uri, String fileName) {
        String path;
        if (uri.getPath().lastIndexOf('/') != uri.getPath().length() - 1) {
            path = uri.getPath().substring(0, uri.getPath().lastIndexOf('/') + 1);
        } else {
            path = uri.getPath();
        }
        return uri.getHost() + path + fileName;
    }
}
