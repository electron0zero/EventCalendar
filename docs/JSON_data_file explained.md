### `eventMetadata` : Event Meta data object

### `eventSchedule` : Array of JSON objects, Each Object is a Activity (aka a Sessions/Talks/SubEvents in a Event)

##### `register` : object for registration info of that activity, set it to `flase` and leave other fields empty if no regstarion is required
##### `activitySponsors` object is Array of JSON objet each object helds the info Activtiy Sponsors
### `eventSponsors` : array of JSON objects that holds info of Event Sponsors
### `eventMap` : this objet holds info releted to GeoFence feature, Notifications will be delivired to users baed on content of this object if user provied the permission of Device location

### `eventAbout`: generic info about event, it's like about page a event website

#### full sample JSON file for a dummy event can be found in `public-assets` folder

##### Sample event JSON file 
```
{
    "eventMetadata": {
        "event_name": "The Great Droid Event",
        "iconUrl": "http://i.imgur.com/TRg3VQq.png",
        "posterUrl" : "https://placeimg.com/640/480/arch/sepia",
        "update_number": "0"
    },
    "eventSchedule": [
        {
            "Name": "The Dawn of Droid",
            "date": "07 Mar. 2017",
            "time": "10:00 AM",
            "image": "https://placeimg.com/640/480/any",
            "details": "Initially developed by Android Inc., which Google bought in 2005, Android was unveiled in 2007, along with the founding of the Open Handset Alliance – a consortium of hardware, software, and telecommunication companies devoted to advancing open standards for mobile devices.",
            "contact_phone": "+91 96513131313",
            "contact_email": "example@example.com",
            "spacial_note": "This event is about the History of Android aka Droid",
            "event_location": "The Droid way",
            "register": {
                "isRequired": "false",
                "url": "",
                "contact": "",
                "fees": ""
            },
            "activitySponsors": [
                {
                    "name": "The Droid Inc.",
                    "image": "https://placeimg.com/640/480/any",
                    "url": "https://www.android.com/"
                }
            ]
        }
    ],
    "eventSponsors": [
        {
            "name": "The Droid Inc.",
            "image": "https://placeimg.com/640/480/any",
            "url": "https://www.android.com/",
            "type": "Title Sponsor"
        }
    ],
    "eventMap": {
        "latitude": "27.9614358",
        "longitude": "76.4019588",
        "radiusInMeters": "1000",
        "timeForNotificationInSec": "1",
        "NotificationMessage": "Hey There, Thanks for coming to event. Call 1800-00-EVENT for help"
    },
    "eventAbout": {
        "info": "Android provides a rich application framework that allows you to build innovative apps and games for mobile devices in a Java language environment. The documents listed in the left navigation provide details about how to build apps using Android's various APIs.",
        "organiser": "The Dude",
        "organiser_contact_phone": "+91 6111666262",
        "organiser_contact_email": "example@example.com",
        "address_of_event": "12-3 The Dude Building. Dudeist Street, The Duderino Way. North Pole. Pin - 200147"
    }
}
```