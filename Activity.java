package PracThirdYear;

import java.io.Serializable;
import java.util.Date;

public final class Activity implements Serializable {

    private static final long serialVersionUID = 6076492675589159174L;

    private Date time;

    private String location;

    private String description;

    public Activity() {
        time = new Date();
    }

    public Date getTime() {
        return time;
    }

    public String getDescription() {
        if (description == null) {
            return "unknown activity";
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        if (location == null) {
            return "unknown location";
        }
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append(time.toString());
        buff.append(" - ");
        buff.append(getLocation());
        buff.append(" - ");
        buff.append(getDescription());
        buff.append('\n');
        return buff.toString();
    }
}
