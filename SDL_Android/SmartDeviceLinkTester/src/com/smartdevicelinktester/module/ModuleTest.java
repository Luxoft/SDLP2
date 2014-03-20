package com.smartdevicelinktester.module;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.xmlpull.v1.XmlPullParser;

import com.smartdevicelinktester.activity.SmartDeviceLinkTester;
import com.smartdevicelinktester.adapters.logAdapter;
import com.smartdevicelinktester.constants.AcceptedRPC;
import com.smartdevicelinktester.logmessages.RPCLogMessage;
import com.smartdevicelinktester.logmessages.StringLogMessage;
import com.smartdevicelinktester.service.ProxyService;
import com.smartdevicelink.exception.SmartDeviceLinkException;
import com.smartdevicelink.proxy.RPCRequest;
import com.smartdevicelink.proxy.constants.Names;
import com.smartdevicelink.proxy.rpc.AddCommand;
import com.smartdevicelink.proxy.rpc.AddSubMenu;
import com.smartdevicelink.proxy.rpc.Alert;
import com.smartdevicelink.proxy.rpc.AlertManeuver;
import com.smartdevicelink.proxy.rpc.ChangeRegistration;
import com.smartdevicelink.proxy.rpc.CreateInteractionChoiceSet;
import com.smartdevicelink.proxy.rpc.DeleteCommand;
import com.smartdevicelink.proxy.rpc.DeleteFile;
import com.smartdevicelink.proxy.rpc.DeleteInteractionChoiceSet;
import com.smartdevicelink.proxy.rpc.DeleteSubMenu;
import com.smartdevicelink.proxy.rpc.DialNumber;
import com.smartdevicelink.proxy.rpc.EndAudioPassThru;
import com.smartdevicelink.proxy.rpc.GetDTCs;
import com.smartdevicelink.proxy.rpc.GetVehicleData;
import com.smartdevicelink.proxy.rpc.ListFiles;
import com.smartdevicelink.proxy.rpc.PerformAudioPassThru;
import com.smartdevicelink.proxy.rpc.PerformInteraction;
import com.smartdevicelink.proxy.rpc.PutFile;
import com.smartdevicelink.proxy.rpc.ReadDID;
import com.smartdevicelink.proxy.rpc.RegisterAppInterface;
import com.smartdevicelink.proxy.rpc.ResetGlobalProperties;
import com.smartdevicelink.proxy.rpc.ScrollableMessage;
import com.smartdevicelink.proxy.rpc.SetAppIcon;
import com.smartdevicelink.proxy.rpc.SetDisplayLayout;
import com.smartdevicelink.proxy.rpc.SetGlobalProperties;
import com.smartdevicelink.proxy.rpc.SetMediaClockTimer;
import com.smartdevicelink.proxy.rpc.Show;
import com.smartdevicelink.proxy.rpc.ShowConstantTBT;
import com.smartdevicelink.proxy.rpc.Slider;
import com.smartdevicelink.proxy.rpc.Speak;
import com.smartdevicelink.proxy.rpc.StartTime;
import com.smartdevicelink.proxy.rpc.SubscribeButton;
import com.smartdevicelink.proxy.rpc.SubscribeVehicleData;
import com.smartdevicelink.proxy.rpc.UnregisterAppInterface;
import com.smartdevicelink.proxy.rpc.UnsubscribeButton;
import com.smartdevicelink.proxy.rpc.UnsubscribeVehicleData;
import com.smartdevicelink.proxy.rpc.UpdateTurnList;
import com.smartdevicelink.proxy.rpc.enums.Result;
import com.smartdevicelink.proxy.rpc.enums.UpdateMode;
import com.smartdevicelinktester.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.util.Xml;

public class ModuleTest {
        private static ModuleTest _instance;
        private SmartDeviceLinkTester _mainInstance;
        private logAdapter _msgAdapter;
        private static Runnable threadContext;
        private static ModuleTest DialogThreadContext;
        private Thread mainThread;

        private boolean pass;
        private boolean integration;
        private String userPrompt;

        private int numIterations;

        private ArrayList<Pair<Integer, Result>> expecting = new ArrayList<Pair<Integer, Result>>();
        private ArrayList<Pair<String, ArrayList<AutoTestIteration>>> testList = new ArrayList<Pair<String, ArrayList<AutoTestIteration>>> ();

        public static ArrayList<Pair<Integer, Result>> responses = new ArrayList<Pair<Integer, Result>>();

