package com.ark.robokart_robotics.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class ChapterContent implements Parcelable {

    public final String chapter_content;
    public final String video_time;
    public final String video_url;
    public final String assignment_url;
    public final String quiz_id;

    public ChapterContent(String chapter_content, String video_time, String video_url, String assignment_url, String quiz_id) {
        this.chapter_content = chapter_content;
        this.video_time = video_time;
        this.video_url = video_url;
        this.assignment_url = assignment_url;
        this.quiz_id = quiz_id;
    }


    protected ChapterContent(Parcel in) {
        chapter_content = in.readString();
        video_time = in.readString();
        video_url = in.readString();
        assignment_url = in.readString();
        quiz_id = in.readString();
    }

    public static final Creator<ChapterContent> CREATOR = new Creator<ChapterContent>() {
        @Override
        public ChapterContent createFromParcel(Parcel in) {
            return new ChapterContent(in);
        }

        @Override
        public ChapterContent[] newArray(int size) {
            return new ChapterContent[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(chapter_content);
    }
}
