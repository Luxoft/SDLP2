package com.smartdevicelinktester.pmtests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.util.Log;

import com.smartdevicelinktester.activity.SmartDeviceLinkTester;
import com.smartdevicelinktester.adapters.logAdapter;
import com.smartdevicelinktester.logmessages.StringLogMessage;
import com.smartdevicelinktester.service.ProxyService;
import com.smartdevicelink.exception.SmartDeviceLinkException;
import com.smartdevicelink.proxy.RPCMessage;
import com.smartdevicelink.proxy.RPCRequest;
import com.smartdevicelink.proxy.RPCRequestFactory;
import com.smartdevicelink.proxy.RPCResponse;
import com.smartdevicelink.proxy.rpc.enums.MobileAppState;
import com.smartdevicelink.proxy.rpc.enums.Result;
import com.smartdevicelink.proxy.rpc.pm.AddProfile;
import com.smartdevicelink.proxy.rpc.pm.OnProfileClosed;
import com.smartdevicelink.proxy.rpc.pm.OnSendProfileToAppMessage;
import com.smartdevicelink.proxy.rpc.pm.ProfileBinaryPacketizer;
import com.smartdevicelink.proxy.rpc.pm.UnloadProfileResponse;

public class ProfileManagerTests {
    private static final String TAG = ProfileManagerTests.class.getName();

    private static final String SAMPLE_PROFILE_NAME = "SampleProfile";
    private static final String TEST_ECHO_PROFILE_NAME = "TestEchoProfile";
    private static final String TEST_SENDING_MSG_PROFILE_NAME = "TestSendingMsgProfile";
    private static final String NOT_EXISTING_PROFILE_NAME = "NotExistingProfile";

    private static final String TEST19_STRING = "TEST19";
    private static final String TEST30_STRING = "TEST30";
    
    private static final String MOBILE_APP_BACKGROUND_STR = "MOBILE_APP_BACKGROUND";
    private static final String MOBILE_APP_FOREGROUND_STR = "MOBILE_APP_FOREGROUND";
    private static final String MOBILE_APP_LOCK_SCREEN_STR = "MOBILE_APP_LOCK_SCREEN";

    private logAdapter mLogAdapter;
    private boolean mTestsInProgress = false;
    private int mAutoIncCorrId = 100000000;
    private int mCurrentTest = 0;
    private int mLocalErrorCounter = 0;
    private int mTest41MsgCounter = 0;
    private Map<Integer, Boolean> mRequestsSuccessExpected = new HashMap<Integer, Boolean>();
    private List<String> mExpectedMessages = new ArrayList<String>();
    private Map<Integer, String> mUnloadingProfiles = new HashMap<Integer, String>();
    private List<Integer> mFailedTests = new ArrayList<Integer>();
    private SmartDeviceLinkTester mMainActivity;
    private boolean mStateReceived = false;
    
    int mManyMessagesCount = 1000;
    int mBigDataSize = 10000;
    byte[] mBigData = new byte[mBigDataSize];

    public ProfileManagerTests(logAdapter _msgAdapter, SmartDeviceLinkTester mainActivity) {
        mLogAdapter = _msgAdapter;
        mMainActivity = mainActivity;
    }

