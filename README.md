# Real Time Dashboard App

A Java based, [Google App Engine] (https://developers.google.com/appengine) real time dashboard application that uses [Cloud Endpoint](https://developers.google.com/appengine/docs/java/endpoints/) and the [Channel API](https://cloud.google.com/appengine/docs/java/channel/). The dashboard receives sensor data from a Raspberry Pi Zero running a Cloud Endpoint [client application][8].


![Alt text](http://omerio.com/wp-content/uploads/2016/01/raspberrypid1.png "Sensor Dashboard")

## Live Demo
- [https://raspberrypi-dash.appspot.com/](https://raspberrypi-dash.appspot.com/)

## Documentation
- [Real Time Sensor Dashboard Using Google App Engine and Raspberry Pi Zero](http://omerio.com/2016/01/16/real-time-sensor-dashboard-using-google-app-engine-and-raspberry-pi-zero/)

## Prerequisites
- [Apache Maven](https://maven.apache.org/) installed
- Signup for [Google App Engine] (https://developers.google.com/appengine)
- Create a new cloud project
- Create a new OAuth 2.0 client id as shown in this screenshot

![Alt text](http://omerio.com/wp-content/uploads/2016/01/Screen-Shot-2016-01-16-at-16.12.36.png "Create OAuth 2.0 Client")

## Setup Instructions
1. Checkout the project `git clone https://github.com/omerio/raspberrypi-appengine-portal.git`

1. Update the value of `app.id` & `app.version` in [`pom.xml`](https://github.com/omerio/raspberrypi-appengine-portal/blob/master/pom.xml) to the app ID you have registered in the App Engine admin console and would like to use to host your instance of this sample.

1. Update the values in [`src/main/java/uk/co/inetria/pi/endpoint/Constants.java`](https://github.com/omerio/raspberrypi-appengine-portal/blob/master/src/main/java/uk/co/inetria/pi/endpoint/Constants.java)
 to reflect the respective client IDs you have registered in the
 [APIs Console][4]. Update the EMAIL_ADDRESS field with the Google Account you are going to use to authenticate the [Raspberry Pi App][8].
1. Build the application using `mvn clean install`
1. Run the application with `mvn appengine:devserver`, and ensure it's
   running by visiting your local server's api explorer's address (by
   default [localhost:8080/_ah/api/explorer][5].)

1. (Optional) Get the client library with

   $ mvnappengine:endpoints_get_client_lib

   It will generate a client library jar file under the
   `target/endpoints-client-libs/<api-name>/target` directory of your
   project, as well as install the artifact into your local maven
   repository.

1. Deploy your application to Google App Engine with

   $ mvn appengine:update

[1]: https://developers.google.com/appengine
[2]: http://java.com/en/
[3]: https://developers.google.com/appengine/docs/java/endpoints/
[4]: https://developers.google.com/appengine/docs/java/tools/maven
[5]: https://localhost:8080/_ah/api/explorer
[6]: https://console.developers.google.com/
[8]: https://github.com/omerio/raspberrypi-app
