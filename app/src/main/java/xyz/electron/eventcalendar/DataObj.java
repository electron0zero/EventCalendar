package xyz.electron.eventcalendar;

import java.util.List;

public class DataObj {

    /**
     * eventMetadata : {"event_name":"My Best event","iconUrl":"http://roman.nurik.net/media/material_icon_gutenberg.png","posterUrl":"https://placeimg.com/640/480/arch/sepia","update_number":"0"}
     * eventSchedule : [{"Name":"activity1","date":"10-12-2017","time":"10:15","image":"https://i.imgur.com/4AiXzf8.jpg","details":"an paragraph about the event","contact_phone":"+91 96513131313","contact_email":"example@example.com","spacial_note":"anything extra about event, requirement, dress Code etc.","event_location":"70.151500,15.891515","register":{"isRequired":"true","url":"reg_form_URL","contact":"registration_contact","fees":"fees"},"activitySponsors":[{"name":"sponsor Name 1","image":"http://www.clker.com/cliparts/a/Z/q/o/I/p/uncle-sam-hi.png","url":"https://www.google.co.in/"},{"name":"sponsor Name 2","image":"https://i.imgur.com/NiTuE1J.jpg","url":"https://www.google.co.in/"}]},{"Name":"activity2","date":"10-12-2017","time":"10:15","image":"https://i.imgur.com/4AiXzf8.jpg","details":"an paragraph about the event","contact_phone":"+91 96513131313","contact_email":"example@example.com","spacial_note":"anything extra about event, requirement, dress Code etc.","event_location":"70.151500,15.891515","register":{"isRequired":"true","url":"reg_form_URL","contact":"registration_contact","fees":"fees"},"activitySponsors":[{"name":"sponsor Name 1","image":"http://www.clker.com/cliparts/a/Z/q/o/I/p/uncle-sam-hi.png","url":"https://www.google.co.in/"},{"name":"sponsor Name 2","image":"https://i.imgur.com/NiTuE1J.jpg","url":"https://www.google.co.in/"}]},{"Name":"activity3","date":"10-12-2017","time":"10:15","image":"https://i.imgur.com/4AiXzf8.jpg","details":"an paragraph about the event","contact_phone":"+91 96513131313","contact_email":"example@example.com","spacial_note":"anything extra about event, requirement, dress Code etc.","event_location":"70.151500,15.891515","register":{"isRequired":"true","url":"reg_form_URL","contact":"registration_contact","fees":"fees"},"activitySponsors":[{"name":"sponsor Name 1","image":"http://www.clker.com/cliparts/a/Z/q/o/I/p/uncle-sam-hi.png","url":"https://www.google.co.in/"},{"name":"sponsor Name 2","image":"https://i.imgur.com/NiTuE1J.jpg","url":"https://www.google.co.in/"}]},{"Name":"activity4","date":"10-12-2017","time":"10:15","image":"https://i.imgur.com/4AiXzf8.jpg","details":"an paragraph about the event","contact_phone":"+91 96513131313","contact_email":"example@example.com","spacial_note":"anything extra about event, requirement, dress Code etc.","event_location":"70.151500,15.891515","register":{"isRequired":"true","url":"reg_form_URL","contact":"registration_contact","fees":"fees"},"activitySponsors":[{"name":"sponsor Name 1","image":"http://www.clker.com/cliparts/a/Z/q/o/I/p/uncle-sam-hi.png","url":"https://www.google.co.in/"},{"name":"sponsor Name 2","image":"https://i.imgur.com/NiTuE1J.jpg","url":"https://www.google.co.in/"}]},{"Name":"activity5","date":"10-12-2017","time":"10:15","image":"https://i.imgur.com/4AiXzf8.jpg","details":"an paragraph about the event","contact_phone":"+91 96513131313","contact_email":"example@example.com","spacial_note":"anything extra about event, requirement, dress Code etc.","event_location":"70.151500,15.891515","register":{"isRequired":"true","url":"reg_form_URL","contact":"registration_contact","fees":"fees"},"activitySponsors":[{"name":"sponsor Name 1","image":"http://www.clker.com/cliparts/a/Z/q/o/I/p/uncle-sam-hi.png","url":"https://www.google.co.in/"},{"name":"sponsor Name 2","image":"https://i.imgur.com/NiTuE1J.jpg","url":"https://www.google.co.in/"}]},{"Name":"activity6","date":"10-12-2017","time":"10:15","image":"https://i.imgur.com/4AiXzf8.jpg","details":"an paragraph about the event","contact_phone":"+91 96513131313","contact_email":"example@example.com","spacial_note":"anything extra about event, requirement, dress Code etc.","event_location":"70.151500,15.891515","register":{"isRequired":"true","url":"reg_form_URL","contact":"registration_contact","fees":"fees"},"activitySponsors":[{"name":"sponsor Name 1","image":"http://www.clker.com/cliparts/a/Z/q/o/I/p/uncle-sam-hi.png","url":"https://www.google.co.in/"},{"name":"sponsor Name 2","image":"https://i.imgur.com/NiTuE1J.jpg","url":"https://www.google.co.in/"}]},{"Name":"activity7","date":"10-12-2017","time":"10:15","image":"https://i.imgur.com/4AiXzf8.jpg","details":"an paragraph about the event","contact_phone":"+91 96513131313","contact_email":"example@example.com","spacial_note":"anything extra about event, requirement, dress Code etc.","event_location":"70.151500,15.891515","register":{"isRequired":"true","url":"reg_form_URL","contact":"registration_contact","fees":"fees"},"activitySponsors":[{"name":"sponsor Name 1","image":"http://www.clker.com/cliparts/a/Z/q/o/I/p/uncle-sam-hi.png","url":"https://www.google.co.in/"},{"name":"sponsor Name 2","image":"https://i.imgur.com/NiTuE1J.jpg","url":"https://www.google.co.in/"}]},{"Name":"activity8","date":"10-12-2017","time":"10:15","image":"https://i.imgur.com/4AiXzf8.jpg","details":"an paragraph about the event","contact_phone":"+91 96513131313","contact_email":"example@example.com","spacial_note":"anything extra about event, requirement, dress Code etc.","event_location":"70.151500,15.891515","register":{"isRequired":"true","url":"reg_form_URL","contact":"registration_contact","fees":"fees"},"activitySponsors":[{"name":"sponsor Name 1","image":"http://www.clker.com/cliparts/a/Z/q/o/I/p/uncle-sam-hi.png","url":"https://www.google.co.in/"},{"name":"sponsor Name 2","image":"https://i.imgur.com/NiTuE1J.jpg","url":"https://www.google.co.in/"}]},{"Name":"activity9","date":"10-12-2017","time":"10:15","image":"https://i.imgur.com/4AiXzf8.jpg","details":"an paragraph about the event","contact_phone":"+91 96513131313","contact_email":"example@example.com","spacial_note":"anything extra about event, requirement, dress Code etc.","event_location":"70.151500,15.891515","register":{"isRequired":"true","url":"reg_form_URL","contact":"registration_contact","fees":"fees"},"activitySponsors":[{"name":"sponsor Name 1","image":"http://www.clker.com/cliparts/a/Z/q/o/I/p/uncle-sam-hi.png","url":"https://www.google.co.in/"},{"name":"sponsor Name 2","image":"https://i.imgur.com/NiTuE1J.jpg","url":"https://www.google.co.in/"}]},{"Name":"activity10","date":"10-12-2017","time":"10:15","image":"https://i.imgur.com/4AiXzf8.jpg","details":"an paragraph about the event","contact_phone":"+91 96513131313","contact_email":"example@example.com","spacial_note":"anything extra about event, requirement, dress Code etc.","event_location":"70.151551,15.891515","register":{"isRequired":"true","url":"reg_form_URL"},"activitySponsors":[{"name":"sponsor Name 1","image":"http://www.clker.com/cliparts/a/Z/q/o/I/p/uncle-sam-hi.png","url":"https://www.google.co.in/"},{"name":"sponsor Name 2","image":"https://i.imgur.com/NiTuE1J.jpg","url":"https://www.google.co.in/"}]}]
     * eventSponsors : [{"name":"sponsor Name 1","image":"https://i.imgur.com/sFNj4bs.jpg","url":"https://www.google.co.in/","type":"Title sponsor"},{"name":"sponsor Name 2","image":"https://i.imgur.com/sFNj4bs.jpg","url":"https://www.google.co.in/","type":"Title sponsor"},{"name":"sponsor Name 3","image":"https://i.imgur.com/sFNj4bs.jpg","url":"https://www.google.co.in/","type":"Title sponsor"},{"name":"sponsor Name 4","image":"https://i.imgur.com/sFNj4bs.jpg","url":"https://www.google.co.in/","type":"Title sponsor"},{"name":"sponsor Name 5","image":"https://i.imgur.com/sFNj4bs.jpg","url":"https://www.google.co.in/","type":"Title sponsor"},{"name":"sponsor Name 6","image":"https://i.imgur.com/sFNj4bs.jpg","url":"https://www.google.co.in/","type":"Title sponsor"},{"name":"sponsor Name 7","image":"https://i.imgur.com/sFNj4bs.jpg","url":"https://www.google.co.in/","type":"Title sponsor"},{"name":"sponsor Name 8","image":"https://i.imgur.com/sFNj4bs.jpg","url":"https://www.google.co.in/","type":"Title sponsor"},{"name":"sponsor Name 9","image":"https://i.imgur.com/sFNj4bs.jpg","url":"https://www.google.co.in/","type":"Title sponsor"},{"name":"sponsor Name 10","image":"https://i.imgur.com/sFNj4bs.jpg","url":"https://www.google.co.in/","type":"gold sponsor"}]
     * eventMap : {"latitude":"27.9614358","longitude":"76.4019588","radiusInMeters":"60","timeForNotificationInSec":"1","NotificationMessage":"Hey There, Thanks for coming to event. Call 1800-00-EVENT for help"}
     * eventAbout : {"info":"an paragraph about the event","organiser":"organiser of event","organiser_contact_phone":"+91 6111666262","organiser_contact_email":"example@example.com","address_of_event":"address of event"}
     */