    public void runProfileManagerTests() {
        mLogAdapter.logMessage(new StringLogMessage("Starting Profile Manager Tests"), true);
        mTestsInProgress = true;

        new Thread() {
            public void run() {
                test02();
                checkAndResetLocalErrors(2);
                test03();
                checkAndResetLocalErrors(3);
                test04();
                checkAndResetLocalErrors(4);
                test05();
                checkAndResetLocalErrors(5);
                test09();
                checkAndResetLocalErrors(9);
                test10();
                checkAndResetLocalErrors(10);
                test11();
                checkAndResetLocalErrors(11);
                test14();
                checkAndResetLocalErrors(14);
                test17();
                checkAndResetLocalErrors(17);
                test19();
                checkAndResetLocalErrors(19);
                test22();
                checkAndResetLocalErrors(22);
                test24();
                checkAndResetLocalErrors(24);
                test25();
                checkAndResetLocalErrors(25);
                test26();
                checkAndResetLocalErrors(26);
                test30();
                checkAndResetLocalErrors(30);
                test32();
                checkAndResetLocalErrors(32);
                test33();
                checkAndResetLocalErrors(33);
                test34();
                checkAndResetLocalErrors(34);
                test36();
                checkAndResetLocalErrors(36);
                test38();
                checkAndResetLocalErrors(38);
                test39();
                checkAndResetLocalErrors(39);
                test41();
                checkAndResetLocalErrors(41);

                if (mFailedTests.size() > 0) {
                    StringBuffer failedTests = new StringBuffer();
                    for (Integer val : mFailedTests) {
                        failedTests.append(val).append(", ");
                    }
                    failedTests.replace(failedTests.length() - 2, failedTests.length() - 1, "");
                    mLogAdapter.logMessage(new StringLogMessage("Failed tests: " 
                    + failedTests.toString()), true);
                } else {
                    mLogAdapter.logMessage(new StringLogMessage("All tests passed"), true);
                }
                mTestsInProgress = false;
            }
        }.start();
    }

    public void handleMessage(RPCMessage message) {
        Log.i(TAG, "handleMessage");
        if (mTestsInProgress) {
            synchronized (mRequestsSuccessExpected) {
                if (message instanceof RPCResponse) {
                    Log.i(TAG, "RPCResponse received");
                    RPCResponse response = (RPCResponse) message;
                    int corrId = response.getCorrelationID();
                    if (mRequestsSuccessExpected.containsKey(corrId)) {
                        if (mRequestsSuccessExpected.get(corrId)) {
                            if (response.getResultCode() != Result.SUCCESS) {
                                mLocalErrorCounter++;
                                mLogAdapter.logMessage(new StringLogMessage("Error occured " 
                                + response.toString()), true);
                            }
                        } else {
                            if (response.getResultCode() == Result.SUCCESS) {
                                mLocalErrorCounter++;
                                mLogAdapter.logMessage(new StringLogMessage("Error occured " +
                                		"(unexpected success) " + response.toString()), true);
                            }
                        }
                        
                        if (response instanceof UnloadProfileResponse) {
                            if (response.getResultCode() != Result.SUCCESS) {
                                synchronized (mUnloadingProfiles) {
                                    mUnloadingProfiles.remove(corrId);
                                }                                
                            }
                        }
                        mRequestsSuccessExpected.remove(response.getCorrelationID());
                    }
                } else if (message instanceof OnSendProfileToAppMessage) {
                    Log.i(TAG, "OnSendProfileToAppMessage received");
                    if (message.getBulkData() != null) {
                        String msg = new String(
                                ((OnSendProfileToAppMessage) message).getMessage())
                                .replace("\0", "");
                        switch (mCurrentTest) {
                            case 19:                                
                                mLogAdapter.logMessage(new StringLogMessage("received: " + msg), true);
                                compareMessages(msg, TEST19_STRING);
                                break;
                            case 26:
                                compareMessages(msg, new String(mBigData));
                                break;
                            case 30:
                                compareMessages(msg, TEST30_STRING);
                                break;
                            case 41:
                                mLogAdapter.logMessage(new StringLogMessage("received: " + msg), true);
                                if (mTest41MsgCounter == 0) {
                                    compareMessages(msg, MOBILE_APP_BACKGROUND_STR);
                                    mTest41MsgCounter++;
                                } else if (mTest41MsgCounter == 1) {
                                    compareMessages(msg, MOBILE_APP_FOREGROUND_STR);
                                    mTest41MsgCounter++;
                                } else if (mTest41MsgCounter == 2) {
                                    compareMessages(msg, MOBILE_APP_LOCK_SCREEN_STR);
                                    mTest41MsgCounter = 0;
                                }
                                break;
                            default:
                                break;
                        }
                    }
                } else if (message instanceof OnProfileClosed) {
                    Log.i(TAG, "OnProfileClosed received");
                    String name = ((OnProfileClosed) message).getProfileId();
                    if (name != null) {
                        for (Entry<Integer, String> entry : mUnloadingProfiles.entrySet()) {
                            if (name.equals(entry.getValue())) {
                                synchronized (mUnloadingProfiles) {
                                    mUnloadingProfiles.remove(entry.getKey());
                                }
                                break;
                            }
                        }
                    }                    
                }
            }
        }
    }
    
