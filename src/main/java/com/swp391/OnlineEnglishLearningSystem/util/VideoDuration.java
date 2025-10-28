package com.swp391.OnlineEnglishLearningSystem.util;

import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.info.MultimediaInfo;

import java.io.File;

public class VideoDuration {
    public static long getVideoDuration(MultipartFile videoFile) throws Exception {
        File tempFile = File.createTempFile("video_", "_temp");
        try{
            videoFile.transferTo(tempFile);

            MultimediaObject multimediaObject = new MultimediaObject(tempFile);
            MultimediaInfo multimediaInfo = multimediaObject.getInfo();

            return multimediaInfo.getDuration();
        }finally {
            if (tempFile.exists()){
                tempFile.delete();
            }
        }
    }

    public static String formaDuration(long milliseconds){
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        seconds = seconds % 60;
        minutes = minutes % 60;

        return hours == 0 ? String.format("%02d:%02d", minutes, seconds) : String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