    private EventMetadataBean eventMetadata;
    private EventMapBean eventMap;
    private EventAboutBean eventAbout;
    private List<EventScheduleBean> eventSchedule;
    private List<EventSponsorsBean> eventSponsors;

    public EventMetadataBean getEventMetadata() {
        return eventMetadata;
    }

    public void setEventMetadata(EventMetadataBean eventMetadata) {
        this.eventMetadata = eventMetadata;
    }

    public EventMapBean getEventMap() {
        return eventMap;
    }

    public void setEventMap(EventMapBean eventMap) {
        this.eventMap = eventMap;
    }

    public EventAboutBean getEventAbout() {
        return eventAbout;
    }

    public void setEventAbout(EventAboutBean eventAbout) {
        this.eventAbout = eventAbout;
    }

    public List<EventScheduleBean> getEventSchedule() {
        return eventSchedule;
    }

    public void setEventSchedule(List<EventScheduleBean> eventSchedule) {
        this.eventSchedule = eventSchedule;
    }

    public List<EventSponsorsBean> getEventSponsors() {
        return eventSponsors;
    }

    public void setEventSponsors(List<EventSponsorsBean> eventSponsors) {
        this.eventSponsors = eventSponsors;
    }

    public static class EventMetadataBean {
        /**
         * event_name : My Best event
         * iconUrl : http://roman.nurik.net/media/material_icon_gutenberg.png
         * posterUrl : https://placeimg.com/640/480/arch/sepia
         * update_number : 0
         */