    private void compareMessages(String receivedMsg, String expectedMsg) {
        if (!receivedMsg.equals(expectedMsg)) {
            mLocalErrorCounter++;
            mLogAdapter.logMessage(new StringLogMessage(
                    "Error occured (received message != sent message)"), true);
            mLogAdapter.logMessage(new StringLogMessage(
                    "sent: " + expectedMsg + " , received: " + receivedMsg), true);
        } 
        synchronized (mExpectedMessages) {
            mExpectedMessages.remove(expectedMsg);
        }
    }

    private void loadProfile(String name, boolean successExpected) {
        try {
            RPCRequest msg = RPCRequestFactory.buildLoadProfile(name, mAutoIncCorrId);
            synchronized (mRequestsSuccessExpected) {
                mRequestsSuccessExpected.put(mAutoIncCorrId++, successExpected);
            }

            ProxyService.getProxyInstance().sendRPCRequest(msg);
        } catch (SmartDeviceLinkException e) {
            e.printStackTrace();
        }
    }

    private void unloadProfile(String name, boolean successExpected) {
        try {
            RPCRequest msg = RPCRequestFactory.buildUnloadProfile(name, mAutoIncCorrId);
            synchronized (mRequestsSuccessExpected) {
                mRequestsSuccessExpected.put(mAutoIncCorrId, successExpected);
            }
            synchronized (mUnloadingProfiles) {
                mUnloadingProfiles.put(mAutoIncCorrId++, name);
            }

            ProxyService.getProxyInstance().sendRPCRequest(msg);
        } catch (SmartDeviceLinkException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String profileName, byte[] message, boolean successExpected) {
        try {
            ProxyService.getProxyInstance().sendAppToProfileMessage(profileName, message,
                    mAutoIncCorrId);

            synchronized (mRequestsSuccessExpected) {
                mRequestsSuccessExpected.put(mAutoIncCorrId++, successExpected);
            }
        } catch (SmartDeviceLinkException e) {
            e.printStackTrace();
        }
    }

    private void addProfile(String profileName, String assetName, boolean successExpected) {
        byte[] profileData = mMainActivity.loadAssetsFile(assetName);
        if (profileData != null) {
            List<AddProfile> requests = ProfileBinaryPacketizer.createAddProfileRequests(
                    profileData, SAMPLE_PROFILE_NAME, mAutoIncCorrId);

            synchronized (mRequestsSuccessExpected) {
                for (int i = 0; i < requests.size(); i++) {
                    mRequestsSuccessExpected.put(mAutoIncCorrId + i, successExpected);
                }
            }

            try {
                mAutoIncCorrId = ProxyService.getProxyInstance().addProfile(profileName,
                        profileData, mAutoIncCorrId);
                mAutoIncCorrId++;
            } catch (SmartDeviceLinkException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeProfile(String profileName, boolean successExpected) {
        try {
            ProxyService.getProxyInstance().removeProfile(profileName, mAutoIncCorrId);
            synchronized (mRequestsSuccessExpected) {
                mRequestsSuccessExpected.put(mAutoIncCorrId++, successExpected);
            }
        } catch (SmartDeviceLinkException e) {
            e.printStackTrace();
        }
    }

    private void changeAppState(String profileName, MobileAppState state, boolean successExpected) {
        Log.i(TAG, "changeAppState : " + state.name());
        try {
            ProxyService.getProxyInstance().appStateChanged(profileName, state, mAutoIncCorrId);

            synchronized (mRequestsSuccessExpected) {
                mRequestsSuccessExpected.put(mAutoIncCorrId++, successExpected);
            }
        } catch (SmartDeviceLinkException e) {
            e.printStackTrace();
        }
    }

    private void waitForResponses() {
        while (!mRequestsSuccessExpected.isEmpty()) {
            sleep(50);
        }
    }

    private void waitForMessage() {
        while (!mExpectedMessages.isEmpty()) {
            sleep(50);
        }
    }
    
    private void waitForProfileUnloading() {
        while (!mUnloadingProfiles.isEmpty()) {
            sleep(50);
        }
    }
    
    private void sleep(int msec) {
        try {
            Thread.sleep(msec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void checkAndResetLocalErrors(int testNumber) {
        if (mLocalErrorCounter != 0) {
            mLogAdapter.logMessage(new StringLogMessage("test " + testNumber + " failed"), Log.ERROR, true);
            mFailedTests.add(testNumber);
        } else {
            mLogAdapter.logMessage(new StringLogMessage("test " + testNumber + " succeeded"), true);
        }
        mLocalErrorCounter = 0;
    }

    /**
     * TESTS
     */

    /**
     * Load + Unload profile A
     */
    private void test02() {
        mLogAdapter.logMessage(new StringLogMessage("test02 started >>>>>>>>>>>>>>>>>>>>>>>>>>> "), true);
        mCurrentTest = 2;

        loadProfile(TEST_ECHO_PROFILE_NAME, true);
        waitForResponses();
        unloadProfile(TEST_ECHO_PROFILE_NAME, true);
        waitForResponses();
        waitForProfileUnloading();
    }

    /**
     * Load A + Unload A + Load B + Unload B
     */
    private void test03() {
        mLogAdapter.logMessage(new StringLogMessage("test03 started >>>>>>>>>>>>>>>>>>>>>>>>>>> "), true);
        mCurrentTest = 3;

        loadProfile(TEST_SENDING_MSG_PROFILE_NAME, true);
        waitForResponses();
        unloadProfile(TEST_SENDING_MSG_PROFILE_NAME, true);
        waitForResponses();
        waitForProfileUnloading();
        loadProfile(TEST_ECHO_PROFILE_NAME, true);
        waitForResponses();
        unloadProfile(TEST_ECHO_PROFILE_NAME, true);
        waitForResponses();
        waitForProfileUnloading();
    }

    /**
     * Load A + Load B + Unload A + Unload B
     */
    private void test04() {
        mLogAdapter.logMessage(new StringLogMessage("test04 started >>>>>>>>>>>>>>>>>>>>>>>>>>> "), true);
        mCurrentTest = 4;

        loadProfile(TEST_SENDING_MSG_PROFILE_NAME, true);
        waitForResponses();
        loadProfile(TEST_ECHO_PROFILE_NAME, true);
        waitForResponses();
        unloadProfile(TEST_SENDING_MSG_PROFILE_NAME, true);
        waitForResponses();
        waitForProfileUnloading();
        unloadProfile(TEST_ECHO_PROFILE_NAME, true);
        waitForResponses();
        waitForProfileUnloading();
    }

    /**
     * Load A + Load A + Unload A
     */
    private void test05() {
        mLogAdapter.logMessage(new StringLogMessage("test05 started >>>>>>>>>>>>>>>>>>>>>>>>>>> "), true);
        mCurrentTest = 5;

        loadProfile(TEST_SENDING_MSG_PROFILE_NAME, true);
        waitForResponses();
        loadProfile(TEST_SENDING_MSG_PROFILE_NAME, false);
        waitForResponses();
        unloadProfile(TEST_SENDING_MSG_PROFILE_NAME, true);
        waitForResponses();
        waitForProfileUnloading();
    }

    /**
     * Load C (profile does not exist)
     */
    private void test09() {
        mLogAdapter.logMessage(new StringLogMessage("test09 started >>>>>>>>>>>>>>>>>>>>>>>>>>> "), true);
        mCurrentTest = 9;

        loadProfile(NOT_EXISTING_PROFILE_NAME, false);
        waitForResponses();
    }

    /**
     * Load A + Unload A + Load A + Unload A
     */
    private void test10() {
        mLogAdapter.logMessage(new StringLogMessage("test10 started >>>>>>>>>>>>>>>>>>>>>>>>>>> "), true);
        mCurrentTest = 10;

        loadProfile(TEST_ECHO_PROFILE_NAME, true);
        waitForResponses();
        unloadProfile(TEST_ECHO_PROFILE_NAME, true);
        waitForResponses();
        waitForProfileUnloading();
        loadProfile(TEST_ECHO_PROFILE_NAME, true);
        waitForResponses();
        unloadProfile(TEST_ECHO_PROFILE_NAME, true);
        waitForResponses();
        waitForProfileUnloading();
    }
    
    /**
     * Load A + Unload A (initiated by A) + Load A + Unload A
     */
    private void test11() {
        mLogAdapter.logMessage(new StringLogMessage("test11 started >>>>>>>>>>>>>>>>>>>>>>>>>>> "), true);
        mCurrentTest = 11;

        loadProfile(TEST_SENDING_MSG_PROFILE_NAME, true);
        waitForResponses();
        synchronized (mUnloadingProfiles) {
            mUnloadingProfiles.put(mAutoIncCorrId++, TEST_SENDING_MSG_PROFILE_NAME);
        }
        sendMessage(TEST_SENDING_MSG_PROFILE_NAME, "UNLOAD".getBytes(), true);
        waitForResponses();
        waitForProfileUnloading();
        loadProfile(TEST_SENDING_MSG_PROFILE_NAME, true);
        waitForResponses();
        unloadProfile(TEST_SENDING_MSG_PROFILE_NAME, true);
        waitForResponses();
        waitForProfileUnloading();
    }

    /**
     * Load A + Unload A + Unload A
     */
    private void test14() {
        mLogAdapter.logMessage(new StringLogMessage("test14 started >>>>>>>>>>>>>>>>>>>>>>>>>>> "), true);
        mCurrentTest = 14;

        loadProfile(TEST_ECHO_PROFILE_NAME, true);
        waitForResponses();
        unloadProfile(TEST_ECHO_PROFILE_NAME, true);
        waitForResponses();
        waitForProfileUnloading();
        unloadProfile(TEST_ECHO_PROFILE_NAME, false);
        waitForResponses();
        waitForProfileUnloading();
    }

    /**
     * Load A + Unload B + Unload A
     */
    private void test17() {
        mLogAdapter.logMessage(new StringLogMessage("test17 started >>>>>>>>>>>>>>>>>>>>>>>>>>> "), true);
        mCurrentTest = 17;

        loadProfile(TEST_SENDING_MSG_PROFILE_NAME, true);
        waitForResponses();
        unloadProfile(TEST_ECHO_PROFILE_NAME, false);
        waitForResponses();
        waitForProfileUnloading();
        unloadProfile(TEST_SENDING_MSG_PROFILE_NAME, true);
        waitForResponses();
        waitForProfileUnloading();
    }

    /**
     * Load A + Send message to A
     */
    private void test19() {
        mLogAdapter.logMessage(new StringLogMessage("test19 (+21) started >>>>>>>>>>>>>>>>>>>>>>>>>>> "), true);
        mCurrentTest = 19;

        loadProfile(TEST_ECHO_PROFILE_NAME, true);
        waitForResponses();
        synchronized (mExpectedMessages) {
            mExpectedMessages.add(TEST19_STRING);
        }
        sendMessage(TEST_ECHO_PROFILE_NAME, TEST19_STRING.getBytes(), true);
        waitForMessage();
        unloadProfile(TEST_ECHO_PROFILE_NAME, true);
        waitForResponses();
        waitForProfileUnloading();
    }

    /**
     * Send message to A while it is not loaded
     */
    private void test22() {
        mLogAdapter.logMessage(new StringLogMessage("test22 started >>>>>>>>>>>>>>>>>>>>>>>>>>> "), true);
        mCurrentTest = 22;

        sendMessage(TEST_SENDING_MSG_PROFILE_NAME, "MSG_TO_A".getBytes(), false);
        waitForResponses();
    }

    /**
     * Send message with null data from A
     */
    private void test24() {
        mLogAdapter.logMessage(new StringLogMessage("test24 started >>>>>>>>>>>>>>>>>>>>>>>>>>> "), true);
        mCurrentTest = 24;

        loadProfile(TEST_SENDING_MSG_PROFILE_NAME, true);
        waitForResponses();
        sendMessage(TEST_SENDING_MSG_PROFILE_NAME, "SEND_ME_NULL".getBytes(), true);
        waitForResponses();
        unloadProfile(TEST_SENDING_MSG_PROFILE_NAME, true);
        waitForResponses();
        waitForProfileUnloading();
    }

    /**
     * Send message with null data to A
     */
    private void test25() {
        mLogAdapter.logMessage(new StringLogMessage("test25 started >>>>>>>>>>>>>>>>>>>>>>>>>>> "), true);
        mCurrentTest = 25;

        loadProfile(TEST_SENDING_MSG_PROFILE_NAME, true);
        waitForResponses();
        sendMessage(TEST_SENDING_MSG_PROFILE_NAME, null, false);
        waitForResponses();
        unloadProfile(TEST_SENDING_MSG_PROFILE_NAME, true);
        waitForResponses();
        waitForProfileUnloading();
    }

    /**
     * Send message with large data size to A and receive it from A
     */
    private void test26() {
        mLogAdapter.logMessage(new StringLogMessage("test26 (+ 27) started >>>>>>>>>>>>>>>>>>>>>>>>>>> "), true);
        mCurrentTest = 26;

        loadProfile(TEST_ECHO_PROFILE_NAME, true);
        waitForResponses();
        for (int i = 0; i < mBigDataSize; i++) {
            mBigData[i] = 1;
        }

        synchronized (mExpectedMessages) {
            mExpectedMessages.add(new String(mBigData));
        }

        sendMessage(TEST_ECHO_PROFILE_NAME, mBigData, true);
        waitForMessage();
        unloadProfile(TEST_ECHO_PROFILE_NAME, true);
        waitForResponses();
        waitForProfileUnloading();
    }

    /**
     * Send a lot of messages to A and receive them from A
     */
    private void test30() {
        mLogAdapter.logMessage(new StringLogMessage("test30 (+ 31) started >>>>>>>>>>>>>>>>>>>>>>>>>>> "), true);
        mCurrentTest = 30;

        loadProfile(TEST_ECHO_PROFILE_NAME, true);
        waitForResponses();
        for (int i = 0; i < mManyMessagesCount; i++) {
            sendMessage(TEST_ECHO_PROFILE_NAME, TEST30_STRING.getBytes(), true);
            synchronized (mExpectedMessages) {
                mExpectedMessages.add(TEST30_STRING);
            }
        }
        waitForMessage();
        unloadProfile(TEST_ECHO_PROFILE_NAME, true);
        waitForResponses();
        waitForProfileUnloading();
    }

    /**
     * Add and remove profile
     */
    private void test32() {
        mLogAdapter.logMessage(new StringLogMessage("test32 started >>>>>>>>>>>>>>>>>>>>>>>>>>> "), true);
        mCurrentTest = 32;

        addProfile(TEST_ECHO_PROFILE_NAME, "libTestEchoProfile.so", true);
        waitForResponses();
        removeProfile(TEST_ECHO_PROFILE_NAME, true);
        waitForResponses();
    }

    /**
     * Load B, add A, remove A, unload B
     */
    private void test33() {
        mLogAdapter.logMessage(new StringLogMessage("test33 started >>>>>>>>>>>>>>>>>>>>>>>>>>> "), true);
        mCurrentTest = 33;

        loadProfile(TEST_SENDING_MSG_PROFILE_NAME, true);
        waitForResponses();
        addProfile(TEST_ECHO_PROFILE_NAME, "libTestEchoProfile.so", true);
        waitForResponses();
        removeProfile(TEST_ECHO_PROFILE_NAME, true);
        waitForResponses();
        unloadProfile(TEST_SENDING_MSG_PROFILE_NAME, true);
        waitForResponses();
        waitForProfileUnloading();
    }

    /**
     * Add, load, unload, remove profile
     */
    private void test34() {
        mLogAdapter.logMessage(new StringLogMessage("test34 started >>>>>>>>>>>>>>>>>>>>>>>>>>> "), true);
        mCurrentTest = 34;

        addProfile(TEST_ECHO_PROFILE_NAME, "libTestEchoProfile.so", true);
        waitForResponses();
        loadProfile(TEST_ECHO_PROFILE_NAME, true);
        waitForResponses();
        unloadProfile(TEST_ECHO_PROFILE_NAME, true);
        waitForResponses();
        waitForProfileUnloading();
        removeProfile(TEST_ECHO_PROFILE_NAME, true);
        waitForResponses();
    }

    /**
     * Remove profile without unloading
     */
    private void test36() {
        mLogAdapter.logMessage(new StringLogMessage("test36 started >>>>>>>>>>>>>>>>>>>>>>>>>>> "), true);
        mCurrentTest = 36;

        addProfile(TEST_ECHO_PROFILE_NAME, "libTestEchoProfile.so", true);
        waitForResponses();
        loadProfile(TEST_ECHO_PROFILE_NAME, true);
        waitForResponses();
        removeProfile(TEST_ECHO_PROFILE_NAME, false);
        waitForResponses();
        unloadProfile(TEST_ECHO_PROFILE_NAME, true);
        waitForResponses();
        waitForProfileUnloading();        
    }

    /**
     * Add profile that doesn't exists
     */
    private void test38() {
        mLogAdapter.logMessage(new StringLogMessage("test38 started >>>>>>>>>>>>>>>>>>>>>>>>>>> "), true);
        mCurrentTest = 38;

        addProfile(NOT_EXISTING_PROFILE_NAME, "libNotExistingProfile.so", false);
        waitForResponses();
        loadProfile(NOT_EXISTING_PROFILE_NAME, false);
        waitForResponses();
    }

    /**
     * Remove profile that doesn't exists
     */
    private void test39() {
        mLogAdapter.logMessage(new StringLogMessage("test39 started >>>>>>>>>>>>>>>>>>>>>>>>>>> "), true);
        mCurrentTest = 39;

        removeProfile(NOT_EXISTING_PROFILE_NAME, false);
        waitForResponses();
    }

    /**
     * Mobile app states
     */
    private void test41() {
        mLogAdapter.logMessage(new StringLogMessage("test41 started >>>>>>>>>>>>>>>>>>>>>>>>>>> "), true);
        mCurrentTest = 41;
        mTest41MsgCounter = 0;

        loadProfile(TEST_ECHO_PROFILE_NAME, true);
        waitForResponses();
        
        synchronized (mExpectedMessages) {
            mExpectedMessages.add(MOBILE_APP_BACKGROUND_STR);
        }
        changeAppState(TEST_ECHO_PROFILE_NAME, MobileAppState.MOBILE_APP_BACKGROUND, true);
        waitForResponses();
        waitForMessage();
        
        synchronized (mExpectedMessages) {
            mExpectedMessages.add(MOBILE_APP_FOREGROUND_STR);
        }
        changeAppState(TEST_ECHO_PROFILE_NAME, MobileAppState.MOBILE_APP_FOREGROUND, true);
        mStateReceived = false;
        waitForResponses();
        waitForMessage();
        
        synchronized (mExpectedMessages) {
            mExpectedMessages.add(MOBILE_APP_LOCK_SCREEN_STR);
        }
        changeAppState(TEST_ECHO_PROFILE_NAME, MobileAppState.MOBILE_APP_LOCK_SCREEN, true);
        mStateReceived = false;
        waitForResponses();
        waitForMessage();

        unloadProfile(TEST_ECHO_PROFILE_NAME, true);
        waitForResponses();
        waitForProfileUnloading();
    }
}
