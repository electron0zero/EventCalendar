package xyz.electron.eventcalendar;

import java.util.List;

public class DataObj {

    /**
     * eventMetadata : {"event_name":"My Best event","splashImage":"url of splash image","posterUrl":"url of poster","update_number":"0"}
     * eventSchedule : [{"Name":"activity1","date":"10-12-2017","time":"10:15","image":"https://image_url_of_activity_image.png","details":"an paragraph about the event","contact_phone":"+91 96513131313","contact_email":"example@example.com","spacial_note":"anything extra about event, requirement, dress Code etc.","event_location":"70.151500,15.891515","register":{"isRequired":"true","url":"reg_form_URL","contact":"registration_contact","fees":"fees"},"activitySponsors":[{"name":"sponsor Name 1","image":"https://image_url_of_sponsor_image.png","url":"url of sponsor"},{"name":"sponsor Name 2","image":"https://image_url_of_sponsor_image.png","url":"url of sponsor"}]},{"Name":"activity2","date":"10-12-2017","time":"10:15","image":"https://image_url_of_activity_image.png","details":"an paragraph about the event","contact_phone":"+91 96513131313","contact_email":"example@example.com","spacial_note":"anything extra about event, requirement, dress Code etc.","event_location":"70.151551,15.891515","register":{"isRequired":"true","url":"reg_form_URL"},"activitySponsors":[{"name":"sponsor Name 1","image":"https://image_url_of_sponsor_image.png","url":"url of sponsor"},{"name":"sponsor Name 2","image":"https://image_url_of_sponsor_image.png","url":"url of sponsor"}]}]
     * eventSponsors : [{"name":"sponsor Name 1","image":"https://url-to-image-of_sponsor.png","url":"link-to-sponsor's website","type":"Title sponsor"},{"name":"sponsor Name 2","image":"https://url-to-image-of_sponsor.png","url":"link-to-sponsor's website","type":"gold sponsor"}]
     * eventMap : {"location":"latitude, longitude","radius":"60"}
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
         * splashImage : url of splash image
         * posterUrl : url of poster
         * update_number : 0
         */

        private String event_name;
        private String splashImage;
        private String posterUrl;
        private String update_number;

        public String getEvent_name() {
            return event_name;
        }

        public void setEvent_name(String event_name) {
            this.event_name = event_name;
        }

        public String getSplashImage() {
            return splashImage;
        }

        public void setSplashImage(String splashImage) {
            this.splashImage = splashImage;
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
         * location : latitude, longitude
         * radius : 60
         */

        private String location;
        private String radius;

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getRadius() {
            return radius;
        }

        public void setRadius(String radius) {
            this.radius = radius;
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
         * image : https://image_url_of_activity_image.png
         * details : an paragraph about the event
         * contact_phone : +91 96513131313
         * contact_email : example@example.com
         * spacial_note : anything extra about event, requirement, dress Code etc.
         * event_location : 70.151500,15.891515
         * register : {"isRequired":"true","url":"reg_form_URL","contact":"registration_contact","fees":"fees"}
         * activitySponsors : [{"name":"sponsor Name 1","image":"https://image_url_of_sponsor_image.png","url":"url of sponsor"},{"name":"sponsor Name 2","image":"https://image_url_of_sponsor_image.png","url":"url of sponsor"}]
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
             * image : https://image_url_of_sponsor_image.png
             * url : url of sponsor
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
         * image : https://url-to-image-of_sponsor.png
         * url : link-to-sponsor's website
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
