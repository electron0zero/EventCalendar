auth : xyz.electron.eventcalendar.provider

paths :
        auth/sch       // have schedules info (exposed for Home screen)
        auth/sch/1     //single event JSON item saved as text
        auth/spo       // have sponsors info
        auth/spo/1     // single sponsors JSON item saved as text
        
Handle matches


Use Shared Preferences for Saving Metadata, Map, About JSON objects.
Save them as JSON String in shared Preferences.

for schedules and sponsors save each item of JSON array as a single ITEM in Content provider


