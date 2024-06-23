/*
 * Copyright (c) 2020 ww23(https://github.com/ww23/BlindWatermark).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.syp.test.watermark.image;

import com.syp.test.watermark.image.converter.Converter;
import com.syp.test.watermark.image.converter.DctConverter;
import com.syp.test.watermark.image.converter.DftConverter;
import com.syp.test.watermark.image.dencoder.Decoder;
import com.syp.test.watermark.image.dencoder.Encoder;
import com.syp.test.watermark.image.dencoder.ImageEncoder;
import com.syp.test.watermark.image.dencoder.TextEncoder;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;
import org.opencv.core.Core;


/**
 * @author ww23
 */
public class BlindWatermark {

    private static final String FOURIER = "f";
    private static final String COSINE = "c";
    private static final String IMAGE = "i";
    private static final String TEXT = "t";

    static {
        //在使用OpenCV前必须加载Core.NATIVE_LIBRARY_NAME类,否则会报错
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {

        //Loader.load(opencv_java.class);

        Converter converter = new DctConverter();
//        String type = "encode";
//        String src = "/Users/mac/Downloads/头像.png";
//        String dst = "/Users/mac/Downloads/头像暗水印—java.png";
        String wm  = "12345678";

        String type = "decode";
        String src = "/Users/mac/Downloads/截图22.png";
        String dst = "/Users/mac/Downloads/头像暗水印提取—22.png";


        switch (type) {
            case "encode":
                Encoder encoder = null;
                encoder = new TextEncoder(converter);
                //encoder = new ImageEncoder(converter);
                encoder.encode(src, wm, dst);
                break;
            case "decode":
                Decoder decoder = new Decoder(converter);
                decoder.decode(src, dst);
                break;
            default:
                help();
        }
    }

//    public static void main(String[] args) {
//
//        Loader.load(opencv_java.class);
//
//        if (args.length < 4) {
//            help();
//        }
//
//        Converter converter = null;
//        String option = args[1].substring(1);
//
//        if (option.contains(FOURIER)) {
//            converter = new DftConverter();
//        } else if (option.contains(COSINE)) {
//            converter = new DctConverter();
//        } else {
//            help();
//        }
//
//        switch (args[0]) {
//            case "encode":
//                Encoder encoder = null;
//                if (option.contains(IMAGE)) {
//                    encoder = new ImageEncoder(converter);
//                } else if (option.contains(TEXT)) {
//                    encoder = new TextEncoder(converter);
//                } else {
//                    help();
//                }
//                assert encoder != null;
//                encoder.encode(args[2], args[3], args[4]);
//                break;
//            case "decode":
//                Decoder decoder = new Decoder(converter);
//                decoder.decode(args[2], args[3]);
//                break;
//            default:
//                help();
//        }
//    }

    private static void help() {
        System.out.println("Usage: java -jar BlindWatermark.jar <commands>\n" +
                "   commands: \n" +
                "       encode <option> <original image> <watermark> <embedded image>\n" +
                "       decode <option> <original image> <embedded image>\n" +
                "   encode options: \n" +
                "       -c discrete cosine transform\n" +
                "       -f discrete fourier transform (Deprecated)\n" +
                "       -i image watermark\n" +
                "       -t text  watermark\n" +
                "   decode options: \n" +
                "       -c discrete cosine transform\n" +
                "       -f discrete fourier transform (Deprecated)\n" +
                "   example: \n" +
                "       encode -ct foo.png test bar.png" +
                "       decode -c  foo.png bar.png"
        );
        System.exit(-1);
    }
}