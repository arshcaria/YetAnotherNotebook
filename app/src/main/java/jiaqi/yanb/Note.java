package jiaqi.yanb;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;


public class Note implements Serializable {
    private UUID mId;
    private String mTitle;
    private String mContent;
    private Date mDate;

    public Note(UUID id, String title, String content, Date date) {
        mId = id;
        mTitle = title;
        mContent = content;
        mDate = date;
    }

    /**
     * used to update existing note, the ID of the note won't change.
     */
    public void updateNote(String title, String content, Date date) {
        if (title != null) {
            mTitle = title;
        }

        if (content != null) {
            mContent = content;
        }

        if (date != null) {
            mDate = date;
        }
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
