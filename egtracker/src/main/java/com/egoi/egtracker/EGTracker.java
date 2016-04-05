package com.egoi.egtracker;

import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.egoi.egtracker.database.EGDataBaseManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * EGTrackerExample
 * Created by miguelchaves on 05/04/16.
 * Miguel Chaves(c) 2016
 */
public class EGTracker {

    /**
     * Private variables
     */
    private static EGTracker tracker;
    private Context context;
    private SimpleDateFormat dateFormat;

    /**
     * Public variables
     */
    public boolean logActive = true;
    public EGConfigurations configurations;

    /**
     * Default constructor
     */
    private EGTracker() { }

    /**
     * The shared instance of the class
     * @return the shared instance
     */
    public static synchronized EGTracker sharedInstance() {

        if (tracker == null) {
            tracker = new EGTracker();
        }

        return tracker;
    }

    /**
     * Init all the elements necessary to the tracker work
     */
    public void initEngine(Context context) {
        log("Initing engine...");

        this.context = context;
        this.dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:MM:ss", Locale.getDefault());
        this.configurations.timestamp = Long.valueOf(System.currentTimeMillis() / 1000).toString();
        this.configurations._id = generateRandomHashString();
        setScreenSize();

        log("Initing database system...");
        EGDataBaseManager.sharedInstance().initDataBaseSystem(this.context);

        log("Engine inited!");

        checkStoredEvents();
    }

    /**
     * Send an event to the E-Goi server
     * @param action the event to send
     */
    public void trackEvent(String action) {
        log("Tracking event: " + action);
        sendOrSaveRequest(configureRequestWithAction(action));
    }

    /**
     * Check stored events in the database. Delete the events that were sent previous and
     * send the stored ones
     */
    private void checkStoredEvents() {
        log("Searching for stored events...");

        ArrayList<EGTrackerRequest> events =  EGDataBaseManager.sharedInstance().getEvents();

        if (events != null && events.size() > 0) {
            log("Found stored events...");

            for (EGTrackerRequest event : events) {
                // if the event was sent, delete the event
                if (event.getSent()) {
                    EGDataBaseManager.sharedInstance().removeEventByID(event.getId());
                } else {
                    sendStoredRequests(event);
                }
            }
        }
    }

    /**
     * Send the request to the E-Goi server, or, if there is no internet connection, save to database
     * @param url to build the request
     */
    private void sendOrSaveRequest(String url) {
        if (this.egTrackerCheckConnection()) {
            log("Sending event...");
            EGRequestUrlString  request = new EGRequestUrlString();
            request.execute(url);
        } else {
            log("No Internet connection. Saving event...");
            EGTrackerRequest event = new EGTrackerRequest();
            event.setUrl(url);
            event.setDate(this.dateFormat.format(new Date()));
            event.setSent(false);

            EGDataBaseManager.sharedInstance().insertEvent(event);
        }
    }

    /**
     * Send to the E-Goi server the event
     * @param event the event to send
     */
    private void sendStoredRequests(EGTrackerRequest event) {
        if (event != null) {
            if (this.egTrackerCheckConnection()) {
                log("Sending event...");
                EGRequest request = new EGRequest();
                request.execute(event);
            }
        }
    }

    /**
     * Get the string for the request full configured
     * @param action the action to track
     * @return the string with all the information
     */
    private String configureRequestWithAction(String action) {
        return String.format("%saction_name=%s%s", EGConfigurations.endPoint, action, getCompletionRequest());
    }

    /**
     * Get the string filled to make the web request
     * @return the filled string
     */
    private String getCompletionRequest() {

        setTime();

        String string = "&clientid=" + this.configurations.clientID;
        string = string + "&listid=" + this.configurations.listID;
        string = string + "&subscriber=" + this.configurations.subscriber;
        string = string + "&campaign=" + this.configurations.campaign;
        string = string + "&rec=1";
        string = string + "&r=" + this.configurations.r;
        string = string + "&h=" + this.configurations.h;
        string = string + "&m=" + this.configurations.m;
        string = string + "&s=" + this.configurations.s;
        string = string + "&url=" + this.configurations.url;
        string = string + "&_id=" + this.configurations._id;
        string = string + "&_idts=" + this.configurations._idts;
        string = string + "&_idvc=" + this.configurations._idvc;
        string = string + "&_idn=" + this.configurations._idn;
        string = string + "&_refts=" + this.configurations._refts;
        string = string + "&_viewts=" + this.configurations.timestamp;
        string = string + "&_ref=" + this.configurations._ref;
        string = string + "&send_image=" + this.configurations.send_image;
        string = string + "&pdf=" + this.configurations.pdf;
        string = string + "&qt=" + this.configurations.qt;
        string = string + "&realp=" + this.configurations.realp;
        string = string + "&wma=" + this.configurations.wma;
        string = string + "&dir=" + this.configurations.dir;
        string = string + "&fla=" + this.configurations.fla;
        string = string + "&java=" + this.configurations.java;
        string = string + "&gears=" + this.configurations.gears;
        string = string + "&ag=" + this.configurations.ag;
        string = string + "&cookie=" + this.configurations.cookie;
        string = string + "&res=" + this.configurations.res;
        string = string + "&gt_ms=" + this.configurations.gt_ms;
        string = string + "&idsite=" + this.configurations.idsite;

        return string;
    }

    /**
     * Update the current time for the request
     */
    private void setTime() {
        Calendar now = Calendar.getInstance();
        this.configurations.h = now.get(Calendar.HOUR_OF_DAY);
        this.configurations.m = now.get(Calendar.MINUTE);
        this.configurations.s = now.get(Calendar.SECOND);
    }
    /**
     * Generate a random hexadecimal String to use in this library
     * @return the generate String
     */
    private String generateRandomHashString() {
        String[] sourceArray = new String[] {"1", "a", "2", "b", "3", "8", "c", "4", "d", "5", "e", "6", "f", "7", "9"};
        String returnString = "";
        Random r = new Random();

        for (int i = 0 ; i < 16 ; i ++ ) {
            returnString = returnString + sourceArray[r.nextInt(16)];
        }

        log("Generated hash string: " + returnString);

        return returnString;
    }

    /**
     * Log some message if the logActive is set to true
     * @param text the text to log
     */
    private void log(String text) {
        if (this.logActive) {
            Log.i("E-Goi Tracker: %s", text);
        }
    }

    /**
     * Set the screen size in use
     */
    private void setScreenSize() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        this.configurations.res = String.format("%dx%d", width, height);
    }

    /**
     * Check if there is some connection to the Internet
     * @return if there is connection
     */
    private boolean egTrackerCheckConnection() {
        return (((ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null);
    }
}
