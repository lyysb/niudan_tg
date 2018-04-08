package com.niudantg.http;

class CFMessage
{
	private CFHttpMsg httpMsg;
	private StringBuffer sb;
	private Object object;
	
	public CFHttpMsg getHttpMsg()
	{
		return httpMsg;
	}
	public void setHttpMsg(CFHttpMsg httpMsg)
	{
		this.httpMsg = httpMsg;
	}
	public StringBuffer getSb()
	{
		return sb;
	}
	public void setSb(StringBuffer sb)
	{
		this.sb = sb;
	}
	public Object getObject()
	{
		return object;
	}
	public void setObject(Object object)
	{
		this.object = object;
	}
	public CFMessage(CFHttpMsg httpMsg, StringBuffer sb, Object object)
	{
		this.httpMsg = httpMsg;
		this.sb = sb;
		this.object = object;
	}
}
