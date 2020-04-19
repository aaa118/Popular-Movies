package com.demo.adi.model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideoQuery implements Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;

    @Override
    public String toString() {
        return "VideoQuery{" +
                "id=" + id +
                ", results=" + results +
                '}';
    }

    @SerializedName("results")
    @Expose
    private List<VideoQueryResult> results = null;
    public final static Parcelable.Creator<VideoQuery> CREATOR = new Creator<VideoQuery>() {

        public VideoQuery createFromParcel(Parcel in) {
            return new VideoQuery(in);
        }

        public VideoQuery[] newArray(int size) {
            return (new VideoQuery[size]);
        }

    }
            ;

    protected VideoQuery(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.results, (VideoQueryResult.class.getClassLoader()));
    }

    public VideoQuery() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<VideoQueryResult> getResults() {
        return results;
    }

    public void setResults(List<VideoQueryResult> results) {
        this.results = results;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeList(results);
    }

    public int describeContents() {
        return 0;
    }

}