        public ModuleTest() {
                this._mainInstance = SmartDeviceLinkTester.getInstance();
                this._msgAdapter = SmartDeviceLinkTester.getMessageAdapter();

                // Set this's instance
                _instance = this;
                _mainInstance.setTesterMain(_instance);

                mainThread = makeThread();
        }

        public void runTests() {
                mainThread.start();
        }

        public void restart() {
                mainThread.interrupt();
                mainThread = null;
                mainThread = makeThread();
                mainThread.start();
        }

        private String[] mFileList;
        //private File mPath = new File(Environment.getExternalStorageDirectory() + "");//"//yourdir//");
        //private File mPath = new File("/sdcard/");
        private File mPath = new File(Environment.getExternalStorageDirectory() + "");
        private String mChosenFile;
        private static final String FTYPE = ".xml";    
        private static final int DIALOG_LOAD_FILE = 1000;

        private void loadFileList(){
                try{
                        mPath.mkdirs();
                } catch(SecurityException e) {
                        Log.e("ModuleTest", "unable to write on the sd card " + e.toString());
                }
                if (mPath.exists()) {
                        FilenameFilter filter = new FilenameFilter() {
                                public boolean accept(File dir, String filename) {
                                        File sel = new File(dir, filename);
                                        return filename.contains(FTYPE);// || sel.isDirectory();
                                }
                        };
                        mFileList = mPath.list(filter);
                } else {
                        mFileList= new String[0];
                }
        }

        Dialog dialog;

