package com.smartdevicelinktester.adapters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.smartdevicelinktester.logmessages.LogMessage;
import com.smartdevicelinktester.logmessages.RPCLogMessage;
import com.smartdevicelinktester.logmessages.StringLogMessage;
import com.smartdevicelinktester.R;
import com.smartdevicelink.proxy.RPCMessage;
import com.smartdevicelink.proxy.constants.Names;
import com.smartdevicelink.proxy.rpc.enums.Result;

public class MessageAdapter extends ArrayAdapter<LogMessage> {
	private LayoutInflater vi;
	private ArrayList<LogMessage> items;

	public MessageAdapter(Context context, int textViewResourceId,
			ArrayList<LogMessage> items) {
		super(context, textViewResourceId, items);
		this.vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.items = items;
	}

	/** Adds the specified message to the items list and notifies of the change. */
	public void addMessage(LogMessage m) {
		add(m);
	}

	static class ViewHolder {
		TextView lblTop;
		TextView lblBottom;
		TextView lblTime;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		TextView lblTop = null;
		TextView lblBottom = null;
		TextView lblTime = null;

		ViewGroup rowView = (ViewGroup) convertView;
		if (rowView == null) {
			rowView = (ViewGroup) vi.inflate(R.layout.row, null);

			lblTop = (TextView) rowView.findViewById(R.id.toptext);
			lblBottom = (TextView) rowView.findViewById(R.id.bottomtext);
			lblTime = (TextView) rowView.findViewById(R.id.text_date_time);

			holder = new ViewHolder();
			holder.lblTop = lblTop;
			holder.lblBottom = lblBottom;
			holder.lblTime = lblTime;
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
			lblTop = holder.lblTop;
			lblBottom = holder.lblBottom;
			lblTime = holder.lblTime;

			lblBottom.setVisibility(View.VISIBLE);
			lblBottom.setText(null);
			lblTop.setTextColor(getContext().getResources().getColor(
					R.color.log_regular_text_color));
			lblTop.setText(null);
		}

		LogMessage rpcObj = getItem(position);
		if (rpcObj != null) {
			if (rpcObj instanceof StringLogMessage) {
				
				StringLogMessage myStringLog = (StringLogMessage) rpcObj;
				
				lblTop.setText(myStringLog.getMessage());
				lblTime.setText(rpcObj.getDate());
				lblBottom.setVisibility(View.GONE);				
				int iColor = myStringLog.getColor();
				lblTop.setTextColor(iColor);								
			} else if (rpcObj instanceof RPCLogMessage) {
				RPCMessage func = ((RPCLogMessage) rpcObj).getMessage();
				if (func.getMessageType().equals(Names.request)) {
					lblTop.setTextColor(Color.CYAN);
				} else if (func.getMessageType().equals(Names.notification)) {
					lblTop.setTextColor(Color.YELLOW);
				} else if (func.getMessageType().equals(Names.response)) {
					lblTop.setTextColor(Color.argb(255, 32, 161, 32));
				}
				lblTime.setText(rpcObj.getDate());
				lblTop.setText(func.getFunctionName() + " ("
						+ func.getMessageType() + ")");

				try {
					Method getSuccessMethod = func.getClass().getMethod(
							"getSuccess");
					boolean isSuccess = (Boolean) getSuccessMethod.invoke(func);
					if (isSuccess) {
						lblTop.setTextColor(Color.GREEN);
					} else {
						lblTop.setTextColor(Color.RED);
					}
					Method getInfoMethod = func.getClass().getMethod(
							"getInfo");
					Method getResultCodeMethod = func.getClass().getMethod(
							"getResultCode");

					String info = (String) getInfoMethod.invoke(func);
					Result result = (Result) getResultCodeMethod.invoke(func);

					lblBottom.setText(result
							+ (info != null ? ": " + info : ""));

				} catch (NoSuchMethodException e) {
					lblBottom.setVisibility(View.GONE);
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return rowView;
	}
}
