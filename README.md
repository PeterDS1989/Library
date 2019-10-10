# Library

- Local in-memory setup of db in application-dev.properties.
 Db script is automatically executed at startup for dev.
- Profile currently set to 'dev' in application.properties
- Connection for real postgresql db can be entered in application-prod.properties
- Schema.sql contains database script
- No additional server configuration is needed, just run ApiApplication to start the application
- ISBN was taken as unique ID for a book, but not generated by this api.
- Name of a collection is considered unique, but a technical ID is used by the application for collections.