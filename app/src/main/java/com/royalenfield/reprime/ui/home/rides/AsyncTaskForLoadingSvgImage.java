package com.royalenfield.reprime.ui.home.rides;

import android.os.AsyncTask;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;
import com.caverock.androidsvg.SVGParseException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.royalenfield.reprime.utils.RELog;

public class AsyncTaskForLoadingSvgImage extends AsyncTask<Void,Void, SVG> {

    private String mUrl;
    private SVGImageView mSvgImageView;

    public AsyncTaskForLoadingSvgImage(String url, SVGImageView svgImageView) {
        this.mUrl = url;
        this.mSvgImageView = svgImageView;
    }

    @Override
    protected SVG doInBackground(Void... voids) {
        SVG svg;
        try {
            InputStream inputStream = new URL(mUrl).openStream();
            svg = SVG.getFromInputStream(inputStream);
            return svg;
        } catch (IOException e) {
            RELog.e(e);
        } catch (SVGParseException e) {
            RELog.e(e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(SVG svg) {
        super.onPostExecute(svg);
        if (svg != null) {
            mSvgImageView.setSVG(svg);
        }
    }

}
