package com.teiki.cafea1e.business;

import android.os.Parcel;
import android.os.Parcelable;

public class Record {
	
	private String recordid;
	
	private Fields fields;
	

	public String getRecordid() {
		return recordid;
	}

	public void setRecordid(String recordid) {
		this.recordid = recordid;
	}

	public Fields getFields() {
		return fields;
	}

	public void setFields(Fields fields) {
		this.fields = fields;
	}


/*    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(recordid);
        dest.writeParcelable(fields,1);
    }

    public static final Parcelable.Creator<Record> CREATOR = new Parcelable.Creator<Record>() {
        public Record createFromParcel(Parcel in) {
            return new Record(in);
        }

        public Record[] newArray(int size) {
            return new Record[size];
        }
    };

    private Record(Parcel in) {
        recordid=in.readString();
        fields = in.readParcelable(Fields);
    }
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }*/

}