        private String event_name;
        private String iconUrl;
        private String posterUrl;
        private String update_number;

        public String getEvent_name() {
            return event_name;
        }

        public void setEvent_name(String event_name) {
            this.event_name = event_name;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getPosterUrl() {
            return posterUrl;
        }

        public void setPosterUrl(String posterUrl) {
            this.posterUrl = posterUrl;
        }

        public String getUpdate_number() {
            return update_number;
        }

        public void setUpdate_number(String update_number) {
            this.update_number = update_number;
        }
    }

    public static class EventMapBean {
        /**
         * latitude : 27.9614358
         * longitude : 76.4019588
         * radiusInMeters : 60
         * timeForNotificationInSec : 1
         * NotificationMessage : Hey There, Thanks for coming to event. Call 1800-00-EVENT for help
         */

        private String latitude;
        private String longitude;
        private String radiusInMeters;
        private String timeForNotificationInSec;
        private String NotificationMessage;

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getRadiusInMeters() {
            return radiusInMeters;
        }

        public void setRadiusInMeters(String radiusInMeters) {
            this.radiusInMeters = radiusInMeters;
        }

        public String getTimeForNotificationInSec() {
            return timeForNotificationInSec;
        }

        public void setTimeForNotificationInSec(String timeForNotificationInSec) {
            this.timeForNotificationInSec = timeForNotificationInSec;
        }

        public String getNotificationMessage() {
            return NotificationMessage;
        }

