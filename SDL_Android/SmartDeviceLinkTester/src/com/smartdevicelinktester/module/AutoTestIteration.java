package com.smartdevicelinktester.module;

import com.smartdevicelink.proxy.RPCRequest;

public class AutoTestIteration {
        private RPCRequest request;
        private boolean isWait;
        private long waitMillis;
        
        public RPCRequest getRequest() {
                return request;
        }
        public void setRequest(RPCRequest request) {
                this.request = request;
        }
        public boolean isWait() {
                return isWait;
        }
        public void setWait(boolean isWait) {
                this.isWait = isWait;
        }
        public long getWaitMillis() {
                return waitMillis;
        }
        public void setWaitMillis(long waitMillis) {
                this.waitMillis = waitMillis;
        }

}