<%@ page language="java" pageEncoding="GBK"%>

<html>
<SCRIPT src="js/openwd.js" type=text/javascript></SCRIPT>
<%String hostUrl=request.getLocalAddr()+":"+request.getLocalPort(); %>
<form name="form" id="formid" action ="/">
	<OBJECT id=cal classid=clsid:79DAD3A5-311C-41C5-8F57-D083A2933D2B
		CODEBASE="http://127.0.0.1:8080/openwd/openwd.cab#version=10,0.0,1"
		width="0" height="0">
	</OBJECT>
<input name="path" value="<%=request.getContextPath()%>" >
<input name="url" value="<%=hostUrl%>" >
<input type="button"  name="openDOC"   value="docview"   onclick="viewLegalDoc('d1z9999999999999999999999999',1)">
<input type="button"  name="docUpload" value="docUpload" onclick="upLoad('u199999999999999999999999')">
</form>



</html>