        protected Dialog onCreateDialog(final int id) {
                DialogThreadContext = this;
                //Dialog dialog = null;
                _mainInstance.runOnUiThread(new Runnable() {
                        public void run() {
                                dialog = null;
                                AlertDialog.Builder builder = new Builder(_mainInstance);

                                switch(id) {
                                case DIALOG_LOAD_FILE:
                                        builder.setTitle("Choose your file");
                                        if (mFileList == null) {
                                                Log.e("ModuleTest", "Showing file picker before loading the file list");
                                                dialog = builder.create();
                                                //return dialog;
                                                synchronized (DialogThreadContext) { DialogThreadContext.notify();}
                                                Thread.currentThread().interrupt();
                                        }
                                        builder.setItems(mFileList, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                        mChosenFile = mFileList[which];
                                                        //you can do stuff with the file here too
                                                        synchronized (DialogThreadContext) { DialogThreadContext.notify();}
                                                        Thread.currentThread().interrupt();
                                                }
                                        });
                                        break;
                                }
                                dialog = builder.show();
                        }
                });

                try {
                        synchronized (this) { this.wait();}
                } catch (InterruptedException e) {
                        _msgAdapter.logMessage(new StringLogMessage("InterruptedException"), true);
                }
                return dialog;

                /*
                Dialog dialog = null;
                AlertDialog.Builder builder = new Builder(_mainInstance);

                switch(id) {
                        case DIALOG_LOAD_FILE:
                                builder.setTitle("Choose your file");
                                if (mFileList == null) {
                                        Log.e("ModuleTest", "Showing file picker before loading the file list");
                                        dialog = builder.create();
                                        return dialog;
                                }
                                builder.setItems(mFileList, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                                mChosenFile = mFileList[which];
                                                //you can do stuff with the file here too
                                        }
                                });
                                break;
                }
                dialog = builder.show();
                return dialog;
                 */
        }

        public Thread makeThread () {
                return new Thread (new Runnable () {
                        public void run () {
                                mChosenFile = null;
                                loadFileList();
                                onCreateDialog(DIALOG_LOAD_FILE);
                                if (mChosenFile != null) {
                                        AcceptedRPC acceptedRPC = new AcceptedRPC();
                                        XmlPullParser parser = Xml.newPullParser();
                                        RPCRequest rpc;
                                        try {
                                                //FileInputStream fin = new FileInputStream("/sdcard/test.xml");
                                                //FileInputStream fin = new FileInputStream("/sdcard/" + mChosenFile);
                                                FileInputStream fin = new FileInputStream(Environment.getExternalStorageDirectory() + "/" + mChosenFile);

                                                InputStreamReader isr = new InputStreamReader(fin);

                                                String outFile = Environment.getExternalStorageDirectory() + "/" + mChosenFile.substring(0, mChosenFile.length() - 4) + ".csv";
                                                File out = new File(outFile);
                                                FileWriter writer = new FileWriter(out);
                                                writer.flush();

                                                String outErrorFile = Environment.getExternalStorageDirectory() + "/" + mChosenFile + "Errors.csv";
                                                File outError = new File(outErrorFile);
                                                FileWriter errorWriter = new FileWriter(outError);
                                                errorWriter.flush();

                                                parser.setInput(isr);
                                                int eventType = parser.getEventType();
                                                String name;
                                                boolean done = false;
                                                boolean inTest = false;
                                                while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                                                        name = parser.getName();

                                                        switch (eventType) {
                                                        case XmlPullParser.START_DOCUMENT:
                                                                Log.e("TESTING", "START_DOCUMENT, name: " + name);
                                                                break;
                                                        case XmlPullParser.END_DOCUMENT:
                                                                Log.e("TESTING", "END_DOCUMENT, name: " + name);
                                                                break;
                                                        case XmlPullParser.START_TAG:
                                                                name = parser.getName();
                                                                if (name.equalsIgnoreCase("test")) {
                                                                        inTest = true;
                                                                        _msgAdapter.logMessage(new StringLogMessage("test " + parser.getAttributeValue(0)), true);
                                                                        testList.add(new Pair<String, ArrayList<AutoTestIteration>> (parser.getAttributeValue(0), new ArrayList<AutoTestIteration> ()));
                                                                        expecting.clear();
                                                                        responses.clear();
                                                                        try {
                                                                                if (parser.getAttributeName(1) != null) {
                                                                                        if (parser.getAttributeName(1).equalsIgnoreCase("iterations")) {
                                                                                                try {numIterations = Integer.parseInt(parser.getAttributeValue(1));} 
                                                                                                catch (Exception e) {Log.e("parser", "Unable to parse number of iterations");}
                                                                                        } else numIterations = 1;
                                                                                } else numIterations = 1;
                                                                        } catch (Exception e) {
                                                                                numIterations = 1;
                                                                        }
                                                                } else if (name.equalsIgnoreCase("type")) {
                                                                        if (parser.getAttributeValue(0).equalsIgnoreCase("integration")) integration = true;
                                                                        else if (parser.getAttributeValue(0).equalsIgnoreCase("unit")) integration = false;
                                                                } else if (name.equalsIgnoreCase("wait")){
                                                                        //This can't happen here.  It works for between tests but for RPCs it will not wait during the text execution
                                                                        if(parser.getAttributeName(0) != null && parser.getAttributeName(0).equalsIgnoreCase("millis")){
                                                                                if(!inTest){
                                                                                        _msgAdapter.logMessage(new StringLogMessage("Waiting for: " + Long.valueOf(parser.getAttributeValue(0)) + "ms"));
                                                                                        Thread.sleep(Long.valueOf(parser.getAttributeValue(0)));
                                                                                } else {
                                                                                        Pair<String, ArrayList<AutoTestIteration>> temp = testList.get(testList.size()-1);
                                                                                        AutoTestIteration ati = new AutoTestIteration();
                                                                                        ati.setWait(true);
                                                                                        ati.setWaitMillis(Long.valueOf(parser.getAttributeValue(0)));
                                                                                        temp.second.add(ati);
                                                                                        testList.add(temp);
                                                                                } 
                                                                        }
                                                                } else if (acceptedRPC.isAcceptedRPC(name)) {
                                                                        //Create correct object
                                                                        if (name.equalsIgnoreCase(Names.RegisterAppInterface)) {
                                                                                rpc = new RegisterAppInterface();
                                                                        } else if (name.equalsIgnoreCase(Names.UnregisterAppInterface)) {
                                                                                rpc = new UnregisterAppInterface();
                                                                        } else if (name.equalsIgnoreCase(Names.SetGlobalProperties)) {
                                                                                rpc = new SetGlobalProperties();
                                                                        } else if (name.equalsIgnoreCase(Names.ResetGlobalProperties)) {
                                                                                rpc = new ResetGlobalProperties();
                                                                        } else if (name.equalsIgnoreCase(Names.AddCommand)) {
                                                                                rpc = new AddCommand();
                                                                        } else if (name.equalsIgnoreCase(Names.DeleteCommand)) {
                                                                                rpc = new DeleteCommand();
                                                                        } else if (name.equalsIgnoreCase(Names.AddSubMenu)) {
                                                                                rpc = new AddSubMenu();
                                                                        } else if (name.equalsIgnoreCase(Names.DeleteSubMenu)) {
                                                                                rpc = new DeleteSubMenu();
                                                                        } else if (name.equalsIgnoreCase(Names.CreateInteractionChoiceSet)) {
                                                                                rpc = new CreateInteractionChoiceSet();
                                                                        } else if (name.equalsIgnoreCase(Names.PerformInteraction)) {
                                                                                rpc = new PerformInteraction();
                                                                        } else if (name.equalsIgnoreCase(Names.DeleteInteractionChoiceSet)) {
                                                                                rpc = new DeleteInteractionChoiceSet();
                                                                        } else if (name.equalsIgnoreCase(Names.Alert)) {
                                                                                rpc = new Alert();
                                                                        } else if (name.equalsIgnoreCase(Names.Show)) {
                                                                                rpc = new Show();
                                                                        } else if (name.equalsIgnoreCase(Names.Speak)) {
                                                                                rpc = new Speak();
                                                                        } else if (name.equalsIgnoreCase(Names.SetMediaClockTimer)) {
                                                                                rpc = new SetMediaClockTimer();
                                                                        } else if (name.equalsIgnoreCase(Names.DialNumber)) {
                                                                                rpc = new DialNumber();
                                                                        } else if (name.equalsIgnoreCase(Names.PerformAudioPassThru)) {
                                                                                rpc = new PerformAudioPassThru();
                                                                        } else if (name.equalsIgnoreCase(Names.EndAudioPassThru)) {
                                                                                rpc = new EndAudioPassThru();
                                                                        } else if (name.equalsIgnoreCase(Names.SubscribeButton)) {
                                                                                rpc = new SubscribeButton();
                                                                        } else if (name.equalsIgnoreCase(Names.UnsubscribeButton)) {
                                                                                rpc = new UnsubscribeButton();
                                                                        } else if (name.equalsIgnoreCase(Names.SubscribeVehicleData)) {
                                                                                rpc = new SubscribeVehicleData();
                                                                        } else if (name.equalsIgnoreCase(Names.UnsubscribeVehicleData)) {
                                                                                rpc = new UnsubscribeVehicleData();
                                                                        } else if (name.equalsIgnoreCase(Names.GetVehicleData)) {
                                                                                rpc = new GetVehicleData();
                                                                        } else if (name.equalsIgnoreCase(Names.ReadDID)) {
                                                                                rpc = new ReadDID();
                                                                        } else if (name.equalsIgnoreCase(Names.GetDTCs)) {
                                                                                rpc = new GetDTCs();
                                                                        } else if (name.equalsIgnoreCase(Names.ScrollableMessage)) {
                                                                                rpc = new ScrollableMessage();
                                                                        } else if (name.equalsIgnoreCase(Names.Slider)) {
                                                                                rpc = new Slider();
                                                                        } else if (name.equalsIgnoreCase(Names.ShowConstantTBT)) {
                                                                                rpc = new ShowConstantTBT();
                                                                        } else if (name.equalsIgnoreCase(Names.AlertManeuver)) {
                                                                                rpc = new AlertManeuver();
                                                                        } else if (name.equalsIgnoreCase(Names.UpdateTurnList)) {
                                                                                rpc = new UpdateTurnList();
                                                                        } else if (name.equalsIgnoreCase(Names.ChangeRegistration)) {
                                                                                rpc = new ChangeRegistration();
                                                                        } else if (name.equalsIgnoreCase(Names.PutFile)) {
                                                                                rpc = new PutFile();

                                                                                Bitmap photo = BitmapFactory.decodeResource(_mainInstance.getResources(), R.drawable.fiesta);
                                                                                ByteArrayOutputStream bas = new ByteArrayOutputStream();
                                                                                photo.compress(CompressFormat.JPEG, 100, bas);
                                                                                byte[] data = new byte[bas.toByteArray().length];
                                                                                data = bas.toByteArray();

                                                                                rpc.setBulkData(data);
                                                                        } else if (name.equalsIgnoreCase(Names.DeleteFile)) {
                                                                                rpc = new DeleteFile();
                                                                        } else if (name.equalsIgnoreCase(Names.ListFiles)) {
                                                                                rpc = new ListFiles();
                                                                        } else if (name.equalsIgnoreCase(Names.SetAppIcon)) {
                                                                                rpc = new SetAppIcon();
                                                                        } else if (name.equalsIgnoreCase(Names.SetDisplayLayout)) {
                                                                                rpc = new SetDisplayLayout();
                                                                        } else if (name.equalsIgnoreCase("ClearMediaClockTimer")) {
                                                                                rpc = new Show();
                                                                                ((Show) rpc).setMainField1(null);
                                                                                ((Show) rpc).setMainField2(null);
                                                                                ((Show) rpc).setStatusBar(null);
                                                                                ((Show) rpc).setMediaClock("     ");
                                                                                ((Show) rpc).setMediaTrack(null);
                                                                                ((Show) rpc).setAlignment(null);
                                                                        } else if (name.equalsIgnoreCase("PauseMediaClockTimer")) {
                                                                                rpc = new SetMediaClockTimer();
                                                                                StartTime startTime = new StartTime();
                                                                                startTime.setHours(0);
                                                                                startTime.setMinutes(0);
                                                                                startTime.setSeconds(0);
                                                                                ((SetMediaClockTimer) rpc).setStartTime(startTime);
                                                                                ((SetMediaClockTimer) rpc).setUpdateMode(UpdateMode.PAUSE);
                                                                        } else if (name.equalsIgnoreCase("ResumeMediaClockTimer")) {
                                                                                rpc = new SetMediaClockTimer();
                                                                                StartTime startTime = new StartTime();
                                                                                startTime.setHours(0);
                                                                                startTime.setMinutes(0);
                                                                                startTime.setSeconds(0);
                                                                                ((SetMediaClockTimer) rpc).setStartTime(startTime);
                                                                                ((SetMediaClockTimer) rpc).setUpdateMode(UpdateMode.RESUME);
                                                                        } else {
                                                                                rpc = new SetGlobalProperties();
                                                                        }

                                                                        if (parser.getAttributeName(0) != null && 
                                                                                        parser.getAttributeName(0).equalsIgnoreCase("correlationID")) {
                                                                                try {rpc.setCorrelationID(Integer.parseInt(parser.getAttributeValue(0)));} 
                                                                                catch (Exception e) {Log.e("parser", "Unable to parse Integer");}
                                                                        }

                                                                        //TODO: Set rpc parameters
                                                                        Hashtable hash = setParams(name, parser);
                                                                        Log.e("TESTING", "" + hash);
                                                                        //TODO: Iterate through hash table and add it to parameters
                                                                        for (Object key : hash.keySet()) {
                                                                                rpc.setParameters((String) key, hash.get(key));
                                                                        }

                                                                        Iterator it = hash.entrySet().iterator();
                                                                        while (it.hasNext()) {
                                                                                Hashtable.Entry pairs = (Hashtable.Entry)it.next();
                                                                                System.out.println(pairs.getKey() + " = " + pairs.getValue());
                                                                        }

                                                                        Pair<String, ArrayList<AutoTestIteration>> temp = testList.get(testList.size()-1);
                                                                        AutoTestIteration ati = new AutoTestIteration();
                                                                        ati.setRequest(rpc);
                                                                        temp.second.add(ati);
                                                                        testList.set(testList.size()-1, temp);
                                                                } else if (name.equalsIgnoreCase("result")) {
                                                                        expecting.add(new Pair<Integer, Result>(Integer.parseInt(parser.getAttributeValue(0)), (Result.valueForString(parser.getAttributeValue(1)))));
                                                                } else if (name.equalsIgnoreCase("userPrompt") && integration) {
                                                                        userPrompt = parser.getAttributeValue(0);
                                                                }
                                                                break;
                                                        case XmlPullParser.END_TAG:
                                                                name = parser.getName();
                                                                if (name.equalsIgnoreCase("test")) {
                                                                        try {
                                                                                boolean localPass = true;
                                                                                int i = numIterations;
                                                                                int numPass = 0;
                                                                                while (i > 0) {
                                                                                		Thread.sleep(1000);
                                                                                        xmlTest();
                                                                                        if (pass) numPass++;
                                                                                        else {
                                                                                                localPass = false;
                                                                                                errorWriter.write("" + testList.get(testList.size()-1).first + " Expected");
                                                                                                for (Pair p : expecting) errorWriter.write(", " + p.first + " " + p.second);
                                                                                                errorWriter.write("\n");

                                                                                                errorWriter.write("" + testList.get(testList.size()-1).first + " Actual");
                                                                                                for (Pair p : responses) errorWriter.write(", " + p.first + " " + p.second);
                                                                                                errorWriter.write("\n");
                                                                                        }
                                                                                        i--;
                                                                                }
                                                                                if (localPass) writer.write("" + testList.get(testList.size()-1).first + ", Pass, " + numPass + ", " + numIterations + "\n");
                                                                                if (!localPass) writer.write("" + testList.get(testList.size()-1).first + ", Fail, " + numPass + ", " + numIterations + "\n");
                                                                                Log.i("Test App Result", "" + testList.get(testList.size()-1).first + ", " + localPass + ", " + numPass + ", " + numIterations);
                                                                                _msgAdapter.logMessage(new StringLogMessage("" + testList.get(testList.size()-1).first + ", " + localPass + ", " + numPass + ", " + numIterations), true);
                                                                        } catch (Exception e) {
                                                                                _msgAdapter.logMessage(new StringLogMessage("Test " + testList.get(testList.size()-1).first + " Failed! "), Log.ERROR, e);
                                                                        }finally {
                                                                                inTest = false;
                                                                        }
                                                                }
                                                                break;
                                                        case XmlPullParser.TEXT:
                                                                //Log.e("TESTING", "TEXT, name: " + name);
                                                                break;
                                                        case XmlPullParser.CDSECT:
                                                                Log.e("TESTING", "CDSECT, name: " + name);
                                                                break;
                                                        case XmlPullParser.ENTITY_REF:
                                                                Log.e("TESTING", "ENTITY_REF, name: " + name);
                                                                break;
                                                        case XmlPullParser.IGNORABLE_WHITESPACE:
                                                                Log.e("TESTING", "IGNORABLE_WHITESPACE, name: " + name);
                                                                break;
                                                        case XmlPullParser.PROCESSING_INSTRUCTION:
                                                                Log.e("TESTING", "PROCESSING_INSTRUCTION, name: " + name);
                                                                break;
                                                        case XmlPullParser.COMMENT:
                                                                Log.e("TESTING", "COMMENT, name: " + name);
                                                                break;
                                                        case XmlPullParser.DOCDECL:
                                                                Log.e("TESTING", "DOCDECL, name: " + name);
                                                                break;
                                                        default:
                                                                break;
                                                        }
                                                        eventType = parser.next();
                                                }
                                                writer.close();
                                                errorWriter.close();

                                                Intent email = new Intent(Intent.ACTION_SEND);
                                                email.setType("plain/text");
                                                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"youremail@ford.com"});           
                                                email.putExtra(Intent.EXTRA_SUBJECT, "Lua Unit Test Export");
                                                email.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(out));

                                                _mainInstance.startActivity(Intent.createChooser(email, "Choose an Email client :"));

                                        } catch (Exception e) {
                                                _msgAdapter.logMessage(new StringLogMessage("Parser Failed!!"), Log.ERROR, e);
                                        }
                                }
                        }
                });
        }

        private Hashtable setParams(String name, XmlPullParser parser) {

                Log.e("TESTING", "setParams start name: " + name);

                Hashtable hash = new Hashtable();

                int eventType = 0;
                Boolean done = false;
                String tempName = null;
                String vectorName = null;

                try {
                        while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                                tempName = parser.getName();

                                switch (eventType) {
                                case XmlPullParser.START_DOCUMENT:
                                        Log.e("TESTING", "START_DOCUMENT, tempName: " + tempName);
                                        break;
                                case XmlPullParser.END_DOCUMENT:
                                        Log.e("TESTING", "END_DOCUMENT, tempName: " + tempName);
                                        break;
                                case XmlPullParser.START_TAG:
                                        if (tempName.equalsIgnoreCase("Vector")) {
                                                Log.e("TESTING", "In Vector");
                                                Vector<Object> vector = new Vector<Object>();

                                                if (parser.getAttributeName(0) != null) vectorName = parser.getAttributeValue(0);

                                                Boolean nestedWhileDone = false;

                                                eventType = parser.next();
                                                while (eventType != XmlPullParser.START_TAG && !nestedWhileDone) {
                                                        if (eventType == XmlPullParser.END_TAG) {
                                                                if (parser.getName().equalsIgnoreCase("Vector")) {
                                                                        Log.e("TESTING", "In Vector Loop, nestedWhileDone == true, END_TAG, name: " + name);
                                                                        nestedWhileDone = true;
                                                                }
                                                        } else eventType = parser.next();
                                                }

                                                while (eventType != XmlPullParser.END_DOCUMENT && !nestedWhileDone) {
                                                        tempName = parser.getName();
                                                        Log.e("TESTING", "In Vector Loop, tempName: " + tempName);

                                                        switch (eventType) {
                                                        case XmlPullParser.START_DOCUMENT:
                                                                Log.e("TESTING", "In Vector Loop, START_DOCUMENT, name: " + name);
                                                                break;
                                                        case XmlPullParser.END_DOCUMENT:
                                                                Log.e("TESTING", "In Vector Loop, END_DOCUMENT, name: " + name);
                                                                break;
                                                        case XmlPullParser.START_TAG:
                                                                if (tempName.equalsIgnoreCase("Integer")) {
                                                                        Log.e("TESTING", "In Nested Vector Integer");
                                                                        if (parser.getAttributeName(0) != null) {
                                                                                //try {vector.add(Integer.parseInt(parser.getAttributeValue(0)));} 
                                                                                try {vector.add(Double.parseDouble(parser.getAttributeValue(0)));} 
                                                                                catch (Exception e) {Log.e("parser", "Unable to parse Integer");}
                                                                        }
                                                                } else if (tempName.equalsIgnoreCase("String")) {
                                                                        Log.e("TESTING", "In Nested Vector String");
                                                                        if (parser.getAttributeName(0) != null) {
                                                                                vector.add(parser.getAttributeValue(0));
                                                                        }
                                                                } else {
                                                                        vector.add(setParams(tempName, parser));
                                                                }
                                                                break;
                                                        case XmlPullParser.END_TAG:
                                                                Log.e("TESTING", "In Vector Loop, END_TAG, name: " + name);
                                                                if (tempName.equalsIgnoreCase("Vector")) {
                                                                        Log.e("TESTING", "In Vector Loop, nestedWhileDone == true, END_TAG, name: " + name);
                                                                        nestedWhileDone = true;
                                                                }
                                                                break;
                                                        case XmlPullParser.TEXT:
                                                                //Log.e("TESTING", "TEXT, name: " + name);
                                                                break;
                                                        case XmlPullParser.CDSECT:
                                                                Log.e("TESTING", "In Vector Loop, CDSECT, name: " + name);
                                                                break;
                                                        case XmlPullParser.ENTITY_REF:
                                                                Log.e("TESTING", "In Vector Loop, ENTITY_REF, name: " + name);
                                                                break;
                                                        case XmlPullParser.IGNORABLE_WHITESPACE:
                                                                Log.e("TESTING", "In Vector Loop, IGNORABLE_WHITESPACE, name: " + name);
                                                                break;
                                                        case XmlPullParser.PROCESSING_INSTRUCTION:
                                                                Log.e("TESTING", "In Vector Loop, PROCESSING_INSTRUCTION, name: " + name);
                                                                break;
                                                        case XmlPullParser.COMMENT:
                                                                Log.e("TESTING", "In Vector Loop, COMMENT, name: " + name);
                                                                break;
                                                        case XmlPullParser.DOCDECL:
                                                                Log.e("TESTING", "In Vector Loop, DOCDECL, name: " + name);
                                                                break;
                                                        default:
                                                                break;
                                                        }
                                                        eventType = parser.next();
                                                }
                                                Log.e("TESTING", "out of Vector loop");
                                                hash.put(vectorName, vector);
                                        } else if (tempName.equalsIgnoreCase("Integer")) {
                                                Log.e("TESTING", "In Integer");
                                                if (parser.getAttributeName(0) != null) {
                                                        //try {hash.put(parser.getAttributeName(0), Integer.parseInt(parser.getAttributeValue(0)));} 
                                                        try {hash.put(parser.getAttributeName(0), Double.parseDouble(parser.getAttributeValue(0)));} 
                                                        catch (Exception e) {Log.e("parser", "Unable to parse Integer");}
                                                }
                                        } else if (tempName.equalsIgnoreCase("Boolean")) {
                                                Log.e("TESTING", "In Boolean");
                                                if (parser.getAttributeName(0) != null) {
                                                        if (parser.getAttributeValue(0).equalsIgnoreCase("true")) hash.put(parser.getAttributeName(0), true);
                                                        else if (parser.getAttributeValue(0).equalsIgnoreCase("false")) hash.put(parser.getAttributeName(0), false);
                                                }
                                        } else if (tempName.equalsIgnoreCase("String")) {
                                                Log.e("TESTING", "In String");
                                                if (parser.getAttributeName(0) != null) {
                                                        hash.put(parser.getAttributeName(0), parser.getAttributeValue(0));
                                                }
                                        } else {
                                                Log.e("TESTING", "Returning in else statement");
                                                //return setParams(tempName, parser);
                                                hash.put(tempName, setParams(tempName, parser));
                                        }
                                        break;
                                case XmlPullParser.END_TAG:
                                        if (tempName.equalsIgnoreCase(name)) {
                                                done = true;
                                        }
                                        break;
                                case XmlPullParser.TEXT:
                                        //Log.e("TESTING", "TEXT, tempName: " + tempName);
                                        break;
                                case XmlPullParser.CDSECT:
                                        Log.e("TESTING", "CDSECT, tempName: " + tempName);
                                        break;
                                case XmlPullParser.ENTITY_REF:
                                        Log.e("TESTING", "ENTITY_REF, tempName: " + tempName);
                                        break;
                                case XmlPullParser.IGNORABLE_WHITESPACE:
                                        Log.e("TESTING", "IGNORABLE_WHITESPACE, tempName: " + tempName);
                                        break;
                                case XmlPullParser.PROCESSING_INSTRUCTION:
                                        Log.e("TESTING", "PROCESSING_INSTRUCTION, tempName: " + tempName);
                                        break;
                                case XmlPullParser.COMMENT:
                                        Log.e("TESTING", "COMMENT, tempName: " + tempName);
                                        break;
                                case XmlPullParser.DOCDECL:
                                        Log.e("TESTING", "DOCDECL, tempName: " + tempName);
                                        break;
                                default:
                                        break;
                                }
                                eventType = parser.next();
                        }
                } catch (Exception e) {
                        _msgAdapter.logMessage(new StringLogMessage("Parser Failed!!"), Log.ERROR, e);
                }

                Log.e("TESTING", "Returning at end of setParams function");
                return hash;
        }

        private Boolean xmlTest() {
                pass = false;

                Thread newThread = new Thread(new Runnable() {
                        public void run () {
                                threadContext = this;

                                int numResponses = expecting.size();
                                if (numResponses > 0) ProxyService.waiting(true);

                                for (AutoTestIteration ati: testList.get(testList.size()-1).second) {
                                        if(!ati.isWait()){
                                                _msgAdapter.logMessage(new RPCLogMessage(ati.getRequest()), true);
                                                try {
                                                        ProxyService.getProxyInstance().sendRPCRequest(ati.getRequest());
                                                } catch (SmartDeviceLinkException e) {
                                                        _msgAdapter.logMessage(new StringLogMessage("Error sending RPC"), Log.ERROR, e, true);
                                                }
                                        } else {
                                                _msgAdapter.logMessage(new StringLogMessage("Waiting: " + ati.getWaitMillis() + "ms"), true);
                                                synchronized(this)
                                                {
                        							try {
                        									Thread.sleep(ati.getWaitMillis());
                        								} 
                        							catch (InterruptedException e) 
                        							{
                        								Log.e("sleep", "Unable to sleep thread.");
                        							}
                                                }
                                        }
                                }

                                try {
                                        int killSwitch = responses.size();
                                        for (int i = 0; i < numResponses; i++) {
                                                synchronized (this) {
                                                        this.wait(10000);
                                                }
                                                if (killSwitch == responses.size()) i = numResponses;
                                                else killSwitch = responses.size();
                                        }
                                } catch (InterruptedException e) {
                                        _msgAdapter.logMessage(new StringLogMessage("InterruptedException"), true);
                                }

                                ProxyService.waiting(false);

                                try {
                                        synchronized (this) { this.wait(5000);}
                                } catch (InterruptedException e) {
                                        _msgAdapter.logMessage(new StringLogMessage("InterruptedException"), true);
                                }

                                if (expecting.equals(responses)) {
                                        pass = true;
                                        if (integration) {
                                                _mainInstance.runOnUiThread(new Runnable() {
                                                        public void run() {
                                                                AlertDialog.Builder alert = new AlertDialog.Builder(_mainInstance);
                                                                alert.setMessage(userPrompt);
                                                                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                                pass = true;
                                                                                synchronized (threadContext) { threadContext.notify();}
                                                                        }
                                                                });
                                                                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                                pass = false;
                                                                                synchronized (threadContext) { threadContext.notify();}
                                                                        }
                                                                });
                                                                alert.show();
                                                        }
                                                });

                                                try {
                                                        synchronized (this) { this.wait();}
                                                } catch (InterruptedException e) {
                                                        _msgAdapter.logMessage(new StringLogMessage("InterruptedException"), true);
                                                }
                                        }
                                }

                                synchronized (_instance) { _instance.notify();}

                                Thread.currentThread().interrupt();
                        }
                });
                newThread.start();

                try {
                        synchronized (this) { this.wait();}
                } catch (InterruptedException e) {
                        _msgAdapter.logMessage(new StringLogMessage("InterruptedException"), true);
                }

                newThread.interrupt();
                newThread = null;
                return pass;
        }

        public static ModuleTest getModuleTestInstance() {
                return _instance;
        }

        public Runnable getThreadContext() {
                return threadContext;
        }
}

/*
        public void setParameters(String functionName, Object value) {
                if (value != null) {
                        parameters.put(functionName, value);
                } else {
                        parameters.remove(functionName);
                }
        }

        public Object getParameters(String functionName) {
                return parameters.get(functionName);
        }
 */