        public void setNotificationMessage(String NotificationMessage) {
            this.NotificationMessage = NotificationMessage;
        }
    }

    public static class EventAboutBean {
        /**
         * info : an paragraph about the event
         * organiser : organiser of event
         * organiser_contact_phone : +91 6111666262
         * organiser_contact_email : example@example.com
         * address_of_event : address of event
         */

        private String info;
        private String organiser;
        private String organiser_contact_phone;
        private String organiser_contact_email;
        private String address_of_event;

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getOrganiser() {
            return organiser;
        }

        public void setOrganiser(String organiser) {
            this.organiser = organiser;
        }

        public String getOrganiser_contact_phone() {
            return organiser_contact_phone;
        }

        public void setOrganiser_contact_phone(String organiser_contact_phone) {
            this.organiser_contact_phone = organiser_contact_phone;
        }

        public String getOrganiser_contact_email() {
            return organiser_contact_email;
        }

        public void setOrganiser_contact_email(String organiser_contact_email) {
            this.organiser_contact_email = organiser_contact_email;
        }

        public String getAddress_of_event() {
            return address_of_event;
        }

        public void setAddress_of_event(String address_of_event) {
            this.address_of_event = address_of_event;
        }
    }

    public static class EventScheduleBean {
        /**
         * Name : activity1
         * date : 10-12-2017
         * time : 10:15
         * image : https://i.imgur.com/4AiXzf8.jpg
         * details : an paragraph about the event
         * contact_phone : +91 96513131313
         * contact_email : example@example.com
         * spacial_note : anything extra about event, requirement, dress Code etc.
         * event_location : 70.151500,15.891515
         * register : {"isRequired":"true","url":"reg_form_URL","contact":"registration_contact","fees":"fees"}
         * activitySponsors : [{"name":"sponsor Name 1","image":"http://www.clker.com/cliparts/a/Z/q/o/I/p/uncle-sam-hi.png","url":"https://www.google.co.in/"},{"name":"sponsor Name 2","image":"https://i.imgur.com/NiTuE1J.jpg","url":"https://www.google.co.in/"}]
         */

        private String Name;
        private String date;
        private String time;
        private String image;
        private String details;
        private String contact_phone;
        private String contact_email;
        private String spacial_note;
        private String event_location;
        private RegisterBean register;
        private List<ActivitySponsorsBean> activitySponsors;

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getContact_phone() {
            return contact_phone;
        }

        public void setContact_phone(String contact_phone) {
            this.contact_phone = contact_phone;
        }

        public String getContact_email() {
            return contact_email;
        }

        public void setContact_email(String contact_email) {
            this.contact_email = contact_email;
        }

        public String getSpacial_note() {
            return spacial_note;
        }

        public void setSpacial_note(String spacial_note) {
            this.spacial_note = spacial_note;
        }

        public String getEvent_location() {
            return event_location;
        }

        public void setEvent_location(String event_location) {
            this.event_location = event_location;
        }

        public RegisterBean getRegister() {
            return register;
        }

        public void setRegister(RegisterBean register) {
            this.register = register;
        }

        public List<ActivitySponsorsBean> getActivitySponsors() {
            return activitySponsors;
        }

        public void setActivitySponsors(List<ActivitySponsorsBean> activitySponsors) {
            this.activitySponsors = activitySponsors;
        }

        public static class RegisterBean {
            /**
             * isRequired : true
             * url : reg_form_URL
             * contact : registration_contact
             * fees : fees
             */

            private String isRequired;
            private String url;
            private String contact;
            private String fees;

            public String getIsRequired() {
                return isRequired;
            }

            public void setIsRequired(String isRequired) {
                this.isRequired = isRequired;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getContact() {
                return contact;
            }

            public void setContact(String contact) {
                this.contact = contact;
            }

            public String getFees() {
                return fees;
            }

            public void setFees(String fees) {
                this.fees = fees;
            }
        }

        public static class ActivitySponsorsBean {
            /**
             * name : sponsor Name 1
             * image : http://www.clker.com/cliparts/a/Z/q/o/I/p/uncle-sam-hi.png
             * url : https://www.google.co.in/
             */

            private String name;
            private String image;
            private String url;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }

    public static class EventSponsorsBean {
        /**
         * name : sponsor Name 1
         * image : https://i.imgur.com/sFNj4bs.jpg
         * url : https://www.google.co.in/
         * type : Title sponsor
         */

        private String name;
        private String image;
        private String url;
        private String type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
