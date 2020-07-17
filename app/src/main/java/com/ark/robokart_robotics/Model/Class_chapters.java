package com.ark.robokart_robotics.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Class_chapters {

    private int num;
    private String chapter_name;
    private ArrayList<Course_List> courses;

    public Class_chapters(int num, String chapter_name, ArrayList<Course_List> courses) {
        this.num = num;
        this.chapter_name = chapter_name;
        this.courses = courses;
    }

    public Class_chapters() {
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getChapter_name() {
        return chapter_name;
    }

    public void setChapter_name(String chapter_name) {
        this.chapter_name = chapter_name;
    }

    public ArrayList<Course_List> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course_List> courses) {
        this.courses = courses;
    }

    public static class Course_List implements Parcelable {

        public Course_List(int num,String chapter_content, String video_time, String video_url, String assignment_url, String quiz_id) {
            this.num = num;
            this.chapter_content = chapter_content;
            this.video_time = video_time;
            this.video_url = video_url;
            this.assignment_url = assignment_url;
            this.quiz_id = quiz_id;
        }

        public Course_List() {
        }

        private int num;
        private String chapter_content, video_time, video_url, assignment_url, quiz_id;

        protected Course_List(Parcel in) {
            num = in.readInt();
            chapter_content = in.readString();
            video_time = in.readString();
            video_url = in.readString();
            assignment_url = in.readString();
            quiz_id = in.readString();
        }

        public static final Creator<Course_List> CREATOR = new Creator<Course_List>() {
            @Override
            public Course_List createFromParcel(Parcel in) {
                return new Course_List(in);
            }

            @Override
            public Course_List[] newArray(int size) {
                return new Course_List[size];
            }
        };

        public String getChapter_content() {
            return chapter_content;
        }

        public void setChapter_content(String chapter_content) {
            this.chapter_content = chapter_content;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getVideo_time() {
            return video_time;
        }

        public void setVideo_time(String video_time) {
            this.video_time = video_time;
        }

        public String getVideo_url() {
            return video_url;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
        }

        public String getAssignment_url() {
            return assignment_url;
        }

        public void setAssignment_url(String assignment_url) {
            this.assignment_url = assignment_url;
        }

        public String getQuiz_id() {
            return quiz_id;
        }

        public void setQuiz_id(String quiz_id) {
            this.quiz_id = quiz_id;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.num);
            parcel.writeString(this.chapter_content);
            parcel.writeString(this.video_time);
            parcel.writeString(this.video_url);
            parcel.writeString(this.assignment_url);
            parcel.writeString(this.quiz_id);
        }
    }
}
